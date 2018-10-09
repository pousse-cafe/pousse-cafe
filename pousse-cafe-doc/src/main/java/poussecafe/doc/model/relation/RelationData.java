package poussecafe.doc.model.relation;

import java.io.Serializable;
import poussecafe.property.Property;

@SuppressWarnings("serial")
public class RelationData implements Relation.Data, Serializable {

    @Override
    public Property<RelationKey> key() {
        return new Property<RelationKey>() {
            @Override
            public RelationKey get() {
                return new RelationKey(fromClass, toClass);
            }

            @Override
            public void set(RelationKey value) {
                fromClass = value.fromClass();
                toClass = value.toClass();
            }
        };
    }

    private String fromClass;

    private String toClass;

    @Override
    public Property<ComponentType> fromType() {
        return new Property<ComponentType>() {
            @Override
            public ComponentType get() {
                return fromType;
            }

            @Override
            public void set(ComponentType value) {
                fromType = value;
            };
        };
    }

    private ComponentType fromType;

    @Override
    public Property<ComponentType> toType() {
        return new Property<ComponentType>() {
            @Override
            public ComponentType get() {
                return toType;
            }

            @Override
            public void set(ComponentType value) {
                toType = value;
            };
        };
    }

    private ComponentType toType;
}
