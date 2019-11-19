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

    private double backOffSlotTime;

    public double backOffSlotTime() {
        return backOffSlotTime;
    }

    private int backOffCeiling;

    public int backOffCeiling() {
        return backOffCeiling;
    }

    private int maxConsumptionRetries;

    public int maxConsumptionRetries() {
        return maxConsumptionRetries;
    }
}
