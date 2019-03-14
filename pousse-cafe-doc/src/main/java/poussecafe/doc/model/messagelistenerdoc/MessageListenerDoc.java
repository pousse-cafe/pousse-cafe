package poussecafe.doc.model.messagelistenerdoc;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.ListAttribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = MessageListenerDocFactory.class,
    repository = MessageListenerDocRepository.class
)
public class MessageListenerDoc extends AggregateRoot<MessageListenerDocKey, MessageListenerDoc.Attributes> {

    public static interface Attributes extends EntityAttributes<MessageListenerDocKey> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        Attribute<StepMethodSignature> stepMethodSignature();

        ListAttribute<String> producedEvents();
    }
}
