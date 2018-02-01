package poussecafe.sample.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.sample.process.ProductManagement;

@RestController
public class RestResource {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductManagement productManagement;

    @RequestMapping(path = "/product", method = RequestMethod.POST)
    public void createProduct(@RequestBody CreateProductView input) {
        logger.info("Creating product with key {}", input.key);
        ProductKey productKey = new ProductKey(input.key);
        productManagement.createProduct(new CreateProduct(productKey));
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(path = "/product/{key}", method = RequestMethod.GET)
    public ProductView getProduct(@PathVariable("key") String key) {
        logger.info("Fetching product with key {}", key);
        ProductKey productKey = new ProductKey(key);
        Product product = productRepository.find(productKey);

        ProductView view = new ProductView();
        view.key = key;
        view.availableUnits = product.getAvailableUnits();
        view.totalUnits = product.getTotalUnits();
        return view;
    }
}
