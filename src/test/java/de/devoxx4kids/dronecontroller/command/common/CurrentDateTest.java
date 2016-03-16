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
 * Unit test of {@link CurrentDate}.
 *
 * @author  Tobias Schneider
 */
public class CurrentDateTest {

    private CurrentDate sut;

    @Before
    public void setUp() throws Exception {

        sut = CurrentDate.currentDate(Clock.fixed(Instant.EPOCH, ZoneId.systemDefault()));
    }


    @Test
    public void getBytes() {

        byte[] bytesPackage = sut.getBytes(1);

        assertThat(bytesPackage,
            is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 0, 4, 0, 0, 49, 57, 55, 48, 45, 48, 49, 45, 48, 49, 0 }));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("CurrentDate 1970-01-01"));
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
