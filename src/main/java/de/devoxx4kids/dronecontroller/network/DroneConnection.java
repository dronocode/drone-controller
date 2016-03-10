package de.devoxx4kids.dronecontroller.network;

import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.listener.EventListener;

import java.io.IOException;


/**
 * @author  Tobias Schneider
 */
public interface DroneConnection {

    /**
     * Connect with the drone with the constructor injected credentials.
     *
     * @throws  IOException
     */
    void connect() throws IOException;


    /**
     * Sends the given {@link Command} to the drone.
     *
     * @param  command  to send to drone
     */
    void sendCommand(Command command);


    /**
     * Register the given {@link EventListener} to the {@link DroneConnection}.
     *
     * @param  eventListener  with the capsuled functionality
     */
    void addEventListener(EventListener eventListener);
}
