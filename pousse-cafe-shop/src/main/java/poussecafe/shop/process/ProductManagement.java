package poussecafe.shop.process;

import poussecafe.process.DomainProcess;
import poussecafe.shop.command.AddUnits;
import poussecafe.shop.command.CreateProduct;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductFactory;
import poussecafe.shop.domain.ProductRepository;

public class ProductManagement extends DomainProcess {

    private ProductFactory productFactory;

    private ProductRepository productRepository;

    public void createProduct(CreateProduct command) {
        Product product = productFactory.buildProductWithNoStock(command.getProductKey());
        runInTransaction(Product.class, () -> productRepository.add(product));
    }

    public void addUnits(AddUnits command) {
        runInTransaction(Product.class, () -> {
            Product product = productRepository.get(command.getProductKey());
            product.addUnits(command.getUnits());
            productRepository.update(product);
        });
    }
}
