package de.devoxx4kids.dronecontroller.network;

import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandException;
import de.devoxx4kids.dronecontroller.command.PacketType;
import de.devoxx4kids.dronecontroller.command.common.CommonCommand;
import de.devoxx4kids.dronecontroller.command.common.CurrentDate;
import de.devoxx4kids.dronecontroller.command.common.CurrentTime;
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
        runConsumer(commandQueue);
        runConsumer(commonCommandQueue);
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
            this.commonEventListeners.add(eventListener);
        } else {
            this.eventListeners.add(eventListener);
        }
    }


    /**
     * Drone response handler.
     *
     * <p>Will listen to the udp packages send from the drone to the receiver and looks up what command it is and react
     * to it</p>
     */
    private void runResponseHandler() {

        new Thread(() -> {
            try(DatagramSocket sumoSocket = new DatagramSocket(devicePort)) {
                LOGGER.info("Listing for response on port {}", devicePort);

                while (true) {
                    byte[] buffer = new byte[65000];

                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                    sumoSocket.receive(datagramPacket);

                    byte[] packet = datagramPacket.getData();

                    LOGGER.debug("Receiving Packet: {}",
                        Arrays.toString(
                            copyOfRange(convertPacket(packet), 0, signedTwoComplementIntegerToUnsigned(packet[3]))));
                    LOGGER.debug("---------");
                    commonEventListeners.stream().filter(e -> e.test(packet)).forEach(e -> e.consume(packet));

                    // Answer with a Pong
                    if (packet[0] == 4 || packet[0] == 2) {
                        sendCommand(Pong.pong(packet[2]));

                        continue;
                    }

                    eventListeners.stream().filter(e -> e.test(packet)).forEach(e -> e.consume(packet));
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                LOGGER.error("Error occurred while receiving packets from the drone.");
            }
        }).start();
    }


    private void runConsumer(BlockingQueue<Command> queue) {

        LOGGER.debug("Creating a command queue consumer");

        new Thread(() -> {
            try(DatagramSocket sumoSocket = new DatagramSocket()) {
                while (true) {
                    try {
                        Command command = queue.take();

                        byte[] packet = command.getPacket(getNextSequenceNumber(command));
                        sumoSocket.send(new DatagramPacket(packet, packet.length, getByName(deviceIp), devicePort));

                        if (command instanceof Pong || command instanceof CurrentDate
                                || command instanceof CurrentTime) {
                            LOGGER.debug("Sending command: {}", command);
                            LOGGER.debug("Sending Packet: {}",
                                Arrays.toString(
                                    copyOfRange(packet, 0, signedTwoComplementIntegerToUnsigned(packet[3]))));
                            LOGGER.debug("---------");
                        }

                        MILLISECONDS.sleep(command.waitingTime());
                    } catch (InterruptedException e) {
                        throw new CommandException("Got interrupted while taking a command", e);
                    }
                }
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


    private byte[] convertPacket(byte[] packets) {

        byte[] newArray = new byte[packets.length - 1];

        for (int i = 0; i < packets.length - 1; i++) {
            newArray[i] = (byte) (packets[i] & 0xFF);
        }

        return newArray;
    }


    private int signedTwoComplementIntegerToUnsigned(byte b) {

        return b & 0xFF;
    }
}
