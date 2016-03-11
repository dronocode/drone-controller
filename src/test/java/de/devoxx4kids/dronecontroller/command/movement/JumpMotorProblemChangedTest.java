package de.devoxx4kids.dronecontroller.command.movement;

import de.devoxx4kids.dronecontroller.command.Acknowledge;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test of {@link JumpMotorProblemChanged}.
 *
 * @author  Tobias Schneider
 */
public class JumpMotorProblemChangedTest {

    private JumpMotorProblemChanged sut;

    @Before
    public void setUp() throws Exception {

        sut = JumpMotorProblemChanged.jumpMotorProblemChanged();
    }


    @Test
    public void getBytes() {

        byte[] bytesPackage = sut.getBytes(1);
        assertThat(bytesPackage, is(new byte[] { 4, 11, 1, 15, 0, 0, 0, 3, 3, 2, 0, 1, 0, 0, 0 }));
    }


    @Test
    public void getAcknowledge() {

        Acknowledge acknowledge = sut.getAcknowledge();

        assertThat(acknowledge, is(Acknowledge.None));
    }


    @Test
    public void testToString() {

        assertThat(sut.toString(), is("JumpMotorProblemChanged"));
    }


    @Test
    public void waitingTime() {

        int waitingTime = sut.waitingTime();

        assertThat(waitingTime, is(500));
    }
}
