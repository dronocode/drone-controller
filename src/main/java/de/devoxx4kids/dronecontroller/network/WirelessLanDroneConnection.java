package de.devoxx4kids.dronecontroller.network;

import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandException;
import de.devoxx4kids.dronecontroller.command.PacketType;
import de.devoxx4kids.dronecontroller.command.common.CommonCommand;
import de.devoxx4kids.dronecontroller.command.common.Pong;
import de.devoxx4kids.dronecontroller.listener.EventListener;
import de.devoxx4kids.dronecontroller.listener.common.CommonEventListener;
import de.devoxx4kids.dronecontroller.network.handshake.HandshakeRequest;
import de.devoxx4kids.dronecontroller.network.handshake.HandshakeResponse;
import de.devoxx4kids.dronecontroller.network.handshake.TcpHandshakeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.lang.invoke.MethodHandles;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.time.Clock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static de.devoxx4kids.dronecontroller.command.common.CurrentDate.currentDate;
import static de.devoxx4kids.dronecontroller.command.common.CurrentTime.currentTime;

import static java.net.InetAddress.getByName;

import static java.util.Arrays.copyOfRange;
import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * Represents the queue wireless lan connection to the drone.
 *
 * @author  Tobias Schneider
 */
public class WirelessLanDroneConnection implements DroneConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BlockingQueue<Command> commonCommandQueue = new ArrayBlockingQueue<>(25);
    private final BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(25);
    private final List<EventListener> commonEventListeners = new ArrayList<>();
    private final List<EventListener> eventListeners = new ArrayList<>();

    private final String deviceIp;
    private final int tcpPort;
    private final String wirelessLanName;
    private final Clock clock;
    private final byte[] nextSequenceNumbers;

    private boolean listenToResponse = true;
    private boolean sendCommands = true;

    private int devicePort;

    public WirelessLanDroneConnection(String deviceIp, int tcpPort, String wirelessLanName) {

        LOGGER.info("Creating {} for {}:{}...", this.getClass().getSimpleName(), deviceIp, tcpPort);

        this.deviceIp = deviceIp;
        this.tcpPort = tcpPort;
        this.wirelessLanName = wirelessLanName;
        this.clock = Clock.systemDefaultZone();

        nextSequenceNumbers = new byte[PacketType.values().length];
    }

    @Override
    public void connect() throws ConnectionException {

        LOGGER.debug("Connecting to drone...");
        listenToResponse = true;

        HandshakeResponse handshakeResponse;

        try {
            handshakeResponse = new TcpHandshakeService(deviceIp, tcpPort).shake(new HandshakeRequest(wirelessLanName));
        } catch (IOException e) {
            throw new ConnectionException("Error while trying to connect to the drone - check your connection", e);
        }

        devicePort = handshakeResponse.getC2d_port();
        LOGGER.info("Connected to drone - Handshake completed with {}", handshakeResponse);

        sendCommand(currentDate(clock));
        sendCommand(currentTime(clock));

        runResponseHandler();
        startSender(commandQueue);
        startSender(commonCommandQueue);
    }


    @Override
    public void disconnect() {

        LOGGER.debug("Disconnecting from drone...");
        listenToResponse = false;
        sendCommands = false;
        LOGGER.debug("Disconnected from drone");
    }


    @Override
    public void sendCommand(Command command) {

        try {
            if (command instanceof CommonCommand) {
                commonCommandQueue.put(command);
            } else {
                commandQueue.put(command);
            }
        } catch (InterruptedException e) {
            LOGGER.info("Could not add {} to a queue.", command);
        }
    }


    @Override
    public void addEventListener(EventListener eventListener) {

        if (eventListener instanceof CommonEventListener) {
            LOGGER.debug("adding eventListener common: {}", eventListener.getClass().getName());
            this.commonEventListeners.add(eventListener);
        } else {
            LOGGER.debug("adding eventListener       : {}", eventListener.getClass().getName());
            this.eventListeners.add(eventListener);
        }
    }


    /**
     * Drone response handler.
     *
     * <p>Will listen to the udp packages sent from the drone to the receiver and looks up what command it is and react
     * to it</p>
     */
    private void runResponseHandler() {

        new Thread(() -> {
            try(DatagramSocket sumoSocket = new DatagramSocket(devicePort)) {
                LOGGER.debug("Listing for response on port {}", devicePort);

                while (listenToResponse) {
                    byte[] buffer = new byte[65000];

                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                    sumoSocket.receive(datagramPacket);

                    byte[] packet = datagramPacket.getData();
                    LOGGER.debug("Receiving packet {}", convertAndCutPacket(packet, false));

                    commonEventListeners.stream().filter(e -> e.test(packet)).forEach(e -> e.consume(packet));

                    // Answer with a Pong
                    if (packet[0] == 4 || packet[0] == 2) {
                        sendCommand(Pong.pong(packet[2]));

                        continue;
                    }

                    eventListeners.stream().filter(e -> e.test(packet)).forEach(e -> e.consume(packet));
                }

                LOGGER.debug("Stop listening drone packets on port {}", devicePort);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                LOGGER.error("Error occurred while receiving packets from the drone.");
            }
        }).start();
    }


    /**
     * Starts a new thread as sender.
     *
     * @param  queue
     */
    private void startSender(BlockingQueue<Command> queue) {

        LOGGER.debug("Creating a command queue consumer");

        new Thread(() -> {
            try(DatagramSocket sumoSocket = new DatagramSocket()) {
                while (sendCommands) {
                    try {
                        Command command = queue.take();
                        byte[] packet = command.getPacket(getNextSequenceNumber(command));

                        LOGGER.debug("Sending command '{}' with packet {}", command, convertAndCutPacket(packet, false));
                        sumoSocket.send(new DatagramPacket(packet, packet.length, getByName(deviceIp), devicePort));

                        int waitingTime = command.waitingTime();
                        LOGGER.debug("Waiting time until send next packet is {}", waitingTime);
                        MILLISECONDS.sleep(waitingTime);
                    } catch (InterruptedException e) {
                        throw new CommandException("Got interrupted while taking a command", e);
                    }
                }

                LOGGER.debug("Stop command queue consumer");
            } catch (IOException e) {
                LOGGER.error("Error occurred while sending packets to the drone.");
            }
        }).start();
    }


    private synchronized int getNextSequenceNumber(final Command command) {

        byte packetTypeId = command.getPacketType().toByte();

        byte nextSequenceNumber = nextSequenceNumbers[packetTypeId];
        nextSequenceNumbers[packetTypeId] = (byte) (nextSequenceNumber + 1);

        return nextSequenceNumber;
    }


    /**
     * Converts the complete packet into decimals and cuts off all parts without information.
     *
     * @param  packet  to convert and cut
     * @param  toDecimal  convert packet to decimal
     *
     * @return  converter and cut packet
     */
    private String convertAndCutPacket(byte[] packet, boolean toDecimal) {

        byte[] thisPacket = packet.clone();

        if (toDecimal) {
            thisPacket = convertPacket(thisPacket);
        }

        return Arrays.toString(copyOfRange(thisPacket, 0, convertByte(packet[3])));
    }


    /**
     * Converts a signed two complement byte, as used in java, to a decimal.
     *
     * @param  b  byte to convert into decimal system
     *
     * @return  converted byte into decimal system
     */
    private int convertByte(byte b) {

        return b & 0xFF;
    }


    private byte[] convertPacket(byte[] packets) {

        byte[] newArray = new byte[packets.length - 1];

        for (int i = 0; i < packets.length - 1; i++) {
            newArray[i] = (byte) (convertByte(packets[i]));
        }

        return newArray;
    }
}
