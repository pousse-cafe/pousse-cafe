package poussecafe.doc.model.entitydoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.domain.DomainException;
import poussecafe.domain.Entity;
import poussecafe.domain.Factory;

public class EntityDocFactory extends Factory<EntityDocKey, EntityDoc, EntityDoc.Data> {

    public EntityDoc newEntityDoc(AggregateDocKey aggregateDocKey, ClassDoc entityClassDoc) {
        if(!isEntityDoc(entityClassDoc)) {
            throw new DomainException("Class " + entityClassDoc.name() + " is not an entity");
        }

        String name = entityClassDoc.simpleTypeName();
        EntityDocKey key = new EntityDocKey(aggregateDocKey, name);
        EntityDoc serviceDoc = newStorableWithKey(key);
        serviceDoc.description(entityClassDoc.commentText());
        return serviceDoc;
    }

    public static boolean isEntityDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, Entity.class);
    }
}
