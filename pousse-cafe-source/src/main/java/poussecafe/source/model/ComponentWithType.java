package poussecafe.source.model;

import poussecafe.source.analysis.Name;

class ComponentWithType {

    String simpleName;

    public String simpleName() {
        return simpleName;
    }

    String packageName;

    public String packageName() {
        return packageName;
    }

    public Name name() {
        return new Name(packageName, simpleName);
    }

    ComponentWithType() {

    }
}
