package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.Acknowledge;

import org.junit.Before;
import org.junit.Test;

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
        byte sequenceNumber = 1;

        sut = Pong.pong(data, sequenceNumber);
    }


    @Test
    public void getBytes() {

        int notNeededCounter = 2;
        byte[] bytesPackage = sut.getPacket(notNeededCounter);

        assertThat(bytesPackage, is(new byte[] { 1, -2, 1, 8, 0, 0, 0, 1 }));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("Pong"));
    }


    @Test
    public void getAcknowledge() {

        Acknowledge acknowledge = sut.getAcknowledge();

        assertThat(acknowledge, is(Acknowledge.None));
    }


    @Test
    public void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(100));
    }
}
