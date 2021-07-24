package app.demo.serializer;

import framework.core.annotation.Component;
import framework.web.constant.MediaType;
import framework.web.serializer.BodySerializer;

import java.util.Arrays;

/**
 * Demo body serializer for an endpoint
 */
@Component
public class DataSerializer implements BodySerializer<int[]> {

    /**
     * Serialize Java object to String as response.
     */
    @Override
    public String serialize(int[] object) {
        return Arrays.toString(object);
    }

    /**
     * Deserialize request String to Java object.
     */
    @Override
    public int[] deserialize(String body) {
        return new int[]{1, 2, 3, 4, 5};
    }

    /**
     * Define content type of response.
     */
    @Override
    public MediaType getContentType() {
        return MediaType.APPLICATION_JSON;
    }
}
