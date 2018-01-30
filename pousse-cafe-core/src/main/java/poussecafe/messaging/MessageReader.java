package poussecafe.messaging;

public interface MessageReader<M extends Message> {

    M read(String data);
}
