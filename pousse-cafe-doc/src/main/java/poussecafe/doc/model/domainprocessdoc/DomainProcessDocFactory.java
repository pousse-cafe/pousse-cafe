package poussecafe.doc.model.domainprocessdoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.process.DomainProcess;

public class DomainProcessDocFactory extends Factory<DomainProcessDocKey, DomainProcessDoc, DomainProcessDoc.Data> {

    public DomainProcessDoc newDomainProcessDoc(BoundedContextDocKey boundedContextDocKey, ClassDoc doc) {
        if(!isDomainProcessDoc(doc)) {
            throw new DomainException("Class " + doc.name() + " is not a domain process");
        }

        String name = name(doc);
        DomainProcessDocKey key = DomainProcessDocKey.ofClassName(doc.qualifiedName());
        DomainProcessDoc domainProcessDoc = newStorableWithKey(key);
        domainProcessDoc.boundedContextComponentDoc(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
                .componentDoc(new ComponentDoc.Builder()
                        .name(name)
                        .description(doc.commentText())
                        .build())
                .build());
        return domainProcessDoc;
    }

    public static boolean isDomainProcessDoc(ClassDoc doc) {
        return ClassDocPredicates.documentsWithSuperclass(doc, DomainProcess.class);
    }

    public static String name(ClassDoc doc) {
        return doc.simpleTypeName();
    }
}
