package poussecafe.storage.memory;

import java.util.Optional;
import poussecafe.util.ReflectionException;
import poussecafe.util.ReflectionUtils;

import static poussecafe.check.Checks.checkThatValue;

public class OptimisticLocker {

    public OptimisticLocker(String versionField) {
        checkThatValue(versionField).notNull();
        this.versionField = versionField;
    }

    private String versionField;

    public void ensureAndIncrement(long expectedVersion, Object data) {
        Optional<Long> actualVersion = getVersion(data);
        if(!actualVersion.isPresent() || actualVersion.get() != expectedVersion) {
            throw new OptimisticLockingException("Actual version " + actualVersion + " <> " + expectedVersion);
        }
        setVersion(data, expectedVersion + 1);
    }

    public Optional<Long> getVersion(Object data) {
        try {
            Object value = ReflectionUtils.access(data).get(versionField);
            if(value instanceof Long) {
                return Optional.of((Long) value);
            } else {
                throw new OptimisticLockingException("Version field must have Long or long type");
            }
        } catch(ReflectionException e) {
            return Optional.empty();
        }
    }

    private void setVersion(
            Object data,
            long version) {
        ReflectionUtils.access(data).set(versionField, version);
    }

    public void initializeVersion(Object data) {
        setVersion(data, 0L);
    }
}
