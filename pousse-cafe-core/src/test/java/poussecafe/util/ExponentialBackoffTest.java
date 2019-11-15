package poussecafe.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ExponentialBackoffTest {

    @Test(expected = IllegalArgumentException.class)
    public void exponentialBackoffCannotBeCreatedWithNegativeSlotTime() {
        givenSlotTime(-2.);
        givenCeiling(10);
        whenGeneratingValues(10);
    }

    @Test
    public void exponentialBackoffGeneratesExpectedNumbers() {
        givenSlotTime(42.);
        givenCeiling(10);
        whenGeneratingValues(10);
        thenValuesAreInExpectedRanges();
    }

    private void givenSlotTime(double slotTime) {
        this.slotTime = slotTime;
    }

    private double slotTime;

    private void givenCeiling(int ceiling) {
        this.ceiling = ceiling;
    }

    private int ceiling;

    private void whenGeneratingValues(int maxValues) {
        values = new double[maxValues];
        ExponentialBackoff exponentialBackoff = new ExponentialBackoff.Builder()
                .slotTime(slotTime)
                .ceiling(ceiling)
                .build();
        for(int i = 0; i < maxValues; ++i) {
            values[i] = exponentialBackoff.nextValue();
        }
    }

    private double[] values;

    private void thenValuesAreInExpectedRanges() {
        for(int i = 0; i < values.length; ++i) {
            double value = values[i];
            double upperBound;
            if(ceiling > 0 && i > ceiling) {
                upperBound = slotTime * Math.pow(2, ceiling);
            } else {
                upperBound = slotTime * Math.pow(2, i);
            }
            assertTrue(value >= 0);
            assertTrue(String.format("%f must be less than %f", value, upperBound), value < upperBound);
        }
    }

    @Test
    public void exponentialBackoffCanBeTruncated() {
        givenSlotTime(42.);
        givenCeiling(10);
        whenGeneratingValues(20);
        thenValuesAreInExpectedRanges();
    }

    @Test
    public void exponentialBackoffNotTruncatedWithNegativeCeiling() {
        givenSlotTime(42.);
        givenCeiling(-1);
        whenGeneratingValues(20);
        thenValuesAreInExpectedRanges();
    }
}
