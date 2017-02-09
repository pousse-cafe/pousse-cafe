package poussecafe.sample.workflow;

import poussecafe.process.Final;
import poussecafe.process.State;

public class WaitOrderCreated extends State {
    public Final toEnd() {
        return new Final();
    }
}