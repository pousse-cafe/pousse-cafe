package poussecafe.doc;

import javax.lang.model.element.TypeElement;

@FunctionalInterface
public interface PathHandler {

    void handle(TypeElement from, TypeElement to);
}
