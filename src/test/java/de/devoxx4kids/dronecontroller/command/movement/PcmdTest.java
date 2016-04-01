package de.devoxx4kids.dronecontroller.command.movement;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;

import org.junit.runner.RunWith;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.api.Assertions.expectThrows;


/**
 * Unit test of {@link Pcmd}.
 *
 * @author  Tobias Schneider
 */
@RunWith(JUnit5.class)
class PcmdTest {

    private Pcmd sut;

    @BeforeEach
    void initialize() {

        sut = Pcmd.pcmd(40, 180);
    }


    @Test
    void getBytesOverflowInSpeedLow() {

        IllegalArgumentException thrown = expectThrows(IllegalArgumentException.class, () -> Pcmd.pcmd(-129, 0));
        assertEquals("Movement: Speed must be between -128 and 127 but is -129", thrown.getMessage());
    }


    @Test
    void getBytesOverflowInSpeedHigh() {

        IllegalArgumentException thrown = expectThrows(IllegalArgumentException.class, () -> Pcmd.pcmd(128, 0));
        assertEquals("Movement: Speed must be between -128 and 127 but is 128", thrown.getMessage());
    }


    @Test
    void waitingTimeLessThenZero() {

        IllegalArgumentException thrown = expectThrows(IllegalArgumentException.class, () -> Pcmd.pcmd(128, 0, -5));
        assertEquals("Waiting time must be greater or equal zero but is -5", thrown.getMessage());
    }


    @Test
    void getBytesWIthCorrect180DegreeTo50Percent() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 2, 10, 1, 14, 0, 0, 0, 3, 0, 0, 0, 1, 40, 50 }));
    }


    @Test
    void testToString() {

        assertThat(sut.toString(), is("Pcmd{speed=40, turn=50}"));
    }


    @Test
    void getPacketType() {

        PacketType packetType = sut.getPacketType();

        assertThat(packetType, is(DATA));
    }


    @Test
    void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(500));
    }


    @Test
    void waitingTimeSetByUser() {

        int waitingTime = Pcmd.pcmd(50, 50, 0).waitingTime();

        assertThat(waitingTime, is(0));
    }
}
