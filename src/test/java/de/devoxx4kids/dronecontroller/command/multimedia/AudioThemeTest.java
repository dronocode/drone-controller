package de.devoxx4kids.dronecontroller.command.multimedia;

import de.devoxx4kids.dronecontroller.command.Acknowledge;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link AudioTheme}.
 *
 * @author  Tobias Schneider
 */
public class AudioThemeTest {

    private AudioTheme sut;

    @Before
    public void setUp() throws Exception {

        sut = AudioTheme.audioTheme(AudioTheme.Theme.Default);
    }


    @Test
    public void themes() {

        assertThat(AudioTheme.Theme.Default.ordinal(), is(0));
        assertThat(AudioTheme.Theme.Robot.ordinal(), is(1));
        assertThat(AudioTheme.Theme.Insect.ordinal(), is(2));
        assertThat(AudioTheme.Theme.Monster.ordinal(), is(3));
    }


    @Test
    public void getBytes() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 3, 12, 1, 0, 0, 0, 0, 0 }));
    }


    @Test
    public void getAcknowledge() {

        assertThat(sut.getAcknowledge(), is(Acknowledge.AckBefore));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("AudioTheme{theme=Default}"));
    }
}
