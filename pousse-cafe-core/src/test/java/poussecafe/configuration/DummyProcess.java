package poussecafe.configuration;

import poussecafe.messaging.DomainEventListener;
import poussecafe.service.Process;

public class DummyProcess extends Process {

    @DomainEventListener
    public void domainEventListenerWithDefaultId(TestDomainEvent event) {

    }

    @DomainEventListener(id = "customDomainEventListenerId")
    public void domainEventListenerWithCustomId(TestDomainEvent command) {

    }

    public void notListener(String parameter) {

    }

}
