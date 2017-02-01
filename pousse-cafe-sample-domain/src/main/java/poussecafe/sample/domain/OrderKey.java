package poussecafe.sample.domain;

public class OrderKey {

    private ProductKey productKey;

    private String reference;

    public OrderKey(ProductKey productKey, String reference) {
        setProductKey(productKey);
        setReference(reference);
    }

    public ProductKey getProductKey() {
        return productKey;
    }

    public void setProductKey(ProductKey productKey) {
        this.productKey = productKey;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        return "OrderKey [productKey=" + productKey + ", reference=" + reference + "]";
    }
}
