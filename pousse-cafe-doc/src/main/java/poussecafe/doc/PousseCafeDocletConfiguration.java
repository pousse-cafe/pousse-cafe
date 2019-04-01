package poussecafe.doc;

import java.io.PrintWriter;

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

        public Builder sourceDirectory(String sourceDirectory) {
            configuration.sourceDirectory = sourceDirectory;
            return this;
        }

        public Builder rootPackage(String rootPackage) {
            configuration.rootPackage = rootPackage;
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

        public PousseCafeDocletConfiguration build() {
            if(configuration.errorWriter == null) {
                configuration.errorWriter = new PrintWriter(System.err);
                configuration.warningWriter = new PrintWriter(System.err);
                configuration.noticeWriter = new PrintWriter(System.out);
            }
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

    private String sourceDirectory;

    public String sourceDirectory() {
        return sourceDirectory;
    }

    private String rootPackage;

    public String rootPackage() {
        return rootPackage;
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
}
