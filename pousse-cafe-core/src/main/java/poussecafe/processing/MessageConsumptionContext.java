package poussecafe.processing;

import java.util.Objects;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.runtime.MessageConsumptionHandler;

public class MessageConsumptionContext {

    public static class Builder {

        private MessageConsumptionContext context = new MessageConsumptionContext();

        public MessageConsumptionContext build() {
            Objects.requireNonNull(context.messageConsumptionHandler);
            Objects.requireNonNull(context.messageConsumptionConfiguration);
            Objects.requireNonNull(context.applicationPerformanceMonitoring);
            return context;
        }

        public Builder messageConsumptionConfiguration(MessageConsumptionConfiguration messageConsumptionConfiguration) {
            context.messageConsumptionConfiguration = messageConsumptionConfiguration;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            context.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            context.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }
    }

    private MessageConsumptionContext() {

    }

    private MessageConsumptionHandler messageConsumptionHandler;

    public MessageConsumptionHandler messageConsumptionHandler() {
        return messageConsumptionHandler;
    }

    private MessageConsumptionConfiguration messageConsumptionConfiguration;

    public MessageConsumptionConfiguration messageConsumptionConfiguration() {
        return messageConsumptionConfiguration;
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    public ApplicationPerformanceMonitoring applicationPerformanceMonitoring() {
        return applicationPerformanceMonitoring;
    }
}
