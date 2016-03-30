package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.Before;
import org.junit.Test;

import static de.devoxx4kids.dronecontroller.command.PacketType.ACK;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link Pong}.
 *
 * @author  Tobias Schneider
 */
public class PongTest {

    private Pong sut;

    @Before
    public void setUp() throws Exception {

        byte data = 1;

        sut = Pong.pong(data);
    }


    @Test
    public void getBytes() {

        int sequenceNumber = 2;
        byte[] bytesPackage = sut.getPacket(sequenceNumber);

        assertThat(bytesPackage, is(new byte[] { 1, -2, 2, 8, 0, 0, 0, 1 }));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("Pong"));
    }


    @Test
    public void getPacketType() {

        PacketType packetType = sut.getPacketType();

        assertThat(packetType, is(ACK));
    }


    @Test
    public void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(100));
    }
}
