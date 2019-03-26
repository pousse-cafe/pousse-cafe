package poussecafe.shop.domain;

public class OrderKey {

    private ProductKey productKey;

    private CustomerKey customerKey;

    private String reference;

    public OrderKey(ProductKey productKey, CustomerKey customerKey, String reference) {
        setProductKey(productKey);
        setCustomerKey(customerKey);
        setReference(reference);
    }

    public ProductKey getProductKey() {
        return productKey;
    }

    private void setProductKey(ProductKey productKey) {
        this.productKey = productKey;
    }

    public CustomerKey getCustomerKey() {
        return customerKey;
    }

    private void setCustomerKey(CustomerKey customerKey) {
        this.customerKey = customerKey;
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
        result = prime * result + ((customerKey == null) ? 0 : customerKey.hashCode());
        result = prime * result + ((productKey == null) ? 0 : productKey.hashCode());
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
        OrderKey other = (OrderKey) obj;
        if (customerKey == null) {
            if (other.customerKey != null) {
                return false;
            }
        } else if (!customerKey.equals(other.customerKey)) {
            return false;
        }
        if (productKey == null) {
            if (other.productKey != null) {
                return false;
            }
        } else if (!productKey.equals(other.productKey)) {
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
        return "OrderKey [productKey=" + productKey + ", customerKey=" + customerKey + ", reference=" + reference + "]";
    }

}
