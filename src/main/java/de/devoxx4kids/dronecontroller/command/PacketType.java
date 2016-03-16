package de.devoxx4kids.dronecontroller.command;

/**
 * UDP packet types of the drone connection.
 *
 * <p>These types represents the first byte of the udp packet.</p>
 *
 * <ul>
 *   <li>0 - UNINITIALIZED (Unknown type. Don't use)</li>
 *   <li>1 - ACK (Acknowledgment type)
 *
 *     <ul>
 *       <li>is used to acknowledge the IOControl command sequence number. Done by the device and by the controller. If
 *         the ACK-packet is not send within 100ms the device will send it again up to 3 times</li>
 *     </ul>
 *   </li>
 *   <li>2 - DATA (Data type. Main type for data that does not require an acknowledge)</li>
 *   <li>3 - DATA_LOW_LATENCY (Low latency data type. Should only be used when needed)
 *
 *     <ul>
 *       <li>Incoming-Only packet, carries the latest camera image starting from byte at position 13. The image is in
 *         jpeg format with a vga resolution (640x480px) with 15 frames per second</li>
 *     </ul>
 *   </li>
 *   <li>4 - DATA_WITH_ACK (Data that should have an acknowledge type. This type can have a long latency)
 *
 *     <ul>
 *       <li>Bidirectional packet - over this packet type it is possible to send commands to the drone based on
 *         {@link Command}. The drone sends basic information over this packet type to communicate with the client
 *
 *         <ul>
 *           <li>Enabled Video streaming</li>
 *           <li>BatteryLevel</li>
 *           <li>...</li>
 *         </ul>
 *       </li>
 *     </ul>
 *   </li>
 *   <li>5 - MAX (Unused, iterator maximum value)</li>
 * </ul>
 *
 * @author  Tobias Schneider
 * @author  Alexander Bischof
 */
public enum PacketType {

    UNINITIALIZED,
    ACK,
    DATA,
    DATA_LOW_LATENCY,
    DATA_WITH_ACK,
    MAX;

    public byte toByte() {

        return (byte) this.ordinal();
    }
}
