package de.devoxx4kids.dronecontroller.listener.common;

import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * [4, 126, 73, 12, 0, 0, 0, 0, 5, 1, 0, 50].
 *
 * @author  Tobias Schneider
 */
public final class BatteryListener implements CommonEventListener {

    private final Consumer<Byte> consumer;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private BatteryListener(Consumer<Byte> consumer) {

        this.consumer = consumer;
    }

    public static BatteryListener batteryListener(Consumer<Byte> consumer) {

        return new BatteryListener(consumer);
    }


    @Override
    public void consume(byte[] data) {
        LOGGER.debug("consuming battery packet");
        consumer.accept(data[11]);
    }


    @Override
    public boolean test(byte[] data) {
        LOGGER.debug("check for battery packet");
        return filterProject(data, 0, 5, 1);
    }
}
