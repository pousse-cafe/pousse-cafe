package poussecafe.check;

public class Checks {

    public static <T> void checkThat(CheckSpecification<T> specification) {
        new Check<>(specification).run();
    }

    public static <T> CheckBuilder<T> checkThatValue(T value) {
        return new CheckBuilder<>(value);
    }
}
