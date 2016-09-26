package de.devoxx4kids.dronecontroller.listener.multimedia;

import de.devoxx4kids.dronecontroller.listener.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

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
    private boolean writeToDisk=true;
    private float frameRate = 0;
    long lastFrame = System.currentTimeMillis()-20;
    MovingAverage average = new MovingAverage(20);

    private VideoListener() {

        // private, please use fabric method
    }

    public static VideoListener videoListener() {

        return new VideoListener();
    }


    @Override
    public void consume(byte[] data) {
        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
        average.add(new BigDecimal((System.currentTimeMillis() - lastFrame)).divide(new BigDecimal(1000),mc));

        LOGGER.debug("consuming video packet at a framerate of {}", new BigDecimal(1).divide(average.getAverage(),mc));
        byte[] jpeg = getJpeg(data);
        if (writeToDisk) {
            File file = new File(FRAME_JPG);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                LOGGER.debug("writing video jpg to " + file.getAbsolutePath());

                fos.write(jpeg);
            } catch (IOException e) {
                LOGGER.error("Could not generate jpg");
            }
        }
        lastFrame=System.currentTimeMillis();
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

    /**
     * @param enable By default writing is on. If file on disk is not needed, it can be switched off
     */
    public void setWriteToDisk(boolean enable) {
        this.writeToDisk = enable;
    }
}
