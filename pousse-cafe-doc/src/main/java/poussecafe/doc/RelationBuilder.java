package poussecafe.doc;

import javax.lang.model.element.TypeElement;

@FunctionalInterface
public interface RelationBuilder {

    void classRelationBuilder(TypeElement from, TypeElement to);
}
