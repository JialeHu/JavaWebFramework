package framework.web.renderer.impl;

import framework.web.RequestProcessorChain;
import framework.web.constant.StatusCode;
import framework.web.renderer.ResultRenderer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class InternalErrorResultRenderer implements ResultRenderer {
    private final String errorMsg;
    private final StatusCode statusCode;

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Rendering {} {}", statusCode.getCode(), errorMsg);
        requestProcessorChain.getResponse().sendError(statusCode.getCode(), errorMsg);
    }
}
