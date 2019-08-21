package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.runtime.Runtime;

public class AggregateDocCreator extends ModuleComponentDocCreator {

    public AggregateDocCreator(DocletEnvironment environment) {
        super(environment);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return aggregateDocFactory.isAggregateDoc(classDoc);
    }

    private AggregateDocFactory aggregateDocFactory;

    @Override
    protected String componentName() {
        return "aggregate";
    }

    @Override
    protected void addDoc(ModuleDocId moduleDocId,
            TypeElement componentClassDoc) {
        CreateAggregateDoc command = runtime.newCommand(CreateAggregateDoc.class);
        command.moduleId().value(moduleDocId);
        command.className().value(componentClassDoc.getQualifiedName().toString());
        runtime.submitCommand(command);
    }

    private Runtime runtime;
}
