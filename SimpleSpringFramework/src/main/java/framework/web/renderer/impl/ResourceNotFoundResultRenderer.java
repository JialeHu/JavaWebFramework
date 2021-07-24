package framework.web.renderer.impl;

import framework.web.RequestProcessorChain;
import framework.web.constant.StatusCode;
import framework.web.renderer.ResultRenderer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceNotFoundResultRenderer implements ResultRenderer {
    private final String httpMethod;
    private final String httpPath;

    public ResourceNotFoundResultRenderer(String method, String path) {
        this.httpMethod = method;
        this.httpPath = path;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Rendering");
        requestProcessorChain.getResponse().sendError(StatusCode.HTTP_404_NOT_FOUND.getCode(),
                "Request: " + httpMethod + " " + httpPath + " Not Found"
        );
    }
}
