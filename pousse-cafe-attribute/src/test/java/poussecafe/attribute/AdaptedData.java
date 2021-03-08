package poussecafe.attribute;

import static poussecafe.util.Equality.referenceEquals;

public class AdaptedData {

    public AdaptedData() {

    }

    public AdaptedData(String data) {
        this.data = data;
    }

    public String data;

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(data -> data.data.equals(this.data));
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
}
