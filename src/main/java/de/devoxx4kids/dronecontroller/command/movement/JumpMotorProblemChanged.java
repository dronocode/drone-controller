package de.devoxx4kids.dronecontroller.command.movement;

import de.devoxx4kids.dronecontroller.command.Acknowledge;
import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;


/**
 * @author  Alexander Bischof
 */
public final class JumpMotorProblemChanged implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 3, 2);

    private JumpMotorProblemChanged() {

        // use fabric method
    }

    public static JumpMotorProblemChanged jumpMotorProblemChanged() {

        return new JumpMotorProblemChanged();
    }


    @Override
    public byte[] getBytes(int sequence) {

        return new byte[] {
                (byte) PacketType.DATA_WITH_ACK.ordinal(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(),
                (byte) sequence, 15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0, 1, 0, 0, 0
            };
    }


    @Override
    public Acknowledge getAcknowledge() {

        return Acknowledge.None;
    }


    @Override
    public String toString() {

        return "JumpMotorProblemChanged";
    }
}
