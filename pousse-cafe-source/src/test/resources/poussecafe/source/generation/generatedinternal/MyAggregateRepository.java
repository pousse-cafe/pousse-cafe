package poussecafe.source.generation.generatedinternal;

import poussecafe.domain.AggregateRepository;

public class MyAggregateRepository extends AggregateRepository<MyAggregate, MyAggregateId, MyAggregate.Attributes> {

    @Override
    public MyAggregateDataAccess<MyAggregate.Attributes> dataAccess() {
        return (MyAggregateDataAccess<MyAggregate.Attributes>) super.dataAccess();
    }
}