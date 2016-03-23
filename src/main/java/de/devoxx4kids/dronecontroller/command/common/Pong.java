package de.devoxx4kids.dronecontroller.command.common;

import de.devoxx4kids.dronecontroller.command.Acknowledge;

import static de.devoxx4kids.dronecontroller.command.PacketType.ACK;


//0  4  7   8  9
//---------
//   x  0   3  1 - unknown
//   x  0   5  0 - unknown
//  12  5,  1  0 - Battery -> data[11] ist immer 0 - wird alle 10 sequencen gesendet | [4, 126, 73, 12, 0, 0, 0, 0, 5, 1, 0, 50]
//  15  3   1  1 - unknown -> data[11] ist immer 1
//   0  3   3  0 - unknown -> data[11] ist immer 1
//  15  3   3  2 - unknown -> data[11] ist immer 0
//  15  3  19  0 - unknown -> data[11] ist 1 oder 0
// Receiving Packet: [4, 126, 8, 21, 0, 0, 0, 0, 5, 2, 0, 0, 105, 110, 116, 101, 114, 110, 97, 108, 0]

/**
 * @author  Alexander Bischof
 */
public final class Pong implements CommonCommand {

    private final byte sequenceNumber;
    private final byte sequenceNumberToAck;

    private Pong(byte data, byte sequenceNumber) {

        this.sequenceNumberToAck = data;
        this.sequenceNumber = sequenceNumber;
    }

    public static Pong pong(byte sequenceNumberToAck, byte sequenceNumber) {

        return new Pong(sequenceNumberToAck, sequenceNumber);
    }


    @Override
    public byte[] getBytes(int sequence) {

        return new byte[] { ACK.toByte(), (byte) 0xfe, this.sequenceNumber, 8, 0, 0, 0, sequenceNumberToAck };
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
