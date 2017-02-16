package poussecafe.sample.workflow;

import poussecafe.process.Final;
import poussecafe.process.State;

public class WaitOrderCreated extends State {
    public Final toFinal() {
        return new Final();
    }
}