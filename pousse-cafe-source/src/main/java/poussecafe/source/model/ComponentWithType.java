package poussecafe.source.model;

import java.io.Serializable;
import poussecafe.source.Source;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.SafeClassName;

@SuppressWarnings("serial")
public class ComponentWithType implements Serializable, WithTypeComponent {

    protected String name;

    public String simpleName() {
        return name;
    }

    protected String packageName;

    public String packageName() {
        return packageName;
    }

    public ClassName name() {
        return new ClassName(packageName, name);
    }

    protected Source source;

    public Source source() {
        return source;
    }

    protected ComponentWithType() {

    }

    @Override
    public TypeComponent typeComponent() {
        return new TypeComponent.Builder()
                .name(SafeClassName.ofRootClass(name()))
                .source(source())
                .build();
    }
}
