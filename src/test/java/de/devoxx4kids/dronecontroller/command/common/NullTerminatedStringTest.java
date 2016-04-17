package de.devoxx4kids.dronecontroller.command.common;

import org.junit.gen5.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

import static java.time.format.DateTimeFormatter.ISO_DATE;


/**
 * Unit test of {@link NullTerminatedString}.
 *
 * @author  Tobias Schneider
 */

class NullTerminatedStringTest {

    @Test
    void getBytes() {

        byte[] bytesPackage = new NullTerminatedString(LocalDate.of(2015, 12, 1).format(ISO_DATE))
            .getNullTerminatedString();

        assertThat(bytesPackage, is(new byte[] { 50, 48, 49, 53, 45, 49, 50, 45, 48, 49, 0 }));
    }
}
