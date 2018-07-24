package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ToStepData implements Serializable {

    public static ToStepData of(ToStep to) {
        ToStepData data = new ToStepData();
        data.name = to.name();
        data.directly = to.directly();
        return data;
    }

    public String name;

    public boolean directly;

    public ToStep toModel() {
        return new ToStep.Builder()
                .name(name)
                .directly(directly)
                .build();
    }
}
