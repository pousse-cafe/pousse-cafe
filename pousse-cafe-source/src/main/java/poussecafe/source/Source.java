package poussecafe.source;

import org.eclipse.jdt.core.dom.ASTParser;

public interface Source {

    String id();

    void configure(ASTParser parser);
}
