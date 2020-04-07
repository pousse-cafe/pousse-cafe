package poussecafe.processing;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.runtime.ConsumptionIdGenerator;

class MessageProcessor {

    public static class Builder {

        private MessageProcessor processor = new MessageProcessor();

        public Builder id(String id) {
            processor.consumptionIdGenerator = new ConsumptionIdGenerator(id);
            return this;
        }

        public Builder messageConsumptionConfiguration(MessageConsumptionConfiguration messageConsumptionConfiguration) {
            processor.messageConsumptionConfiguration = messageConsumptionConfiguration;
            return this;
        }

        public MessageProcessor build() {
            Objects.requireNonNull(processor.consumptionIdGenerator);
            Objects.requireNonNull(processor.messageConsumptionConfiguration);
            processor.logger = LoggerFactory.getLogger(MessageProcessor.class.getName() + "_" + processor.consumptionIdGenerator.prefix());
            return processor;
        }
    }

    private MessageProcessor() {

    }

    public void processMessage(MessageListenersGroup message) {
        String consumptionId = consumptionIdGenerator.next();
        MessageConsumption consumption = new MessageConsumption.Builder()
                .consumptionId(consumptionId)
                .messageListenerGroup(message)
                .messageConsumptionConfiguration(messageConsumptionConfiguration)
                .processorLogger(logger)
                .build();
        consumption.execute();
    }

    private ConsumptionIdGenerator consumptionIdGenerator;

    protected Logger logger;

    private MessageConsumptionConfiguration messageConsumptionConfiguration;
}
