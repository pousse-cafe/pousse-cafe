package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.process.ValueObjectDocCreation;

public class ValueObjectDocCreator extends ModuleComponentDocCreator {

    public ValueObjectDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return valueObjectDocFactory.isValueObjectDoc(classDoc);
    }

    private ValueObjectDocFactory valueObjectDocFactory;

    @Override
    protected String componentName() {
        return "value object";
    }

    @Override
    protected void addDoc(ModuleDocId moduleDocId,
            TypeElement componentClassDoc) {
        valueObjectDocCreation.addValueObjectDoc(moduleDocId, componentClassDoc);
    }

    private ValueObjectDocCreation valueObjectDocCreation;
}
