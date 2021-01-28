package poussecafe.source.model;

import poussecafe.source.analysis.Name;

public class ComponentWithType {

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
