package poussecafe.doc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

public class PousseCafeDocletTest {

    @Test
    public void docletGeneratesExpectedDoc() {
        givenDocletConfiguration();
        givenEmptyOutputDirectory();
        whenExecutingDoclet();
        thenGeneratedDocContainsExpectedData();
    }

    private void givenDocletConfiguration() {
        configuration = new PousseCafeDocletConfiguration.Builder()
                .domainName("Pousse-Café Doc")
                .version("Test")
                .sourcePath(asList(System.getProperty("user.dir") + "/src/main/java/"))
                .outputDirectory(System.getProperty("java.io.tmpdir") + "/ddd-doc/")
                .basePackage("poussecafe.doc")
                .includeGenerationDate(false)
                .customDotExecutable(Optional.of("dot"))
                .customFdpExecutable(Optional.of("fdp"))
                .build();
    }

    private PousseCafeDocletConfiguration configuration;

    private void givenEmptyOutputDirectory() {
        File outputDirectory = new File(configuration.outputDirectory());
        new File(outputDirectory, "index.html").delete();
    }

    private void whenExecutingDoclet() {
        new PousseCafeDocletExecutor(configuration).execute();
    }

    private void thenGeneratedDocContainsExpectedData() {
        Path expectedDocDirectory = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "expected-doc");
        Path targetDirectory = Paths.get(configuration.outputDirectory());
        try {
            Files.walkFileTree(expectedDocDirectory, new FileContentComparator.Builder()
                    .targetDirectory(targetDirectory)
                    .expectedDirectory(expectedDocDirectory)
                    .build());
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}
