package poussecafe.doc;

import com.sun.javadoc.ClassDoc;

@FunctionalInterface
public interface Restarter {

    void restartFrom(ClassDoc classDoc);

}
