package poussecafe.doc.model.entitydoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Entity;
import poussecafe.domain.Factory;

public class EntityDocFactory extends Factory<EntityDocId, EntityDoc, EntityDoc.Attributes> {

    public EntityDoc newEntityDoc(BoundedContextDocId boundedContextId, ClassDoc entityClassDoc) {
        if(!isEntityDoc(entityClassDoc)) {
            throw new DomainException("Class " + entityClassDoc.name() + " is not an entity");
        }

        String name = name(entityClassDoc);
        EntityDocId id = EntityDocId.ofClassName(entityClassDoc.qualifiedTypeName());
        EntityDoc entityDoc = newAggregateWithId(id);
        entityDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextId)
                .componentDoc(componentDocFactory.buildDoc(name, entityClassDoc))
                .build());

        entityDoc.idClassName(idClassName(entityClassDoc));

        return entityDoc;
    }

    private ComponentDocFactory componentDocFactory;

    private String idClassName(ClassDoc entityClassDoc) {
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
