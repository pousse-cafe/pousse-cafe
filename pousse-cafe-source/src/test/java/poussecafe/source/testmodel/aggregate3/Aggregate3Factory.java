package poussecafe.source.testmodel.aggregate3;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;
import poussecafe.source.testmodel.events.Event2;
import poussecafe.source.testmodel.process.Process2;

public class Aggregate3Factory extends Factory<String, Aggregate3, Aggregate3.Attributes> {

    @MessageListener(processes = Process2.class)
    public Aggregate3 process2Listener0(Event2 command) {
        return null;
    }
}
