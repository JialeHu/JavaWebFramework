package framework.web.renderer.impl;

import framework.web.RequestProcessorChain;
import framework.web.constant.MediaType;
import framework.web.constant.StatusCode;
import framework.web.renderer.ResultRenderer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

@Slf4j
@AllArgsConstructor
public class RawResultRenderer implements ResultRenderer {

    private final String data;
    private final StatusCode statusCode;
    private final MediaType mediaType;

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Rendering " + data);
        requestProcessorChain.getResponse().setStatus(statusCode.getCode());
        requestProcessorChain.getResponse().setContentType(mediaType.toString());
        requestProcessorChain.getResponse().setCharacterEncoding("UTF-8");

        try (PrintWriter writer = requestProcessorChain.getResponse().getWriter()) {
            writer.write(data);
            writer.flush();
        }
    }
}
