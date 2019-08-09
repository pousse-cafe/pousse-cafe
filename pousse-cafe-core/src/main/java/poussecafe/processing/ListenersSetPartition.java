package poussecafe.processing;

import java.util.Objects;

class ListenersSetPartition {

    public static class Builder {

        private ListenersSetPartition partition = new ListenersSetPartition();

        public Builder ofSet(ListenersSet ofSet) {
            partition.ofSet = ofSet;
            return this;
        }

        public Builder partitionListenersSet(ListenersSet partitionListenersSet) {
            partition.partitionListenersSet = partitionListenersSet;
            return this;
        }

        public ListenersSetPartition build() {
            Objects.requireNonNull(partition.ofSet);
            Objects.requireNonNull(partition.partitionListenersSet);
            return partition;
        }
    }

    private ListenersSetPartition() {

    }

    public ListenersSet ofSet() {
        return ofSet;
    }

    private ListenersSet ofSet;

    public ListenersSet partitionListenersSet() {
        return partitionListenersSet;
    }

    private ListenersSet partitionListenersSet;
}
