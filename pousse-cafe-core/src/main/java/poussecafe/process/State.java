package poussecafe.process;

import poussecafe.messaging.Command;
import poussecafe.storable.MessageCollection;

public class State {

    protected transient StateMachine stateMachine;

    protected transient MessageCollection messageCollection;

    void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    void setMessageCollection(MessageCollection messageCollection) {
        this.messageCollection = messageCollection;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    protected void addCommand(Command command) {
        messageCollection.addMessage(command);
    }

}
