package de.devoxx4kids.dronecontroller.command;

/**
 * This interface represents a command sent to the jumping sumo.
 *
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public interface Command {

    /**
     * Returns the byte package of the specific command.
     *
     * <p>Packet structure:</p>
     *
     * <ul>
     *   <li>1 - the main protocol type, see {@link PacketType}</li>
     *   <li>2 - extension of this type {@link ChannelType}</li>
     *   <li>3 - sequence number for this type</li>
     *   <li>4 - packet-size including header</li>
     *   <li>5 until end are packet dependent</li>
     * </ul>
     *
     * <p>The first 4 bytes can be identified as header and until position 5 as body</p>
     *
     * @param  sequence  the increasing sequence number of the packets
     *
     * @return  byte package of command
     */
    byte[] getPacket(int sequence);


    /**
     * Describes if the sequence number have to increased before/after sending the command or maybe never.
     *
     * @return  the {@link Acknowledge} type
     */
    Acknowledge getAcknowledge();


    /**
     * Define the time to wait after a command was send to the drone to wait until the next command should be fired.
     *
     * @return  time to wait until send next command to the drone
     */
    default int waitingTime() {

        return 500;
    }
}
