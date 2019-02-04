package poussecafe.doc.model.vodoc;

import poussecafe.contextconfigurer.Aggregate;
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

    public String id() {
        return StringNormalizer.normalizeString(data().boundedContextComponentDoc().get().componentDoc().name());
    }

    public static interface Data extends EntityData<ValueObjectDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
