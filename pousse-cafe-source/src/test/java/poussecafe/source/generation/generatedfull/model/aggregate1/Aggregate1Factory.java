package poussecafe.source.generation.generatedfull.model.aggregate1;

import java.util.Collection;
import java.util.Optional;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;
import poussecafe.source.generation.generatedfull.commands.Command1;
import poussecafe.source.generation.generatedfull.commands.Command3;
import poussecafe.source.generation.generatedfull.commands.Command4;
import poussecafe.source.generation.generatedfull.process.Process1;

public class Aggregate1Factory extends Factory<Aggregate1Id, Aggregate1, Aggregate1.Attributes> {

    @MessageListener(processes = Process1.class)
    public Aggregate1 process1Listener0(Command1 command) {
        // TODO: build aggregate
        return null;
    }

    @MessageListener(processes = Process1.class)
    public Optional<Aggregate1> process1Listener4(Command3 command) {
        // TODO: build optional aggregate
        return null;
    }

    @MessageListener(processes = Process1.class)
    public Collection<Aggregate1> process1Listener5(Command4 command) {
        // TODO: build aggregate(s)
        return null;
    }
}