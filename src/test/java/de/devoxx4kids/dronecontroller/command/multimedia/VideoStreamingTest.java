package de.devoxx4kids.dronecontroller.command.multimedia;

import de.devoxx4kids.dronecontroller.command.Acknowledge;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;


/**
 * Unit test of {@link VideoStreaming}.
 *
 * @author  Tobias Schneider
 */
public class VideoStreamingTest {

    private VideoStreaming sut;

    @Before
    public void setUp() throws Exception {

        sut = VideoStreaming.enableVideoStreaming();
    }


    @Test
    public void enableVideoStreamingGetBytes() {

        VideoStreaming sut = VideoStreaming.enableVideoStreaming();
        byte[] bytesPacket = sut.getPacket(1);

        assertThat(bytesPacket, is(new byte[] { 4, 11, 1, 12, 0, 0, 0, 3, 18, 0, 0, 1, 0 }));
    }


    @Test
    public void disableVideoStreamingGetBytes() {

        VideoStreaming sut = VideoStreaming.disableVideoStreaming();
        byte[] bytesPacket = sut.getPacket(1);

        assertThat(bytesPacket, is(new byte[] { 4, 11, 1, 12, 0, 0, 0, 3, 18, 0, 0, 0, 0 }));
    }


    @Test
    public void getAcknowledge() {

        Acknowledge acknowledge = sut.getAcknowledge();
        assertThat(acknowledge, is(Acknowledge.AckAfter));
    }


    @Test
    public void testToStringEnable() {

        String string = VideoStreaming.enableVideoStreaming().toString();
        assertThat(string, is("VideoStreaming On"));
    }


    @Test
    public void testToStringDisable() {

        String string = VideoStreaming.disableVideoStreaming().toString();
        assertThat(string, is("VideoStreaming Off"));
    }
}
