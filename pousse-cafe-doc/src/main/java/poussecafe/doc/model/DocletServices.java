package poussecafe.doc.model;

import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.domain.Service;

public class DocletServices implements Service {

    public DocletEnvironment docletEnvironment() {
        return docletEnvironment;
    }

    private DocletEnvironment docletEnvironment;

    public DocletAccess docletAccess() {
        return docletAccess;
    }

    private DocletAccess docletAccess;

    public AnnotationsResolver annotationsResolver() {
        return annotationsResolver;
    }

    private AnnotationsResolver annotationsResolver;

    private EntityDocFactory entityDocFactory;

    public EntityDocFactory entityDocFactory() {
        return entityDocFactory;
    }

    private ValueObjectDocFactory valueObjectDocFactory;

    public ValueObjectDocFactory valueObjectDocFactory() {
        return valueObjectDocFactory;
    }
}
