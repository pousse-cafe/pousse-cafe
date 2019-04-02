package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.process.EntityDocCreation;

public class EntityDocCreator extends BoundedContextComponentDocCreator {

    public EntityDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return EntityDocFactory.isEntityDoc(classDoc);
    }

    @Override
    protected String componentName() {
        return "entity";
    }

    @Override
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            ClassDoc componentClassDoc) {
        entityDocCreation.addEntityDoc(boundedContextDocId, componentClassDoc);
    }

    private EntityDocCreation entityDocCreation;
}
