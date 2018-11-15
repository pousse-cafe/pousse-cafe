package poussecafe.util;

import java.util.Optional;
import java.util.function.Function;

public class ReferenceEquals {

    private ReferenceEquals() {

    }

    @SuppressWarnings("unchecked")
    public static <T> OrElse<T> referenceEquals(T thisObject, Object thatObject) {
        if (thisObject == thatObject) {
            return new OrElse<>(true);
        }
        if (thatObject == null) {
            return new OrElse<>(false);
        }
        if (thisObject.getClass() != thatObject.getClass()) {
            return new OrElse<>(false);
        }
        return new OrElse<>((T) thatObject);
    }

    public static class OrElse<T> {

        private OrElse(boolean simpleEqualsResult) {
            this.simpleEqualsResult = Optional.of(simpleEqualsResult);
        }

        private Optional<Boolean> simpleEqualsResult = Optional.empty();

        private OrElse(T other) {
            this.other = other;
        }

        private T other;

        public Boolean orElse(Function<T, Boolean> equalsFunction) {
            if(simpleEqualsResult.isPresent()) {
                return simpleEqualsResult.get();
            } else {
                return equalsFunction.apply(other);
            }
        }
    }
}
