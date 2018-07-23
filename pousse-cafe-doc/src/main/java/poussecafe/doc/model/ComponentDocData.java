package poussecafe.doc.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ComponentDocData implements Serializable {

    public static ComponentDocData of(ComponentDoc componentDoc) {
        ComponentDocData data = new ComponentDocData();
        data.name = componentDoc.name();
        data.description = componentDoc.description();
        return data;
    }

    public String name;

    public String description;

    public String className;

    public ComponentDoc toModel() {
        return new ComponentDoc.Builder()
                .name(name)
                .description(description)
                .build();
    }
}
