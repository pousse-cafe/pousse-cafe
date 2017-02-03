package poussecafe.data.memory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class InMemoryJsonCodec<D> {

    private Class<D> dataType;

    private Gson gson;

    public InMemoryJsonCodec(Class<D> dataType) {
        gson = new GsonBuilder()
                .registerTypeAdapter(dataType,
                        (InstanceCreator<?>) type -> InMemoryDataUtils.newDataImplementation(dataType))
                .registerTypeAdapter(dataType, new InMemoryDataJsonSerializer<>())
                .registerTypeAdapter(dataType, new InMemoryDataJsonDeserializer<>())
                .create();
    }

    public String serializa(D data) {
        return gson.toJson(data, dataType);
    }

    public D deserialize(String json) {
        return gson.fromJson(json, dataType);
    }
}
