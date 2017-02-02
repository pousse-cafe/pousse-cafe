package poussecafe.sample.workflow;

import poussecafe.consequence.CommandListener;
import poussecafe.sample.command.AddUnits;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.service.Workflow;

public class ProductManagement extends Workflow {

    private ProductFactory productFactory;

    private ProductRepository productRepository;

    @CommandListener
    public void createProduct(CreateProduct command) {
        Product product = productFactory.buildProductWithNoStock(command.getProductKey());
        runInTransaction(() -> productRepository.add(product));
    }

    @CommandListener
    public void addUnits(AddUnits command) {
        runInTransaction(() -> {
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
