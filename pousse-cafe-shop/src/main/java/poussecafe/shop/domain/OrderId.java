package poussecafe.shop.domain;

public class OrderId {

    private ProductId productId;

    private CustomerId customerId;

    private String reference;

    public OrderId(ProductId productId, CustomerId customerId, String reference) {
        setProductId(productId);
        setCustomerId(customerId);
        setReference(reference);
    }

    public ProductId getProductId() {
        return productId;
    }

    private void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    private void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public String getReference() {
        return reference;
    }

    private void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((reference == null) ? 0 : reference.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrderId other = (OrderId) obj;
        if (customerId == null) {
            if (other.customerId != null) {
                return false;
            }
        } else if (!customerId.equals(other.customerId)) {
            return false;
        }
        if (productId == null) {
            if (other.productId != null) {
                return false;
            }
        } else if (!productId.equals(other.productId)) {
            return false;
        }
        if (reference == null) {
            if (other.reference != null) {
                return false;
            }
        } else if (!reference.equals(other.reference)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrderId [productId=" + productId + ", customerId=" + customerId + ", reference=" + reference + "]";
    }

}
