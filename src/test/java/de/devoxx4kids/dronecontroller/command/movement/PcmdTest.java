package de.devoxx4kids.dronecontroller.command.movement;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.Before;
import org.junit.Test;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link Pcmd}.
 *
 * @author  Tobias Schneider
 */
public class PcmdTest {

    private Pcmd sut;

    @Before
    public void setUp() throws Exception {

        sut = Pcmd.pcmd(40, 180);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getBytesOverflowInSpeedLow() {

        Pcmd.pcmd(-129, 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getBytesOverflowInSpeedHigh() {

        Pcmd.pcmd(128, 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void waitingTimeLessThenZero() {

        Pcmd.pcmd(128, 0, -5);
    }


    @Test
    public void getBytesWIthCorrect180DegreeTo50Percent() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 2, 10, 1, 14, 0, 0, 0, 3, 0, 0, 0, 1, 40, 50 }));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("Pcmd{speed=40, turn=50}"));
    }


    @Test
    public void getPacketType() {

        PacketType packetType = sut.getPacketType();

        assertThat(packetType, is(DATA));
    }


    @Test
    public void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(500));
    }


    @Test
    public void waitingTimeSetByUser() {

        int waitingTime = Pcmd.pcmd(50, 50, 0).waitingTime();

        assertThat(waitingTime, is(0));
    }
}
