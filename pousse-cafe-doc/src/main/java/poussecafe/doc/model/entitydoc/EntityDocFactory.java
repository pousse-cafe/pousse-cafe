package poussecafe.doc.model.entitydoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
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
        EntityDoc entityDoc = newAggregateWithKey(key);
        entityDoc.componentDoc(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextKey)
                .componentDoc(componentDocFactory.buildDoc(name, entityClassDoc))
                .build());

        entityDoc.keyClassName(keyClassName(entityClassDoc));

        return entityDoc;
    }

    private ComponentDocFactory componentDocFactory;

    private String keyClassName(ClassDoc entityClassDoc) {
        return entityClassDoc.superclassType().asParameterizedType().typeArguments()[KEY_TYPE_INDEX].qualifiedTypeName();
    }

    private static final int KEY_TYPE_INDEX = 0;

    public static boolean isEntityDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, Entity.class);
    }

    public static String name(ClassDoc classDoc) {
        return classDoc.simpleTypeName();
    }
}
