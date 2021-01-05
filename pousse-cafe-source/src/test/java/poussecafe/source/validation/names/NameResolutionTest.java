package poussecafe.source.validation.names;

import java.util.Optional;
import org.junit.Test;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.SourceFileLine;
import poussecafe.source.validation.model.Module;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class NameResolutionTest {

    @Test
    public void noModuleResolves() {
        givenNoModules();
        whenResolvingName(component(new Name("base.package.ComponentClass"), "Component"));
        thenResultIs("Component");
    }

    private NamedComponent component(Name className, String name) {
        return new NamedComponent() {
            @Override
            public Optional<SourceFileLine> sourceFileLine() {
                return Optional.empty();
            }

            @Override
            public Name className() {
                return className;
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    private void givenNoModules() {
        modules = new Modules(emptyList());
    }

    private Modules modules;

    private void whenResolvingName(NamedComponent component) {
        resolved = modules.qualifyName(component);
    }

    private Name resolved;

    private void thenResultIs(String expected) {
        assertThat(resolved, equalTo(new Name(expected)));
    }

    @Test
    public void oneModuleResolves() {
        givenModules(module1);
        whenResolvingName(component(new Name("base.package.MyComponentClass"), "MyComponent"));
        thenResultIs("Module1.MyComponent");
    }

    private Module module1 = new Module.Builder()
            .sourceFileLine(mock(SourceFileLine.class))
            .name(new Name("base.package.Module1"))
            .build();

    private void givenModules(Module... list) {
        modules = new Modules(asList(list));
    }

    @Test
    public void severalModuleResolves() {
        givenModules(module1, module2, module3);
        whenResolvingName(component(new Name("base.package2.MyComponentClass"), "MyComponent"));
        thenResultIs("Module2.MyComponent");
    }

    private Module module2 = new Module.Builder()
            .sourceFileLine(mock(SourceFileLine.class))
            .name(new Name("base.package2.Module2"))
            .build();

    private Module module3 = new Module.Builder()
            .sourceFileLine(mock(SourceFileLine.class))
            .name(new Name("base.package3.Module3"))
            .build();
}
