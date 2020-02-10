package poussecafe.storage.internal;

import java.util.Optional;
import org.junit.Test;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.storage.internal.OptimisticLocker;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OptimisticLockerTest {

    @Test
    public void emptyVersionIfNoField() {
        givenUnversionedObject();
        whenGettingVersion();
        thenExpectedVersionIs(Optional.empty());
    }

    private void givenUnversionedObject() {
        object = new UnversionedObject();
    }

    private static class UnversionedObject {

    }

    private Object object;

    private void whenGettingVersion() {
        actualVersion = new OptimisticLocker(VERSION_FIELD).getVersion(object);
    }

    private static final String VERSION_FIELD = "version";

    private Optional<Long> actualVersion;

    private void thenExpectedVersionIs(Optional<Long> expectedVersion) {
        assertThat(actualVersion, is(expectedVersion));
    }

    @Test(expected = OptimisticLockingException.class)
    public void exceptionIfWrongType() {
        givenVersionedObjectWithWrongType();
        whenGettingVersion();
    }

    private void givenVersionedObjectWithWrongType() {
        object = new VersionedObjectWithWrongType();
    }

    private static class VersionedObjectWithWrongType {

        @SuppressWarnings("unused")
        private String version;
    }

    @Test
    public void expectedVersionWithVersioned() {
        givenVersionedObjectWithVersion(42L);
        whenGettingVersion();
        thenExpectedVersionIs(Optional.of(42L));
    }

    private void givenVersionedObjectWithVersion(long version) {
        object = new VersionedObject(version);
    }

    private static class VersionedObject {

        @SuppressWarnings("unused")
        private long version;

        public VersionedObject() {
            version = -1;
        }

        public VersionedObject(long version) {
            this.version = version;
        }
    }

    @Test(expected = OptimisticLockingException.class)
    public void exceptionIfUnexpectedVersion() {
        givenVersionedObjectWithVersion(42L);
        whenIncrementingWithExpectedVersion(41L);
    }

    private void whenIncrementingWithExpectedVersion(long expectedVersion) {
        OptimisticLocker locker = new OptimisticLocker(VERSION_FIELD);
        locker.ensureAndIncrement(expectedVersion, object);
        actualVersion = locker.getVersion(object);
    }

    @Test
    public void versionIncrementedIfExpectedVersion() {
        givenVersionedObjectWithVersion(42L);
        whenIncrementingWithExpectedVersion(42L);
        thenExpectedVersionIs(Optional.of(43L));
    }

    @Test
    public void initialVersionIsZero() {
        givenVersionedObjectWithNoVersion();
        whenInitializingVersion();
        thenExpectedVersionIs(Optional.of(0L));
    }

    private void givenVersionedObjectWithNoVersion() {
        object = new VersionedObject();
    }

    private void whenInitializingVersion() {
        OptimisticLocker locker = new OptimisticLocker(VERSION_FIELD);
        locker.initializeVersion(object);
        actualVersion = locker.getVersion(object);
    }
}
