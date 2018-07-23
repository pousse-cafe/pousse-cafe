package poussecafe.doc;

import com.sun.javadoc.ClassDoc;

@FunctionalInterface
public interface RelationBuilder {

    void classRelationBuilder(ClassDoc from, ClassDoc to);
}
