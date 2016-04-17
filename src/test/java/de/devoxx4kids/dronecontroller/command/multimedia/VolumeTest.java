package de.devoxx4kids.dronecontroller.command.multimedia;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.gen5.api.Assertions.expectThrows;


/**
 * Unit test of {@link Volume}.
 *
 * @author  Tobias Schneider
 */

class VolumeTest {

    private Volume sut;

    @BeforeEach
    void initialize() {

        sut = Volume.volume(50);
    }


    @Test
    void volumeToLow() {

        expectThrows(IllegalArgumentException.class, () -> Volume.volume(-1));
    }


    @Test
    void volumeToHigh() {

        expectThrows(IllegalArgumentException.class, () -> Volume.volume(101));
    }


    @Test
    void volume() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 12, 0, 0, 0, 3, 12, 0, 0, 50, 0 }));
    }


    @Test
    void getPacketType() {

        assertThat(sut.getPacketType(), is(DATA_WITH_ACK));
    }


    @Test
    void testToString() {

        assertThat(sut.toString(), is("Volume{volume=50}"));
    }
}
