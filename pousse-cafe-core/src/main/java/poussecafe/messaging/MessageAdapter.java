package poussecafe.messaging;

import poussecafe.journal.data.SerializedMessage;

public interface MessageAdapter {

    Message adaptSerializedMessage(SerializedMessage serializedMessage);

    SerializedMessage adaptMessage(Message message);
}
