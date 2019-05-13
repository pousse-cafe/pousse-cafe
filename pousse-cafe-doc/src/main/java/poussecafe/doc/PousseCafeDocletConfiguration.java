package poussecafe.doc;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class PousseCafeDocletConfiguration {

    public static class Builder {

        private PousseCafeDocletConfiguration configuration = new PousseCafeDocletConfiguration();

        public Builder domainName(String domainName) {
            configuration.domainName = domainName;
            return this;
        }

        public Builder version(String version) {
            configuration.version = version;
            return this;
        }

        public Builder outputDirectory(String outputDirectory) {
            configuration.outputDirectory = outputDirectory;
            return this;
        }

        public Builder basePackage(String basePackage) {
            configuration.basePackage = basePackage;
            return this;
        }

        public Builder sourcePath(List<String> sourcePath) {
            configuration.sourcePath = sourcePath;
            return this;
        }

        public Builder includeGenerationDate(boolean includeGenerationDate) {
            configuration.includeGenerationDate = includeGenerationDate;
            return this;
        }

        public Builder errorWriter(PrintWriter errorWriter) {
            configuration.errorWriter = errorWriter;
            return this;
        }

        public Builder warningWriter(PrintWriter warningWriter) {
            configuration.warningWriter = warningWriter;
            return this;
        }

        public Builder noticeWriter(PrintWriter noticeWriter) {
            configuration.noticeWriter = noticeWriter;
            return this;
        }

        public Builder classPath(List<String> classPath) {
            configuration.classPath = classPath;
            return this;
        }

        public Builder debug(boolean debug) {
            configuration.debug = debug;
            return this;
        }

        public Builder customDotExecutable(Optional<String> customDotExecutable) {
            configuration.customDotExecutable = customDotExecutable;
            return this;
        }

        public Builder customFdpExecutable(Optional<String> customFdpExecutable) {
            configuration.customFdpExecutable = customFdpExecutable;
            return this;
        }

        public PousseCafeDocletConfiguration build() {
            Objects.requireNonNull(configuration.domainName);
            Objects.requireNonNull(configuration.version);
            Objects.requireNonNull(configuration.outputDirectory);
            Objects.requireNonNull(configuration.basePackage);
            if(configuration.sourcePath == null || configuration.sourcePath.isEmpty()) {
                throw new IllegalStateException("Source path must contain at least one element");
            }
            Objects.requireNonNull(configuration.classPath);
            if(configuration.errorWriter == null) {
                configuration.errorWriter = new PrintWriter(System.err);
                configuration.warningWriter = new PrintWriter(System.err);
                configuration.noticeWriter = new PrintWriter(System.out);
            }
            Objects.requireNonNull(configuration.customDotExecutable);
            Objects.requireNonNull(configuration.customFdpExecutable);
            return configuration;
        }
    }

    private PousseCafeDocletConfiguration() {

    }

    private String domainName;

    public String domainName() {
        return domainName;
    }

    private String version;

    public String version() {
        return version;
    }

    private String outputDirectory;

    public String outputDirectory() {
        return outputDirectory;
    }

    private String basePackage;

    public String basePackage() {
        return basePackage;
    }

    private List<String> sourcePath;

    public List<String> sourceDirectory() {
        return sourcePath;
    }

    private PrintWriter errorWriter;

    public PrintWriter errorWriter() {
        return errorWriter;
    }

    private PrintWriter warningWriter;

    public PrintWriter warningWriter() {
        return warningWriter;
    }

    private PrintWriter noticeWriter;

    public PrintWriter noticeWriter() {
        return noticeWriter;
    }

    private boolean includeGenerationDate;

    public boolean includeGenerationDate() {
        return includeGenerationDate;
    }

    private List<String> classPath = emptyList();

    public List<String> classPath() {
        return classPath;
    }

    private boolean debug;

    public boolean isDebug() {
        return debug;
    }

    private Optional<String> customDotExecutable = Optional.empty();

    public Optional<String> customDotExecutable() {
        return customDotExecutable;
    }

    private Optional<String> customFdpExecutable = Optional.empty();

    public Optional<String> customFdpExecutable() {
        return customFdpExecutable;
    }
}
