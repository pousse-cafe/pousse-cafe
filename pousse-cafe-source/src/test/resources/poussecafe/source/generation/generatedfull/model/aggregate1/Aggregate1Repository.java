package poussecafe.source.generation.generatedfull.model.aggregate1;

import poussecafe.domain.Repository;

public class Aggregate1Repository extends Repository<Aggregate1, Aggregate1Id, Aggregate1.Attributes> {

    @Override
    public Aggregate1DataAccess<Aggregate1.Attributes> dataAccess() {
        return (Aggregate1DataAccess<Aggregate1.Attributes>) super.dataAccess();
    }
}