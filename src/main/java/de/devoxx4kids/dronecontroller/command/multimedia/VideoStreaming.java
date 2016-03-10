package de.devoxx4kids.dronecontroller.command.multimedia;

import de.devoxx4kids.dronecontroller.command.Acknowledge;
import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.FrameType;


/**
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class VideoStreaming implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 18, 0);
    private final byte enable;

    protected VideoStreaming(byte enable) {

        this.enable = enable;
    }

    public static VideoStreaming enableVideoStreaming() {

        return new VideoStreaming((byte) 1);
    }


    public static VideoStreaming disableVideoStreaming() {

        return new VideoStreaming((byte) 0);
    }


    @Override
    public byte[] getBytes(int sequence) {

        return new byte[] {
                (byte) FrameType.ARNETWORKAL_FRAME_TYPE_DATA_WITH_ACK.ordinal(),
                ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(), (byte) sequence, 12, 0, 0, 0,
                commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0, enable, 0
            };
    }


    @Override
    public Acknowledge getAcknowledge() {

        return Acknowledge.AckAfter;
    }
}
