package poussecafe.environment;

import java.util.HashMap;
import java.util.Map;
import poussecafe.messaging.Message;
import poussecafe.runtime.AttributesValidator;
import poussecafe.runtime.MessageValidator;

import static java.util.Objects.requireNonNull;

public class EnvironmentMessageValidator implements MessageValidator {

    public EnvironmentMessageValidator(Environment environment) {
        requireNonNull(environment);
        this.environment = environment;
    }

    private Environment environment;

    @Override
    public void validOrElseThrow(Message message) {
        Class<?> messageClass = environment.definedMessageClass(message.getClass());
        var validator = validator(messageClass);
        validator.validOrElseThrow(message);
    }

    private synchronized AttributesValidator validator(Class<?> messageClass) {
        return validators.computeIfAbsent(messageClass, this::newValidator);
    }

    private AttributesValidator newValidator(Class<?> messageClass) {
        return new AttributesValidator.Builder()
            .definition(messageClass)
            .build();
    }

    private Map<Class<?>, AttributesValidator> validators = new HashMap<>();
}
