package poussecafe.consequence;

public class DefaultSourceSelector implements SourceSelector {

    @Override
    public Source selectSource(Consequence consequence) {
        if (consequence instanceof Command) {
            return Source.DEFAULT_COMMAND_SOURCE;
        } else {
            return Source.DEFAULT_DOMAIN_EVENT_SOURCE;
        }
    }

}
