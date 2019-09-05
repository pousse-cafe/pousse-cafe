package poussecafe.messaging.internal;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.domain.EntityAttributes;

public interface ChainElementAttributes<I> extends EntityAttributes<I> {

    Attribute<Boolean> touched();

    OptionalAttribute<NextChainElementId> next();
}
