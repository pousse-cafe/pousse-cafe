package poussecafe.context;

import poussecafe.messaging.DomainEventListener;
import poussecafe.process.DomainProcess;

public class DummyProcess extends DomainProcess {

    @DomainEventListener
    public void domainEventListenerWithDefaultId(TestDomainEvent event) {

    }

    @DomainEventListener(id = "customDomainEventListenerId")
    public void domainEventListenerWithCustomId(TestDomainEvent command) {

    }

    public void notListener(String parameter) {

    }

}
