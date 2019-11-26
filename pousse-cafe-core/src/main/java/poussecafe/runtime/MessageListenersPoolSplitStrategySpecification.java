package poussecafe.runtime;


public class MessageListenersPoolSplitStrategySpecification {

    public static class Builder {

        private MessageListenersPoolSplitStrategySpecification specification = new MessageListenersPoolSplitStrategySpecification();

        public Builder expectedNumberOfPools(int expectedNumberOfPools) {
            specification.expectedNumberOfPools = expectedNumberOfPools;
            return this;
        }

        public Builder strategyType(MessageListenersPoolSplitStrategyType strategyType) {
            specification.strategyType = strategyType;
            return this;
        }

        public MessageListenersPoolSplitStrategySpecification build() {
            return specification;
        }
    }

    private MessageListenersPoolSplitStrategySpecification() {

    }

    private int expectedNumberOfPools;

    public int expectedNumberOfPools() {
        return expectedNumberOfPools;
    }

    private MessageListenersPoolSplitStrategyType strategyType;

    public MessageListenersPoolSplitStrategyType strategyType() {
        return strategyType;
    }
}
