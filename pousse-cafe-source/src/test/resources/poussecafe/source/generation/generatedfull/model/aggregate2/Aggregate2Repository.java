package poussecafe.source.generation.generatedfull.model.aggregate2;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRepository;
import poussecafe.source.generation.generatedfull.commands.Command2;
import poussecafe.source.generation.generatedfull.process.Process1;

public class Aggregate2Repository extends AggregateRepository<Aggregate2, Aggregate2Id, Aggregate2.Attributes> {

    @Override
    public Aggregate2DataAccess<Aggregate2.Attributes> dataAccess() {
        return (Aggregate2DataAccess<Aggregate2.Attributes>) super.dataAccess();
    }

    @MessageListener(processes = Process1.class)
    public void process1Listener3(Command2 command) {
        // TODO: delete aggregate(s)
    }
}