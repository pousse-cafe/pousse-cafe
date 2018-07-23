package poussecafe.doc.model.vodoc;

import java.util.List;
import poussecafe.domain.Repository;

public class ValueObjectDocRepository extends Repository<ValueObjectDoc, ValueObjectDocKey, ValueObjectDoc.Data> {

    public List<ValueObjectDoc> findAll() {
        return newStorablesWithData(dataAccess().findAll());
    }

    private ValueObjectDocDataAccess<ValueObjectDoc.Data> dataAccess() {
        return (ValueObjectDocDataAccess<ValueObjectDoc.Data>) dataAccess;
    }
}
