package poussecafe.process;

import poussecafe.storable.StorableData;

public interface ProcessManagerData<K> extends StorableData<K> {

    void setCurrentState(String currentState);

    String getCurrentState();

    void setExpectedNextState(String expectedState);

    String getExpectedNextState();
}
