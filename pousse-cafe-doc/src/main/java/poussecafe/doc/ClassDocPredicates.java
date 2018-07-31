package poussecafe.doc;

import com.sun.javadoc.ClassDoc;

public class ClassDocPredicates {

    public static boolean documentsWithSuperclass(ClassDoc classDoc, Class<?> expectedSuperclass) {
        if(expectedSuperclass.isInterface()) {
            throw new IllegalArgumentException("Given expected class is an interface");
        }
        return classDoc.superclass() != null && documents(classDoc.superclass(), expectedSuperclass);
    }

    public static boolean documents(ClassDoc classDoc,
            Class<?> documentedClass) {
        return classDoc.qualifiedName().equals(documentedClass.getName());
    }

    public static boolean documentsWithSuperinterface(ClassDoc classDoc, Class<?> expectedInterface) {
        if(!expectedInterface.isInterface()) {
            throw new IllegalArgumentException("Given expected class is not an interface");
        }
        return documents(classDoc.interfaces(), expectedInterface);
    }

    public static boolean documents(ClassDoc[] classDocs,
            Class<?> expectedType) {
        for(ClassDoc classDoc : classDocs) {
            if(documents(classDoc, expectedType)) {
                return true;
            }
        }
        return false;
    }
}
