package poussecafe.sample.adapters.storage;

import poussecafe.contextconfigurer.DataAccessImplementation;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Product.class,
    dataImplementation = ProductData.class,
    storageName = InternalStorage.NAME
)
public class ProductDataAccess extends InternalDataAccess<ProductKey, Product.Data> {

}
