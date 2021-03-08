package poussecafe.testclasses;

import poussecafe.discovery.MessageListener;
import poussecafe.process.ExplicitDomainProcess;
import poussecafe.testmodule.TestDomainEvent;

public class DummyProcess extends ExplicitDomainProcess {

    @MessageListener
    public void domainEventListenerWithDefaultId(TestDomainEvent event) {

    }

    @MessageListener(id = "customDomainEventListenerId")
    public void domainEventListenerWithCustomId(TestDomainEvent command) {

    }

    public void notListener(String parameter) {

    }

}
