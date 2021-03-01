package poussecafe.source.generation.generatedfull.model.aggregate2;

import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRepository;
import poussecafe.source.generation.generatedfull.commands.Command2;
import poussecafe.source.generation.generatedfull.model.events.Event6;
import poussecafe.source.generation.generatedfull.process.Process1;

public class Aggregate2Repository extends AggregateRepository<Aggregate2Id, Aggregate2Root, Aggregate2Root.Attributes> {

    @Override
    public Aggregate2DataAccess<Aggregate2Root.Attributes> dataAccess() {
        return (Aggregate2DataAccess<Aggregate2Root.Attributes>) super.dataAccess();
    }

    @MessageListener(processes = Process1.class)
    @ProducesEvent(Event6.class)
    public Aggregate2Id process1Listener3(Command2 command) {
        // TODO: return identifier(s) of aggregates to delete
    }
}