package de.devoxx4kids.dronecontroller.command.common;

import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;

import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Unit test of {@link CommonCommand}.
 *
 * @author  Tobias Schneider
 */
@RunWith(JUnit5.class)
class CommonCommandTest {

    @Test
    void waitingTime() {

        byte data = 1;

        int waitingTime = Pong.pong(data).waitingTime();
        assertThat(waitingTime, is(100));
    }
}
