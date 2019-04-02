package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.function.Consumer;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocId;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.relation.Component;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.RelationFactory.NewRelationParameters;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocId;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.doc.process.ComponentLinking;

public class RelationCreator implements Consumer<ClassDoc> {

    public RelationCreator(RootDocWrapper rootDocWrapper) {
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    @Override
    public void accept(ClassDoc classDoc) {
        if(AggregateDocFactory.isAggregateDoc(classDoc)) {
            tryRelationAggregateId(classDoc);
        }
        if(EntityDocFactory.isEntityDoc(classDoc)) {
            tryRelationEntityId(classDoc);
        }
        if(AggregateDocFactory.isAggregateDoc(classDoc) || EntityDocFactory.isEntityDoc(classDoc)) {
            tryAttributes(classDoc);
        }
        if(ValueObjectDocFactory.isValueObjectDoc(classDoc)) {
            CodeExplorer codeExplorer = new CodeExplorer.Builder()
                    .basePackage(rootDocWrapper.basePackage())
                    .rootClassDoc(classDoc)
                    .classRelationBuilder(this::classRelationBuilder)
                    .build();
            codeExplorer.explore();
        }
    }

    private void tryRelationAggregateId(ClassDoc classDoc) {
        AggregateDoc aggregateDoc = aggregateDocRepository.find(AggregateDocId.ofClassName(classDoc.qualifiedTypeName()));
        if(aggregateDoc != null) {
            ValueObjectDoc idDoc = valueObjectDocRepository.find(ValueObjectDocId.ofClassName(aggregateDoc.attributes().idClassName().value()));
            if(idDoc != null) {
                Logger.debug("Building bi-directional relation between aggregate " + classDoc.qualifiedTypeName() + " and its id " + aggregateDoc.attributes().idClassName().value());
                NewRelationParameters aggregateIdParameters = new NewRelationParameters();
                aggregateIdParameters.fromComponent = component(classDoc);
                aggregateIdParameters.toComponent = new Component(ComponentType.VALUE_OBJECT, aggregateDoc.attributes().idClassName().value());
                componentLinking.linkComponents(aggregateIdParameters);

                NewRelationParameters idAggregateParameters = new NewRelationParameters();
                idAggregateParameters.fromComponent = aggregateIdParameters.toComponent;
                idAggregateParameters.toComponent = aggregateIdParameters.fromComponent;
                componentLinking.linkComponents(idAggregateParameters);
            }
        }
    }

    private AggregateDocRepository aggregateDocRepository;

    private ValueObjectDocRepository valueObjectDocRepository;

    private void tryRelationEntityId(ClassDoc classDoc) {
        EntityDoc entityDoc = entityDocRepository.find(EntityDocId.ofClassName(classDoc.qualifiedTypeName()));
        if(entityDoc != null) {
            ValueObjectDoc idDoc = valueObjectDocRepository.find(ValueObjectDocId.ofClassName(entityDoc.attributes().idClassName().value()));
            if(idDoc != null) {
                Logger.debug("Building relation between entity " + classDoc.qualifiedTypeName() + " and its id " + entityDoc.attributes().idClassName().value());
                NewRelationParameters entityIdParameters = new NewRelationParameters();
                entityIdParameters.fromComponent = component(classDoc);
                entityIdParameters.toComponent = new Component(ComponentType.VALUE_OBJECT, entityDoc.attributes().idClassName().value());
                componentLinking.linkComponents(entityIdParameters);
            }
        }
    }

    private EntityDocRepository entityDocRepository;

    private Component component(ClassDoc classDoc) {
        return new Component(componentType(classDoc), classDoc.qualifiedTypeName());
    }

    private ComponentType componentType(ClassDoc classDoc) {
        if(AggregateDocFactory.isAggregateDoc(classDoc)) {
            return ComponentType.AGGREGATE;
        } else if(EntityDocFactory.isEntityDoc(classDoc)) {
            return ComponentType.ENTITY;
        } else if(ValueObjectDocFactory.isValueObjectDoc(classDoc)) {
            return ComponentType.VALUE_OBJECT;
        } else {
            throw new IllegalArgumentException("Unsupported component class " + classDoc.qualifiedName());
        }
    }

    private ComponentLinking componentLinking;

    private void classRelationBuilder(ClassDoc from, ClassDoc to) {
        if(from != to) {
            linkComponents(from, to);
        }
    }

    private void linkComponents(ClassDoc from,
            ClassDoc to) {
        Logger.debug("Building relation between " + from.qualifiedName() + " and " + to.qualifiedName());
        NewRelationParameters parameters = new NewRelationParameters();
        parameters.fromComponent = component(from);
        parameters.toComponent = component(to);
        componentLinking.linkComponents(parameters);
    }

    private void tryAttributes(ClassDoc classDoc) {
        ClassDoc attributesClassDoc = classDoc.superclassType().asParameterizedType().typeArguments()[1].asClassDoc();
        CodeExplorer pathFinder = new CodeExplorer.Builder()
                .rootClassDoc(classDoc)
                .basePackage(rootDocWrapper.basePackage())
                .classRelationBuilder(this::classRelationBuilder)
                .build();
        pathFinder.explore(attributesClassDoc);
    }
}
