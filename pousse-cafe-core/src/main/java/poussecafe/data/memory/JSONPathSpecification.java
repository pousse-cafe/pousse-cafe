package poussecafe.data.memory;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import java.util.function.Predicate;

public class JSONPathSpecification {

    private String jsonPath;

    private Predicate<?> predicate;

    public JSONPathSpecification(String jsonPath, Predicate<?> predicate) {
        setJsonPath(jsonPath);
        setPredicate(predicate);
    }

    private void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    private void setPredicate(Predicate<?> predicate) {
        this.predicate = predicate;
    }

    public boolean matches(String json) {
        try {
            return predicate.test(JsonPath.read(json, jsonPath));
        } catch (PathNotFoundException e) { // NOSONAR
            return false;
        }
    }

}
