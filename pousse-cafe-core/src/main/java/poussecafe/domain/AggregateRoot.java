package poussecafe.domain;

import poussecafe.consequence.ScheduledConsequence;
import poussecafe.storable.ActiveStorable;

public abstract class AggregateRoot<K, D extends AggregateData<K>> extends ActiveStorable<K, D> {

    protected void addDomainEvent(DomainEvent event) {
        getUnitOfConsequence().addConsequence(event);
    }

    protected void scheduleDomainEvent(ScheduledConsequence event) {
        getUnitOfConsequence().scheduledConsequence(event);
    }
}
