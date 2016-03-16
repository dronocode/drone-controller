package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.Acknowledge;
import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;


/**
 * @author  Alexander Bischof
 */
public final class Disconnect implements CommonCommand {

    private final CommandKey commandKey = CommandKey.commandKey(0, 0, 0);

    private Disconnect() {

        // use fabric method
    }

    public static Disconnect disconnect() {

        return new Disconnect();
    }


    @Override
    public byte[] getBytes(int sequence) {

        return new byte[] {
                (byte) PacketType.DATA_WITH_ACK.ordinal(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(),
                (byte) sequence, 15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0
            };
    }


    @Override
    public Acknowledge getAcknowledge() {

        return Acknowledge.AckAfter;
    }


    @Override
    public String toString() {

        return "Disconnect";
    }
}
