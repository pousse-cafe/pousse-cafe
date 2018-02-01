package poussecafe.messaging;

import poussecafe.journal.SerializedMessage;

public interface MessageAdapter {

    Message adaptSerializedMessage(SerializedMessage serializedMessage);

    SerializedMessage adaptMessage(Message message);
}
