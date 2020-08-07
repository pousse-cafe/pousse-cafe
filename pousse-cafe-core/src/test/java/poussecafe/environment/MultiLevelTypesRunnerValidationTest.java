package poussecafe.environment;

public class MultiLevelTypesRunnerValidationTest extends RunnerValidationTest {

    @SuppressWarnings("rawtypes")
    @Override
    protected Class runnerClass() {
        return MultiLevelTypesRunner.class;
    }
}
