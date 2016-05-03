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
public final class Ondulation implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 2, 4);
    private final PacketType packetType = DATA_WITH_ACK;

    private Ondulation() {

        // use fabric method
    }

    public static Ondulation ondulation() {

        return new Ondulation();
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        return new byte[] {
                packetType.toByte(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(),
                (byte) sequenceNumber, 15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0, 5, 0, 0, 0
            };
    }


    @Override
    public PacketType getPacketType() {

        return packetType;
    }


    @Override
    public String toString() {

        return "Ondulation";
    }


    @Override
    public int waitingTime() {

        return 1500;
    }
}
