package poussecafe.doc.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;

@MessageImplementation(message = CreateAggregateDoc.class)
@SuppressWarnings("serial")
public class CreateAggregateDocData implements Serializable, CreateAggregateDoc {

    @Override
    public Attribute<BoundedContextDocId> boundedContextId() {
        return AttributeBuilder.stringId(BoundedContextDocId.class)
                .read(() -> boundedContextDocId)
                .write(value -> boundedContextDocId = value)
                .build();
    }

    private String boundedContextDocId;

    @Override
    public Attribute<String> className() {
        return AttributeBuilder.single(String.class)
                .read(() -> className)
                .write(value -> className = value)
                .build();
    }

    private String className;
}
