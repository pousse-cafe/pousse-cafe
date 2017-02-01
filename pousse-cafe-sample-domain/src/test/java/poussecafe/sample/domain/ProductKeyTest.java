package poussecafe.sample.domain;

import java.util.List;
import poussecafe.test.KeyTest;

import static java.util.Arrays.asList;

public class ProductKeyTest extends KeyTest<ProductKey> {

    @Override
    protected ProductKey referenceKey() {
        return new ProductKey("product-1");
    }

    @Override
    protected List<Object> otherKeys() {
        return asList(new ProductKey("product-2"), new ProductKey("product-3"));
    }

}
