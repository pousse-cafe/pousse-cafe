package poussecafe.environment;

public class SingleLevelTypesImplementingRunnerValidationTest extends RunnerValidationTest {

    @SuppressWarnings("rawtypes")
    @Override
    protected Class runnerClass() {
        return SingleLevelTypesImplementingRunner.class;
    }
}
