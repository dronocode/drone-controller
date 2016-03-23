package de.devoxx4kids.dronecontroller.network.handshake;

/**
 * Request send to the drone to establish the connection and exchange information.
 *
 * <ul>
 *   <li>controller name
 *
 *     <ul>
 *       <li>The wireless lan name</li>
 *     </ul>
 *   </li>
 *   <li>controller type
 *
 *     <ul>
 *       <li>_arsdk-0902._udp</li>
 *     </ul>
 *   </li>
 *   <li>d2c port - 54321
 *
 *     <ul>
 *       <li>Describes the port where the drone has to send the UDP packets</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>This Request will be answered with {@link HandshakeResponse}</p>
 *
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class HandshakeRequest {

    private final String controller_name;
    private final String controller_type;
    private final int d2c_port;

    public HandshakeRequest(String controller_name) {

        this.controller_name = controller_name;
        this.controller_type = "_arsdk-0902._udp";
        this.d2c_port = 54321;
    }

    public String getController_name() {

        return controller_name;
    }


    public String getController_type() {

        return controller_type;
    }


    public int getD2c_port() {

        return d2c_port;
    }


    @Override
    public String toString() {

        return "HandshakeRequest{"
            + "controller_name='" + controller_name + '\''
            + ", controller_type='" + controller_type + '\''
            + ", d2c_port=" + d2c_port + '}';
    }
}
