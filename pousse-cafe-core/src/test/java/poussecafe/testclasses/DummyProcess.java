package poussecafe.testclasses;

import poussecafe.discovery.MessageListener;
import poussecafe.process.DomainProcess;
import poussecafe.testmodule.TestDomainEvent;

public class DummyProcess extends DomainProcess {

    @MessageListener
    public void domainEventListenerWithDefaultId(TestDomainEvent event) {

    }

    @MessageListener(id = "customDomainEventListenerId")
    public void domainEventListenerWithCustomId(TestDomainEvent command) {

    }

    public void notListener(String parameter) {

    }

}
