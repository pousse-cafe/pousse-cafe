package poussecafe.doc;

import com.sun.javadoc.ProgramElementDoc;

@FunctionalInterface
public interface ProgramElementHandler {

    void handle(ProgramElementDoc doc);
}
