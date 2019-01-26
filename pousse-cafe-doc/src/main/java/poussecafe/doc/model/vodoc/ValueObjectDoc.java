package poussecafe.doc.model.vodoc;

import java.util.Objects;
import poussecafe.context.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

@Aggregate(
  factory = ValueObjectDocFactory.class,
  repository = ValueObjectDocRepository.class
)
public class ValueObjectDoc extends AggregateRoot<ValueObjectDocKey, ValueObjectDoc.Data> {

    void boundedContextComponentDoc(BoundedContextComponentDoc componentDoc) {
        Objects.requireNonNull(componentDoc);
        data().boundedContextComponentDoc().set(componentDoc);
    }

    public BoundedContextComponentDoc boundedContextComponentDoc() {
        return data().boundedContextComponentDoc().get();
    }

    public String id() {
        return StringNormalizer.normalizeString(boundedContextComponentDoc().componentDoc().name());
    }

    public static interface Data extends EntityData<ValueObjectDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
