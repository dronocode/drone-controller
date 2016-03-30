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

        byte data = 1;

        int waitingTime = Pong.pong(data).waitingTime();
        assertThat(waitingTime, is(100));
    }
}
