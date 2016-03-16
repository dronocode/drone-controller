package de.devoxx4kids.dronecontroller.command.flip;

import de.devoxx4kids.dronecontroller.command.Acknowledge;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;

import static de.devoxx4kids.dronecontroller.command.ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID;
import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;


/**
 * @author  Tobias Schneider
 */
public final class Balance implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 0, 1);

    private Balance() {

        // use fabric method
    }

    public static Balance balance() {

        return new Balance();
    }


    @Override
    public byte[] getBytes(int sequence) {

        return new byte[] {
                DATA_WITH_ACK.toByte(), JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(), (byte) sequence, 15, 0, 0, 0,
                commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0, 0, 0, 0, 0
            };
    }


    @Override
    public Acknowledge getAcknowledge() {

        return Acknowledge.AckBefore;
    }


    @Override
    public String toString() {

        return "Balance";
    }
}
