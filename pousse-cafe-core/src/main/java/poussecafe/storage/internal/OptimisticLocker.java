package poussecafe.storage.internal;

import java.util.Objects;
import java.util.Optional;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.util.ReflectionUtils;

public class OptimisticLocker {

    public OptimisticLocker(String versionField) {
        Objects.requireNonNull(versionField);
        this.versionField = versionField;
    }

    private String versionField;

    public void initializeVersion(Object data) {
        setVersion(data, 0L);
    }

    private void setVersion(
            Object data,
            long version) {
        ReflectionUtils.access(data).instanceField(versionField).set(version);
    }

    public void ensureAndIncrement(long expectedVersion, Object data) {
        Optional<Long> actualVersion = getVersion(data);
        if(!actualVersion.isPresent() || actualVersion.get() != expectedVersion) {
            throw new OptimisticLockingException("Actual version " + actualVersion.orElse(-1L) + " <> " + expectedVersion);
        }
        setVersion(data, expectedVersion + 1);
    }

    public Optional<Long> getVersion(Object data) {
        var instanceField = ReflectionUtils.access(data).instanceField(versionField);
        if(instanceField.isPresent()) {
            Object value = instanceField.get();
            if(value instanceof Long) {
                return Optional.of((Long) value);
            } else {
                throw new OptimisticLockingException("Version field must have Long or long type");
            }
        } else {
            return Optional.empty();
        }
    }
}
