package poussecafe.process;

import java.util.function.Function;
import poussecafe.messaging.Command;
import poussecafe.storable.MessageCollection;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class StateMachine {

    private State currentState;

    private transient MessageCollection messageCollection;

    public State getCurrentState() {
        wireCurrentState();
        return currentState;
    }

    private void wireCurrentState() {
        currentState.setMessageCollection(messageCollection);
        currentState.setStateMachine(this);
    }

    protected void setCurrentState(State state) {
        checkThat(value(state).notNull().because("Current state cannot be null"));
        currentState = state;
    }

    void setMessageCollection(MessageCollection messageCollection) {
        this.messageCollection = messageCollection;
    }

    protected void addCommand(Command command) {
        messageCollection.addMessage(command);
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
