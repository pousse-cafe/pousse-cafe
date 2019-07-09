package poussecafe.discovery;

import poussecafe.process.DomainProcess;

public abstract class BaseDomainProcess extends DomainProcess {

    @MessageListener
    public void handle(Event1 event) {

    }
}
