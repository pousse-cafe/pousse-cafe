package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.process.EntityDocCreation;

public class EntityDocCreator extends BoundedContextComponentDocCreator {

    public EntityDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return entityDocFactory.isEntityDoc(classDoc);
    }

    private EntityDocFactory entityDocFactory;

    @Override
    protected String componentName() {
        return "entity";
    }

    @Override
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            TypeElement componentClassDoc) {
        entityDocCreation.addEntityDoc(boundedContextDocId, componentClassDoc);
    }

    private EntityDocCreation entityDocCreation;
}
