package poussecafe.sample.workflow;

import poussecafe.sample.command.AddUnits;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.service.Process;

public class ProductManagement extends Process {

    private ProductFactory productFactory;

    private ProductRepository productRepository;

    public void createProduct(CreateProduct command) {
        Product product = productFactory.buildProductWithNoStock(command.getProductKey());
        runInTransaction(Product.Data.class, () -> productRepository.add(product));
    }

    public void addUnits(AddUnits command) {
        runInTransaction(Product.Data.class, () -> {
            Product product = productRepository.get(command.getProductKey());
            product.addUnits(command.getUnits());
            productRepository.update(product);
        });
    }

    public void setProductFactory(ProductFactory productFactory) {
        this.productFactory = productFactory;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
