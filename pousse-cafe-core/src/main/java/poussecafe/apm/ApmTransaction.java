package poussecafe.apm;

public interface ApmTransaction extends ApmSpan {

    void setResult(String result);

    void addLabel(String key, String value);
}
