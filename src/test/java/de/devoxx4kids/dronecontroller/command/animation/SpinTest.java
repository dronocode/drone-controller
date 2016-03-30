package de.devoxx4kids.dronecontroller.command.animation;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.Before;
import org.junit.Test;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link Spin}.
 *
 * @author  Tobias Schneider
 */
public class SpinTest {

    private Spin sut;

    @Before
    public void setUp() throws Exception {

        sut = Spin.spin();
    }


    @Test
    public void getBytes() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 3, 2, 4, 0, 1, 0, 0, 0 }));
    }


    @Test
    public void getPacketType() {

        PacketType packetType = sut.getPacketType();
        assertThat(packetType, is(DATA_WITH_ACK));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("Spin"));
    }


    @Test
    public void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(3000));
    }
}
