package poussecafe.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import poussecafe.runtime.Runtime;

public class MessageListeningBean implements InitializingBean {

    @Override
    public void afterPropertiesSet()
            throws Exception {
        runtime.registerListenersOf(this);
    }

    @Autowired
    private Runtime runtime;
}
