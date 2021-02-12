package poussecafe.discovery;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ReflectionsWrapperTest {

    @Test(expected = IllegalArgumentException.class)
    public void instantiationFailsWithWrongPackageName() {
        givenPackage("1.2.3");
        whenInstantiatingWrapper();
    }

    private void givenPackage(String packageName) {
        packages.add(packageName);
    }

    private List<String> packages = new ArrayList<>();

    private void whenInstantiatingWrapper() {
        new ReflectionsWrapper(packages);
    }

    @Test
    public void instantiationSuccessWithValidPackageName() {
        givenPackage("a.b.c");
        whenInstantiatingWrapper();
    }

    @Test
    public void instantiationSuccessWithValidButDiscouragedPackageName() {
        givenPackage("a.B.c");
        whenInstantiatingWrapper();
    }

    @Test
    public void instantiationSuccessWithMoreRealisticPackageName() {
        givenPackage("poussecafe.discovery.test");
        whenInstantiatingWrapper();
    }

    @Test
    public void instantiationSuccessWithSingleSegmentPackageName() {
        givenPackage("poussecafe");
        whenInstantiatingWrapper();
    }
}
