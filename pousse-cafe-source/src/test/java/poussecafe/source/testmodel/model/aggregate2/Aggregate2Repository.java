package poussecafe.source.testmodel.model.aggregate2;

import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRepository;
import poussecafe.source.testmodel.commands.Command2;
import poussecafe.source.testmodel.model.events.Event6;
import poussecafe.source.testmodel.process.Process1;

public class Aggregate2Repository extends AggregateRepository<String, Aggregate2Root, Aggregate2Root.Attributes> {

    @MessageListener(processes = Process1.class)
    @ProducesEvent(Event6.class)
    public void process1Listener3(Command2 event) {

    }
}
