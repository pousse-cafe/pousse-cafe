package poussecafe.processing;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.runtime.ConsumptionIdGenerator;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

class MessageProcessor {

    public static class Builder {

        private MessageProcessor processor = new MessageProcessor();

        public Builder id(String id) {
            processor.consumptionIdGenerator = new ConsumptionIdGenerator(id);
            return this;
        }

        public Builder listenersPartition(ListenersSetPartition listenersPartition) {
            processor.listenersPartition = listenersPartition;
            return this;
        }

        public Builder failFast(boolean failFast) {
            processor.failFast = failFast;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            processor.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            processor.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public MessageProcessor build() {
            Objects.requireNonNull(processor.consumptionIdGenerator);
            Objects.requireNonNull(processor.listenersPartition);
            Objects.requireNonNull(processor.messageConsumptionHandler);
            Objects.requireNonNull(processor.applicationPerformanceMonitoring);
            processor.logger = LoggerFactory.getLogger(MessageProcessor.class.getName() + "_" + processor.consumptionIdGenerator.prefix());
            return processor;
        }
    }

    private MessageProcessor() {

    }

    private boolean failFast;

    private MessageConsumptionHandler messageConsumptionHandler;

    public void processMessage(OriginalAndMarshaledMessage message) {
        String consumptionId = consumptionIdGenerator.next();
        MessageConsumption consumption = new MessageConsumption.Builder()
                .consumptionId(consumptionId)
                .message(message)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .listenersPartition(listenersPartition)
                .messageConsumptionHandler(messageConsumptionHandler)
                .failFast(failFast)
                .logger(logger)
                .build();
        consumption.execute();
    }

    private ConsumptionIdGenerator consumptionIdGenerator;

    private ListenersSetPartition listenersPartition;

    protected Logger logger;

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;
}
