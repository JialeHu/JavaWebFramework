package framework.web.serializer;

import framework.web.constant.MediaType;

public interface BodySerializer<T> {
    String serialize(T object);
    T deserialize(String body);
    MediaType getContentType();
}
