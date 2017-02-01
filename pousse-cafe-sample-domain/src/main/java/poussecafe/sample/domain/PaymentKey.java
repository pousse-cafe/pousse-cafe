package poussecafe.sample.domain;

public class PaymentKey {

    private String id;

    public PaymentKey(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }
}
