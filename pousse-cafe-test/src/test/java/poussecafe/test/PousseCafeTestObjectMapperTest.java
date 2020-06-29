package poussecafe.test;

import org.junit.Test;
import poussecafe.discovery.BundleConfigurer;
import poussecafe.domain.chain1.Chain1ElementId;
import poussecafe.domain.chain1.Chain1ElementRepository;
import poussecafe.exception.PousseCafeException;
import poussecafe.runtime.Runtime.Builder;

import static org.junit.Assert.assertTrue;

public class PousseCafeTestObjectMapperTest extends PousseCafeTest {

    @Test
    public void validDataLoad() {
        loadDataFile("/validChain1ElementData.json");
        assertTrue(chain1ElementRepository.existsById(new Chain1ElementId("test")));
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

    @Test(expected = PousseCafeException.class)
    public void dataWithUnknownFieldFails() {
        loadDataFile("/chain1ElementDataWithUnknownField.json");
    }
}
