package poussecafe.source.validation.names;

import java.util.Optional;
import org.junit.Test;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.validation.SourceLine;
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
        whenResolvingName(component(new ClassName("base.package.ComponentClass"), "Component"));
        thenResultIs("Component");
    }

    @SuppressWarnings("serial")
    private NamedComponent component(ClassName className, String name) {
        return new NamedComponent() {
            @Override
            public Optional<SourceLine> sourceLine() {
                return Optional.empty();
            }

            @Override
            public ClassName className() {
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

    private ClassName resolved;

    private void thenResultIs(String expected) {
        assertThat(resolved, equalTo(new ClassName(expected)));
    }

    @Test
    public void oneModuleResolves() {
        givenModules(module1);
        whenResolvingName(component(new ClassName("base.package.MyComponentClass"), "MyComponent"));
        thenResultIs("Module1.MyComponent");
    }

    private Module module1 = new Module.Builder()
            .sourceLine(mock(SourceLine.class))
            .className(new ClassName("base.package.Module1"))
            .build();

    private void givenModules(Module... list) {
        modules = new Modules(asList(list));
    }

    @Test
    public void severalModuleResolves() {
        givenModules(module1, module2, module3);
        whenResolvingName(component(new ClassName("base.package2.MyComponentClass"), "MyComponent"));
        thenResultIs("Module2.MyComponent");
    }

    private Module module2 = new Module.Builder()
            .sourceLine(mock(SourceLine.class))
            .className(new ClassName("base.package2.Module2"))
            .build();

    private Module module3 = new Module.Builder()
            .sourceLine(mock(SourceLine.class))
            .className(new ClassName("base.package3.Module3"))
            .build();
}
