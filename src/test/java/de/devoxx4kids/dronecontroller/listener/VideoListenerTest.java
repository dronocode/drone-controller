package de.devoxx4kids.dronecontroller.listener;

import de.devoxx4kids.dronecontroller.listener.multimedia.VideoListener;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;

import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Unit test of {@link VideoListener}.
 *
 * @author  Tobias Schneider
 */
@RunWith(JUnit5.class)
class VideoListenerTest {

    private VideoListener sut;

    @BeforeEach
    void initialize() {

        sut = VideoListener.videoListener();
    }


    @Test
    void consume() {
    }


    @Test
    void testTestIsVideoPacket() {

        byte[] tcpInPacket = new byte[] { 3, 125, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, };

        boolean test = sut.test(tcpInPacket);
        assertThat(test, is(true));
    }


    @Test
    void testTestIsNoVideoPacket() {

        byte[] tcpInPacket = new byte[] { 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, };

        boolean test = sut.test(tcpInPacket);
        assertThat(test, is(false));
    }
}
