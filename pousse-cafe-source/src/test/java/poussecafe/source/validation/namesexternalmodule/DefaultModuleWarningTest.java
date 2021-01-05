package poussecafe.source.validation.namesexternalmodule;

import org.junit.Test;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;

@SuppressWarnings("squid:S2699")
public class DefaultModuleWarningTest extends ValidatorTest {

    @Test
    public void defaultModuleWarning() {
        givenValidator();
        givenComponents();
        whenValidating();
        thenAtLeast(this::aggregateWarning);
        thenAtLeast(this::entityWarning);
        thenAtLeast(this::messageWarning);
        thenAtLeast(this::processWarning);
    }

    private void givenComponents() {
        includeClass(MyAggregate.class);
        includeClass(MyEntity.class);
        includeClass(MyMessage.class);
        includeClass(MyProcess.class);
    }

    private boolean aggregateWarning(ValidationMessage message) {
        return defaultModuleWarning(message, "/MyAggregate.java");
    }

    private boolean defaultModuleWarning(ValidationMessage message, String fileNameSuffix) {
        return message.location().sourceFile().id().endsWith(fileNameSuffix)
                && message.message().contains("in default module")
                && message.type() == ValidationMessageType.WARNING;
    }

    private boolean entityWarning(ValidationMessage message) {
        return defaultModuleWarning(message, "/MyEntity.java");
    }

    private boolean messageWarning(ValidationMessage message) {
        return defaultModuleWarning(message, "/MyMessage.java");
    }

    private boolean processWarning(ValidationMessage message) {
        return defaultModuleWarning(message, "/MyProcess.java");
    }

    @Test
    public void externalModuleNoMessage() {
        givenValidator();
        givenExternalModule();
        givenComponents();
        whenValidating();
        thenNone(this::aggregateWarning);
        thenNone(this::entityWarning);
        thenNone(this::messageWarning);
        thenNone(this::processWarning);
    }

    private void givenExternalModule() {
        addSubType(new Name(CompilationUnitResolver.MODULE_INTERFACE),
                new Name("poussecafe.source.validation.namesexternalmodule.MyModule"));
    }
}
