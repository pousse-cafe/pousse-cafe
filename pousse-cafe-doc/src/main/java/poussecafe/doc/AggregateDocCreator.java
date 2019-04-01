package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.commands.CreateAggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.runtime.Runtime;

public class AggregateDocCreator extends BoundedContextComponentDocCreator {

    public AggregateDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return AggregateDocFactory.isAggregateDoc(classDoc);
    }

    @Override
    protected String componentName() {
        return "aggregate";
    }

    @Override
    protected void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc) {
        CreateAggregateDoc command = runtime.newCommand(CreateAggregateDoc.class);
        command.boundedContextKey().value(boundedContextDocKey);
        command.className().value(componentClassDoc.qualifiedName());
        runtime.submitCommand(command);
    }

    private Runtime runtime;
}
