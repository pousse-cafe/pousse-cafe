package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import java.util.Optional;
import org.junit.Test;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.domainprocessdoc.ComponentMethodName;
import poussecafe.doc.model.step.StepDoc;
import poussecafe.doc.model.step.StepMethodSignature;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AggregateDocTest {

    @Test
    public void stepDocBySignatureWorks() {
        givenAggregateDoc();
        whenLookingUpStepDoc();
        thenExpectedStepDocFound();
    }

    private void givenAggregateDoc() {
        aggregateDoc = new AggregateDoc();
        aggregateDoc.attributes(new AggregateDocData());

        aggregateDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(BoundedContextDocKey.ofPackageName("test"))
                .componentDoc(new ComponentDoc.Builder()
                        .name("A")
                        .description("")
                        .build())
                .build());
        aggregateDoc.attributes().keyClassName().value("test.K");
        aggregateDoc.stepDocs(stepDocs());
    }

    private List<StepDoc> stepDocs() {
        StepDoc stepDoc = new StepDoc.Builder()
                .componentDoc(new ComponentDoc.Builder()
                        .name(methodSignature().toString())
                        .description("")
                        .build())
                .methodSignature(methodSignature())
                .build();
        return asList(stepDoc);
    }

    private StepMethodSignature methodSignature() {
        return new StepMethodSignature.Builder()
                .componentMethodName(new ComponentMethodName.Builder()
                        .aggregateName("A")
                        .methodName("m")
                        .build())
                .consumedEventName(Optional.of("E"))
                .build();
    }

    private AggregateDoc aggregateDoc;

    private void whenLookingUpStepDoc() {
        foundStepDoc = aggregateDoc.stepDocBySignature(methodSignature());
    }

    private Optional<StepDoc> foundStepDoc;

    private void thenExpectedStepDocFound() {
        assertThat(foundStepDoc.get().methodSignature(), equalTo(methodSignature()));
    }
}
