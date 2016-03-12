package de.devoxx4kids.dronecontroller.listener;

import java.util.function.Consumer;


/**
 * @author  Tobias Schneider
 */
public final class BatteryListener implements EventListener {

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
