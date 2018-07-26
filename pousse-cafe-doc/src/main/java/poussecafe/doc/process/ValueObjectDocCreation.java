package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.process.DomainProcess;

public class ValueObjectDocCreation extends DomainProcess {

    public void addValueObjectDoc(BoundedContextDocKey boundedContextKey, ClassDoc valueObjectClassDoc) {
        ValueObjectDoc entityDoc = valueObjectDocFactory.newValueObjectDoc(boundedContextKey, valueObjectClassDoc);
        runInTransaction(ValueObjectDoc.class, () -> valueObjectDocRepository.add(entityDoc));
    }

    private ValueObjectDocFactory valueObjectDocFactory;

    private ValueObjectDocRepository valueObjectDocRepository;
}
