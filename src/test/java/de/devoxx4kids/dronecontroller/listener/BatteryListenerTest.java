package de.devoxx4kids.dronecontroller.listener;

import de.devoxx4kids.dronecontroller.listener.common.BatteryListener;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;


/**
 * Unit test of {@link BatteryListener}.
 *
 * @author  Tobias Schneider
 */
public class BatteryListenerTest {

    private BatteryListener sut;

    private byte batteryState;

    @Before
    public void setUp() {

        sut = BatteryListener.batteryListener(b -> batteryState = b);
    }


    @Test
    public void testTestIsBatteryPacket() {

        byte[] tcpInPacket = new byte[] { -1, -1, -1, -1, -1, -1, -1, 0, 5, 1, -1, 11 };

        boolean test = sut.test(tcpInPacket);
        assertThat(test, is(true));
    }


    @Test
    public void testTestIsNoBatteryPacket() {

        byte[] tcpInPacket = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, 127, -1, 11 };

        boolean test = sut.test(tcpInPacket);
        assertThat(test, is(false));
    }


    @Test
    public void consume() {

        byte expectedByte = 11;
        byte[] tcpInPacket = new byte[] { -1, -1, -1, -1, -1, -1, -1, 0, 5, 1, -1, expectedByte };

        sut.consume(tcpInPacket);
        assertThat(batteryState, is(expectedByte));
    }
}
