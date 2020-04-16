package poussecafe.processing;

public class MessageConsumptionConfiguration {

    public static class Builder {

        private MessageConsumptionConfiguration configuration = new MessageConsumptionConfiguration();

        public Builder backOffSlotTime(double backOffSlotTime) {
            configuration.backOffSlotTime = backOffSlotTime;
            return this;
        }

        public Builder backOffCeiling(int backOffCeiling) {
            configuration.backOffCeiling = backOffCeiling;
            return this;
        }

        public Builder maxConsumptionRetries(int maxConsumptionRetries) {
            configuration.maxConsumptionRetries = maxConsumptionRetries;
            return this;
        }

        public MessageConsumptionConfiguration build() {
            return configuration;
        }
    }

    private MessageConsumptionConfiguration() {

    }

    private double backOffSlotTime = DEFAULT_CONSUMPTION_BACK_OFF_SLOT_TIME;

    public static final double DEFAULT_CONSUMPTION_BACK_OFF_SLOT_TIME = 3.0;

    public double backOffSlotTime() {
        return backOffSlotTime;
    }

    private int backOffCeiling = DEFAULT_CONSUMPTION_BACK_OFF_CEILING;

    public static final int DEFAULT_CONSUMPTION_BACK_OFF_CEILING = 10;

    public int backOffCeiling() {
        return backOffCeiling;
    }

    private int maxConsumptionRetries = DEFAULT_CONSUMPTION_MAX_RETRIES;

    public static final int DEFAULT_CONSUMPTION_MAX_RETRIES = 50;

    public int maxConsumptionRetries() {
        return maxConsumptionRetries;
    }
}
