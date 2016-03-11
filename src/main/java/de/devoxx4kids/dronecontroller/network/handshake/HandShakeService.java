package de.devoxx4kids.dronecontroller.network.handshake;

import java.io.IOException;


/**
 * Service interface defined to encapsulate the different kind of hand shake service to get information of the drone.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface HandShakeService extends AutoCloseable {

    /**
     * Starts the handshake with the drone.
     *
     * @param  handshakeRequest  holds all important information for the handshake
     *
     * @return  the {@link HandshakeResponse} from the drone
     *
     * @throws  IOException
     */
    HandshakeResponse shake(HandshakeRequest handshakeRequest) throws IOException;
}
