package poussecafe.pulsar;

import java.util.Optional;
import poussecafe.messaging.Message;

public class DefaultPublicationTopicChooser implements PublicationTopicChooser {

    @Override
    public Optional<String> chooseTopicForMessage(Message message) {
        return Optional.empty();
    }
}
