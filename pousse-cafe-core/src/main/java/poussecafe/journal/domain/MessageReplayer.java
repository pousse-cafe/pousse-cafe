package poussecafe.journal.domain;

import java.util.List;
import poussecafe.domain.Service;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;

import static java.util.stream.Collectors.toList;

public class MessageReplayer implements Service {

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
}
