package de.devoxx4kids.dronecontroller.command.multimedia;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;

import org.junit.runner.RunWith;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Unit test of {@link AudioTheme}.
 *
 * @author  Tobias Schneider
 */
@RunWith(JUnit5.class)
class AudioThemeTest {

    private AudioTheme sut;

    @BeforeEach
    void initialize() {

        sut = AudioTheme.audioTheme(AudioTheme.Theme.Default);
    }


    @Test
    void themes() {

        assertThat(AudioTheme.Theme.Default.ordinal(), is(0));
        assertThat(AudioTheme.Theme.Robot.ordinal(), is(1));
        assertThat(AudioTheme.Theme.Insect.ordinal(), is(2));
        assertThat(AudioTheme.Theme.Monster.ordinal(), is(3));
    }


    @Test
    void getBytes() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 3, 12, 1, 0, 0, 0, 0, 0 }));
    }


    @Test
    void getPacketType() {

        assertThat(sut.getPacketType(), is(DATA_WITH_ACK));
    }


    @Test
    void testToString() {

        assertThat(sut.toString(), is("AudioTheme{theme=Default}"));
    }
}
