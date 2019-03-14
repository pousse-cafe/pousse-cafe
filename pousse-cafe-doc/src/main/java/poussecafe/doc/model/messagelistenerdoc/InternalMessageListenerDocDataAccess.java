package poussecafe.doc.model.messagelistenerdoc;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.exception.PousseCafeException;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = MessageListenerDoc.class,
    dataImplementation = MessageListenerDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalMessageListenerDocDataAccess extends InternalDataAccess<MessageListenerDocKey, MessageListenerDocData> implements MessageListenerDataAccess<MessageListenerDocData> {

    @Override
    public MessageListenerDocData getByStepMethodSignature(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature) {
        return findAll().stream()
                .filter(data -> data.boundedContextComponentDoc().value().boundedContextDocKey().equals(boundedContextDocKey))
                .filter(data -> data.stepMethodSignature().valueEquals(stepMethodSignature))
                .findFirst().orElseThrow(PousseCafeException::new);
    }
}
