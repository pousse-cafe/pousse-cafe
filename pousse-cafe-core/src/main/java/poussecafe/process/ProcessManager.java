package poussecafe.process;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import poussecafe.exception.PousseCafeException;
import poussecafe.storable.ActiveStorable;
import poussecafe.storable.StorableData;

public class ProcessManager extends ActiveStorable<ProcessManagerKey, ProcessManager.Data> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(State.class, new StateSerializer())
            .registerTypeAdapter(State.class, new StateDeserializer())
            .registerTypeAdapter(StateMachine.class, new StateMachineSerializer())
            .registerTypeAdapter(StateMachine.class, new StateMachineDeserializer())
            .create();

    private transient StateMachine stateMachine;

    void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
        stateMachine.setUnitOfConsequence(getUnitOfConsequence());
        setStateMachineData(stateMachine);
    }

    private void setStateMachineData(StateMachine stateMachine) {
        getData().setStateMachineData(GSON.toJson(stateMachine, StateMachine.class));
    }

    public boolean isInFinalState() {
        return Final.NAME.equals(getStateMachine().getCurrentState().getName());
    }

    public StateMachine getStateMachine() {
        if (stateMachine != null) {
            return stateMachine;
        } else {
            stateMachine = getStateMachineFromData();
            return stateMachine;
        }
    }

    private StateMachine getStateMachineFromData() {
        try {
            StateMachine stateMachine = GSON.fromJson(getData().getStateMachineData(),
                    StateMachine.class);
            stateMachine.setUnitOfConsequence(getUnitOfConsequence());
            return stateMachine;
        } catch (Exception e) {
            throw new PousseCafeException("Unable to instantiate state machine from data", e);
        }
    }

    public boolean isInErrorState() {
        return ErrorState.NAME.equals(getStateMachine().getCurrentState().getName());
    }

    public ErrorState getErrorState() {
        return (ErrorState) getStateMachine().getCurrentState();
    }

    void refreshStateMachineData() {
        setStateMachineData(stateMachine);
    }

    public interface Data extends StorableData<ProcessManagerKey> {

        void setStateMachineData(String data);

        String getStateMachineData();
    }

}
