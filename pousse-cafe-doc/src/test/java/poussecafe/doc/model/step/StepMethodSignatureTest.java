package poussecafe.doc.model.step;

import java.util.Optional;
import org.junit.Test;
import poussecafe.doc.model.domainprocessdoc.ComponentMethodName;
import poussecafe.doc.model.processstepdoc.StepMethodSignature;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StepMethodSignatureTest {

    @Test
    public void signatureStringWithEventIsExpected() {
        givenStepMethodSignature(new StepMethodSignature.Builder()
                .componentMethodName(new ComponentMethodName.Builder()
                        .componentName("A")
                        .methodName("b")
                        .build())
                .consumedMessageName(Optional.of("E"))
                .build());
        whenBuildingSignatureString();
        thenSignatureStringIs("A.b(E)");
    }

    private void givenStepMethodSignature(StepMethodSignature signature) {
        this.signature = signature;
    }

    private StepMethodSignature signature;

    private void whenBuildingSignatureString() {
        signatureString = signature.toString();
    }

    private String signatureString;

    private void thenSignatureStringIs(String expected) {
        assertThat(signatureString, equalTo(expected));
    }

    @Test
    public void signatureWithEventIsExpected() {
        givenStepMethodSignatureString("A.b(E)");
        whenParsingSignatureString();
        thenSignatureIs(new StepMethodSignature.Builder()
                .componentMethodName(new ComponentMethodName.Builder()
                        .componentName("A")
                        .methodName("b")
                        .build())
                .consumedMessageName(Optional.of("E"))
                .build());
    }

    private void givenStepMethodSignatureString(String string) {
        signatureString = string;
    }

    private void whenParsingSignatureString() {
        signature = StepMethodSignature.parse(signatureString);
    }

    private void thenSignatureIs(StepMethodSignature expected) {
        assertThat(signature, equalTo(expected));
    }

    @Test
    public void signatureStringWithoutEventIsExpected() {
        givenStepMethodSignature(new StepMethodSignature.Builder()
                .componentMethodName(new ComponentMethodName.Builder()
                        .componentName("A")
                        .methodName("b")
                        .build())
                .consumedMessageName(Optional.empty())
                .build());
        whenBuildingSignatureString();
        thenSignatureStringIs("A.b()");
    }

    @Test
    public void signatureWithoutEventIsExpected() {
        givenStepMethodSignatureString("A.b()");
        whenParsingSignatureString();
        thenSignatureIs(new StepMethodSignature.Builder()
                .componentMethodName(new ComponentMethodName.Builder()
                        .componentName("A")
                        .methodName("b")
                        .build())
                .consumedMessageName(Optional.empty())
                .build());
    }
}
