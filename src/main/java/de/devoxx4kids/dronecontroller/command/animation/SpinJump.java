package de.devoxx4kids.dronecontroller.command.animation;

import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;


/**
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class SpinJump implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 2, 4);
    private final PacketType packetType = DATA_WITH_ACK;

    private SpinJump() {

        // use fabric method
    }

    public static SpinJump spinJump() {

        return new SpinJump();
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        return new byte[] {
                (byte) packetType.ordinal(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(),
                (byte) sequenceNumber, 15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0, 6, 0, 0, 0
            };
    }


    @Override
    public PacketType getPacketType() {

        return packetType;
    }


    @Override
    public String toString() {

        return "SpinJump";
    }


    @Override
    public int waitingTime() {

        return 5000;
    }
}
