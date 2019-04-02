package poussecafe.doc.model.servicedoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Service;

public class ServiceDocFactory extends Factory<ServiceDocId, ServiceDoc, ServiceDoc.Attributes> {

    public ServiceDoc newServiceDoc(BoundedContextDocId boundedContextDocId, ClassDoc classDoc) {
        if(!isServiceDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not a service");
        }

        String name = classDoc.simpleTypeName();
        ServiceDocId id = ServiceDocId.ofClassName(classDoc.qualifiedName());
        ServiceDoc serviceDoc = newAggregateWithId(id);
        serviceDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
                .componentDoc(componentDocFactory.buildDoc(name, classDoc))
                .build());
        return serviceDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isServiceDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperinterface(classDoc, Service.class);
    }
}
