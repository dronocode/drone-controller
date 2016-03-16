package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.Acknowledge;
import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.CommandException;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.time.Clock;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_DATE;


/**
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class CurrentDate implements CommonCommand {

    private final CommandKey commandKey = CommandKey.commandKey(0, 4, 0);
    private final Clock clock;

    private CurrentDate(Clock clock) {

        this.clock = clock;
    }

    public static CurrentDate currentDate(Clock clock) {

        return new CurrentDate(clock);
    }


    @Override
    public byte[] getBytes(int sequence) {

        byte[] header = {
            (byte) PacketType.DATA_WITH_ACK.ordinal(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(),
            (byte) sequence, 15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(),
            0
        };

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(header);
            outputStream.write(new NullTerminatedString(now(clock).format(ISO_DATE)).getNullTerminatedString());

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new CommandException("Could not generate CurrentDate command.", e);
        }
    }


    @Override
    public Acknowledge getAcknowledge() {

        return Acknowledge.AckAfter;
    }


    @Override
    public String toString() {

        return "CurrentDate{" + clock.instant() + '}';
    }
}
