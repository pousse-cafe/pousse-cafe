package poussecafe.shop.domain;

import poussecafe.domain.DomainEvent;
import poussecafe.domain.DomainException;
import poussecafe.domain.Service;

public class ContentChooser implements Service {

    public ContentType chooseContent(DomainEvent event) {
        if (event instanceof OrderRejected) {
            return ContentType.ORDER_REJECTED;
        } else if (event instanceof OrderCreated) {
            return ContentType.ORDER_READY_FOR_SETTLEMENT;
        } else if (event instanceof OrderSettled) {
            return ContentType.ORDER_SETTLED;
        } else if (event instanceof OrderReadyForShipping) {
            return ContentType.ORDER_READY_FOR_SHIPMENT;
        } else {
            throw new DomainException("No suitable content found");
        }
    }

}
