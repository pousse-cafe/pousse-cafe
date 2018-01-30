package poussecafe.journal;

import java.util.List;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageReplayer {

    private ConsumptionFailureRepository consumptionFailureRepository;

    private MessageSender messageSender;

    public void replayMessage(String messageId) {
        List<ConsumptionFailure> entries = consumptionFailureRepository.findConsumptionFailures(messageId);
        replayFailedConsumptions(entries);
    }

    private void replayFailedConsumptions(List<ConsumptionFailure> entries) {
        replayMessages(entries.stream().map(ConsumptionFailure::getMessage).collect(toList()));
    }

    private void replayMessages(List<Message> messages) {
        for (Message message : messages) {
            messageSender.sendMessage(message);
        }
    }

    public void replayAllFailedConsumptions() {
        List<ConsumptionFailure> entries = consumptionFailureRepository.findAllConsumptionFailures();
        replayFailedConsumptions(entries);
    }

    public void setConsumptionFailureRepository(ConsumptionFailureRepository consumptionFailureRepository) {
        checkThat(
                value(consumptionFailureRepository).notNull().because("Consumption failure repository cannot be null"));
        this.consumptionFailureRepository = consumptionFailureRepository;
    }

    public void setMessageSender(MessageSender messageSender) {
        checkThat(value(messageSender).notNull().because("Message sender cannot be null"));
        this.messageSender = messageSender;
    }
}
