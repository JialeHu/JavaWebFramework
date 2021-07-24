package framework.web.serializer.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import framework.web.serializer.BodySerializer;
import framework.web.constant.MediaType;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Default JsonBodySerializer converting body to a Map<String, Object>
 */
public class JsonBodySerializer implements BodySerializer<Object> {

    private final Gson gson = new Gson();

    @Override
    public String serialize(Object object) {
        return gson.toJson(object);
    }

    @Override
    public Map<String, Object> deserialize(String body) {
        Type defaultMapType = new TypeToken<Map<String, Object>>() {}.getType();
        return gson.fromJson(body, defaultMapType);
    }

    @Override
    public MediaType getContentType() {
        return MediaType.APPLICATION_JSON;
    }
}
