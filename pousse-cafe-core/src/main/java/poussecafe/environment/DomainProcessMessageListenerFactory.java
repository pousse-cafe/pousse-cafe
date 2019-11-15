package poussecafe.environment;

import java.lang.reflect.Method;
import poussecafe.exception.PousseCafeException;
import poussecafe.process.DomainProcess;
import poussecafe.util.MethodInvoker;

public class DomainProcessMessageListenerFactory {

    public DomainProcessMessageListenerFactory(Environment environment) {
        this.environment = environment;
    }

    public MessageListener buildMessageListener(MessageListenerDefinition definition) {
        Method method = definition.method();
        @SuppressWarnings("unchecked")
        DomainProcess target = environment.domainProcess((Class<? extends DomainProcess>) definition.method().getDeclaringClass()).orElseThrow(PousseCafeException::new);
        MethodInvoker invoker = new MethodInvoker.Builder()
                .method(method)
                .target(target)
                .build();
        return definition.messageListenerBuilder()
                .priority(MessageListenerType.DOMAIN_PROCESS)
                .consumer(buildConsumer(invoker, definition.shortId()))
                .collisionSpace(definition.collisionSpace())
                .build();
    }

    private Environment environment;

    private MessageConsumer buildConsumer(MethodInvoker invoker, String listenerId) {
        return message -> {
            invoker.invoke(message);
            return MessageListenerConsumptionReport.success(listenerId);
        };
    }
}
