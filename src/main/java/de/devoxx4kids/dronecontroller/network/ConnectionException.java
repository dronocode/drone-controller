package de.devoxx4kids.dronecontroller.network;

/**
 * Exception to throw if something went wrong of the drone connection.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class ConnectionException extends RuntimeException {

    public ConnectionException(String message, Throwable cause) {

        super(message, cause);
    }
}
