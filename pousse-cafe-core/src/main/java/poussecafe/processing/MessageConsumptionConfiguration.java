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

    private double backOffSlotTime = 1.0;

    public double backOffSlotTime() {
        return backOffSlotTime;
    }

    private int backOffCeiling = 10;

    public int backOffCeiling() {
        return backOffCeiling;
    }

    private int maxConsumptionRetries = 50;

    public int maxConsumptionRetries() {
        return maxConsumptionRetries;
    }
}
