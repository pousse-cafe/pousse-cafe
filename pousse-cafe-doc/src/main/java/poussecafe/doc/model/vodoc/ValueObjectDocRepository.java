package poussecafe.doc.model.vodoc;

import java.util.List;
import poussecafe.domain.AggregateRepository;

public class ValueObjectDocRepository extends AggregateRepository<ValueObjectDocId, ValueObjectDoc, ValueObjectDoc.Attributes> {

    public List<ValueObjectDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    @Override
    public ValueObjectDocDataAccess<ValueObjectDoc.Attributes> dataAccess() {
        return (ValueObjectDocDataAccess<ValueObjectDoc.Attributes>) super.dataAccess();
    }
}
