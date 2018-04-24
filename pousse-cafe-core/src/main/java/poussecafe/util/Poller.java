package poussecafe.util;

import java.util.Optional;
import org.slf4j.Logger;
import poussecafe.exception.PousseCafeException;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class Poller<T> {

    public T get() {
        long currentWait = 10;
        try {
            Thread.sleep(currentWait);
        } catch (InterruptedException e) {
            return null;
        }

        int maxRetries = 10;
        for(int i = 0; i < maxRetries; ++i) {
            Optional<T> value = poll();
            if(value.isPresent()) {
                return value.get();
            } else {
                currentWait = currentWait * 2;
                logger.debug("Value not yet available, polling again in {} ms", currentWait);
                try {
                    Thread.sleep(currentWait);
                } catch (InterruptedException e) {
                    return null;
                }
            }
        }

        throw new PousseCafeException("No value retrieved after " + maxRetries + " retries");
    }

    protected abstract Optional<T> poll();

    private Logger logger = getLogger(getClass());
}
