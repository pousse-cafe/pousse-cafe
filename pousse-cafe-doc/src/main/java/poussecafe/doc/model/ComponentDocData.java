package poussecafe.doc.model;

import java.io.Serializable;
import java.util.Optional;

@SuppressWarnings("serial")
public class ComponentDocData implements Serializable {

    public static ComponentDocData of(ComponentDoc componentDoc) {
        ComponentDocData data = new ComponentDocData();
        data.name = componentDoc.name();
        data.description = componentDoc.description();
        data.shortDescription = componentDoc.shortDescription().orElse(null);
        data.trivial = componentDoc.trivial();
        return data;
    }

    public String name;

    public String description;

    public String shortDescription;

    public boolean trivial;

    public ComponentDoc toModel() {
        return new ComponentDoc.Builder()
                .name(name)
                .description(description)
                .shortDescription(Optional.ofNullable(shortDescription))
                .trivial(trivial)
                .build();
    }
}
