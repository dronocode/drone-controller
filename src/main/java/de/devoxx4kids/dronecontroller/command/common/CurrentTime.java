package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.ChannelType;
import de.devoxx4kids.dronecontroller.command.CommandException;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;


/**
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class CurrentTime implements CommonCommand {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("'T'HHmmssZZZ");
    private final CommandKey commandKey = CommandKey.commandKey(0, 4, 1);
    private final Clock clock;
    private final PacketType packetType = DATA_WITH_ACK;

    private CurrentTime(Clock clock) {

        this.clock = clock;
    }

    public static CurrentTime currentTime(Clock clock) {

        return new CurrentTime(clock);
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        byte[] header = {
            packetType.toByte(), ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(), (byte) sequenceNumber,
            15, 0, 0, 0, commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0
        };

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(header);
            outputStream.write(new NullTerminatedString(ZonedDateTime.now(clock).format(TIME_FORMATTER))
                .getNullTerminatedString());

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new CommandException("Could not generate CurrentTime command.", e);
        }
    }


    @Override
    public PacketType getPacketType() {

        return packetType;
    }


    @Override
    public String toString() {

        return "CurrentTime " + ZonedDateTime.now(clock).format(TIME_FORMATTER);
    }
}
