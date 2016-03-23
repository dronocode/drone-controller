package de.devoxx4kids.dronecontroller.listener;

import de.devoxx4kids.dronecontroller.listener.multimedia.VideoListener;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;


/**
 * Unit test of {@link VideoListener}.
 *
 * @author  Tobias Schneider
 */
public class VideoListenerTest {

    private VideoListener sut;

    @Before
    public void setUp() {

        sut = VideoListener.videoListener();
    }


    @Test
    public void consume() {
    }


    @Test
    public void testTestIsVideoPacket() {

        byte[] tcpInPacket = new byte[] { 3, 125, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, };

        boolean test = sut.test(tcpInPacket);
        assertThat(test, is(true));
    }


    @Test
    public void testTestIsNoVideoPacket() {

        byte[] tcpInPacket = new byte[] { 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, };

        boolean test = sut.test(tcpInPacket);
        assertThat(test, is(false));
    }
}
