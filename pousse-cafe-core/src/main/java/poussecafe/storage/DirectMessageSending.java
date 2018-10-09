package poussecafe.storage;

import poussecafe.property.MessageCollection;

public class DirectMessageSending extends MessageSendingPolicy {

    @Override
    public void considerSending(MessageCollection messageCollection) {
        sendCollection(messageCollection);
    }

    @Override
    public MessageCollection newMessageCollection() {
        return new DefaultMessageCollection();
    }

}
