package poussecafe.storage;

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
