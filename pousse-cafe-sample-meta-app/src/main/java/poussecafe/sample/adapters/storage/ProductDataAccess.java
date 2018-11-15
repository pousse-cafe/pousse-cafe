package poussecafe.sample.adapters.storage;

import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.storage.memory.InMemoryDataAccess;

public class ProductDataAccess extends InMemoryDataAccess<ProductKey, Product.Data> {

}
