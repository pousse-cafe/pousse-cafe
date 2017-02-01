package poussecafe.consequence;

public class ConsequenceListenerRoutingKey {

    private Source source;

    private Class<? extends Consequence> consequenceClass;

    public ConsequenceListenerRoutingKey(Source source, Class<? extends Consequence> consequenceClass) {
        setSource(source);
        setConsequenceClass(consequenceClass);
    }

    public Source getSource() {
        return source;
    }

    private void setSource(Source source) {
        this.source = source;
    }

    public Class<? extends Consequence> getConsequenceClass() {
        return consequenceClass;
    }

    private void setConsequenceClass(Class<? extends Consequence> consequenceClass) {
        this.consequenceClass = consequenceClass;
    }

    @Override
    public String toString() {
        return "ConsequenceListenerRoutingKey [source=" + source + ", consequenceClass=" + consequenceClass + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consequenceClass == null) ? 0 : consequenceClass.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConsequenceListenerRoutingKey other = (ConsequenceListenerRoutingKey) obj;
        if (consequenceClass == null) {
            if (other.consequenceClass != null) {
                return false;
            }
        } else if (!consequenceClass.equals(other.consequenceClass)) {
            return false;
        }
        if (source == null) {
            if (other.source != null) {
                return false;
            }
        } else if (!source.equals(other.source)) {
            return false;
        }
        return true;
    }

}
