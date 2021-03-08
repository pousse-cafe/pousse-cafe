package poussecafe.discovery;

import poussecafe.process.ExplicitDomainProcess;

public abstract class BaseDomainProcess extends ExplicitDomainProcess {

    @MessageListener
    public void handle(Event1 event) {

    }
}
