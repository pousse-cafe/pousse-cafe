package poussecafe.files;

import java.nio.file.Path;

public class Difference {

    public static Difference targetDoesNotExist(Path relativePath) {
        var difference = new Difference();
        difference.type = DifferenceType.TARGET_DOES_NOT_EXIST;
        difference.relativePath = relativePath;
        return difference;
    }

    private Difference() {

    }

    private DifferenceType type;

    public DifferenceType type() {
        return type;
    }

    private Path relativePath;

    public Path relativePath() {
        return relativePath;
    }

    public static Difference fileContentDoesNotMatch(Path relativePath, boolean contentSorted, String targetContent, String expectedContent) {
        var difference = new Difference();
        difference.type = DifferenceType.CONTENT_DOES_NOT_MATCH;
        difference.relativePath = relativePath;
        difference.contentSorted = contentSorted;
        difference.targetContent = targetContent;
        difference.expectedContent = expectedContent;
        return difference;
    }

    private String targetContent;

    public String targetContent() {
        contentDoesNotMatchOrElseThrow();
        return targetContent;
    }

    private void contentDoesNotMatchOrElseThrow() {
        if(type != DifferenceType.CONTENT_DOES_NOT_MATCH) {
            throw new UnsupportedOperationException();
        }
    }

    private String expectedContent;

    public String expectedContent() {
        contentDoesNotMatchOrElseThrow();
        return expectedContent;
    }

    private boolean contentSorted;

    public boolean contentSorted() {
        contentDoesNotMatchOrElseThrow();
        return contentSorted;
    }
}
