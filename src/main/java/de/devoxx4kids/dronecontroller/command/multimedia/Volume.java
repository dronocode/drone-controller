package de.devoxx4kids.dronecontroller.command.multimedia;

import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;


/**
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class Volume implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 12, 0);
    private final byte volume;
    private final PacketType packetType = DATA_WITH_ACK;

    private Volume(int volume) {

        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Audio: Volume must be between 0 and 100.");
        }

        this.volume = (byte) volume;
    }

    public static Volume volume(int volume) {

        return new Volume(volume);
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        return new byte[] {
                (byte) packetType.ordinal(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(),
                (byte) sequenceNumber, 12, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0, volume, 0
            };
    }


    @Override
    public PacketType getPacketType() {

        return packetType;
    }


    @Override
    public String toString() {

        return "Volume{"
            + "volume=" + volume + '}';
    }
}
