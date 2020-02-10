package poussecafe.doc.model.aggregatedoc;

import java.util.Optional;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.annotations.AnnotationUtils;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.ClassDocRepository;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.DocletAccess;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;

public class AggregateDocFactory extends Factory<AggregateDocId, AggregateDoc, AggregateDoc.Attributes> {

    /**
     * @process AggregateDocCreation
     */
    @MessageListener
    public AggregateDoc newAggregateDoc(CreateAggregateDoc command) {
        String className = command.className().value();
        TypeElement aggregateClassDoc = classDocRepository.getClassDoc(className);
        if(!isAggregateDoc(aggregateClassDoc)) {
            throw new DomainException("Class " + aggregateClassDoc.getQualifiedName() + " is not an aggregate root");
        }

        AggregateDocId id = AggregateDocId.ofClassName(aggregateClassDoc.getQualifiedName().toString());
        AggregateDoc aggregateDoc = newAggregateWithId(id);

        String name = id.name();
        ModuleDocId moduleDocId = command.moduleId().value();
        checkAggregateRootPackage(aggregateClassDoc, moduleDocId);
        aggregateDoc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(name, aggregateClassDoc))
                .build());

        aggregateDoc.attributes().idClassName().value(entityDocFactory.idClassName(aggregateClassDoc));

        return aggregateDoc;
    }

    private ClassDocRepository classDocRepository;

    private void checkAggregateRootPackage(TypeElement aggregateClassDoc, ModuleDocId moduleDocId) {
        Optional<AnnotationMirror> aggregateAnnotation = AnnotationUtils.annotation(aggregateClassDoc, Aggregate.class);
        if(aggregateAnnotation.isPresent()) {
            Optional<AnnotationValue> value = AnnotationUtils.value(aggregateAnnotation.get(), "module");
            if(value.isPresent()) {
                Element moduleClass = docletAccess.getTypesUtils().asElement((TypeMirror) value.get().getValue());
                PackageElement moduleClassPackage = (PackageElement) moduleClass.getEnclosingElement();
                PackageElement aggregateRootPackage = (PackageElement) aggregateClassDoc.getEnclosingElement();
                if(!aggregateRootPackage.getQualifiedName().toString().startsWith(moduleClassPackage.getQualifiedName().toString())) {
                    throw new DomainException("Class " + aggregateClassDoc.getQualifiedName() + " is in the wrong package");
                }
                if(!moduleClassPackage.getQualifiedName().toString().equals(moduleDocId.stringValue())) {
                    throw new DomainException(aggregateClassDoc.getQualifiedName() + " is in 2 different modules, mixing package-info and class based module definition? "
                            + moduleClassPackage.getSimpleName().toString() + " <> " + moduleDocId.stringValue());
                }
            }
        }
    }

    private DocletAccess docletAccess;

    private ComponentDocFactory componentDocFactory;

    private EntityDocFactory entityDocFactory;

    public boolean isAggregateDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, AggregateRoot.class);
    }

    private ClassDocPredicates classDocPredicates;

    public boolean isFactoryDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Factory.class);
    }

    public TypeElement aggregateTypeElementOfFactory(TypeElement factoryTypeElement) {
        if(!isFactoryDoc(factoryTypeElement)) {
            throw new IllegalArgumentException();
        }
        DeclaredType superclass = (DeclaredType) factoryTypeElement.getSuperclass();
        return (TypeElement) docletEnvironment.getTypeUtils().asElement(superclass.getTypeArguments().get(FACTORY_AGGREGATE_TYPE_INDEX));
    }

    private DocletEnvironment docletEnvironment;

    private static final int FACTORY_AGGREGATE_TYPE_INDEX = 1;

    public boolean isRepositoryDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Repository.class);
    }

    public TypeElement aggregateTypeElementOfRepository(TypeElement repositoryTypeElement) {
        if(!isRepositoryDoc(repositoryTypeElement)) {
            throw new IllegalArgumentException();
        }
        DeclaredType superclass = (DeclaredType) repositoryTypeElement.getSuperclass();
        return (TypeElement) docletEnvironment.getTypeUtils().asElement(superclass.getTypeArguments().get(REPOSITORY_AGGREGATE_TYPE_INDEX));
    }

    private static final int REPOSITORY_AGGREGATE_TYPE_INDEX = 0;
}
