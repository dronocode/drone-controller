package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.Acknowledge;


/**
 * @author  Alexander Bischof
 */
public final class Pong implements CommonCommand {

    private final int counter;

    private Pong(int counter) {

        this.counter = counter;
    }

    public static Pong pong(int counter) {

        return new Pong(counter);
    }


    @Override
    public byte[] getBytes(int sequence) {

        return new byte[] { 1, (byte) 0xfe, (byte) this.counter, 8, 0, 0, 0, (byte) this.counter };
    }


    @Override
    public Acknowledge getAcknowledge() {

        return Acknowledge.None;
    }


    @Override
    public String toString() {

        return "Pong";
    }
}
