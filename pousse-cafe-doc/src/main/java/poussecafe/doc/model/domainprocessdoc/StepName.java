package poussecafe.doc.model.domainprocessdoc;

import poussecafe.doc.model.messagelistenerdoc.StepMethodSignature;
import poussecafe.util.StringKey;

public class StepName extends StringKey {

    public StepName(String value) {
        super(value);
    }

    public StepName(StepMethodSignature signature) {
        super(signature.toString());
    }
}
