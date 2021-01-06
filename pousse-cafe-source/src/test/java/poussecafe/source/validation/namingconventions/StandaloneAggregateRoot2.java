package poussecafe.source.validation.namingconventions;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class StandaloneAggregateRoot2 extends AggregateRoot<String, StandaloneAggregateRoot2.Attributes> {

    public interface Attributes extends EntityAttributes<String> {

    }
}
