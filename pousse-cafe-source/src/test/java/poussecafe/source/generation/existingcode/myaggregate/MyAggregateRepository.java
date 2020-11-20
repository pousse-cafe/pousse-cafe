package poussecafe.source.generation.existingcode.myaggregate;

import poussecafe.domain.AggregateRepository;

public class MyAggregateRepository extends AggregateRepository<MyAggregateId, MyAggregate, MyAggregate.Attributes> {

    @Override
    public MyAggregateDataAccess<MyAggregate.Attributes> dataAccess() {
        return (MyAggregateDataAccess<MyAggregate.Attributes>) super.dataAccess();
    }
}
