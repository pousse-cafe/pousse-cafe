package poussecafe.process;

import java.util.function.Function;
import poussecafe.consequence.Command;
import poussecafe.storable.UnitOfConsequence;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class StateMachine {

    private State currentState;

    private transient UnitOfConsequence unitOfConsequence;

    public State getCurrentState() {
        wireCurrentState();
        return currentState;
    }

    private void wireCurrentState() {
        currentState.setUnitOfConsequence(unitOfConsequence);
        currentState.setStateMachine(this);
    }

    protected void setCurrentState(State state) {
        checkThat(value(state).notNull().because("Current state cannot be null"));
        currentState = state;
    }

    void setUnitOfConsequence(UnitOfConsequence unitOfConsequence) {
        this.unitOfConsequence = unitOfConsequence;
    }

    protected void addCommand(Command command) {
        unitOfConsequence.addConsequence(command);
    }

    public void executeTransition(Function<State, State> transitionChooser) {
        currentState = transitionChooser.apply(getCurrentState());
    }

    public void start() {
        Init initialState = initialState();
        setCurrentState(initialState);
        wireCurrentState();
        initialState.start();
    }

    protected abstract Init initialState();
}
