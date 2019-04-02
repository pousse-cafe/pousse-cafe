package poussecafe.shop.adapters.storage;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Product.class,
    dataImplementation = ProductData.class,
    storageName = InternalStorage.NAME
)
public class ProductDataAccess extends InternalDataAccess<ProductId, Product.Attributes> {

}
