package poussecafe.process;

import poussecafe.consequence.Command;
import poussecafe.consequence.ScheduledConsequence;
import poussecafe.storable.ActiveStorable;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.not;

public class ProcessManager<K, D extends ProcessManagerData<K>> extends ActiveStorable<K, D> {

    public static final String INITIAL_STATE = "init";

    public static final String FINAL_STATE = "final";

    public ProcessManager() {
        getData().setCurrentState(INITIAL_STATE);
    }

    protected void transitionTo(String state) {
        checkThat(value(getData().getCurrentState())
                .verifies(not(equalTo(FINAL_STATE)))
                .because("This process already ended"));
        checkThat(value(state)
                .verifies(equalTo(getData().getExpectedNextState()))
                .because("Next state must match expected"));
        getData().setCurrentState(state);
    }

    protected void emitCommand(Command command) {
        getUnitOfConsequence().addConsequence(command);
    }

    protected void scheduleCommand(ScheduledConsequence scheduledConsequence) {
        getUnitOfConsequence().scheduledConsequence(scheduledConsequence);
    }

    protected void setExpectedNextState(String state) {
        checkThat(value(getData().getCurrentState())
                .verifies(not(equalTo(FINAL_STATE)))
                .because("This process already ended"));
        getData().setExpectedNextState(state);
    }
}
