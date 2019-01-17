package <adaptersPackage>;

<if(storage_internal)>import java.io.Serializable;<endif>
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
<if(storage_spring_mongo)>import org.springframework.data.annotation.Id;<endif>
<if(storage_spring_mongo)>import org.springframework.data.annotation.Version;<endif>

import <modelPackage>.<name>;
import <modelPackage>.<name>Key;

<if(storage_internal)>@SuppressWarnings("serial")<endif>
public class <name>Data implements <name>.Data<if(storage_internal)>, Serializable<endif> {

    @Override
    public Property\<<name>Key> key() {
        return PropertyBuilder.simple(<name>Key.class)
                .from(String.class)
                .adapt(<name>Key::new)
                .get(() -> id)
                .adapt(<name>Key::getValue)
                .set(value -> id = value)
                .build();
    }

    <if(storage_spring_mongo)>@Id<endif>
    private String id;

    <if(storage_in_memory && !storage_spring_mongo)>@SuppressWarnings("unused")<endif>
    <if(storage_spring_mongo)>@Version<endif>
    private Long version;

    @Override
    public Property\<String> name() {
        return PropertyBuilder.simple(String.class)
                .get(() -> name)
                .set(value -> name = value)
                .build();
    }

    private String name;
}
