package poussecafe.messaging;

import poussecafe.journal.SerializedMessage;

public interface MessageWriter<M> {

    SerializedMessage write(M message);
}
