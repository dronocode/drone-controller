package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.PacketType;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;

import org.junit.runner.RunWith;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Unit test of {@link CurrentTime}.
 *
 * @author  Tobias Schneider
 */
@RunWith(JUnit5.class)
class CurrentTimeTest {

    private CurrentTime sut;

    @BeforeEach
    void initialize() {

        sut = CurrentTime.currentTime(Clock.fixed(Instant.EPOCH, ZoneId.of("Europe/Berlin")));
    }


    @Test
    void getBytes() {

        byte[] bytesPackage = sut.getPacket(1);

        assertThat(bytesPackage,
            is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 0, 4, 1, 0, 84, 48, 49, 48, 48, 48, 48, 43, 48, 49, 48, 48, 0 }));
    }


    @Test
    void testToString() {

        assertThat(sut.toString(), is("CurrentTime T010000+0100"));
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
