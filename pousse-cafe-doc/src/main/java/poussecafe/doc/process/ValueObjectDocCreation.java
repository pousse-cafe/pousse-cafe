package poussecafe.doc.process;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.vodoc.ValueObjectDoc;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.process.DomainProcess;

public class ValueObjectDocCreation extends DomainProcess {

    public void addValueObjectDoc(ModuleDocId moduleDocId, TypeElement valueObjectClassDoc) {
        ValueObjectDoc entityDoc = valueObjectDocFactory.newValueObjectDoc(moduleDocId, valueObjectClassDoc);
        runInTransaction(ValueObjectDoc.class, () -> valueObjectDocRepository.add(entityDoc));
    }

    private ValueObjectDocFactory valueObjectDocFactory;

    private ValueObjectDocRepository valueObjectDocRepository;
}
