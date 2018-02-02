package poussecafe.sample.process;

import poussecafe.process.DomainProcess;
import poussecafe.sample.command.AddUnits;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductRepository;

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
