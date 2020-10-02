package poussecafe.source.testmodel.model.aggregate1;

import java.util.List;
import java.util.Optional;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateFactory;
import poussecafe.source.testmodel.commands.Command1;
import poussecafe.source.testmodel.commands.Command3;
import poussecafe.source.testmodel.commands.Command4;
import poussecafe.source.testmodel.process.Process1;

public class Aggregate1Factory extends AggregateFactory<String, Aggregate1, Aggregate1.Attributes> {

    @MessageListener(processes = Process1.class)
    public Aggregate1 process1Listener0(Command1 command) {
        return null;
    }

    @MessageListener(processes = Process1.class)
    public Optional<Aggregate1> process1Listener4(Command3 command) {
        return null;
    }

    @MessageListener(processes = Process1.class)
    public List<Aggregate1> process1Listener5(Command4 command) {
        return null;
    }
}
