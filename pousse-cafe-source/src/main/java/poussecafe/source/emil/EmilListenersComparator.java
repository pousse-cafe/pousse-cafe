package poussecafe.source.emil;

import java.util.Comparator;
import poussecafe.source.model.MessageListener;

public class EmilListenersComparator implements Comparator<MessageListener> {

    @Override
    public int compare(MessageListener o1, MessageListener o2) {
        if(o1.container().type() == o2.container().type()) {
            return compareByAggregateName(o1, o2);
        } else {
            return o1.container().type().compareTo(o2.container().type());
        }
    }

    private int compareByAggregateName(MessageListener o1, MessageListener o2) {
        if(o1.container().aggregateName().isPresent() && o2.container().aggregateName().isPresent()) {
            return o1.container().aggregateName().orElseThrow().compareTo(o2.container().aggregateName().orElseThrow());
        } else if(o1.container().aggregateName().isEmpty() && o2.container().aggregateName().isEmpty()) {
            return o1.methodName().compareTo(o2.methodName());
        } else if(o1.container().aggregateName().isPresent()) {
            return -1;
        } else {
            return 1;
        }
    }
}
