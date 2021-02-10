package poussecafe.source.validation.types;

import poussecafe.domain.EntityDataAccess;

public interface MyAggregateDataAccess<D extends MyAggregate.Root.Attributes> extends EntityDataAccess<String, D> {

}
