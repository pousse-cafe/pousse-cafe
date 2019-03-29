package poussecafe.doc;

import com.sun.tools.javadoc.Main;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PousseCafeDocletTest {

    @Test
    public void docletGeneratesExpectedDoc() {
        givenInvokationParameters();
        whenExecutingDoclet();
        thenGeneratedDocContainsExpectedData();
    }

    private void givenInvokationParameters() {
        domain = "Pousse-Café Doc";
        version = "Test";
        outputDirectory = System.getProperty("java.io.tmpdir") + "/ddd-doc/";
        basePackage = "poussecafe.doc";

        String sourcePath = System.getProperty("user.dir") + "/src/main/java/";
        args.add("-sourcepath"); args.add(sourcePath);
        args.add("-output"); args.add(outputDirectory);
        args.add("-domain"); args.add(domain);
        args.add("-version"); args.add(version);
        args.add("-basePackage"); args.add(basePackage);

        args.add("poussecafe.doc");
        File sourceDirectory = new File(sourcePath + "poussecafe/doc/");
        args.addAll(subPackages(sourceDirectory, "poussecafe.doc"));
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

    private String domain;

    private String version;

    private String outputDirectory;

    private String basePackage;

    private List<String> args = new ArrayList<>();

    private void whenExecutingDoclet() {
        Main.execute("javadoc",
                new PrintWriter(System.out),
                new PrintWriter(System.out),
                new PrintWriter(System.out),
                "poussecafe.doc.PousseCafeDoclet",
                args.toArray(new String[args.size()]));
    }

    private void thenGeneratedDocContainsExpectedData() {
        assertTrue(new File(outputDirectory, "index.html").exists());
    }
}
