package de.devoxx4kids.dronecontroller.command.animation;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link SlowShake}.
 *
 * @author  Tobias Schneider
 */

class SlowShakeTest {

    private SlowShake sut;

    @BeforeEach
    void initialize() {

        sut = SlowShake.slowShake();
    }


    @Test
    void getBytes() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 3, 2, 4, 0, 3, 0, 0, 0 }));
    }


    @Test
    void getPacketType() {

        PacketType packetType = sut.getPacketType();
        assertThat(packetType, is(DATA_WITH_ACK));
    }


    @Test
    void testToString() {

        assertThat(sut.toString(), is("SlowShake"));
    }


    @Test
    void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(2000));
    }
}
