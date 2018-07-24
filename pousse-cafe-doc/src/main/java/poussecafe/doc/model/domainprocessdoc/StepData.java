package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;
import poussecafe.doc.model.ComponentDocData;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("serial")
public class StepData implements Serializable {

    public static StepData of(Step from) {
        StepData data = new StepData();
        data.componentDoc = ComponentDocData.of(from.componentDoc());
        data.tos = from.tos().stream().map(ToStepData::of).collect(toCollection(ArrayList::new));
        data.consumedEvent = from.consumedEvent().orElse(null);
        data.external = from.external();
        return data;
    }

    public ComponentDocData componentDoc;

    public ArrayList<ToStepData> tos;

    public String consumedEvent;

    public boolean external;

    public Step toModel() {
        return new Step.Builder()
                .componentDoc(componentDoc.toModel())
                .tos(tos.stream().map(ToStepData::toModel).collect(toList()))
                .consumedEvent(Optional.ofNullable(consumedEvent))
                .external(external)
                .build();
    }

}
