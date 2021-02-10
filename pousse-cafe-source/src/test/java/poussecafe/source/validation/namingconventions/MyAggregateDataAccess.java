package poussecafe.source.validation.namingconventions;

import poussecafe.domain.EntityDataAccess;

public interface MyAggregateDataAccess<D extends MyAggregate.Root.Attributes>
extends EntityDataAccess<String, D> {

}
