package poussecafe.journal.data;

import poussecafe.util.AbstractBuilder;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class SerializedMessage {

    public String getType() {
        return type;
    }

    private String type;

    public String getData() {
        return data;
    }

    private String data;

    public static class Builder extends AbstractBuilder<SerializedMessage> {

        public Builder() {
            super(new SerializedMessage());
        }

        @Override
        protected void checkProduct(SerializedMessage product) {
            checkThat(value(product.type).notNull().because("Type cannot be null"));
            checkThat(value(product.data).notNull().because("Data cannot be null"));
        }

        public Builder withType(String type) {
            product().type = type;
            return this;
        }

        public Builder withData(String data) {
            product().data = data;
            return this;
        }
    }

    private SerializedMessage() {

    }
}
