package de.devoxx4kids.dronecontroller.command.movement;

import de.devoxx4kids.dronecontroller.command.Acknowledge;
import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;


/**
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class Jump implements Command {

    public enum Type {

        Long,
        High;
    }

    private final CommandKey commandKey = CommandKey.commandKey(3, 2, 3);
    private final Type type;

    private Jump(Type type) {

        this.type = type;
    }

    public static Jump jump(Type type) {

        return new Jump(type);
    }


    @Override
    public byte[] getPacket(int sequence) {

        return new byte[] {
                DATA_WITH_ACK.toByte(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(), (byte) sequence,
                15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0,
                (byte) type.ordinal(), 0, 0, 0
            };
    }


    @Override
    public Acknowledge getAcknowledge() {

        return Acknowledge.AckBefore;
    }


    @Override
    public String toString() {

        return "Jump";
    }


    @Override
    public int waitingTime() {

        return 5000;
    }
}
