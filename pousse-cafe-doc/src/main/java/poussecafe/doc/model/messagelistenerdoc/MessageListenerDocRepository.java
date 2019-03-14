package poussecafe.doc.model.messagelistenerdoc;

import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.messagelistenerdoc.MessageListenerDoc.Attributes;
import poussecafe.domain.Repository;

public class MessageListenerDocRepository extends Repository<MessageListenerDoc, MessageListenerDocKey, MessageListenerDoc.Attributes> {

    public MessageListenerDoc getByStepMethodSignature(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature) {
        return wrap(dataAcces().getByStepMethodSignature(boundedContextDocKey, stepMethodSignature));
    }

    private MessageListenerDataAccess<MessageListenerDoc.Attributes> dataAcces() {
        return (MessageListenerDataAccess<Attributes>) super.dataAccess();
    }
}
