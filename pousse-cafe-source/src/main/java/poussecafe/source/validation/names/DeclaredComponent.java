package poussecafe.source.validation.names;

import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;

public interface DeclaredComponent {

    SourceFileLine sourceFileLine();

    Name className();
}
