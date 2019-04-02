package poussecafe.doc.commands;

import poussecafe.attribute.Attribute;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.runtime.Command;

public interface CreateAggregateDoc extends Command {

    Attribute<BoundedContextDocId> boundedContextId();

    Attribute<String> className();
}
