package de.devoxx4kids.dronecontroller.network;

import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandException;
import de.devoxx4kids.dronecontroller.command.common.CommonCommand;
import de.devoxx4kids.dronecontroller.command.common.CurrentDate;
import de.devoxx4kids.dronecontroller.command.common.CurrentTime;
import de.devoxx4kids.dronecontroller.command.common.Pong;
import de.devoxx4kids.dronecontroller.listener.EventListener;
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

    private static final String CONTROLLER_TYPE = "_arsdk-0902._udp";

    private final BlockingQueue<Command> commonCommandQueue = new ArrayBlockingQueue<>(25);
    private final BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(25);
    private final List<EventListener> eventListeners = new ArrayList<>();

    private final String deviceIp;
    private final int tcpPort;
    private final String wirelessLanName;
    private final Clock clock;

    private int devicePort;
    private byte noAckCounter = 0;
    private byte ackCounter = 0;

    public WirelessLanDroneConnection(String deviceIp, int tcpPort, String wirelessLanName) {

        LOGGER.info("Creating {} for {}:{}...", this.getClass().getSimpleName(), deviceIp, tcpPort);

        this.deviceIp = deviceIp;
        this.tcpPort = tcpPort;
        this.wirelessLanName = wirelessLanName;
        this.clock = Clock.systemDefaultZone();
    }

    @Override
    public void connect() throws ConnectionException {

        LOGGER.debug("Connecting to drone...");

        HandshakeRequest handshakeRequest = new HandshakeRequest(wirelessLanName, CONTROLLER_TYPE);
        HandshakeResponse handshakeResponse;

        try {
            handshakeResponse = new TcpHandshakeService(deviceIp, tcpPort).shake(handshakeRequest);
        } catch (IOException e) {
            throw new ConnectionException("Error while trying to connect to the drone - check your connection", e);
        }

        devicePort = handshakeResponse.getC2d_port();
        LOGGER.info("Connected to drone - Handshake completed with {}", handshakeResponse);

        sendCommand(CurrentDate.currentDate(clock));
        sendCommand(CurrentTime.currentTime(clock));

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

        this.eventListeners.add(eventListener);
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

                int pingCounter = 0;

                while (true) {
                    byte[] buffer = new byte[65000];

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    sumoSocket.receive(packet);

                    byte[] data = packet.getData();

                    LOGGER.info("Receiving Packet-Header: " + Arrays.toString(copyOfRange(data, 0, 12)));

                    // Answer with a Pong
                    if (data[1] == 126) {
                        LOGGER.debug("Ping");
                        sendCommand(Pong.pong(data[3]));

                        if (pingCounter > 10 && pingCounter % 10 == 1) {
                            sendCommand(CurrentDate.currentDate(clock));
                            sendCommand(CurrentTime.currentTime(clock));
                        }

                        pingCounter++;

                        continue;
                    }

                    eventListeners.stream().filter(e -> e.test(data)).forEach(e -> e.consume(data));
                }
            } catch (IOException e) {
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

                        byte[] packet = command.getBytes(changeAndGetCounter(command));
                        sumoSocket.send(new DatagramPacket(packet, packet.length, getByName(deviceIp), devicePort));

                        if (command instanceof CommonCommand) {
                            LOGGER.debug("Sending command: {}", command);
                        } else {
                            LOGGER.info("Sending command: {}", command);
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


    private int changeAndGetCounter(Command command) {

        int counter = 0;

        switch (command.getAcknowledge()) {
            case AckBefore:
                counter = ++ackCounter;
                break;

            case AckAfter:
                counter = ackCounter++;
                break;

            case NoAckBefore:
                counter = ++noAckCounter;
                break;

            case None:
            default:
                break;
        }

        return counter;
    }
}
