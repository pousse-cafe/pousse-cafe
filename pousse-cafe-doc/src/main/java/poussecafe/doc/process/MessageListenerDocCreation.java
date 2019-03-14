package poussecafe.doc.process;

import com.sun.javadoc.ClassDoc;
import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.messagelistenerdoc.MessageListenerDoc;
import poussecafe.doc.model.messagelistenerdoc.MessageListenerDocExtractor;
import poussecafe.doc.model.messagelistenerdoc.MessageListenerDocRepository;
import poussecafe.process.DomainProcess;

public class MessageListenerDocCreation extends DomainProcess {

    public void addMessageListenerDoc(BoundedContextDocKey boundedContextKey, ClassDoc classDoc) {
        List<MessageListenerDoc> docs = messageListenerDocExtractor.extractMessageListenerDocs(boundedContextKey, classDoc);
        for(MessageListenerDoc doc : docs) {
            runInTransaction(BoundedContextDoc.class, () -> aggregateDocRepository.add(doc));
        }
    }

    private MessageListenerDocExtractor messageListenerDocExtractor;

    private MessageListenerDocRepository aggregateDocRepository;
}
