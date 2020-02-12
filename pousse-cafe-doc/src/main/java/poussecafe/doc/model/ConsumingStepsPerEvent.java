package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import poussecafe.doc.model.domainprocessdoc.StepName;
import poussecafe.doc.model.processstepdoc.NameRequired;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.processstepdoc.StepMethodSignature;

import static java.util.stream.Collectors.toList;

public class ConsumingStepsPerEvent {

    public static class Builder {

        private ConsumingStepsPerEvent consumingStepsPerEvent = new ConsumingStepsPerEvent();

        public Builder withProcessStepDoc(ProcessStepDoc processStepDoc) {
            Optional<String> consumedEventName;
            Optional<StepMethodSignature> optionalStepMethodSignature = processStepDoc.attributes().stepMethodSignature().value();
            if(optionalStepMethodSignature.isPresent()) {
                consumedEventName = optionalStepMethodSignature.get().consumedEventName();
            } else {
                consumedEventName = Optional.empty();
            }

            if(consumedEventName.isPresent()) {
                String presentConsumedEventName = consumedEventName.get();
                List<String> consumingSteps = consumingStepsPerEvent.eventToConsumingStepsMap.computeIfAbsent(presentConsumedEventName, key -> new ArrayList<>());
                consumingSteps.add(processStepDoc.attributes().moduleComponentDoc().value().componentDoc().name());
            }
            return this;
        }

        public ConsumingStepsPerEvent build() {
            return consumingStepsPerEvent;
        }
    }

    private HashMap<String, List<String>> eventToConsumingStepsMap = new HashMap<>();

    public List<StepName> locateToInternals(ProcessStepDoc stepDoc) {
        List<StepName> tos = new ArrayList<>();
        for(NameRequired producedEvent : stepDoc.attributes().producedEvents()) {
            List<String> consumingSteps = eventToConsumingStepsMap.get(producedEvent);
            if(consumingSteps != null) {
                tos.addAll(consumingSteps.stream().map(StepName::new).collect(toList()));
            }
        }
        return tos;
    }
}
