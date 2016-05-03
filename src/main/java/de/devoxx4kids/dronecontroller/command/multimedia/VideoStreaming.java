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
public final class VideoStreaming implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 18, 0);
    private final byte enable;
    private final PacketType packetType = DATA_WITH_ACK;

    private VideoStreaming(byte enable) {

        this.enable = enable;
    }

    public static VideoStreaming enableVideoStreaming() {

        return new VideoStreaming((byte) 1);
    }


    public static VideoStreaming disableVideoStreaming() {

        return new VideoStreaming((byte) 0);
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        return new byte[] {
                packetType.toByte(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(),
                (byte) sequenceNumber, 12, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(),
                commandKey.getCommandId(), 0, enable, 0
            };
    }


    @Override
    public PacketType getPacketType() {

        return packetType;
    }


    @Override
    public String toString() {

        return "VideoStreaming " + (enable == 1 ? "On" : "Off");
    }
}
