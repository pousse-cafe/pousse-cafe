package poussecafe.source.validation.names;

import java.util.Optional;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;

public interface DeclaredComponent {

    Optional<SourceFileLine> sourceFileLine();

    Name className();
}
