package poussecafe.doc.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import java.util.HashMap;
import java.util.Map;
import poussecafe.domain.Service;
import poussecafe.exception.NotFoundException;

public class ClassDocRepository implements Service {

    public void registerClassDocs(RootDoc rootDoc) {
        for(ClassDoc classDoc : rootDoc.classes()) {
            classDocMap.put(classDoc.qualifiedName(), classDoc);
        }
    }

    private Map<String, ClassDoc> classDocMap = new HashMap<>();

    public ClassDoc getClassDoc(String qualifiedName) {
        ClassDoc classDoc = classDocMap.get(qualifiedName);
        if(classDoc == null) {
            throw new NotFoundException("No class with name " + qualifiedName);
        }
        return classDoc;
    }
}
