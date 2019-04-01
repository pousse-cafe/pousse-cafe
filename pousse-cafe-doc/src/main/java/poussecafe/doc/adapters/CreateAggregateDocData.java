package poussecafe.doc.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;

@MessageImplementation(message = CreateAggregateDoc.class)
@SuppressWarnings("serial")
public class CreateAggregateDocData implements Serializable, CreateAggregateDoc {

    @Override
    public Attribute<BoundedContextDocKey> boundedContextKey() {
        return AttributeBuilder.stringKey(BoundedContextDocKey.class)
                .get(() -> boundedContextDocId)
                .set(value -> boundedContextDocId = value)
                .build();
    }

    private String boundedContextDocId;

    @Override
    public Attribute<String> className() {
        return AttributeBuilder.single(String.class)
                .get(() -> className)
                .set(value -> className = value)
                .build();
    }

    private String className;
}
