package poussecafe.source;

import java.io.Serializable;

public interface WithPersistableState {

    Serializable getSerializableState();

    void loadSerializedState(Serializable state);
}
