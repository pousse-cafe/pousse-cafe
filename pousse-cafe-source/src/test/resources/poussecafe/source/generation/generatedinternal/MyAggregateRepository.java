package poussecafe.source.generation.generatedinternal;

import poussecafe.domain.AggregateRepository;
import poussecafe.domain.EntityDataAccess;

public class MyAggregateRepository extends
        AggregateRepository<MyAggregateId, MyAggregateRoot, MyAggregateRoot.Attributes> {

    @Override
    public DataAccess<MyAggregateRoot.Attributes> dataAccess() {
        return (DataAccess<MyAggregateRoot.Attributes>) super.dataAccess();
    }

    public static interface DataAccess<D extends MyAggregateRoot.Attributes> extends
            EntityDataAccess<MyAggregateId, D> {
    }
}