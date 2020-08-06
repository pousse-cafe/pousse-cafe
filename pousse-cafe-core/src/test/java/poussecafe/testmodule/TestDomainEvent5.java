package poussecafe.testmodule;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.domain.DomainEvent;

@SuppressWarnings("serial")
@MessageImplementation(message = TestDomainEvent5.class)
public class TestDomainEvent5 implements DomainEvent, Serializable {

    public Attribute<String> identifier() {
        return AttributeBuilder.single(String.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;
}
