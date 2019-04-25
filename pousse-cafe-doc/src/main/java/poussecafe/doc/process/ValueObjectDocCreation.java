package poussecafe.doc.process;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.process.DomainProcess;

public class ValueObjectDocCreation extends DomainProcess {

    public void addValueObjectDoc(BoundedContextDocId boundedContextId, TypeElement valueObjectClassDoc) {
        ValueObjectDoc entityDoc = valueObjectDocFactory.newValueObjectDoc(boundedContextId, valueObjectClassDoc);
        runInTransaction(ValueObjectDoc.class, () -> valueObjectDocRepository.add(entityDoc));
    }

    private ValueObjectDocFactory valueObjectDocFactory;

    private ValueObjectDocRepository valueObjectDocRepository;
}
