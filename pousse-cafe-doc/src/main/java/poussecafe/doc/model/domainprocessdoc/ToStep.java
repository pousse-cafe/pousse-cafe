package poussecafe.doc.model.domainprocessdoc;

public class ToStep {

    public static class Builder {

        private ToStep toStep = new ToStep();

        public Builder name(StepName name) {
            toStep.name = name;
            return this;
        }

        public Builder directly(boolean directly) {
            toStep.directly = directly;
            return this;
        }

        public ToStep build() {
            return toStep;
        }
    }

    private ToStep() {

    }

    private StepName name;

    public StepName name() {
        return name;
    }

    private boolean directly;

    public boolean directly() {
        return directly;
    }
}
