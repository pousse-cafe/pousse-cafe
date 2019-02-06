package poussecafe.doc.model.relation;

import java.io.Serializable;
import poussecafe.attribute.Attribute;

@SuppressWarnings("serial")
public class RelationData implements Relation.Attributes, Serializable {

    @Override
    public Attribute<RelationKey> key() {
        return new Attribute<RelationKey>() {
            @Override
            public RelationKey value() {
                return new RelationKey(fromClass, toClass);
            }

            @Override
            public void value(RelationKey value) {
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
