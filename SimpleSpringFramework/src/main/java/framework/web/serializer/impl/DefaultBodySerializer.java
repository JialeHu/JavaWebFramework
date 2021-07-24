package framework.web.serializer.impl;

import framework.web.serializer.BodySerializer;
import framework.web.constant.MediaType;

public class DefaultBodySerializer implements BodySerializer<Object> {
    @Override
    public String serialize(Object object) {
        return object.toString();
    }

    @Override
    public String deserialize(String body) {
        return body;
    }

    @Override
    public MediaType getContentType() {
        return MediaType.TEXT_PLAIN;
    }
}
