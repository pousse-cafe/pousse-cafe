package poussecafe.sample.configuration;

import poussecafe.configuration.AggregateConfiguration;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageFactory;
import poussecafe.sample.domain.MessageKey;
import poussecafe.sample.domain.MessageRepository;

public class MessageConfiguration
extends AggregateConfiguration<MessageKey, Message, Message.Data, MessageFactory, MessageRepository> {

    public MessageConfiguration() {
        super(Message.class, Message.Data.class, MessageFactory.class, MessageRepository.class);
    }

}
