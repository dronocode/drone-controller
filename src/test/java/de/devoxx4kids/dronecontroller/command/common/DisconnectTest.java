package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.Before;
import org.junit.Test;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link Disconnect}.
 *
 * @author  Tobias Schneider
 */
public class DisconnectTest {

    private Disconnect sut;

    @Before
    public void setUp() throws Exception {

        sut = Disconnect.disconnect();
    }


    @Test
    public void getBytes() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 0, 0, 0, 0 }));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("Disconnect"));
    }


    @Test
    public void getPacketType() {

        PacketType packetType = sut.getPacketType();

        assertThat(packetType, is(DATA_WITH_ACK));
    }


    @Test
    public void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(100));
    }
}
