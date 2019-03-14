package poussecafe.doc.model.messagelistenerdoc;

import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.EntityDataAccess;

public interface MessageListenerDataAccess<D extends MessageListenerDoc.Attributes> extends EntityDataAccess<MessageListenerDocKey, D> {

    D getByStepMethodSignature(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature);
}
