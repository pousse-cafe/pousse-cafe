package poussecafe.doc.model.messagelistenerdoc;

import poussecafe.util.StringKey;

public class MessageListenerDocKey extends StringKey {

    public MessageListenerDocKey(String messageListenerId) {
        super(messageListenerId);
    }

    public MessageListenerDocKey(StepMethodSignature signature) {
        super(signature.toString());
    }
}
