package de.devoxx4kids.dronecontroller.command.movement;

import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;


/**
 * @author  Alexander Bischof
 */
public final class JumpMotorProblemChanged implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 3, 2);
    private final PacketType packetType = DATA_WITH_ACK;

    private JumpMotorProblemChanged() {

        // use fabric method
    }

    public static JumpMotorProblemChanged jumpMotorProblemChanged() {

        return new JumpMotorProblemChanged();
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        return new byte[] {
                (byte) packetType.ordinal(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(),
                (byte) sequenceNumber, 15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0, 1, 0, 0, 0
            };
    }


    @Override
    public PacketType getPacketType() {

        return packetType;
    }


    @Override
    public String toString() {

        return "JumpMotorProblemChanged";
    }
}
