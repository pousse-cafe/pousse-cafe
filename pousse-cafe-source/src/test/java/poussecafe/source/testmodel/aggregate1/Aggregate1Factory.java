package poussecafe.source.testmodel.aggregate1;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;
import poussecafe.source.testmodel.commands.Command1;
import poussecafe.source.testmodel.process.Process1;

public class Aggregate1Factory extends Factory<String, Aggregate1, Aggregate1.Attributes> {

    @MessageListener(processes = Process1.class)
    public Aggregate1 process1Listener0(Command1 command) {
        return null;
    }
}
