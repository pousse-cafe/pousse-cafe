package poussecafe.domain;

import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MessageImplementation {

    public Class<?> getMessageClass() {
        return messageClass;
    }

    private Class<?> messageClass;

    public static class Builder extends AbstractBuilder<MessageImplementation> {

        public Builder() {
            super(new MessageImplementation());
        }

        public Builder withMessageClass(Class<?> messageClass) {
            product().messageClass = messageClass;
            return this;
        }

        public Builder withMessageImplementationClass(Class<?> messageImplementationClass) {
            product().messageImplementationClass = messageImplementationClass;
            return this;
        }

        @Override
        protected void checkProduct(MessageImplementation product) {
            checkThat(value(product.messageClass).notNull().because("Message class cannot be null"));
            checkThat(value(product.messageImplementationClass).notNull().because("Message implementation class cannot be null"));
        }
    }

    private MessageImplementation() {

    }

    private Class<?> messageImplementationClass;

    public Class<?> getMessageImplementationClass() {
        return messageImplementationClass;
    }
}
