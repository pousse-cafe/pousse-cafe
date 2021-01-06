package poussecafe.source.validation.namingconventions;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class StandaloneAggregateRoot extends AggregateRoot<String, StandaloneAggregateRoot.Attributes> {

    public interface Attributes extends EntityAttributes<String> {

    }
}
