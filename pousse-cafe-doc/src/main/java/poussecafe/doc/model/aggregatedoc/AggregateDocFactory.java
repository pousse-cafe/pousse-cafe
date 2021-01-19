package poussecafe.doc.model.aggregatedoc;

import java.util.Optional;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.Logger;
import poussecafe.doc.annotations.AnnotationUtils;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.ClassDocRepository;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.process.AggregateDocCreation;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.source.generation.NamingConventions;

public class AggregateDocFactory extends AggregateFactory<AggregateDocId, AggregateDoc, AggregateDoc.Attributes> {

    @MessageListener(processes = AggregateDocCreation.class)
    public AggregateDoc newAggregateDoc(CreateAggregateDoc command) {
        String className = command.className().value();
        TypeElement aggregateClassDoc = classDocRepository.getClassDoc(className);
        if(!isAggregateDoc(aggregateClassDoc)) {
            throw new DomainException("Class " + aggregateClassDoc.getQualifiedName() + " is not an aggregate root");
        }

        AggregateDocId id = AggregateDocId.ofClassName(aggregateClassDoc.getQualifiedName().toString());
        AggregateDoc aggregateDoc = newAggregateWithId(id);

        String name = name(aggregateClassDoc);
        ModuleDocId moduleDocId = command.moduleId().value();
        aggregateDoc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(name, aggregateClassDoc))
                .build());

        if(isStandaloneRoot(aggregateClassDoc)) {
            aggregateDoc.attributes().idClassName().value(entityDocFactory.idClassName(aggregateClassDoc));
        } else {
            String idClassName = innerRoot(aggregateClassDoc).map(entityDocFactory::idClassName).orElseThrow();
            aggregateDoc.attributes().idClassName().value(idClassName);
        }

        return aggregateDoc;
    }

    private ClassDocRepository classDocRepository;

    private String name(TypeElement aggregateClassDoc) {
        var simpleClassName = aggregateClassDoc.getSimpleName().toString();
        if(isStandaloneRoot(aggregateClassDoc)) {
            try {
                return NamingConventions.aggregateNameFromSimpleRootName(simpleClassName);
            } catch (IllegalArgumentException e) {
                Logger.warn("Aggregate root not following naming conventions: {}", simpleClassName);
                return simpleClassName;
            }
        } else {
            return simpleClassName;
        }
    }

    private ComponentDocFactory componentDocFactory;

    private EntityDocFactory entityDocFactory;

    public Optional<TypeElement> innerRoot(TypeElement aggregateClassDoc) {
        for(Element element : aggregateClassDoc.getEnclosedElements()) {
            if(element instanceof TypeElement) {
                var typeElement = (TypeElement) element;
                if(extendsAggregateRoot(typeElement)) {
                    return Optional.of(typeElement);
                }
            }
        }
        return Optional.empty();
    }

    public boolean isAggregateDoc(TypeElement classDoc) {
        return isStandaloneRoot(classDoc)
                || isContainer(classDoc);
    }

    public boolean isStandaloneRoot(TypeElement classDoc) {
        return !isEnclosedType(classDoc)
                && extendsAggregateRoot(classDoc);
    }

    public boolean extendsAggregateRoot(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, AggregateRoot.class);
    }

    private boolean isEnclosedType(TypeElement classDoc) {
        return classDoc.getEnclosingElement().getKind() != ElementKind.PACKAGE;
    }

    public boolean isContainer(TypeElement classDoc) {
        var annotationMirror = AnnotationUtils.annotation(classDoc, Aggregate.class);
        return annotationMirror.isPresent();
    }

    private ClassDocPredicates classDocPredicates;

    public boolean isFactoryDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, AggregateFactory.class);
    }

    public TypeElement aggregateTypeElementOfFactory(TypeElement factoryTypeElement) {
        if(!isFactoryDoc(factoryTypeElement)) {
            throw new IllegalArgumentException();
        }
        if(isEnclosedType(factoryTypeElement)) {
            return (TypeElement) factoryTypeElement.getEnclosingElement();
        } else {
            DeclaredType superclass = (DeclaredType) factoryTypeElement.getSuperclass();
            return (TypeElement) docletEnvironment.getTypeUtils().asElement(superclass.getTypeArguments().get(FACTORY_AGGREGATE_TYPE_INDEX));
        }
    }

    private DocletEnvironment docletEnvironment;

    private static final int FACTORY_AGGREGATE_TYPE_INDEX = 1;

    public boolean isRepositoryDoc(TypeElement classDoc) {
        return isDeprecatedRepository(classDoc)
                || classDocPredicates.documentsWithSuperclass(classDoc, AggregateRepository.class);
    }

    private boolean isDeprecatedRepository(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Repository.class);
    }

    public TypeElement aggregateTypeElementOfRepository(TypeElement repositoryTypeElement) {
        if(!isRepositoryDoc(repositoryTypeElement)) {
            throw new IllegalArgumentException();
        }
        if(isEnclosedType(repositoryTypeElement)) {
            return (TypeElement) repositoryTypeElement.getEnclosingElement();
        } else {
            DeclaredType superclass = (DeclaredType) repositoryTypeElement.getSuperclass();
            if(isDeprecatedRepository(repositoryTypeElement)) {
                return (TypeElement) docletEnvironment.getTypeUtils().asElement(superclass.getTypeArguments().get(DEPRECATED_REPOSITORY_AGGREGATE_TYPE_INDEX));
            } else {
                return (TypeElement) docletEnvironment.getTypeUtils().asElement(superclass.getTypeArguments().get(REPOSITORY_AGGREGATE_TYPE_INDEX));
            }
        }
    }

    private static final int DEPRECATED_REPOSITORY_AGGREGATE_TYPE_INDEX = 0;

    private static final int REPOSITORY_AGGREGATE_TYPE_INDEX = 1;

    public boolean isStandaloneFactory(TypeElement classDoc) {
        return !isEnclosedType(classDoc)
                && (extendsFactory(classDoc)
                        || extendsDeprecatedFactory(classDoc));
    }

    private boolean extendsFactory(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, AggregateFactory.class);
    }

    private boolean extendsDeprecatedFactory(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Factory.class);
    }

    public boolean isStandaloneRepository(TypeElement classDoc) {
        return !isEnclosedType(classDoc)
                && (extendsRepository(classDoc)
                        || extendsDeprecatedRepository(classDoc));
    }

    public TypeElement aggregateTypeElementOfRoot(TypeElement rootTypeElement) {
        if(!extendsAggregateRoot(rootTypeElement)) {
            throw new IllegalArgumentException();
        }
        if(isEnclosedType(rootTypeElement)) {
            return (TypeElement) rootTypeElement.getEnclosingElement();
        } else {
            return rootTypeElement;
        }
    }

    private boolean extendsRepository(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, AggregateRepository.class);
    }

    private boolean extendsDeprecatedRepository(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Repository.class);
    }
}
