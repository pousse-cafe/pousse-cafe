package poussecafe.source.validation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.Name;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class ValidatorTest {

    protected void givenValidator() {
        classPathExplorer = new ClassPathExplorerMock();
        modelBuilder = new ValidationModelBuilder();
    }

    private ClassPathExplorerMock classPathExplorer;

    private ValidationModelBuilder modelBuilder;

    private Validator validator;

    protected void whenValidating() {
        validator = new Validator(modelBuilder.build(), new ClassLoaderClassResolver(), Optional.of(classPathExplorer));
        validator.validate();
        result = validator.result();
    }

    protected ValidationResult result;

    protected void thenNoMessage() {
        assertTrue(result.messages().isEmpty());
    }

    protected void thenAtLeast(Predicate<ValidationMessage> predicate) {
        assertTrue(result.messages().stream().anyMatch(predicate));
    }

    protected void thenNone(Predicate<ValidationMessage> predicate) {
        assertTrue(result.messages().stream().noneMatch(predicate));
    }

    protected void includeRelativeClass(String... relativeClassName) {
        try {
            var thisClassName = new Name(getClass().getCanonicalName());
            var thisClassNameSegments = thisClassName.segments();
            String[] pathSegments = new String[2 + (thisClassNameSegments.length - 1) + relativeClassName.length];
            pathSegments[0] = "test";
            pathSegments[1] = "java";
            for(int i = 0; i < thisClassNameSegments.length - 1; ++i) {
                pathSegments[2 + i] = thisClassNameSegments[i];
            }
            for(int i = 0; i < relativeClassName.length; ++i) {
                pathSegments[2 + (thisClassNameSegments.length - 1) + i] = relativeClassName[i];
            }
            pathSegments[pathSegments.length - 1] = pathSegments[pathSegments.length - 1] + ".java";
            modelBuilder.includeFile(Path.of("src", pathSegments));
        } catch (IOException e) {
            fail(e.toString());
        }
    }

    protected void includeClass(Class<?> classObject) {
        try {
            var thisClassName = new Name(classObject.getCanonicalName());
            var thisClassNameSegments = thisClassName.segments();
            String[] pathSegments = new String[2 + thisClassNameSegments.length];
            pathSegments[0] = "test";
            pathSegments[1] = "java";
            for(int i = 0; i < thisClassNameSegments.length; ++i) {
                pathSegments[2 + i] = thisClassNameSegments[i];
            }
            pathSegments[pathSegments.length - 1] = pathSegments[pathSegments.length - 1] + ".java";
            modelBuilder.includeFile(Path.of("src", pathSegments));
        } catch (IOException e) {
            fail(e.toString());
        }
    }

    protected void addSubType(Name superTypeName, Name subtype) {
        classPathExplorer.addSubType(superTypeName, subtype);
    }

    protected void includeFile(Path filePath) throws IOException {
        modelBuilder.includeFile(filePath);
    }
}
