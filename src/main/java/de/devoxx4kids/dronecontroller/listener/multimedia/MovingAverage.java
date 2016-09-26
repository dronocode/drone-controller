package de.devoxx4kids.dronecontroller.listener.multimedia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Moving Average taken from Stack Overflow
 *
 */
public class MovingAverage {

    private final Queue<BigDecimal> window = new LinkedList<BigDecimal>();
    private final long windowSize;
    private BigDecimal sum = BigDecimal.ZERO;

    public MovingAverage(long windowSize) {
        this.windowSize = windowSize;
    }

    public void add(BigDecimal value) {
        sum = sum.add(value);
        window.add(value);
        if (window.size() > windowSize) {
            sum = sum.subtract(window.remove());
        }
    }

    public BigDecimal getAverage() {
        if (window.isEmpty()) return BigDecimal.ZERO;
        BigDecimal divisor = BigDecimal.valueOf(window.size());
        return sum.divide(divisor, 2, RoundingMode.HALF_UP);
    }
}