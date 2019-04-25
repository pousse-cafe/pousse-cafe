package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.runtime.Runtime;

public class AggregateDocCreator extends BoundedContextComponentDocCreator {

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
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            TypeElement componentClassDoc) {
        CreateAggregateDoc command = runtime.newCommand(CreateAggregateDoc.class);
        command.boundedContextId().value(boundedContextDocId);
        command.className().value(componentClassDoc.getQualifiedName().toString());
        runtime.submitCommand(command);
    }

    private Runtime runtime;
}
