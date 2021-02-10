package poussecafe.source.validation.names;

import java.io.Serializable;
import java.util.Optional;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.validation.SourceLine;

public interface DeclaredComponent extends Serializable {

    Optional<SourceLine> sourceLine();

    ClassName className();
}
