package poussecafe.doc.model;

import com.sun.javadoc.ProgramElementDoc;
import poussecafe.doc.AnnotationsResolver;
import poussecafe.domain.Service;

public class ComponentDocFactory implements Service {

    public ComponentDoc buildDoc(String name, ProgramElementDoc doc) {
        return new ComponentDoc.Builder()
                .name(name)
                .description(doc.commentText())
                .shortDescription(AnnotationsResolver.shortDescription(doc))
                .trivial(AnnotationsResolver.isTrivial(doc))
                .build();
    }
}
