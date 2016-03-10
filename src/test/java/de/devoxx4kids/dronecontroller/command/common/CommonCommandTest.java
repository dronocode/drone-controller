package de.devoxx4kids.dronecontroller.command.common;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;


/**
 * Unit test of {@link CommonCommand}.
 *
 * @author  Tobias Schneider
 */
public class CommonCommandTest {

    @Test
    public void waitingTime() {

        int waitingTime = Pong.pong(1).waitingTime();
        assertThat(waitingTime, is(100));
    }
}
