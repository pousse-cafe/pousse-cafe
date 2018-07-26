package poussecafe.doc;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class AggregateGraphPath {

    public AggregateGraphPath with(String name) {
        AggregateGraphPath newPath = new AggregateGraphPath();
        newPath.names = new ArrayList<>(names);
        newPath.addName(name);
        return newPath;
    }

    private List<String> names = new ArrayList<>();

    private void addName(String name) {
        names.add(name);
    }

    public String formatNames() {
        return formatPath(names);
    }

    private String formatPath(List<String> newPath) {
        return newPath.stream().collect(joining(" -> "));
    }

    public String lastName() {
        return names.get(names.size() - 1);
    }

    public String formatNamesWith(String newNodeName) {
        List<String> newNames = new ArrayList<>(names);
        newNames.add(newNodeName);
        return formatPath(newNames);
    }
}
