package poussecafe.doc.model.servicedoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Service;

public class ServiceDocFactory extends Factory<ServiceDocKey, ServiceDoc, ServiceDoc.Data> {

    public ServiceDoc newServiceDoc(BoundedContextDocKey boundedContextDocKey, ClassDoc classDoc) {
        if(!isServiceDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not a service");
        }

        String name = classDoc.simpleTypeName();
        ServiceDocKey key = ServiceDocKey.ofClassName(classDoc.qualifiedName());
        ServiceDoc serviceDoc = newStorableWithKey(key);
        serviceDoc.boundedContextComponentDoc(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
                .componentDoc(new ComponentDoc.Builder()
                        .name(name)
                        .description(classDoc.commentText())
                        .build())
                .build());
        return serviceDoc;
    }

    public static boolean isServiceDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperinterface(classDoc, Service.class);
    }
}
