package <adaptersPackage>;

<if(storage_internal)>import java.io.Serializable;<endif>
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
<if(storage_spring_mongo)>import org.springframework.data.annotation.Id;<endif>
<if(storage_spring_mongo)>import org.springframework.data.annotation.Version;<endif>

import <modelPackage>.<name>;
import <modelPackage>.<name>Key;

<if(storage_internal)>@SuppressWarnings("serial")<endif>
public class <name>Attributes implements <name>.Attributes<if(storage_internal)>, Serializable<endif> {

    @Override
    public Attribute\<<name>Key> key() {
        return AttributeBuilder.stringKey(<name>Key.class)
                .get(() -> id)
                .set(value -> id = value)
                .build();
    }

    <if(storage_spring_mongo)>@Id<endif>
    private String id;

    <if(storage_in_memory && !storage_spring_mongo)>@SuppressWarnings("unused")<endif>
    <if(storage_spring_mongo)>@Version<endif>
    private Long version;

<if(demoAttribute)>
    @Override
    public Attribute\<String> name() {
        return AttributeBuilder.single(String.class)
                .get(() -> name)
                .set(value -> name = value)
                .build();
    }

    private String name;
<endif>
}
