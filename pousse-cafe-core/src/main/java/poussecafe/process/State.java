package poussecafe.process;

import poussecafe.consequence.Command;
import poussecafe.storable.UnitOfConsequence;

public class State {

    protected transient StateMachine stateMachine;

    protected transient UnitOfConsequence unitOfConsequence;

    void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    void setUnitOfConsequence(UnitOfConsequence unitOfConsequence) {
        this.unitOfConsequence = unitOfConsequence;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    protected void addCommand(Command command) {
        unitOfConsequence.addConsequence(command);
    }

}
