package poussecafe.doc.model.domainprocessdoc;

import poussecafe.doc.model.processstepdoc.StepMethodSignature;
import poussecafe.util.StringId;

public class StepName extends StringId {

    public StepName(String value) {
        super(value);
    }

    public StepName(StepMethodSignature signature) {
        super(signature.toString());
    }
}
