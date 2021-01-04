package poussecafe.source.validation.names;

import org.junit.Test;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;
import poussecafe.source.validation.names.moduleconflict.MyModule2;

@SuppressWarnings("squid:S2699")
public class ModulesValidatorTest extends ValidatorTest {

    @Test
    public void conflictingModulesError() {
        givenValidator();
        givenMyModule();
        givenMyModule2();
        whenValidating();
        thenAtLeast(this::module2ConflictError);
    }

    private void givenMyModule() {
        includeClass(MyModule.class);
    }

    private void givenMyModule2() {
        includeClass(MyModule2.class);
    }

    private boolean module2ConflictError(ValidationMessage message) {
        return message.location().sourceFile().id().endsWith("/MyModule2.java")
                && message.message().contains("Base package is a subpackage of module")
                && message.type() == ValidationMessageType.ERROR;
    }

    @Test
    public void duplicateModuleNamesErrors() {
        givenValidator();
        givenMyModule();
        givenMyModuleDuplicate();
        whenValidating();
        thenAtLeast(this::module1DuplicateError);
        thenAtLeast(this::module2DuplicateError);
    }

    private void givenMyModuleDuplicate() {
        includeClass(poussecafe.source.validation.namesmoduleduplicate.MyModule.class);
    }

    private boolean module1DuplicateError(ValidationMessage message) {
        return message.location().sourceFile().id().endsWith("/names/MyModule.java")
                && message.message().contains("with same name already exists")
                && message.type() == ValidationMessageType.ERROR;
    }

    private boolean module2DuplicateError(ValidationMessage message) {
        return message.location().sourceFile().id().endsWith("/namesmoduleduplicate/MyModule.java")
                && message.message().contains("with same name already exists")
                && message.type() == ValidationMessageType.ERROR;
    }
}
