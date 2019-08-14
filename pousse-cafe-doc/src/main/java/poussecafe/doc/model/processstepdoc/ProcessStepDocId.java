package poussecafe.doc.model.processstepdoc;

import poussecafe.util.StringId;

/**
 * @trivial
 */
public class ProcessStepDocId extends StringId {

    public ProcessStepDocId(String messageListenerId) {
        super(messageListenerId);
    }

    public ProcessStepDocId(StepMethodSignature signature) {
        super(signature.toString());
    }
}
