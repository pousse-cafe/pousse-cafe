package poussecafe.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.exception.PousseCafeException;

import static java.util.stream.Collectors.joining;

public class TreeComparator extends SimpleFileVisitor<Path> {

    private Path targetDirectory;

    private Path expectedDirectory;

    @Override
    public FileVisitResult visitFile(Path file,
            BasicFileAttributes attrs)
            throws IOException {
        if(!file.toFile().isDirectory()) {
            Path relativePath = expectedDirectory.relativize(file);
            compareFiles(relativePath, contentSort(relativePath));
        }
        return FileVisitResult.CONTINUE;
    }

    private boolean contentSort(Path filePath) {
        for(String extension : contentSortForExtensions) {
            if(filePath.toString().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private List<String> contentSortForExtensions = new ArrayList<>();

    private void compareFiles(Path relativePath, boolean sortContent) {
        File targetFile = new File(targetDirectory.toFile(), relativePath.toString());
        if(!targetFile.exists()) {
            differences.add(Difference.targetDoesNotExist(relativePath));
        } else {
            String targetFileContent = readContent(targetFile, sortContent);
            File expectedFile = new File(expectedDirectory.toFile(), relativePath.toString());
            String expectedFileContent = readContent(expectedFile, sortContent);
            if(!targetFileContent.equals(expectedFileContent)) {
                differences.add(Difference.fileContentDoesNotMatch(relativePath, sortContent, targetFileContent, expectedFileContent));
            }
        }
    }

    private List<Difference> differences = new ArrayList<>();

    public List<Difference> differences() {
        return differences;
    }

    private String readContent(File file, boolean sortContent) {
        try {
            var lines = TextFiles.streamLines(file);
            if(sortContent) {
                lines = lines.sorted();
            }
            return lines.collect(joining("\n"));
        } catch (IOException e) {
            throw new PousseCafeException("Unable to read content", e);
        }
    }

    public static class Builder {

        private TreeComparator comparator = new TreeComparator();

        public Builder targetDirectory(Path targetDirectory) {
            comparator.targetDirectory = targetDirectory;
            return this;
        }

        public Builder expectedDirectory(Path expectedDirectory) {
            comparator.expectedDirectory = expectedDirectory;
            return this;
        }

        public Builder sortContent(String fileExtension) {
            comparator.contentSortForExtensions.add(fileExtension);
            return this;
        }

        public TreeComparator build() {
            Objects.requireNonNull(comparator.targetDirectory);
            Objects.requireNonNull(comparator.expectedDirectory);
            return comparator;
        }
    }

    private TreeComparator() {

    }
}
