package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ProgramElementDoc;
import java.util.List;
import java.util.function.Consumer;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.relation.Component;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.RelationFactory.NewRelationParameters;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocKey;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.doc.process.ComponentLinking;
import poussecafe.doc.process.ValueObjectDocCreation;

public class RelationCreator implements Consumer<ClassDoc> {

    public RelationCreator(RootDocWrapper rootDocWrapper) {
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    @Override
    public void accept(ClassDoc classDoc) {
        if(AggregateDocFactory.isAggregateDoc(classDoc)) {
            tryRelationAggregateKey(classDoc);
        }
        if(AggregateDocFactory.isAggregateDoc(classDoc) || EntityDocFactory.isEntityDoc(classDoc) ||
                ValueObjectDocFactory.isValueObjectDoc(classDoc)) {
            CodeExplorer codeExplorer = new CodeExplorer.Builder()
                    .basePackage(rootDocWrapper.basePackage())
                    .rootClassDoc(classDoc)
                    .classRelationBuilder(this::classRelationBuilder)
                    .programElementRelationBuilder(this::programElementRelationBuilder)
                    .build();
            codeExplorer.explore();
        }
    }

    private void tryRelationAggregateKey(ClassDoc classDoc) {
        AggregateDoc aggregateDoc = aggregateDocRepository.find(AggregateDocKey.ofClassName(classDoc.qualifiedTypeName()));
        if(aggregateDoc != null) {
            ValueObjectDoc keyDoc = valueObjectDocRepository.find(ValueObjectDocKey.ofClassName(aggregateDoc.keyClassName()));
            if(keyDoc != null) {
                rootDocWrapper.debug("Building relation between aggregate " + classDoc.qualifiedTypeName() + " and its key");
                NewRelationParameters parameters = new NewRelationParameters();
                parameters.fromComponent = component(classDoc);
                parameters.toComponent = new Component(ComponentType.VALUE_OBJECT, aggregateDoc.keyClassName());
                componentLinking.linkComponents(parameters);
            }
        }
    }

    private AggregateDocRepository aggregateDocRepository;

    private ValueObjectDocRepository valueObjectDocRepository;

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
            tryAggregateRelation(from, to);
        }
    }

    private void linkComponents(ClassDoc from,
            ClassDoc to) {
        rootDocWrapper.debug("Building relation between " + from.qualifiedName() + " and " + to.qualifiedName());
        NewRelationParameters parameters = new NewRelationParameters();
        parameters.fromComponent = component(from);
        parameters.toComponent = component(to);
        componentLinking.linkComponents(parameters);
    }

    private void tryAggregateRelation(ClassDoc from, ClassDoc to) {
        if(AggregateDocFactory.isAggregateDoc(from) && ValueObjectDocFactory.isValueObjectDoc(to)) {
            List<AggregateDoc> otherAggregates = aggregateDocRepository.findByKeyClassName(to.qualifiedTypeName());
            for(AggregateDoc otherAggregate : otherAggregates) {
                if(!from.qualifiedTypeName().equals(otherAggregate.className())) {
                    rootDocWrapper.debug("Building relation between " + from.qualifiedName() + " and " + otherAggregate.getKey());
                    NewRelationParameters parameters = new NewRelationParameters();
                    parameters.fromComponent = component(from);
                    parameters.toComponent = new Component(ComponentType.AGGREGATE, otherAggregate.className());
                    componentLinking.linkComponents(parameters);
                }
            }
        }
    }

    private void programElementRelationBuilder(ProgramElementDoc to) {
        rootDocWrapper.debug("Building relation between " + to.containingClass() + " and " + to.qualifiedName());

        BoundedContextDoc boundedContextDoc = boundedContextDocRepository.findByPackageNamePrefixing(to.qualifiedName());
        if(boundedContextDoc != null) {
            valueObjectCreation.addValueObjectDoc(boundedContextDoc.getKey(), to);

            NewRelationParameters parameters = new NewRelationParameters();
            parameters.fromComponent = component(to.containingClass());
            parameters.toComponent = new Component(ComponentType.VALUE_OBJECT, to.qualifiedName());
            componentLinking.linkComponents(parameters);
        }
    }

    private BoundedContextDocRepository boundedContextDocRepository;

    private ValueObjectDocCreation valueObjectCreation;
}
