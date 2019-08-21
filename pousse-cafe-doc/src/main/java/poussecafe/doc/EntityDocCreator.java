package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.process.EntityDocCreation;

public class EntityDocCreator extends ModuleComponentDocCreator {

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
    protected void addDoc(ModuleDocId moduleDocId,
            TypeElement componentClassDoc) {
        entityDocCreation.addEntityDoc(moduleDocId, componentClassDoc);
    }

    private EntityDocCreation entityDocCreation;
}
