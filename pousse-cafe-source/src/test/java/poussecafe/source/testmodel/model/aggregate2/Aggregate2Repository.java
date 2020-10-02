package poussecafe.source.testmodel.model.aggregate2;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRepository;
import poussecafe.source.testmodel.commands.Command2;
import poussecafe.source.testmodel.process.Process1;

public class Aggregate2Repository extends AggregateRepository<Aggregate2, String, Aggregate2.Attributes> {

    @MessageListener(processes = Process1.class)
    public void process1Listener3(Command2 event) {

    }
}
