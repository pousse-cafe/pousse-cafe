package poussecafe.doc;

import com.sun.javadoc.RootDoc;
import java.util.Optional;

public class RootDocWrapper {

    public RootDocWrapper(RootDoc rootDoc) {
        this.rootDoc = rootDoc;
    }

    private RootDoc rootDoc;

    public RootDoc rootDoc() {
        return rootDoc;
    }

    public Optional<String> readOptionValue(String name) {
        for (String[] option : rootDoc.options()) {
            if (option[0].equals("-" + name)) {
                return Optional.of(option[1]);
            }
        }
        return Optional.empty();
    }

    public String outputPath() {
        return readOptionValue("output").orElseThrow(RuntimeException::new);
    }

    public String version() {
        return readOptionValue("version").orElse("Undefined");
    }

    public String domainName() {
        return readOptionValue("domain").orElse("Undefined");
    }

    public String basePackage() {
        return readOptionValue("basePackage").orElseThrow(RuntimeException::new);
    }

    public void debug(String message) {
        if(isDebug()) {
            rootDoc.printNotice(message);
        }
    }

    public boolean isDebug() {
        if(isDebug == null) {
            isDebug = testIfDebug();
        }
        return isDebug;
    }

    private transient Boolean isDebug;

    private boolean testIfDebug() {
        for (String[] option : rootDoc.options()) {
            if (option[0].equals("-debug")) {
                return true;
            }
        }
        return false;
    }

    public void info(String message) {
        rootDoc.printNotice(message);
    }
}
