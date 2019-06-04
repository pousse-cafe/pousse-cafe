package poussecafe.shop.domain;

import poussecafe.domain.EntityDataAccess;

public interface ProductDataAccess<N extends Product.Attributes> extends EntityDataAccess<ProductId, N> {
}
