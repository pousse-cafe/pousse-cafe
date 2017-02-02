package poussecafe.sample.domain;

import poussecafe.domain.Repository;
import poussecafe.storable.UnitOfConsequence;

public class MessageRepository extends Repository<Message, MessageKey, Message.Data> {

    @Override
    protected Message newAggregate() {
        return new Message();
    }

    @Override
    protected void considerUnitEmissionAfterAdd(Message message,
            UnitOfConsequence unitOfConsequence) {
        unitOfConsequence.addConsequence(new MessageCreated(message.getKey()));
        super.considerUnitEmissionAfterAdd(message, unitOfConsequence);
    }

}
