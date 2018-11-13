package poussecafe.property;

import org.junit.Test;
import poussecafe.property.accessor.PropertyContainerInAnotherPackage;

import static org.junit.Assert.assertTrue;

public class ProtectedPropertyTest {

    @Test
    public void allowReadForAllByDefault() {
        givenContainerInAnotherPackage();
        whenBuildingProtectedProperty();
        thenValueIsReadable(true);
    }

    private void givenContainerInAnotherPackage() {
        propertyContainer = new PropertyContainerInAnotherPackage();
    }

    private PropertyContainer propertyContainer;

    private void whenBuildingProtectedProperty() {
        protectedProperty = defaultPropertyBuilder().build();
    }

    private ConfigurableProtectedProperty<String> defaultPropertyBuilder() {
        return ProtectedPropertyBuilder.protect(propertyContainer.property()).of(propertyContainer);
    }

    private ProtectedProperty<String> protectedProperty;

    private void thenValueIsReadable(boolean readable) {
        boolean success;
        try {
            protectedProperty.let(this).get();
            success = readable;
        } catch(AccessPolicyException e) {
            success = !readable;
        }
        assertTrue(success);
    }

    @Test
    public void allowNoWriteForAllByDefault() {
        givenContainerInAnotherPackage();
        whenBuildingProtectedProperty();
        thenValueIsWritable(false);
    }

    private void thenValueIsWritable(boolean writable) {
        boolean success;
        try {
            protectedProperty.let(this).set("other value");
            success = writable;
        } catch(AccessPolicyException e) {
            success = !writable;
        }
        assertTrue(success);
    }

    @Test
    public void allowWriteInSamePackage() {
        givenContainerInSamePackage();
        whenBuildingPackageProtectedProperty();
        thenValueIsWritable(true);
    }

    private void givenContainerInSamePackage() {
        propertyContainer = new PropertyContainerInSamePackage();
    }

    private void whenBuildingPackageProtectedProperty() {
        protectedProperty = defaultPropertyBuilder()
                .allowPackageWrite(true)
                .build();
    }

    @Test
    public void allowWriteToClass() {
        givenContainerInAnotherPackage();
        whenBuildingClassProtectedProperty();
        thenValueIsWritable(true);
    }

    private void whenBuildingClassProtectedProperty() {
        protectedProperty = defaultPropertyBuilder()
                .allowClassWrite(getClass())
                .build();
    }
}
