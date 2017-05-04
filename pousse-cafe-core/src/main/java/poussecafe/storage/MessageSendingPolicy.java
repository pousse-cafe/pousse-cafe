package poussecafe.storage;

import poussecafe.messaging.Message;
import poussecafe.messaging.MessageRouter;
import poussecafe.storable.MessageCollection;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class MessageSendingPolicy {

    private MessageRouter messageRouter;

    public abstract void considerSending(MessageCollection collection);

    public abstract MessageCollection newMessageCollection();

    protected void sendCollection(MessageCollection collection) {
        for (Message message : collection.getMessages()) {
            messageRouter.routeMessage(message);
        }
    }

    public void setMessageRouter(MessageRouter messageRouter) {
        checkThat(value(messageRouter).notNull().because("Message router cannot be null"));
        this.messageRouter = messageRouter;
    }

}
