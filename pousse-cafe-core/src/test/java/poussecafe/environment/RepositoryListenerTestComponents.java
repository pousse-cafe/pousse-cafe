package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;

public class RepositoryListenerTestComponents {

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        public static interface Attributes extends EntityAttributes<String> {

        }
    }

    public static class Repository extends AggregateRepository<String, Root, Root.Attributes> {

        public static Method deleteSingleMethod() {
            return listener("deleteSingle");
        }

        private static Method listener(String name) {
            try {
                return Repository.class.getMethod(name, Event.class);
            } catch (Exception e) {
                throw new UnsupportedOperationException();
            }
        }

        public static Method deleteOptionalMethod() {
            return listener("deleteOptional");
        }

        public static Method deleteSeveralMethod() {
            return listener("deleteSeveral");
        }

        public void legacyDelete(Event event) {

        }

        public String deleteSingle(Event event) {
            return null;
        }

        public Optional<String> deleteOptional(Event event) {
            return null;
        }

        public List<String> deleteSeveral(Event event) {
            return null;
        }
    }

    public static interface Event extends DomainEvent {

    }
}
