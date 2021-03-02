package poussecafe.test;

import org.junit.Test;
import poussecafe.discovery.BundleConfigurer;
import poussecafe.runtime.Runtime.Builder;
import poussecafe.test.domain.chain1.Chain1Element;
import poussecafe.test.domain.chain1.Chain1ElementData;
import poussecafe.test.domain.chain1.Chain1ElementId;
import poussecafe.test.domain.chain1.Chain1ElementRepository;

import static org.junit.Assert.assertTrue;

public class DataSetTest extends PousseCafeTest {

    @Test
    public void validDataLoadByClass() {
        given(dataSet);
        assertTrue(chain1ElementRepository.existsById(testId()));
    }

    private DataSet dataSet = new DataSet.Builder()
            .withAggregateData(Chain1Element.class, dataSetAttributes())
            .build();

    private Chain1ElementData dataSetAttributes() {
        Chain1ElementData attributes = new Chain1ElementData();
        attributes.identifier().value(testId());
        return attributes;
    }

    private Chain1ElementId testId() {
        return new Chain1ElementId("test");
    }

    private Chain1ElementRepository chain1ElementRepository;

    @Override
    protected Builder runtimeBuilder() {
        return super.runtimeBuilder()
                .withBundle(new BundleConfigurer.Builder()
                        .module(TestModule.class)
                        .build()
                    .defineAndImplementDefault()
                    .build());
    }
}
