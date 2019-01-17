package poussecafe.doc.model.vodoc;

import java.util.List;
import poussecafe.domain.Repository;

public class ValueObjectDocRepository extends Repository<ValueObjectDoc, ValueObjectDocKey, ValueObjectDoc.Data> {

    public List<ValueObjectDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    @Override
    public ValueObjectDocDataAccess<ValueObjectDoc.Data> dataAccess() {
        return (ValueObjectDocDataAccess<ValueObjectDoc.Data>) super.dataAccess();
    }
}
