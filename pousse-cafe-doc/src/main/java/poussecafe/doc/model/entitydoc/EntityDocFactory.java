package poussecafe.doc.model.entitydoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.DomainException;
import poussecafe.domain.Entity;
import poussecafe.domain.Factory;

public class EntityDocFactory extends Factory<EntityDocKey, EntityDoc, EntityDoc.Data> {

    public EntityDoc newEntityDoc(BoundedContextDocKey boundedContextKey, ClassDoc entityClassDoc) {
        if(!isEntityDoc(entityClassDoc)) {
            throw new DomainException("Class " + entityClassDoc.name() + " is not an entity");
        }

        String name = name(entityClassDoc);
        EntityDocKey key = EntityDocKey.ofClassName(entityClassDoc.qualifiedTypeName());
        EntityDoc serviceDoc = newStorableWithKey(key);
        serviceDoc.componentDoc(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextKey)
                .componentDoc(new ComponentDoc.Builder()
                        .name(name)
                        .description(entityClassDoc.commentText())
                        .build())
                .build());
        return serviceDoc;
    }

    public static boolean isEntityDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, Entity.class);
    }

    public static String name(ClassDoc classDoc) {
        return classDoc.simpleTypeName();
    }
}
