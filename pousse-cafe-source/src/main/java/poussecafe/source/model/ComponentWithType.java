package poussecafe.source.model;

import poussecafe.source.analysis.Name;

class ComponentWithType {

    String name;

    public String simpleName() {
        return name;
    }

    String packageName;

    public String packageName() {
        return packageName;
    }

    public Name name() {
        return new Name(packageName, name);
    }

    ComponentWithType() {

    }
}
