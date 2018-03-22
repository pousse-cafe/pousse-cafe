package poussecafe.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DomainProcess extends TransactionAwareService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
}
