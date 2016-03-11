package de.devoxx4kids.dronecontroller.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.invoke.MethodHandles;

import java.util.Arrays;


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
    public void eventFired(byte[] data) {

        if (data[1] == 125) {
            System.out.println("PICTURE");
            System.out.println(Arrays.toString(data));

            try(FileOutputStream fileOutputStream = new FileOutputStream(new File(FRAME_JPG))) {
                fileOutputStream.write(getJpeg(data));
            } catch (IOException e) {
                LOGGER.error("Could not generate jpg");
            }
        }
    }


    private byte[] getJpeg(byte[] data) {

        byte[] jpegData = new byte[data.length];
        System.arraycopy(data, 12, jpegData, 0, data.length - 12);

        return jpegData;
    }
}
