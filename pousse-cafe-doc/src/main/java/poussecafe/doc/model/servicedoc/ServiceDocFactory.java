package poussecafe.doc.model.servicedoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Service;

public class ServiceDocFactory extends Factory<ServiceDocKey, ServiceDoc, ServiceDoc.Data> {

    public ServiceDoc newServiceDoc(ClassDoc classDoc) {
        if(!isServiceDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not a service");
        }

        BoundedContextDoc boundedContextDoc = boundedContextDocRepository.findByPackageNamePrefixing(classDoc.containingPackage().name());
        if(boundedContextDoc == null) {
            throw new DomainException("Unable to locate bounded context for package " + classDoc.containingPackage().name());
        }

        String name = classDoc.simpleTypeName();
        ServiceDocKey key = new ServiceDocKey(boundedContextDoc.name(), name);
        ServiceDoc serviceDoc = newStorableWithKey(key);
        serviceDoc.description(classDoc.commentText());
        return serviceDoc;
    }

    public static boolean isServiceDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperinterface(classDoc, Service.class);
    }

    private BoundedContextDocRepository boundedContextDocRepository;
}
