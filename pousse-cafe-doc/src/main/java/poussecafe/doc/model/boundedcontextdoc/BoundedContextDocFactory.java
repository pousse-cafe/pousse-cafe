package poussecafe.doc.model.boundedcontextdoc;

import javax.lang.model.element.PackageElement;
import poussecafe.doc.model.AnnotationsResolver;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class BoundedContextDocFactory extends Factory<BoundedContextDocId, BoundedContextDoc, BoundedContextDoc.Attributes> {

    public BoundedContextDoc newBoundedContextDoc(PackageElement packageDoc) {
        if(!isBoundedContextDoc(packageDoc)) {
            throw new DomainException("Package " + packageDoc.getQualifiedName().toString() + " is not a valid bounded context");
        }

        String name = annotationsResolver.boundedContext(packageDoc);
        BoundedContextDoc boundedContextDoc = newAggregateWithId(BoundedContextDocId.ofPackageName(packageDoc.getQualifiedName().toString()));
        boundedContextDoc.componentDoc(componentDocFactory.buildDoc(name, packageDoc));
        return boundedContextDoc;
    }

    private AnnotationsResolver annotationsResolver;

    private ComponentDocFactory componentDocFactory;

    public boolean isBoundedContextDoc(PackageElement packageDoc) {
        return annotationsResolver.isBoundedContext(packageDoc);
    }

}
