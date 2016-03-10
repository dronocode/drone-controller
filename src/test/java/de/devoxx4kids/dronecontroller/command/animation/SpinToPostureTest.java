package de.devoxx4kids.dronecontroller.command.animation;

import de.devoxx4kids.dronecontroller.command.Acknowledge;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link SpinToPosture}.
 *
 * @author  Tobias Schneider
 */
public class SpinToPostureTest {

    private SpinToPosture sut;

    @Before
    public void setUp() throws Exception {

        sut = SpinToPosture.spinToPosture();
    }


    @Test
    public void getBytes() {

        byte[] bytesPackage = sut.getBytes(1);

        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 3, 2, 4, 0, 7, 0, 0, 0 }));
    }


    @Test
    public void getAcknowledge() {

        Acknowledge acknowledge = sut.getAcknowledge();
        assertThat(acknowledge, is(Acknowledge.AckBefore));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("SpinToPosture"));
    }


    @Test
    public void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(3000));
    }
}
