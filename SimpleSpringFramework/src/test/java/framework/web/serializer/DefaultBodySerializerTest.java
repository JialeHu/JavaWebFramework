package framework.web.serializer;

import framework.web.constant.MediaType;
import framework.web.serializer.impl.DefaultBodySerializer;
import org.junit.jupiter.api.*;

public class DefaultBodySerializerTest {

    public static class SomeObject {
        @Override
        public String toString() {
            return "ObjectString";
        }
    }

    private SomeObject obj = new SomeObject();
    private DefaultBodySerializer serializer = new DefaultBodySerializer();

    @Test
    public void serializeTest() {
        Assertions.assertEquals("ObjectString", serializer.serialize(obj));
        Assertions.assertEquals(MediaType.TEXT_PLAIN, serializer.getContentType());
    }

    @Test
    public void deserializeTest() {
        Assertions.assertEquals("InputString", serializer.deserialize("InputString"));
    }
}
