package poussecafe.source.validation;

import poussecafe.source.SourceFile;

import static java.util.Objects.requireNonNull;

public class SourceFileLine {

    public SourceFile sourceFile() {
        return sourceFile;
    }

    private SourceFile sourceFile;

    public int line() {
        return line;
    }

    private int line = -1;

    public static class Builder {

        public SourceFileLine build() {
            requireNonNull(sourceFileLine.sourceFile);
            if(sourceFileLine.line < 0) {
                throw new IllegalStateException();
            }
            return sourceFileLine;
        }

        private SourceFileLine sourceFileLine = new SourceFileLine();

        public Builder sourceFile(SourceFile sourceFile) {
            sourceFileLine.sourceFile = sourceFile;
            return this;
        }

        public Builder line(int line) {
            sourceFileLine.line = line;
            return this;
        }
    }

    private SourceFileLine() {

    }
}
