package poussecafe.doc.model.moduledoc;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = ModuleDocFactory.class,
    repository = ModuleDocRepository.class
)
public class ModuleDoc extends AggregateRoot<ModuleDocId, ModuleDoc.Attributes> {

    void componentDoc(ComponentDoc componentDoc) {
        attributes().componentDoc().value(componentDoc);
    }

    public String packageName() {
        return attributes().identifier().value().stringValue();
    }

    public String id() {
        return StringNormalizer.normalizeString(attributes().componentDoc().value().name());
    }

    public static interface Attributes extends EntityAttributes<ModuleDocId> {

        Attribute<ComponentDoc> componentDoc();
    }
}
