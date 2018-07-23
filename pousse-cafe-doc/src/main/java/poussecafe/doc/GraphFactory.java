package poussecafe.doc;

import poussecafe.doc.graph.UndirectedGraph;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.relation.RelationRepository;
import poussecafe.doc.model.vodoc.ValueObjectDocRepository;
import poussecafe.domain.Service;

public class GraphFactory implements Service {

    public UndirectedGraph buildBoundedContextGraph(
            BoundedContextDoc boundedContextDoc) {
        return new BoundedContextGraphFactory.Builder()
                .boundedContextDoc(boundedContextDoc)
                .aggregateDocRepository(aggregateDocRepository)
                .relationRepository(relationRepository)
                .build()
                .buildGraph();
    }

    private AggregateDocRepository aggregateDocRepository;

    private RelationRepository relationRepository;

    public UndirectedGraph buildAggregateGraph(AggregateDoc aggregateDoc) {
        return new AggregateGraphFactory.Builder()
                .aggregateDoc(aggregateDoc)
                .relationRepository(relationRepository)
                .aggregateDocRepository(aggregateDocRepository)
                .entityDocRepository(entityDocRepository)
                .valueObjectDocRepository(valueObjectDocRepository)
                .build()
                .buildGraph();
    }

    private EntityDocRepository entityDocRepository;

    private ValueObjectDocRepository valueObjectDocRepository;
}
