package poussecafe.environment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import poussecafe.exception.SameOperationException;
import poussecafe.runtime.OptimisticLockingException;

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
        
        public Builder listenerType(MessageListenerPriority listenerType) {
            report.listenerType = listenerType;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder aggregateType(Class aggregateType) {
            report.aggregateType = Optional.of(aggregateType);
            return this;
        }

        public Builder aggregateId(Object aggregateId) {
            report.allAggregatesIds.add(aggregateId);
            return this;
        }

        public Builder allAggregatesIds(Set<Object> allAggregatesIds) {
            report.allAggregatesIds.addAll(allAggregatesIds);
            return this;
        }

        public Builder successfulAggregateId(Object successfulAggregateId) {
            report.successfulAggregatesIds.add(successfulAggregateId);
            return this;
        }

        public Builder failedAggregateId(Object failedAggregateId) {
            report.failedAggregatesIds.add(failedAggregateId);
            return this;
        }

        public Builder aggregateIdToRetry(Object aggregateIdToRetry) {
            report.aggregateIdsToRetry.add(aggregateIdToRetry);
            return this;
        }

        public Builder skippedAggregateId(Object skippedAggregateId) {
            report.skippedAggregatesIds.add(skippedAggregateId);
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

            return report;
        }

        public void runAndReport(Object id, Runnable runnable) {
            aggregateId(id);
            try {
                runnable.run();
                successfulAggregateId(id);
            } catch (SameOperationException e) {
                skippedAggregateId(id);
            } catch (OptimisticLockingException e) {
                aggregateIdToRetry(id);
            } catch (Exception e) {
                failure(e);
                failedAggregateId(id);
            }
        }
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
    
    private MessageListenerPriority listenerType;

    public MessageListenerPriority listenerType() {
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
        return allAggregatesIds;
    }

    private Set<Object> successfulAggregatesIds = new HashSet<>();

    public Set<Object> successfulAggregatesIds() {
        return successfulAggregatesIds;
    }

    private Set<Object> failedAggregatesIds = new HashSet<>();

    public Set<Object> failedAggregatesIds() {
        return failedAggregatesIds;
    }

    private Set<Object> aggregateIdsToRetry = new HashSet<>();

    public Set<Object> aggregateIdsToRetry() {
        return aggregateIdsToRetry;
    }

    private Set<Object> skippedAggregatesIds = new HashSet<>();

    public Set<Object> skippedAggregatesIds() {
        return skippedAggregatesIds;
    }

    public boolean mustRetry() {
        return toRetry || !aggregateIdsToRetry.isEmpty();
    }

    public boolean isSuccess() {
        return failures.isEmpty() || successfulAggregatesIds.size() == allAggregatesIds.size();
    }

    public boolean isSkipped() {
        return skipped || !skippedAggregatesIds.isEmpty();
    }

    public boolean isFailed() {
        return !failures.isEmpty() || !failedAggregatesIds.isEmpty();
    }
}
