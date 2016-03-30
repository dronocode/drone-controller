package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.CommandException;
import de.devoxx4kids.dronecontroller.command.CommandKey;
import de.devoxx4kids.dronecontroller.command.PacketType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.time.Clock;

import static de.devoxx4kids.dronecontroller.command.ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID;
import static de.devoxx4kids.dronecontroller.command.PacketType.DATA_WITH_ACK;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_DATE;


/**
 * @author  Alexander Bischof
 * @author  Tobias Schneider
 */
public final class CurrentDate implements CommonCommand {

    private final CommandKey commandKey = CommandKey.commandKey(0, 4, 0);
    private final Clock clock;
    private final PacketType packetType = DATA_WITH_ACK;

    private CurrentDate(Clock clock) {

        this.clock = clock;
    }

    public static CurrentDate currentDate(Clock clock) {

        return new CurrentDate(clock);
    }


    @Override
    public byte[] getPacket(int sequenceNumber) {

        byte[] header = {
            packetType.toByte(), JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.toByte(), (byte) sequenceNumber, 15, 0, 0, 0,
            commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0
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
    public PacketType getPacketType() {

        return packetType;
    }


    @Override
    public String toString() {

        return "CurrentDate " + now(clock).format(ISO_DATE);
    }
}
