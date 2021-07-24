package framework.web.renderer.impl;

import framework.web.RequestProcessorChain;
import framework.web.renderer.ResultRenderer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultResultRenderer implements ResultRenderer {
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Rendering");
        requestProcessorChain.getResponse().setStatus(requestProcessorChain.getResponseCode());
    }
}
