package poussecafe.doc.model.messagelistenerdoc;

import java.io.Serializable;
import java.util.ArrayList;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.ListAttribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

@SuppressWarnings("serial")
public class MessageListenerDocData implements Serializable, MessageListenerDoc.Attributes {

    @Override
    public Attribute<MessageListenerDocKey> key() {
        return AttributeBuilder.stringKey(MessageListenerDocKey.class)
                .get(() -> id)
                .set(value -> id = value)
                .build();
    }

    private String id;

    @Override
    public Attribute<BoundedContextComponentDoc> boundedContextComponentDoc() {
        return AttributeBuilder.simple(BoundedContextComponentDoc.class)
                .fromAutoAdapting(BoundedContextComponentDocData.class)
                .get(() -> boundedContextComponentDoc)
                .set(value -> boundedContextComponentDoc = value)
                .build();
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;

    @Override
    public ListAttribute<String> producedEvents() {
        return AttributeBuilder.list(String.class)
                .withList(producedEvents)
                .build();
    }

    private ArrayList<String> producedEvents = new ArrayList<>();

    @Override
    public Attribute<StepMethodSignature> stepMethodSignature() {
        return AttributeBuilder.simple(StepMethodSignature.class)
                .fromAutoAdapting(StepMethodSignatureData.class)
                .get(() -> stepMethodSignature)
                .set(value -> stepMethodSignature = value)
                .build();
    }

    private StepMethodSignatureData stepMethodSignature;
}
