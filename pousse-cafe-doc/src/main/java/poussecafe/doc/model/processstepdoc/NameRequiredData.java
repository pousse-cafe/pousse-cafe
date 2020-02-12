package poussecafe.doc.model.processstepdoc;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NameRequiredData implements Serializable {

    public static NameRequiredData adapt(NameRequired nameRequired) {
        NameRequiredData data = new NameRequiredData();
        data.name = nameRequired.name();
        data.required = nameRequired.required();
        return data;
    }

    private String name;

    private boolean required;

    public NameRequired adapt() {
        if(required) {
            return NameRequired.required(name);
        } else {
            return NameRequired.optional(name);
        }
    }
}
