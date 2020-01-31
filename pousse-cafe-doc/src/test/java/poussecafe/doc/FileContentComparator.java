package poussecafe.doc;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class FileContentComparator extends SimpleFileVisitor<Path> {

    public static class Builder {

        private FileContentComparator comparator = new FileContentComparator();

        public Builder targetDirectory(Path targetDirectory) {
            comparator.targetDirectory = targetDirectory;
            return this;
        }

        public Builder expectedDirectory(Path expectedDirectory) {
            comparator.expectedDirectory = expectedDirectory;
            return this;
        }

        public FileContentComparator build() {
            Objects.requireNonNull(comparator.targetDirectory);
            Objects.requireNonNull(comparator.expectedDirectory);
            return comparator;
        }
    }

    private FileContentComparator() {

    }

    private Path targetDirectory;

    private Path expectedDirectory;

    @Override
    public FileVisitResult visitFile(Path file,
            BasicFileAttributes attrs)
            throws IOException {
        if(!file.toFile().isDirectory()) {
            thenGeneratedFileContainsExpectedData(expectedDirectory.relativize(file));
        }
        return FileVisitResult.CONTINUE;
    }

    private void thenGeneratedFileContainsExpectedData(Path relativePath) {
        if(relativePath.toString().endsWith(".html")) {
            compareFiles(relativePath, false);
        } else if(relativePath.toString().endsWith(".css")) {
            compareFiles(relativePath, false);
        } else if (relativePath.toString().endsWith(".dot")) {
            compareFiles(relativePath, true);
        } else {
            throw new IllegalArgumentException("Unexpected extension for file '" + relativePath + "'");
        }
    }

    private void compareFiles(Path relativePath, boolean sortContent) {
        File generatedIndexFile = new File(targetDirectory.toFile(), relativePath.toString());
        assertTrue("File " + relativePath + " does not exist", generatedIndexFile.exists());
        String generatedIndexFileContent = readContent(generatedIndexFile, sortContent);

        File expectedIndexFile = new File(expectedDirectory.toFile(), relativePath.toString());
        String expectedIndexFileContent = readContent(expectedIndexFile, sortContent);

        String message = message(relativePath, generatedIndexFileContent, expectedIndexFileContent, sortContent);
        assertThat(message, generatedIndexFileContent, equalTo(expectedIndexFileContent));
    }

    private String message(Path path, String generatedIndexFileContent, String expectedIndexFileContent, boolean sortContent) {
        StringBuilder message = new StringBuilder();
        message.append("File ");
        message.append(path);
        message.append(" does not match expected content");
        if(!sortContent) {
            try {
                Patch<String> diff = DiffUtils.diffInline(expectedIndexFileContent, generatedIndexFileContent);
                message.append(": ");
                message.append(diff.toString());
            } catch (DiffException e) {
                throw new IllegalArgumentException();
            }
        }
        return message.toString();
    }

    private String readContent(File file, boolean sortContent) {
        try {
            return fileContentStream(file, sortContent).collect(joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Stream<String> fileContentStream(File file, boolean sortContent) throws IOException {
        Stream<String> stream = Files.lines(file.toPath(), Charset.forName("UTF-8"));
        if(sortContent) {
            return stream.sorted();
        } else {
            return stream;
        }
    }
}
