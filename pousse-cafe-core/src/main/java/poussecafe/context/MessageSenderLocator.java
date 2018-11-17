package poussecafe.context;

import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Messaging;

public class MessageSenderLocator {

    public MessageSender locate(Class<? extends Message> messageClassOrImplementation) {
        Class<? extends Message> messageClass = environment.getMessageClass(messageClassOrImplementation);
        Messaging messaging = environment.getMessaging(messageClass);
        return messaging.messageSender();
    }

    private Environment environment;
}
