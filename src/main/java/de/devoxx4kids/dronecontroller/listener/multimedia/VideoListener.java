package de.devoxx4kids.dronecontroller.listener.multimedia;

import de.devoxx4kids.dronecontroller.listener.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.invoke.MethodHandles;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_LOW_LATENCY;


/**
 * <p>Wide angle - 640x480px, 15 frames per second</p>
 *
 * <p>Consumes the Packet:</p>
 *
 * <p>[3, 125, sequenceNumber, x, x, x, x, x, x, x, x, x, {Image Starting with -1 (FF), -40 (D8) }</p>
 *
 * @author  Tobias Schneider
 */
public class VideoListener implements EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String FRAME_JPG = "frame.jpg";
    private static byte[] lastJpeg = null;

    private VideoListener() {

        // private, please use fabric method
    }

    public static VideoListener videoListener() {

        return new VideoListener();
    }


    @Override
    public void consume(byte[] data) {
        LOGGER.debug("consuming video packet");
        File file = new File(FRAME_JPG);
        try(FileOutputStream fos = new FileOutputStream(file)) {
            LOGGER.debug("writing video jpg to "+file.getAbsolutePath()  );
            fos.write(getJpeg(data));
        } catch (IOException e) {
            LOGGER.error("Could not generate jpg");
        }
    }


    @Override
    public boolean test(byte[] data) {

        boolean jpgStart = data[12] == -1 && data[13] == -40;

        return data[0] == DATA_LOW_LATENCY.toByte() && data[1] == 125 && jpgStart;
    }


    private byte[] getJpeg(byte[] data) {

        int imageLength = data.length - 12;

        lastJpeg = new byte[imageLength];
        System.arraycopy(data, 12, lastJpeg, 0, imageLength);

        return lastJpeg;
    }

    public byte[] getLastJpeg() {
        return lastJpeg;
    }
}
