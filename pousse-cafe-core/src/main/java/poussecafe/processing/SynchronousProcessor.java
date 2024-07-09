package poussecafe.processing;

import java.util.List;

public class SynchronousProcessor implements Processor {

    public SynchronousProcessor(MessageConsumptionConfiguration messageConsumptionConfiguration) {
        messageProcessor = new MessageProcessor.Builder()
                .id("sync")
                .messageConsumptionConfiguration(messageConsumptionConfiguration)
                .build();
    }

    private MessageProcessor messageProcessor;

    @Override
    public void start() {
        // NOOP
    }

    @Override
    public void submit(ReceivedMessage receivedMessage, List<MessageListenersGroup> groups) {
        for(var group : groups) {
            messageProcessor.processMessage(group);
        }
        receivedMessage.ack();
    }

    @Override
    public void stop() {
        // NOOP
    }

}
