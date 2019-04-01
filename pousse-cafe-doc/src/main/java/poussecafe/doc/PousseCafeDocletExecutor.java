package poussecafe.doc;

import com.sun.tools.javadoc.Main;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PousseCafeDocletExecutor {

    public PousseCafeDocletExecutor(PousseCafeDocletConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    private PousseCafeDocletConfiguration configuration;

    public void execute() {
        List<String> javadocArgs = new ArrayList<>();
        javadocArgs.add("-sourcepath"); javadocArgs.add(configuration.sourceDirectory());
        javadocArgs.add("-output"); javadocArgs.add(configuration.outputDirectory());
        javadocArgs.add("-domain"); javadocArgs.add(configuration.domainName());
        javadocArgs.add("-version"); javadocArgs.add(configuration.version());
        javadocArgs.add("-basePackage"); javadocArgs.add(configuration.rootPackage());
        javadocArgs.add("-includeGeneratedDate"); javadocArgs.add(Boolean.toString(configuration.includeGenerationDate()));
        javadocArgs.addAll(allPackages());

        Main.execute("javadoc",
                configuration.errorWriter(),
                configuration.warningWriter(),
                configuration.noticeWriter(),
                PousseCafeDoclet.class.getName(),
                javadocArgs.toArray(new String[javadocArgs.size()]));
    }

    private List<String> allPackages() {
        List<String> packages = new ArrayList<>();
        String rootPackage = configuration.rootPackage();
        packages.add(rootPackage);
        Path rootPackageDirectoryPath = Paths.get(configuration.sourceDirectory(), rootPackage.split("\\."));
        packages.addAll(subPackages(rootPackageDirectoryPath.toFile(), rootPackage));
        return packages;
    }

    private List<String> subPackages(File sourceDirectory, String currentPackage) {
        List<String> subPackages = new ArrayList<>();
        for(File file : sourceDirectory.listFiles()) {
            if(file.isDirectory()) {
                String directoryName = file.getName();
                String subPackage = currentPackage + "." + directoryName;
                subPackages.add(subPackage);
                subPackages.addAll(subPackages(new File(sourceDirectory, directoryName), subPackage));
            }
        }
        return subPackages;
    }
}
