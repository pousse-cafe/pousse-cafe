package poussecafe.doc.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.moduledoc.ModuleDocId;

@MessageImplementation(message = CreateAggregateDoc.class)
@SuppressWarnings("serial")
public class CreateAggregateDocData implements Serializable, CreateAggregateDoc {

    @Override
    public Attribute<ModuleDocId> moduleId() {
        return AttributeBuilder.stringId(ModuleDocId.class)
                .read(() -> moduleDocId)
                .write(value -> moduleDocId = value)
                .build();
    }

    private String moduleDocId;

    @Override
    public Attribute<String> className() {
        return AttributeBuilder.single(String.class)
                .read(() -> className)
                .write(value -> className = value)
                .build();
    }

    private String className;
}
