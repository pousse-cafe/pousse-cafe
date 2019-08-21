package poussecafe.doc.commands;

import poussecafe.attribute.Attribute;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.runtime.Command;

public interface CreateAggregateDoc extends Command {

    Attribute<ModuleDocId> moduleId();

    Attribute<String> className();
}
