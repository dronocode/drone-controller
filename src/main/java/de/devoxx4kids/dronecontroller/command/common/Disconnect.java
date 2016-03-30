package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;


/**
 * @author  Alexander Bischof
 */
public final class Disconnect implements CommonCommand {

    private final CommandKey commandKey = CommandKey.commandKey(0, 0, 0);
    private final PacketType packetType = DATA_WITH_ACK;

    private Disconnect() {

        // use fabric method
    }

    public static Disconnect disconnect() {

        return new Disconnect();
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        return new byte[] {
                (byte) packetType.ordinal(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(),
                (byte) sequenceNumber, 15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0
            };
    }


    @Override
    public PacketType getPacketType() {

        return packetType;
    }


    @Override
    public String toString() {

        return "Disconnect";
    }
}
