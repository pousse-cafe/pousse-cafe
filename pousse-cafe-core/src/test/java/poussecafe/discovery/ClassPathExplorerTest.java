package poussecafe.discovery;

import java.util.List;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClassPathExplorerTest {

    @Test(expected = IllegalArgumentException.class)
    public void failsWithInvalidPackageName() {
        givenInvalidBasePackage();
        whenCreatingExplorer();
    }

    private void givenInvalidBasePackage() {
        basePackages = asList("poussecafe.discovery.");
    }

    private void whenCreatingExplorer() {
        explorer = new ClassPathExplorer(basePackages);
    }

    private List<String> basePackages;

    private ClassPathExplorer explorer;

    @Test
    public void successWithValidPackageName() {
        givenValidBasePackage();
        whenCreatingExplorer();
        thenExplorerDiscoversProperly();
    }

    private void givenValidBasePackage() {
        basePackages = asList("poussecafe.discovery");
    }

    private void thenExplorerDiscoversProperly() {
        assertThat(explorer.discoverAggregates().size(), is(1));
    }
}
