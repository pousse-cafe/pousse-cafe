package poussecafe.sample;

import java.util.List;
import poussecafe.environment.Bundle;
import poussecafe.shop.Shop;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;

public abstract class ShopTest extends PousseCafeTest {

    @Override
    protected List<Bundle> bundles() {
        return asList(Shop.configure().defineAndImplementDefault().build());
    }
}
