package poussecafe.source;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class AggregateRootSource {

    private String name;

    public String name() {
        return name;
    }

    private Path filePath;

    public Path filePath() {
        return filePath;
    }

    private Map<String, MessageListenerSource> messageListeners = new HashMap<>();

    public Optional<MessageListenerSource> messageListener(String name, String messageName) {
        String listenerId = listenerId(name, messageName);
        return Optional.ofNullable(messageListeners.get(listenerId));
    }

    private static String listenerId(String methodName, String messageName) {
        return methodName + messageName;
    }

    public static class Builder {

        private AggregateRootSource source = new AggregateRootSource();

        public AggregateRootSource build() {
            requireNonNull(source.name);
            requireNonNull(source.filePath);
            return source;
        }

        public Builder name(String name) {
            source.name = name;
            return this;
        }

        public Builder filePath(Path filePath) {
            source.filePath = filePath;
            return this;
        }

        public Builder withListener(MessageListenerSource build) {
            source.messageListeners.put(listenerId(build.methodName(), build.messageName()), build);
            return this;
        }
    }

    private AggregateRootSource() {

    }
}
