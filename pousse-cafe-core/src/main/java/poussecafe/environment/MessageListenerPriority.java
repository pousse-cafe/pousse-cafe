package poussecafe.environment;

public class MessageListenerPriority {

    public static final int REPOSITORY = 0;

    public static final int AGGREGATE = 1;

    public static final int FACTORY = 2;

    public static final int DOMAIN_PROCESS = 3;

    public static final int CUSTOM = 4;

    private MessageListenerPriority() {

    }
}
