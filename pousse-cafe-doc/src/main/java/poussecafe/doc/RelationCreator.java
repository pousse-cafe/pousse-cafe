package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.function.Consumer;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocKey;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.relation.Component;
import poussecafe.doc.model.relation.ComponentType;
import poussecafe.doc.model.relation.RelationFactory.NewRelationParameters;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocKey;
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
            tryRelationAggregateKey(classDoc);
        }
        if(EntityDocFactory.isEntityDoc(classDoc)) {
            tryRelationEntityKey(classDoc);
        }
        if(AggregateDocFactory.isAggregateDoc(classDoc) || EntityDocFactory.isEntityDoc(classDoc) ||
                ValueObjectDocFactory.isValueObjectDoc(classDoc)) {
            CodeExplorer codeExplorer = new CodeExplorer.Builder()
                    .basePackage(rootDocWrapper.basePackage())
                    .rootClassDoc(classDoc)
                    .classRelationBuilder(this::classRelationBuilder)
                    .build();
            codeExplorer.explore();
        }
    }

    private void tryRelationAggregateKey(ClassDoc classDoc) {
        AggregateDoc aggregateDoc = aggregateDocRepository.find(AggregateDocKey.ofClassName(classDoc.qualifiedTypeName()));
        if(aggregateDoc != null) {
            ValueObjectDoc keyDoc = valueObjectDocRepository.find(ValueObjectDocKey.ofClassName(aggregateDoc.keyClassName()));
            if(keyDoc != null) {
                Logger.debug("Building bi-directional relation between aggregate " + classDoc.qualifiedTypeName() + " and its key " + aggregateDoc.keyClassName());
                NewRelationParameters aggregateKeyParameters = new NewRelationParameters();
                aggregateKeyParameters.fromComponent = component(classDoc);
                aggregateKeyParameters.toComponent = new Component(ComponentType.VALUE_OBJECT, aggregateDoc.keyClassName());
                componentLinking.linkComponents(aggregateKeyParameters);

                NewRelationParameters keyAggregateParameters = new NewRelationParameters();
                keyAggregateParameters.fromComponent = aggregateKeyParameters.toComponent;
                keyAggregateParameters.toComponent = aggregateKeyParameters.fromComponent;
                componentLinking.linkComponents(keyAggregateParameters);
            }
        }
    }

    private AggregateDocRepository aggregateDocRepository;

    private ValueObjectDocRepository valueObjectDocRepository;

    private void tryRelationEntityKey(ClassDoc classDoc) {
        EntityDoc entityDoc = entityDocRepository.find(EntityDocKey.ofClassName(classDoc.qualifiedTypeName()));
        if(entityDoc != null) {
            ValueObjectDoc keyDoc = valueObjectDocRepository.find(ValueObjectDocKey.ofClassName(entityDoc.keyClassName()));
            if(keyDoc != null) {
                Logger.debug("Building relation between entity " + classDoc.qualifiedTypeName() + " and its key " + entityDoc.keyClassName());
                NewRelationParameters entityKeyParameters = new NewRelationParameters();
                entityKeyParameters.fromComponent = component(classDoc);
                entityKeyParameters.toComponent = new Component(ComponentType.VALUE_OBJECT, entityDoc.keyClassName());
                componentLinking.linkComponents(entityKeyParameters);
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
}
