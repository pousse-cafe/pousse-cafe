package poussecafe.environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.exception.SameOperationException;
import poussecafe.runtime.DuplicateKeyException;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.util.MethodInvokerException;

public class MessageConsumptionReport {

    public static MessageConsumptionReport success() {
        return new MessageConsumptionReport.Builder().build();
    }

    public static class Builder {

        private MessageConsumptionReport report = new MessageConsumptionReport();

        public Builder failure(Throwable failure) {
            report.failures.add(failure);
            return this;
        }

        public Builder skipped(boolean skipped) {
            report.skipped = skipped;
            return this;
        }

        public Builder toRetry(boolean toRetry) {
            report.toRetry = toRetry;
            return this;
        }

        public Builder listenerType(MessageListenerType listenerType) {
            report.listenerType = listenerType;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder aggregateType(Class aggregateType) {
            report.aggregateType = Optional.of(aggregateType);
            return this;
        }

        public Builder aggregateId(Object aggregateId) {
            if(!report.allAggregatesIds.add(aggregateId)) {
                throw new IllegalArgumentException("Already reported " + aggregateId);
            }
            return this;
        }

        public Builder successfulAggregateId(Object successfulAggregateId) {
            if(!report.successfulAggregatesIds.add(successfulAggregateId)) {
                throw new IllegalArgumentException("Already reported success for " + successfulAggregateId);
            }
            return this;
        }

        public Builder failedAggregateId(Object failedAggregateId) {
            if(!report.failedAggregatesIds.add(failedAggregateId)) {
                throw new IllegalArgumentException("Already reported failure for " + failedAggregateId);
            }
            return this;
        }

        public Builder aggregateIdToRetry(Object aggregateIdToRetry) {
            if(!report.aggregateIdsToRetry.add(aggregateIdToRetry)) {
                throw new IllegalArgumentException("Already reported retry for " + aggregateIdToRetry);
            }
            return this;
        }

        public Builder skippedAggregateId(Object skippedAggregateId) {
            if(!report.skippedAggregatesIds.add(skippedAggregateId)) {
                throw new IllegalArgumentException("Already reported skip for " + skippedAggregateId);
            }
            return this;
        }

        public MessageConsumptionReport build() {
            if(!report.allAggregatesIds.isEmpty() && report.aggregateType.isEmpty()) {
                throw new IllegalStateException("Aggregate IDs provided but no type given");
            }

            if(!report.allAggregatesIds.containsAll(report.skippedAggregatesIds)
                    || !report.allAggregatesIds.containsAll(report.aggregateIdsToRetry)
                    || !report.allAggregatesIds.containsAll(report.failedAggregatesIds)
                    || !report.allAggregatesIds.containsAll(report.successfulAggregatesIds)) {
                throw new IllegalStateException("Unknown aggregate IDs reported");
            }

            int allAggregates = report.allAggregatesIds.size();
            int aggregatesToRetry = report.aggregateIdsToRetry.size();
            int failedAggregates = report.failedAggregatesIds.size();
            int successfulAggregates = report.successfulAggregatesIds.size();
            int skippedAggregates = report.skippedAggregatesIds.size();
            if(allAggregates != (aggregatesToRetry + failedAggregates + successfulAggregates + skippedAggregates)) {
                throw new IllegalStateException(String.format("Unconsistent sets of IDs: %d <> %d + %d + %d + %d",
                        allAggregates,
                        aggregatesToRetry,
                        failedAggregates,
                        successfulAggregates,
                        skippedAggregates));
            }

            return report;
        }

        public void runAndReport(MessageListenerGroupConsumptionState state, Object id, Runnable runnable) {
            aggregateId(id);
            try {
                runnable.run();
                successfulAggregateId(id);
            } catch (SameOperationException e) {
                logger.debug("Will skip", e);
                skippedAggregateId(id);
            } catch (OptimisticLockingException e) {
                logWillRetry(e);
                aggregateIdToRetry(id);
            } catch (DuplicateKeyException e) {
                if(state.isFirstConsumption()) {
                    logWillRetry(e);
                    aggregateIdToRetry(id);
                } else {
                    logWillFail(e);
                    failure(e);
                    failedAggregateId(id);
                }
            } catch (MethodInvokerException e) {
                logWillFail(e.getCause());
                failure(e.getCause());
                failedAggregateId(id);
            } catch (Exception e) {
                logWillFail(e);
                failure(e);
                failedAggregateId(id);
            }
        }

        private void logWillRetry(Throwable e) {
            logger.warn("Will retry", e);
        }

        private void logWillFail(Throwable e) {
            logger.error("Will fail", e);
        }

        private Logger logger = LoggerFactory.getLogger(getClass());
    }

    private MessageConsumptionReport() {

    }

    private List<Throwable> failures = new ArrayList<>();

    public List<Throwable> failures() {
        return failures;
    }

    private boolean skipped;

    public boolean skipped() {
        return skipped;
    }

    private boolean toRetry;

    public boolean toRetry() {
        return toRetry;
    }

    private MessageListenerType listenerType;

    public MessageListenerType listenerType() {
        return listenerType;
    }

    @SuppressWarnings("rawtypes")
    private Optional<Class> aggregateType = Optional.empty();

    @SuppressWarnings("rawtypes")
    public Optional<Class> aggregateType() {
        return aggregateType;
    }

    private Set<Object> allAggregatesIds = new HashSet<>();

    public Set<Object> allAggregatesIds() {
        return Collections.unmodifiableSet(allAggregatesIds);
    }

    private Set<Object> successfulAggregatesIds = new HashSet<>();

    public Set<Object> successfulAggregatesIds() {
        return Collections.unmodifiableSet(successfulAggregatesIds);
    }

    private Set<Object> failedAggregatesIds = new HashSet<>();

    public Set<Object> failedAggregatesIds() {
        return Collections.unmodifiableSet(failedAggregatesIds);
    }

    private Set<Object> aggregateIdsToRetry = new HashSet<>();

    public Set<Object> aggregateIdsToRetry() {
        return Collections.unmodifiableSet(aggregateIdsToRetry);
    }

    private Set<Object> skippedAggregatesIds = new HashSet<>();

    public Set<Object> skippedAggregatesIds() {
        return Collections.unmodifiableSet(skippedAggregatesIds);
    }

    public boolean mustRetry() {
        return toRetry || !aggregateIdsToRetry.isEmpty();
    }

    public boolean isSuccess() {
        return failures.isEmpty() && successfulAggregatesIds.size() == allAggregatesIds.size();
    }

    public boolean isSkipped() {
        return skipped || !skippedAggregatesIds.isEmpty();
    }

    public boolean isFailed() {
        return (!skipped && !toRetry && !failures.isEmpty()) || !failedAggregatesIds.isEmpty();
    }
}
