package poussecafe.doc.model.step;

import java.util.ArrayList;
import java.util.List;
import poussecafe.doc.model.ComponentDoc;

import static poussecafe.check.Checks.checkThatValue;

public class StepDoc {

    public static class Builder {

        private StepDoc stepDoc = new StepDoc();

        public Builder methodSignature(StepMethodSignature methodSignature) {
            stepDoc.methodSignature = methodSignature;
            return this;
        }

        public Builder producedEvents(List<String> producedEvents) {
            stepDoc.producedEvents = producedEvents;
            return this;
        }

        public Builder componentDoc(ComponentDoc componentDoc) {
            stepDoc.componentDoc = componentDoc;
            return this;
        }

        public StepDoc build() {
            checkThatValue(stepDoc.methodSignature).notNull();
            checkThatValue(stepDoc.componentDoc).notNull();
            checkThatValue(stepDoc.producedEvents).notNull();
            return stepDoc;
        }
    }

    private StepDoc() {

    }

    private StepMethodSignature methodSignature;

    public StepMethodSignature methodSignature() {
        return methodSignature;
    }

    private List<String> producedEvents = new ArrayList<>();

    public List<String> producedEvents() {
        return producedEvents;
    }

    private ComponentDoc componentDoc;

    public ComponentDoc componentDoc() {
        return componentDoc;
    }
}
