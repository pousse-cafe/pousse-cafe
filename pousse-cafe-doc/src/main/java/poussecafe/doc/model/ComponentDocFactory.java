package poussecafe.doc.model;

import com.sun.javadoc.Doc;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.domain.Service;

public class ComponentDocFactory implements Service {

    public ComponentDoc buildDoc(String name, Doc doc) {
        return new ComponentDoc.Builder()
                .name(name)
                .description(doc.commentText())
                .shortDescription(AnnotationsResolver.shortDescription(doc))
                .trivial(AnnotationsResolver.isTrivial(doc))
                .build();
    }
}
