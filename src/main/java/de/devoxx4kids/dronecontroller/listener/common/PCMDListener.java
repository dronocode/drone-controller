package de.devoxx4kids.dronecontroller.listener.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;


/**
 * @author  Tobias Schneider
 */
public final class PCMDListener implements CommonEventListener {

    private final Consumer<String> consumer;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private PCMDListener(Consumer<String> consumer) {

        this.consumer = consumer;
    }

    public static PCMDListener pcmdlistener(Consumer<String> consumer) {
        LOGGER.debug("consuming PCMDs");
        return new PCMDListener(consumer);
    }


    @Override
    public void consume(byte[] data) {
        consumer.accept(Byte.toString(data[11]));
    }


    @Override
    public boolean test(byte[] data) {
        LOGGER.debug("check for PCMD  packet");
        return filterProject(data, 3, 1, 0);
    }
}
