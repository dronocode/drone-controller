package de.devoxx4kids.dronecontroller.command.multimedia;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;

import org.junit.runner.RunWith;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link VideoStreaming}.
 *
 * @author  Tobias Schneider
 */
@RunWith(JUnit5.class)
class VideoStreamingTest {

    private VideoStreaming sut;

    @BeforeEach
    void initialize() {

        sut = VideoStreaming.enableVideoStreaming();
    }


    @Test
    void enableVideoStreamingGetBytes() {

        VideoStreaming sut = VideoStreaming.enableVideoStreaming();
        byte[] bytesPacket = sut.getPacket(1);

        assertThat(bytesPacket, is(new byte[] { 4, 11, 1, 12, 0, 0, 0, 3, 18, 0, 0, 1, 0 }));
    }


    @Test
    void disableVideoStreamingGetBytes() {

        VideoStreaming sut = VideoStreaming.disableVideoStreaming();
        byte[] bytesPacket = sut.getPacket(1);

        assertThat(bytesPacket, is(new byte[] { 4, 11, 1, 12, 0, 0, 0, 3, 18, 0, 0, 0, 0 }));
    }


    @Test
    void getPacketType() {

        PacketType packetType = sut.getPacketType();
        assertThat(packetType, is(DATA_WITH_ACK));
    }


    @Test
    void testToStringEnable() {

        String string = VideoStreaming.enableVideoStreaming().toString();
        assertThat(string, is("VideoStreaming On"));
    }


    @Test
    void testToStringDisable() {

        String string = VideoStreaming.disableVideoStreaming().toString();
        assertThat(string, is("VideoStreaming Off"));
    }
}
