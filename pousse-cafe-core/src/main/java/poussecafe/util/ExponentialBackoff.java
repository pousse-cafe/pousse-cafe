package poussecafe.util;

import java.util.Random;

public class ExponentialBackoff {

    public static class Builder {

        private ExponentialBackoff exponentialBackoff = new ExponentialBackoff();

        public Builder slotTime(double slotTime) {
            exponentialBackoff.slotTime = slotTime;
            return this;
        }

        public Builder ceiling(int ceiling) {
            exponentialBackoff.ceiling = ceiling;
            return this;
        }

        public ExponentialBackoff build() {
            if(exponentialBackoff.slotTime < 0) {
                throw new IllegalArgumentException("Slot time must be greater or equal to 0");
            }
            return exponentialBackoff;
        }
    }

    private double slotTime = 51.2; // µs = 10 Mbit/s Ethernet line slot time

    private int attempt = 0;

    private int ceiling = 10; // Negative value implies no ceiling

    private Random random = new Random(System.currentTimeMillis());

    public double nextValue() {
        int k = random.nextInt((int) Math.pow(2, attempt));
        if(ceiling < 0 || attempt < ceiling) {
            ++attempt;
        }
        return slotTime * k;
    }
}
