package de.devoxx4kids.dronecontroller.command.flip;

import de.devoxx4kids.dronecontroller.command.Acknowledge;
import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;


/**
 * @author  Tobias Schneider
 */
public final class DownsideUp implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 0, 1);

    private DownsideUp() {

        // use fabric method
    }

    public static DownsideUp downsideUp() {

        return new DownsideUp();
    }


    @Override
    public byte[] getBytes(int sequence) {

        return new byte[] {
                (byte) PacketType.DATA_WITH_ACK.ordinal(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(),
                (byte) sequence, 15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0, 2, 0, 0, 0
            };
    }


    @Override
    public Acknowledge getAcknowledge() {

        return Acknowledge.AckBefore;
    }


    @Override
    public String toString() {

        return "DownsideUp";
    }
}
