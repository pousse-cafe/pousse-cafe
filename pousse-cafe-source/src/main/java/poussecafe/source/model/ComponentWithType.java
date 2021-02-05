package poussecafe.source.model;

import java.io.Serializable;
import poussecafe.source.analysis.Name;

@SuppressWarnings("serial")
public class ComponentWithType implements Serializable {

    protected String name;

    public String simpleName() {
        return name;
    }

    protected String packageName;

    public String packageName() {
        return packageName;
    }

    public Name name() {
        return new Name(packageName, name);
    }

    protected ComponentWithType() {

    }
}
