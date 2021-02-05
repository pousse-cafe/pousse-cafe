package poussecafe.source.validation.names;

import java.io.Serializable;
import java.util.Optional;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceLine;

public interface DeclaredComponent extends Serializable {

    Optional<SourceLine> sourceLine();

    Name className();
}
