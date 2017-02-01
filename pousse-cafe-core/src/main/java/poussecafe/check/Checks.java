package poussecafe.check;

public class Checks {

    public static <T> void checkThat(CheckSpecification<T> specification) {
        new Check<>(specification).run();
    }

}
