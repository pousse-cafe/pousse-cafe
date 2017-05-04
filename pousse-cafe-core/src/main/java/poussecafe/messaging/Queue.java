package poussecafe.messaging;

public class Queue {

    public static final String DEFAULT_COMMAND_QUEUE_NAME = "commands";

    public static final Queue DEFAULT_COMMAND_QUEUE = Queue.forName(DEFAULT_COMMAND_QUEUE_NAME);

    public static final String DEFAULT_DOMAIN_EVENT_QUEUE_NAME = "domainEvents";

    public static final Queue DEFAULT_DOMAIN_EVENT_QUEUE = Queue.forName(DEFAULT_DOMAIN_EVENT_QUEUE_NAME);

    public static Queue forName(String name) {
        return new Queue(name);
    }

    private String name;

    private Queue(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Queue other = (Queue) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Queue [name=" + name + "]";
    }

}
