package poussecafe.doc;

import com.sun.javadoc.ClassDoc;

@FunctionalInterface
public interface PathHandler {

    void handle(ClassDoc from, ClassDoc to);
}
