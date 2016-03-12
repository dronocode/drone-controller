package de.devoxx4kids.dronecontroller.listener;

import java.util.function.Consumer;


/**
 * @author  Tobias Schneider
 */
public final class PCMDListener implements EventListener {

    private final Consumer<String> consumer;

    private PCMDListener(Consumer<String> consumer) {

        this.consumer = consumer;
    }

    public static PCMDListener pcmdlistener(Consumer<String> consumer) {

        return new PCMDListener(consumer);
    }


    @Override
    public void consume(byte[] data) {

        consumer.accept(Byte.toString(data[11]));
    }


    @Override
    public boolean test(byte[] data) {

        return filterProject(data, 3, 1, 0);
    }
}
