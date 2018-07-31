package poussecafe.doc.model.step;

import java.io.Serializable;
import java.util.ArrayList;
import poussecafe.doc.model.ComponentDocData;

@SuppressWarnings("serial")
public class StepDocData implements Serializable {

    public static StepDocData of(StepDoc from) {
        StepDocData data = new StepDocData();
        data.methodSignature = from.methodSignature().toString();
        data.producedEvents = new ArrayList<>(from.producedEvents());
        data.componentDoc = ComponentDocData.of(from.componentDoc());
        return data;
    }

    public String methodSignature;

    public ArrayList<String> producedEvents;

    public ComponentDocData componentDoc;

    public StepDoc toModel() {
        return new StepDoc.Builder()
                .methodSignature(StepMethodSignature.parse(methodSignature))
                .producedEvents(producedEvents)
                .componentDoc(componentDoc.toModel())
                .build();
    }
}
