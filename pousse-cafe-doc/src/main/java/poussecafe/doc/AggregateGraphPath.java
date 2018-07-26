package poussecafe.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.util.stream.Collectors.joining;

public class AggregateGraphPath {

    public AggregateGraphPath with(String name, String uniqueName) {
        AggregateGraphPath newPath = new AggregateGraphPath();
        newPath.names = new ArrayList<>(names);
        newPath.uniqueNames = new ArrayList<>(uniqueNames);
        newPath.addNames(name, uniqueName);
        return newPath;
    }

    private List<String> names = new ArrayList<>();

    private List<String> uniqueNames = new ArrayList<>();

    private void addNames(String name, String uniqueName) {
        names.add(name);
        uniqueNames.add(uniqueName);
    }

    public String formatNames() {
        return formatPath(names);
    }

    private String formatPath(List<String> newPath) {
        return newPath.stream().collect(joining(" -> "));
    }

    public ListIterator<String> uniqueNamesEndIterator() {
        return uniqueNames.listIterator(uniqueNames.size());
    }

    public String lastUniqueName() {
        return uniqueNames.get(uniqueNames.size() - 1);
    }

    public String formatNamesWith(String newNodeName) {
        List<String> newNames = new ArrayList<>(names);
        newNames.add(newNodeName);
        return formatPath(newNames);
    }
}
