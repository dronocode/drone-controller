package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link Disconnect}.
 *
 * @author  Tobias Schneider
 */

class DisconnectTest {

    private Disconnect sut;

    @BeforeEach
    void initialize() {

        sut = Disconnect.disconnect();
    }


    @Test
    void getBytes() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 0, 0, 0, 0 }));
    }


    @Test
    void testToString() {

        assertThat(sut.toString(), is("Disconnect"));
    }


    @Test
    void getPacketType() {

        PacketType packetType = sut.getPacketType();

        assertThat(packetType, is(DATA_WITH_ACK));
    }


    @Test
    void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(100));
    }
}
