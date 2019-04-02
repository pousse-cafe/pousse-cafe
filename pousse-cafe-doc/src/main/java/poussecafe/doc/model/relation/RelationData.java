package poussecafe.doc.model.relation;

import java.io.Serializable;
import poussecafe.attribute.Attribute;

@SuppressWarnings("serial")
public class RelationData implements Relation.Attributes, Serializable {

    @Override
    public Attribute<RelationId> identifier() {
        return new Attribute<RelationId>() {
            @Override
            public RelationId value() {
                return new RelationId(fromClass, toClass);
            }

            @Override
            public void value(RelationId value) {
                fromClass = value.fromClass();
                toClass = value.toClass();
            }
        };
    }

    private String fromClass;

    private String toClass;

    @Override
    public Attribute<ComponentType> fromType() {
        return new Attribute<ComponentType>() {
            @Override
            public ComponentType value() {
                return fromType;
            }

            @Override
            public void value(ComponentType value) {
                fromType = value;
            };
        };
    }

    private ComponentType fromType;

    @Override
    public Attribute<ComponentType> toType() {
        return new Attribute<ComponentType>() {
            @Override
            public ComponentType value() {
                return toType;
            }

            @Override
            public void value(ComponentType value) {
                toType = value;
            };
        };
    }

    private ComponentType toType;
}
