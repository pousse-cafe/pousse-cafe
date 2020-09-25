package poussecafe.source.generation.generated;

import poussecafe.domain.EntityDataAccess;

public interface MyAggregateDataAccess<D extends MyAggregate.Attributes> extends EntityDataAccess<MyAggregateId, D> {
}