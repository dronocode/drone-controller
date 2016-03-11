package de.devoxx4kids.dronecontroller.command.multimedia;

import de.devoxx4kids.dronecontroller.command.Acknowledge;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link Volume}.
 *
 * @author  Tobias Schneider
 */
public class VolumeTest {

    private Volume sut;

    @Before
    public void setUp() throws Exception {

        sut = Volume.volume(50);
    }


    @Test(expected = IllegalArgumentException.class)
    public void volumeToLow() {

        Volume.volume(-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void volumeToHigh() {

        Volume.volume(101);
    }


    @Test
    public void volume() {

        byte[] bytesPackage = sut.getBytes(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 12, 0, 0, 0, 3, 12, 0, 0, 50, 0 }));
    }


    @Test
    public void getAcknowledge() {

        assertThat(sut.getAcknowledge(), is(Acknowledge.AckBefore));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("Volume{volume=50}"));
    }
}
