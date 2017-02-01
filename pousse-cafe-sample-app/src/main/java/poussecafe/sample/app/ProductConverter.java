package poussecafe.sample.app;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import poussecafe.sample.domain.Product;

@Component
public class ProductConverter implements Converter<Product, ProductView> {

    @Override
    public ProductView convert(Product source) {
        ProductView view = new ProductView();
        view.productKey = source.getKey().getValue();
        view.totalUnits = source.getTotalUnits();
        view.availableUnits = source.getAvailableUnits();
        return view;
    }

}
