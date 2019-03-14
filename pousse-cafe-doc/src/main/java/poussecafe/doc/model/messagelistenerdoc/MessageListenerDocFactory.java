package poussecafe.doc.model.messagelistenerdoc;

import com.sun.javadoc.MethodDoc;
import java.util.List;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.Factory;

public class MessageListenerDocFactory extends Factory<MessageListenerDocKey, MessageListenerDoc, MessageListenerDoc.Attributes> {

    public MessageListenerDoc createMessageListenerDoc(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature,
            MethodDoc messageListenerMethod) {
        MessageListenerDocKey key = new MessageListenerDocKey(stepMethodSignature);
        return createMessageListenerDoc(
                key,
                new BoundedContextComponentDoc.Builder()
                    .boundedContextDocKey(boundedContextDocKey)
                    .componentDoc(componentDocFactory.buildDoc(key.getValue(), messageListenerMethod))
                    .build(),
                stepMethodSignature,
                extractProducedEvents(messageListenerMethod));
    }

    private ComponentDocFactory componentDocFactory;

    private List<String> extractProducedEvents(MethodDoc methodDoc) {
        return AnnotationsResolver.event(methodDoc);
    }

    public MessageListenerDoc createMessageListenerDoc(
            MessageListenerDocKey key,
            BoundedContextComponentDoc boundedContextComponentDoc,
            StepMethodSignature stepMethodSignature,
            List<String> producedEvents) {
        MessageListenerDoc doc = newAggregateWithKey(key);

        doc.attributes().boundedContextComponentDoc().value(boundedContextComponentDoc);

        doc.attributes().stepMethodSignature().value(stepMethodSignature);
        doc.attributes().producedEvents().value(producedEvents);

        return doc;
    }
}
