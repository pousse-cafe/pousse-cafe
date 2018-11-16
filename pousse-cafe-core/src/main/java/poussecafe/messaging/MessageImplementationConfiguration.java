package poussecafe.messaging;

import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageImplementationConfiguration {

    public Class<?> getMessageClass() {
        return messageClass;
    }

    private Class<?> messageClass;

    public static class Builder extends AbstractBuilder<MessageImplementationConfiguration> {

        public Builder() {
            super(new MessageImplementationConfiguration());
        }

        public Builder withMessageClass(Class<?> messageClass) {
            product().messageClass = messageClass;
            return this;
        }

        public Builder withMessageImplementationClass(Class<?> messageImplementationClass) {
            product().messageImplementationClass = messageImplementationClass;
            return this;
        }

        public Builder withMessaging(Messaging messaging) {
            product().messaging = messaging;
            return this;
        }

        @Override
        protected void checkProduct(MessageImplementationConfiguration product) {
            checkThat(value(product.messageClass).notNull().because("Message class cannot be null"));
            checkThat(value(product.messageImplementationClass).notNull().because("Message implementation class cannot be null"));
            checkThat(value(product.messaging).notNull().because("Messaging cannot be null"));
        }
    }

    private MessageImplementationConfiguration() {

    }

    private Class<?> messageImplementationClass;

    public Class<?> getMessageImplementationClass() {
        return messageImplementationClass;
    }

    private Messaging messaging;

    public Messaging getMessaging() {
        return messaging;
    }
}
