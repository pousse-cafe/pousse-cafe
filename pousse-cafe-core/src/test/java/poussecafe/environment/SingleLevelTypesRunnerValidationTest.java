package poussecafe.environment;

public class SingleLevelTypesRunnerValidationTest extends RunnerValidationTest {

    @SuppressWarnings("rawtypes")
    @Override
    protected Class runnerClass() {
        return SingleLevelTypesRunner.class;
    }
}
