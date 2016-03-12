package de.devoxx4kids.dronecontroller.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.invoke.MethodHandles;


/**
 * @author  Tobias Schneider
 */
public class VideoListener implements EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String FRAME_JPG = "frame.jpg";

    private VideoListener() {

        // private, please use fabric method
    }

    public static VideoListener videoListener() {

        return new VideoListener();
    }


    @Override
    public void consume(byte[] data) {

        try(FileOutputStream fos = new FileOutputStream(new File(FRAME_JPG))) {
            fos.write(getJpeg(data));
        } catch (IOException e) {
            LOGGER.error("Could not generate jpg");
        }
    }


    @Override
    public boolean test(byte[] data) {

        return data[1] == 125;
    }


    private byte[] getJpeg(byte[] data) {

        byte[] jpegData = new byte[data.length];
        System.arraycopy(data, 12, jpegData, 0, data.length - 12);

        return jpegData;
    }
}
