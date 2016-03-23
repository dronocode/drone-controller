package de.devoxx4kids.dronecontroller.network.handshake;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.net.Socket;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.when;


/**
 * Unit test of {@link TcpHandshakeService}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TcpHandshakeServiceTest {

    private TcpHandshakeService sut;

    @Mock
    private Socket socketMock;

    @Before
    public void setUp() throws IOException {

        when(socketMock.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(getJsonAsBytes()));

        sut = new TcpHandshakeService("ip", 4444) {

            @Override
            protected Socket createSocket(String deviceIp, int tcpPort) {

                return socketMock;
            }
        };
    }


    @Test
    public void shake() throws IOException {

        HandshakeResponse handshakeResponse = sut.shake(new HandshakeRequest("wirelessLanName"));
        assertThat(handshakeResponse.getStatus(), is("0"));
        assertThat(handshakeResponse.getC2d_port(), is(54321));
        assertThat(handshakeResponse.getArstream_fragment_size(), is(65000));
        assertThat(handshakeResponse.getArstream_fragment_maximum_number(), is(4));
        assertThat(handshakeResponse.getArstream_max_ack_interval(), is(-1));
        assertThat(handshakeResponse.getC2d_update_port(), is(51));
        assertThat(handshakeResponse.getC2d_user_port(), is(21));
    }


    @Test
    public void createSocket() throws IOException {

        Socket socket = sut.createSocket("", 1);
        assertThat(socket, is(instanceOf(Socket.class)));
    }


    /**
     * { "status": 0, "c2d_port": 54321, "arstream_fragment_size": 65000, "arstream_fragment_maximum_number": 4,
     * "arstream_max_ack_interval": -1, "c2d_update_port": 51, "c2d_user_port": 21 }
     *
     * @return  the above json as a byte[]
     */
    private byte[] getJsonAsBytes() {

        return new byte[] {
                123, 32, 34, 115, 116, 97, 116, 117, 115, 34, 58, 32, 48, 44, 32, 34, 99, 50, 100, 95, 112, 111, 114,
                116, 34, 58, 32, 53, 52, 51, 50, 49, 44, 32, 34, 97, 114, 115, 116, 114, 101, 97, 109, 95, 102, 114, 97,
                103, 109, 101, 110, 116, 95, 115, 105, 122, 101, 34, 58, 32, 54, 53, 48, 48, 48, 44, 32, 34, 97, 114,
                115, 116, 114, 101, 97, 109, 95, 102, 114, 97, 103, 109, 101, 110, 116, 95, 109, 97, 120, 105, 109, 117,
                109, 95, 110, 117, 109, 98, 101, 114, 34, 58, 32, 52, 44, 32, 34, 97, 114, 115, 116, 114, 101, 97, 109,
                95, 109, 97, 120, 95, 97, 99, 107, 95, 105, 110, 116, 101, 114, 118, 97, 108, 34, 58, 32, 45, 49, 44,
                32, 34, 99, 50, 100, 95, 117, 112, 100, 97, 116, 101, 95, 112, 111, 114, 116, 34, 58, 32, 53, 49, 44,
                32, 34, 99, 50, 100, 95, 117, 115, 101, 114, 95, 112, 111, 114, 116, 34, 58, 32, 50, 49, 32, 125
            };
    }
}
