package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.process.FactoryDocCreation;

public class FactoryDocCreator extends ModuleComponentDocCreator {

    public FactoryDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return factoryDocFactory.isFactoryDoc(classDoc);
    }

    private FactoryDocFactory factoryDocFactory;

    @Override
    protected String componentName() {
        return "factory";
    }

    @Override
    protected void addDoc(ModuleDocId moduleDocId,
            TypeElement componentClassDoc) {
        factoryDocCreation.addFactoryDoc(moduleDocId, componentClassDoc);
    }

    private FactoryDocCreation factoryDocCreation;
}
