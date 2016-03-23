package de.devoxx4kids.dronecontroller.listener.common;

import de.devoxx4kids.dronecontroller.listener.EventListener;


/**
 * EventListener for basic/common events send from the drone.
 *
 * @author  Tobias Schneider
 */
public interface CommonEventListener extends EventListener {


    default boolean filterProject(byte[] data, int project, int clazz, int cmd) {

        return data[7] == project && data[8] == clazz && data[9] == cmd;
    }
}
