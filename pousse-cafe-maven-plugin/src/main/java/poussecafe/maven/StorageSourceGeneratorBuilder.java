package poussecafe.maven;

import java.io.File;

public interface StorageSourceGeneratorBuilder {

    StorageSourceGeneratorBuilder sourceWriter(SourceWriter sourceWriter);

    StorageSourceGeneratorBuilder aggregateName(String aggregateName);

    StorageSourceGeneratorBuilder adaptersDirectory(File adaptersDirectory);

    StorageSourceGenerator build();
}
