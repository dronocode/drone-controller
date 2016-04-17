package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Unit test of {@link CurrentDate}.
 *
 * @author  Tobias Schneider
 */

class CurrentDateTest {

    private CurrentDate sut;

    @BeforeEach
    void initialize() {

        sut = CurrentDate.currentDate(Clock.fixed(Instant.EPOCH, ZoneId.systemDefault()));
    }


    @Test
    void getBytes() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage,
            is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 0, 4, 0, 0, 49, 57, 55, 48, 45, 48, 49, 45, 48, 49, 0 }));
    }


    @Test
    void testToString() {

        assertThat(sut.toString(), is("CurrentDate 1970-01-01"));
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
