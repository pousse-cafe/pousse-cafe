package poussecafe.source.generation.generatedinternal;

import poussecafe.domain.EntityDataAccess;

public interface MyAggregateDataAccess<D extends MyAggregateRoot.Attributes> extends
        EntityDataAccess<MyAggregateId, D> {
}