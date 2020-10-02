package poussecafe.doc.model.entitydoc;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.DomainException;
import poussecafe.domain.Entity;

public class EntityDocFactory extends AggregateFactory<EntityDocId, EntityDoc, EntityDoc.Attributes> {

    public EntityDoc newEntityDoc(ModuleDocId moduleDocId, TypeElement entityClassDoc) {
        if(!isEntityDoc(entityClassDoc)) {
            throw new DomainException("Class " + entityClassDoc.getQualifiedName() + " is not an entity");
        }

        String name = name(entityClassDoc);
        EntityDocId id = EntityDocId.ofClassName(entityClassDoc.getQualifiedName().toString());
        EntityDoc entityDoc = newAggregateWithId(id);
        entityDoc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(name, entityClassDoc))
                .build());

        entityDoc.idClassName(idClassName(entityClassDoc));

        return entityDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public String idClassName(TypeElement aggregateClassDoc) {
        DeclaredType superclass = (DeclaredType) aggregateClassDoc.getSuperclass();
        TypeElement keyType = (TypeElement) docletEnvironment.getTypeUtils().asElement(superclass.getTypeArguments().get(KEY_TYPE_INDEX));
        return keyType.getQualifiedName().toString();
    }

    private DocletEnvironment docletEnvironment;

    private static final int KEY_TYPE_INDEX = 0;

    public boolean isEntityDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Entity.class);
    }

    private ClassDocPredicates classDocPredicates;

    public static String name(TypeElement classDoc) {
        return classDoc.getSimpleName().toString();
    }
}
