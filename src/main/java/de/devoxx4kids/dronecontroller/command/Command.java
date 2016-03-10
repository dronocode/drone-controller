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
     * <p>A packet contains a header and a</p>
     *
     * <p>Header:</p>
     *
     * <ol>
     * <li>the main protocol type/category {@link FrameType}</li>
     * <li>extension of this type {@link ChannelType}
     *
     * <ul>
     * <li>11 for Outgoing packages</li>
     * </ul>
     * </li>
     * <li>sequence number for this type</li>
     * <li>packet-size including header</li>
     * <li>unknown
     *
     * <ul>
     * <li>0 in the analyzed data</li>
     * </ul>
     * </li>
     * </ol>
     *
     * @param  sequence
     *
     * @return  byte package of command
     */
    byte[] getBytes(int sequence);


    /**
     * TODO Describe why this is needed.
     *
     * @return
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
