package poussecafe.doc.model.processstepdoc;

import poussecafe.util.StringKey;

public class ProcessStepDocKey extends StringKey {

    public ProcessStepDocKey(String messageListenerId) {
        super(messageListenerId);
    }

    public ProcessStepDocKey(StepMethodSignature signature) {
        super(signature.toString());
    }
}
