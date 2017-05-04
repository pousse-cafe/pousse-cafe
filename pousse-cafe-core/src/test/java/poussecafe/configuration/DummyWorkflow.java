package poussecafe.configuration;

import poussecafe.messaging.CommandListener;
import poussecafe.messaging.DomainEventListener;
import poussecafe.service.Workflow;

public class DummyWorkflow extends Workflow {

    @DomainEventListener
    public void domainEventListenerWithDefaultId(TestDomainEvent event) {

    }

    @DomainEventListener(id = "customDomainEventListenerId")
    public void domainEventListenerWithCustomId(TestDomainEvent command) {

    }

    @CommandListener
    public void commandListenerWithDefaultId(TestCommand command) {

    }

    @CommandListener(id = "customCommandListenerId")
    public void commandListenerWithCustomId(AnotherTestCommand parameter) {

    }

    public void notListener(String parameter) {

    }

}
