package poussecafe.doc.commands;

import poussecafe.attribute.Attribute;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.runtime.Command;

public interface CreateAggregateDoc extends Command {

    Attribute<BoundedContextDocKey> boundedContextKey();

    Attribute<String> className();
}
