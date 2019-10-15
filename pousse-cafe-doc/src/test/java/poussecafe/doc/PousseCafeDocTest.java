package poussecafe.doc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public class PousseCafeDocTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

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
                .sourcePath(asList(System.getProperty("user.dir") + "/src/test/java/"))
                .outputDirectory(System.getProperty("java.io.tmpdir") + "/ddd-doc-test/")
                .pdfFileName("domain.pdf")
                .basePackage("poussecafe.sample.test")
                .includeGenerationDate(false)
                .customDotExecutable(Optional.of("dot"))
                .customFdpExecutable(Optional.of("fdp"))
                .build();
        assumeTrue(executableInstalled("dot", configuration.customDotExecutable().orElseThrow()));
        assumeTrue(executableInstalled("fdp", configuration.customFdpExecutable().orElseThrow()));
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
        Path expectedDocDirectory = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "expected-doc-test");
        Path targetDirectory = Paths.get(configuration.outputDirectory());
        try {
            Files.walkFileTree(expectedDocDirectory, new FileContentComparator.Builder()
                    .targetDirectory(targetDirectory)
                    .expectedDirectory(expectedDocDirectory)
                    .build());
        } catch (IOException e) {
            fail();
        }
    }

    private boolean executableInstalled(String type, String executable) {
        try {
            Process process = new ProcessBuilder(executable, "-V").start();
            String version = IOUtils.toString(process.getErrorStream(), Charset.defaultCharset());
            process.waitFor();
            logger.info("Detected custom {} executable. Version: {}", type, version);
            return process.exitValue() == 0;
        } catch (Exception e) {
            logger.warn(String.format("Failed to check custom %s executable: %s", type, executable), e);
            logger.info("Consider installing graphviz package to enable this test.");
            return false;
        }
    }
}
