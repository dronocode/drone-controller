package de.devoxx4kids.dronecontroller.command.movement;

import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.Command;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA;

import static java.lang.String.format;


/**
 * Parrot command.
 *
 * <p>Responsible for the movements of the drone.</p>
 *
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class Pcmd implements Command {

    private final CommandKey commandKey = CommandKey.commandKey(3, 0, 0);
    private final byte speed;
    private final int degrees;
    private final byte turn;
    private final Integer waitingTime;
    private final PacketType packetType = DATA;

    private Pcmd(int speed, int degrees, Integer waitingTime) {

        if (waitingTime != null && waitingTime < 0) {
            throw new IllegalArgumentException(format("Waiting time must be greater or equal zero but is %s",
                    waitingTime));
        }

        if (speed < -128 || speed > 127) {
            throw new IllegalArgumentException(format("Movement: Speed must be between -128 and 127 but is %s", speed));
        }

        this.speed = (byte) speed;
        this.degrees = degrees; // required for cloning
        this.turn = (byte) degreeToPercent(degrees);
        this.waitingTime = waitingTime;
    }

    /**
     * This is one of the main methods to move a parrot drone.
     *
     * @param  speed  how fast the electronic engine of the drone should spin
     * @param  degrees  how much the drone will turn around his own axe in degree (°)
     *
     * @return  an immutable command with the given inputs
     */
    public static Pcmd pcmd(int speed, int degrees) {

        return new Pcmd(speed, degrees, null);
    }


    /**
     * This is one of the main methods to move a parrot drone.
     *
     * @param  speed  how fast the electronic engine of the drone should spin
     * @param  degrees  how much the drone will turn around his own axe in degree (°)
     * @param  waitingTime  set the waiting time to send the next command
     *
     * @return  an immutable command with the given inputs
     */
    public static Pcmd pcmd(int speed, int degrees, int waitingTime) {

        return new Pcmd(speed, degrees, waitingTime);
    }


    /**
     * Converts the degrees to percent on a circle.
     *
     * <p>R/360 = P/100 -> P = R*100/360</p>
     *
     * @param  degrees  to convert in percents of a circle
     *
     * @return  percents from given degrees
     */
    private int degreeToPercent(int degrees) {

        return degrees * 100 / 360;
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        byte touchscreen = 1;

        return new byte[] {
                DATA.toByte(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_NONACK_ID.toByte(), (byte) sequenceNumber,
                14, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0,
                touchscreen, speed, turn
            };
    }


    @Override
    public PacketType getPacketType() {

        return packetType;
    }

    public byte speed() {
        return speed;
    }

    public byte turn() {
        return turn;
    }

    public int degrees() {
        return degrees;
    }

    @Override
    public String toString() {

        return "Pcmd{"
            + "speed=" + speed
            + ", degrees=" + degrees
            + ", turn=" + turn + '}';
    }


    @Override
    public int waitingTime() {

        if (waitingTime == null) {
            return Command.super.waitingTime();
        }

        return this.waitingTime;
    }

    public Command clone(int waitingTime) {
        return new Pcmd(speed, degrees, waitingTime);
    }
}
