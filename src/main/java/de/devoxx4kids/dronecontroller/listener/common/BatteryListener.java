package de.devoxx4kids.dronecontroller.listener.common;

import java.util.function.Consumer;


/**
 * [4, 126, 73, 12, 0, 0, 0, 0, 5, 1, 0, 50].
 *
 * @author  Tobias Schneider
 */
public final class BatteryListener implements CommonEventListener {

    private final Consumer<Byte> consumer;

    private BatteryListener(Consumer<Byte> consumer) {

        this.consumer = consumer;
    }

    public static BatteryListener batteryListener(Consumer<Byte> consumer) {

        return new BatteryListener(consumer);
    }


    @Override
    public void consume(byte[] data) {

        consumer.accept(data[11]);
    }


    @Override
    public boolean test(byte[] data) {

        return filterProject(data, 0, 5, 1);
    }
}
