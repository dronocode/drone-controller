package de.devoxx4kids.dronecontroller.listener;

import de.devoxx4kids.dronecontroller.listener.common.PCMDListener;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;


/**
 * Unit test of {@link PCMDListener}.
 *
 * @author  Tobias Schneider
 */
public class PCMDListenerTest {

    private PCMDListener sut;

    private String pcmd;

    @Before
    public void setUp() {

        sut = PCMDListener.pcmdlistener(s -> pcmd = s);
    }


    @Test
    public void testTestIsBatteryPacket() {

        byte[] tcpInPacket = new byte[] { -1, -1, -1, -1, -1, -1, -1, 3, 1, 0, -1, 11 };

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

        byte[] tcpInPacket = new byte[] { -1, -1, -1, -1, -1, -1, -1, 0, 5, 1, -1, 11 };

        sut.consume(tcpInPacket);
        assertThat(pcmd, is("11"));
    }
}
