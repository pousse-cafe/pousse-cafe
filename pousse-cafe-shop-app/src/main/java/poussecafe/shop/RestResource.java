package poussecafe.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import poussecafe.runtime.Runtime;
import poussecafe.shop.command.CreateProduct;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductKey;
import poussecafe.shop.domain.ProductRepository;

@RestController
public class RestResource {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Runtime runtime;

    @PostMapping(path = "/product")
    public void createProduct(@RequestBody CreateProductView input) {
        logger.info("Creating product with key {}", input.key);
        ProductKey productKey = new ProductKey(input.key);

        CreateProduct command = runtime.newCommand(CreateProduct.class);
        command.productKey().value(productKey);
        runtime.submitCommand(command);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping(path = "/product/{key}")
    public ProductView getProduct(@PathVariable("key") String key) {
        logger.info("Fetching product with key {}", key);
        ProductKey productKey = new ProductKey(key);
        Product product = productRepository.get(productKey);

        ProductView view = new ProductView();
        view.key = key;
        view.availableUnits = product.attributes().availableUnits().value();
        view.totalUnits = product.attributes().totalUnits().value();
        return view;
    }
}
