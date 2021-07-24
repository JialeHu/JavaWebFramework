package framework.web.renderer.impl;

import framework.web.RequestProcessorChain;
import framework.web.renderer.ResultRenderer;
import framework.web.serializer.BodySerializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

/**
 * Render result by invoking body serializer
 */
@Slf4j
@AllArgsConstructor
public class BodySerializerResultRenderer implements ResultRenderer {
    private final Object data;
    private final BodySerializer<Object> bodySerializer;

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Rendering " + data);

        requestProcessorChain.getResponse().setContentType(bodySerializer.getContentType().toString());
        requestProcessorChain.getResponse().setCharacterEncoding("UTF-8");

        try (PrintWriter writer = requestProcessorChain.getResponse().getWriter()) {
            writer.write(bodySerializer.serialize(data));
            writer.flush();
        }
    }
}
