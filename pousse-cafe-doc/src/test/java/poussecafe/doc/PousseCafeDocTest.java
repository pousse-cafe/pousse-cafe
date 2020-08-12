package poussecafe.doc;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.files.Difference;
import poussecafe.files.DifferenceType;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public class PousseCafeDocTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void docletGeneratesExpectedDoc() {
        givenDocletConfiguration("poussecafe.sample.test");
        givenEmptyOutputDirectory();
        whenExecutingDoclet();
        thenGeneratedDocContainsExpectedData("expected-doc-test");
    }

    private void givenDocletConfiguration(String basePackage) {
        configuration = new PousseCafeDocletConfiguration.Builder()
                .domainName("Pousse-Café Doc")
                .version("Test")
                .sourcePath(asList(System.getProperty("user.dir") + "/src/test/java/"))
                .outputDirectory(System.getProperty("java.io.tmpdir") + "/" + basePackage)
                .pdfFileName("domain.pdf")
                .basePackage(basePackage)
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

    private void thenGeneratedDocContainsExpectedData(String expectedDocFolder) {
        Path expectedDocDirectory = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", expectedDocFolder);
        Path targetDirectory = Paths.get(configuration.outputDirectory());
        try {
            assertDifferences(poussecafe.files.Tree.compareTrees(targetDirectory, expectedDocDirectory, ".dot"));
        } catch (IOException e) {
            fail();
        }
    }

    private void assertDifferences(List<Difference> differences) {
        for(Difference difference : differences) {
            if(difference.type() == DifferenceType.TARGET_DOES_NOT_EXIST) {
                assertTrue("File " + difference.relativePath() + " does not exist", false);
            } else if(difference.type() == DifferenceType.CONTENT_DOES_NOT_MATCH) {
                String message = message(difference);
                assertTrue(message, false);
            }
        }
    }

    private String message(Difference difference) {
        StringBuilder message = new StringBuilder();
        message.append("File ");
        message.append(difference.relativePath());
        message.append(" does not match expected content");
        if(!difference.contentSorted()) {
            Patch<String> diff = DiffUtils.diffInline(difference.expectedContent(), difference.targetContent());
            message.append(": ");
            message.append(diff.toString());
        }
        return message.toString();
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

    @Test
    public void docletGeneratesExpectedDocUsingDeprecated() {
        givenDocletConfiguration("poussecafe.sample.test_deprecated");
        givenEmptyOutputDirectory();
        whenExecutingDoclet();
        thenGeneratedDocContainsExpectedData("expected-doc-test-deprecated");
    }
}
