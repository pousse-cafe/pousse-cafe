package poussecafe.sample;

import java.util.List;
import poussecafe.environment.BoundedContext;
import poussecafe.shop.Shop;
import poussecafe.support.PousseCafeSupport;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;

public abstract class ShopTest extends PousseCafeTest {

    @Override
    protected List<BoundedContext> boundedContexts() {
        return asList(Shop.configure().defineAndImplementDefault().build(),
                PousseCafeSupport.configure().defineAndImplementDefault().build());
    }
}
