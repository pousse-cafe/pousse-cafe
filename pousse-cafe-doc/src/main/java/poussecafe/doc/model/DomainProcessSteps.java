package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import poussecafe.doc.model.domainprocessdoc.Step;
import poussecafe.doc.model.domainprocessdoc.StepName;
import poussecafe.doc.model.domainprocessdoc.ToStep;

import static java.util.stream.Collectors.toList;

public class DomainProcessSteps {

    public DomainProcessSteps(Map<StepName, Step> steps) {
        Objects.requireNonNull(steps);
        this.steps = steps;
    }

    private Map<StepName, Step> steps;

    public Map<StepName, Step> steps() {
        return steps;
    }

    public List<Step> orderedSteps() {
        if(orderedSteps == null) {
            Map<String, List<String>> graph = buildGraphMap();
            List<String> orderedStepNames = topologicalOrdering(graph);
            orderedSteps = orderedStepNames
                    .stream()
                    .map(StepName::new)
                    .map(stepName -> steps.get(stepName))
                    .collect(toList());
        }
        return orderedSteps;
    }

    private List<Step> orderedSteps;

    private Map<String, List<String>> buildGraphMap() {
        Map<String, List<String>> graph = new HashMap<>();
        for(Step step : steps.values()) {
            if(!graph.containsKey(step.componentDoc().name())) {
                graph.put(step.componentDoc().name(), new ArrayList<>());
            }
            for(ToStep to : step.tos()) {
                List<String> froms = graph.get(to.name().stringValue());
                if(froms == null) {
                    froms = new ArrayList<>();
                    graph.put(to.name().stringValue(), froms);
                }
                froms.add(step.componentDoc().name());
            }
        }
        return graph;
    }

    private List<String> topologicalOrdering(Map<String, List<String>> graph) {
        List<String> orderedNodes = new ArrayList<>();
        Map<String, List<String>> partialGraph = new HashMap<>(graph);
        while(partialGraph.size() > 0) {
            String nodeWithoutFrom = findNodeWithoutFrom(partialGraph);
            orderedNodes.add(nodeWithoutFrom);
            removeNode(partialGraph, nodeWithoutFrom);
        }
        return orderedNodes;
    }

    private String findNodeWithoutFrom(Map<String, List<String>> graph) {
        for(Entry<String, List<String>> e : graph.entrySet()) {
            if(e.getValue().isEmpty()) {
                return e.getKey();
            }
        }
        return graph.entrySet().iterator().next().getKey();
    }

    private void removeNode(Map<String, List<String>> graph, String nodeName) {
        Iterator<Entry<String, List<String>>> iterator = graph.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String, List<String>> entry = iterator.next();
            if(entry.getKey().equals(nodeName)) {
                iterator.remove();
            } else {
                entry.getValue().remove(nodeName);
            }
        }
    }

    public Step getStep(StepName name) {
        return steps.get(name);
    }
}
