package poussecafe.context;

import poussecafe.messaging.Message;

public class SimpleMessage extends Message {

    SimpleMessage() {

    }

    public SimpleMessage(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    private String payload;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((payload == null) ? 0 : payload.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SimpleMessage other = (SimpleMessage) obj;
        if (payload == null) {
            if (other.payload != null) {
                return false;
            }
        } else if (!payload.equals(other.payload)) {
            return false;
        }
        return true;
    }
}
