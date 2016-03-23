package de.devoxx4kids.dronecontroller.network.handshake;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.net.Socket;


/**
 * Service to establish the connection.
 *
 * <p>Service for the Handshake and establishing the connection to the drone via TCP and JSON. The response contains all
 * information that are needed to communicate with the drone</p>
 *
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public class TcpHandshakeService implements HandShakeService {

    private final Socket tcpSocket;
    private final PrintWriter tcpOut;
    private final BufferedReader tcpIn;

    public TcpHandshakeService(String deviceIp, int tcpPort) throws IOException {

        tcpSocket = createSocket(deviceIp, tcpPort);
        tcpOut = new PrintWriter(tcpSocket.getOutputStream(), true);
        tcpIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
    }

    @Override
    public HandshakeResponse shake(HandshakeRequest handshakeRequest) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        StringWriter shakeData = new StringWriter();
        objectMapper.writeValue(shakeData, handshakeRequest);

        return tcpHandshakeResult(shakeData.toString());
    }


    private HandshakeResponse tcpHandshakeResult(String shakeData) throws IOException {

        // Send to device
        tcpOut.println(shakeData);

        // Reads json response
        String responseLine;
        HandshakeResponse deviceAnswer = null;
        ObjectMapper objectMapper = new ObjectMapper();

        while ((responseLine = tcpIn.readLine()) != null) {
            responseLine = responseLine.substring(0, responseLine.lastIndexOf('}') + 1);
            deviceAnswer = objectMapper.readValue(responseLine, HandshakeResponse.class);
        }

        return deviceAnswer;
    }


    protected Socket createSocket(String deviceIp, int tcpPort) throws IOException {

        return new Socket(deviceIp, tcpPort);
    }


    @Override
    public void close() throws IOException {

        tcpOut.close();
        tcpSocket.close();
        tcpIn.close();
    }
}
