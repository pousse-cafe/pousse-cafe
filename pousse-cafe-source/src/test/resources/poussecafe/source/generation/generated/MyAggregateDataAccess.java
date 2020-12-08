package poussecafe.source.generation.generated;

import poussecafe.domain.EntityDataAccess;

public interface MyAggregateDataAccess<D extends MyAggregateRoot.Attributes> extends
        EntityDataAccess<MyAggregateId, D> {
}