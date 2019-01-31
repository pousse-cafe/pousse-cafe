package poussecafe.messaging;

import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageImplementation {

    public Class<? extends Message> getMessageClass() {
        return messageClass;
    }

    private Class<? extends Message> messageClass;

    public static class Builder extends AbstractBuilder<MessageImplementation> {

        public Builder() {
            super(new MessageImplementation());
        }

        public Builder withMessageClass(Class<? extends Message> messageClass) {
            product().messageClass = messageClass;
            return this;
        }

        public Builder withMessageImplementationClass(Class<? extends Message> messageImplementationClass) {
            product().messageImplementationClass = messageImplementationClass;
            return this;
        }

        public Builder withMessaging(Messaging messaging) {
            product().messaging = messaging;
            return this;
        }

        @Override
        protected void checkProduct(MessageImplementation product) {
            checkThat(value(product.messageClass).notNull().because("Message class cannot be null"));
            checkThat(value(product.messageImplementationClass).notNull().because("Message implementation class cannot be null"));
            checkThat(value(product.messaging).notNull().because("Messaging cannot be null"));
        }
    }

    private MessageImplementation() {

    }

    private Class<? extends Message> messageImplementationClass;

    public Class<? extends Message> getMessageImplementationClass() {
        return messageImplementationClass;
    }

    private Messaging messaging;

    public Messaging getMessaging() {
        return messaging;
    }
}
