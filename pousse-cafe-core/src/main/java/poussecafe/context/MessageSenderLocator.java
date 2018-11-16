package poussecafe.context;

import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;
import poussecafe.messaging.Messaging;

public class MessageSenderLocator {

    public MessageSender locate(Class<? extends Message> messageImplementationClass) {
        Class<? extends Message> messageClass = environment.getMessageClass(messageImplementationClass);
        Messaging messaging = environment.getMessaging(messageClass);
        return messaging.messageSender();
    }

    private Environment environment;
}
