package poussecafe.pulsar;

import java.util.Optional;
import poussecafe.messaging.Message;

public interface PublicationTopicChooser {

    Optional<String> chooseTopicForMessage(Message message);
}
