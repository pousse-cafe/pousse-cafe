package poussecafe.doc.model;

import javax.lang.model.element.Element;
import poussecafe.domain.Service;

public class ComponentDocFactory implements Service {

    public ComponentDoc buildDoc(String name, Element doc) {
        return new ComponentDoc.Builder()
                .name(name)
                .description(annotationsResolver.renderCommentBody(doc))
                .shortDescription(annotationsResolver.shortDescription(doc))
                .trivial(annotationsResolver.isTrivial(doc))
                .build();
    }

    private AnnotationsResolver annotationsResolver;
}
