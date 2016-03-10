package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.Acknowledge;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link CurrentTime}.
 *
 * @author  Tobias Schneider
 */
public class CurrentTimeTest {

    private CurrentTime sut;

    @Before
    public void setUp() throws Exception {

        sut = CurrentTime.currentTime(Clock.fixed(Instant.EPOCH, ZoneId.of("Europe/Berlin")));
    }


    @Test
    public void getBytes() {

        byte[] bytesPackage = sut.getBytes(1);

        assertThat(bytesPackage,
            is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 0, 4, 1, 0, 84, 48, 49, 48, 48, 48, 48, 43, 48, 49, 48, 48, 0 }));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("CurrentTime{1970-01-01T00:00:00Z}"));
    }


    @Test
    public void getAcknowledge() {

        Acknowledge acknowledge = sut.getAcknowledge();

        assertThat(acknowledge, is(Acknowledge.AckAfter));
    }


    @Test
    public void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(100));
    }
}
