package poussecafe.source.generation.generatedinternal;

import poussecafe.domain.Repository;

public class MyAggregateRepository extends Repository<MyAggregate, MyAggregateId, MyAggregate.Attributes> {

    @Override
    public MyAggregateDataAccess<MyAggregate.Attributes> dataAccess() {
        return (MyAggregateDataAccess<MyAggregate.Attributes>) super.dataAccess();
    }
}