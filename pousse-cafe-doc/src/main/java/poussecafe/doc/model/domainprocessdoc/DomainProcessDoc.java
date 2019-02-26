package poussecafe.doc.model.domainprocessdoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.MapAttribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.EntityAttributes;

import static java.util.stream.Collectors.toList;

@Aggregate(
  factory = DomainProcessDocFactory.class,
  repository = DomainProcessDocRepository.class
)
public class DomainProcessDoc extends AggregateRoot<DomainProcessDocKey, DomainProcessDoc.Attributes> {

    void boundedContextComponentDoc(BoundedContextComponentDoc componentDoc) {
        attributes().boundedContextComponentDoc().value(componentDoc);
    }

    void steps(List<Step> steps) {
        Map<String, Step> map = new HashMap<>();
        for(Step step : steps) {
            if(map.put(step.componentDoc().name(), step) != null) {
                throw new DomainException("Steps must have unique names");
            }
        }
        attributes().steps().value(map);
    }

    public Map<String, Step> steps() {
        return attributes().steps().value();
    }

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    public List<Step> orderedSteps() {
        Map<String, List<String>> graph = buildGraph();
        List<String> orderedStepNames = topologicalOrdering(graph);
        return orderedStepNames
                .stream()
                .map(stepName -> attributes().steps().get(stepName).orElseThrow(DomainException::new))
                .collect(toList());
    }

    private Map<String, List<String>> buildGraph() {
        Map<String, List<String>> graph = new HashMap<>();
        for(Step step : attributes().steps().values()) {
            if(!graph.containsKey(step.componentDoc().name())) {
                graph.put(step.componentDoc().name(), new ArrayList<>());
            }
            for(ToStep to : step.tos()) {
                List<String> froms = graph.get(to.name());
                if(froms == null) {
                    froms = new ArrayList<>();
                    graph.put(to.name(), froms);
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

    public static interface Attributes extends EntityAttributes<DomainProcessDocKey> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        MapAttribute<String, Step> steps();
    }
}
