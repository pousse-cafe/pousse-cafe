package poussecafe.doc.model.servicedoc;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Service;

public class ServiceDocFactory extends Factory<ServiceDocId, ServiceDoc, ServiceDoc.Attributes> {

    public ServiceDoc newServiceDoc(BoundedContextDocId boundedContextDocId, TypeElement classDoc) {
        if(!isServiceDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.getQualifiedName() + " is not a service");
        }

        String name = classDoc.getSimpleName().toString();
        ServiceDocId id = ServiceDocId.ofClassName(classDoc.getQualifiedName().toString());
        ServiceDoc serviceDoc = newAggregateWithId(id);
        serviceDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
                .componentDoc(componentDocFactory.buildDoc(name, classDoc))
                .build());
        return serviceDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public boolean isServiceDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperinterface(classDoc, Service.class);
    }

    private ClassDocPredicates classDocPredicates;
}
