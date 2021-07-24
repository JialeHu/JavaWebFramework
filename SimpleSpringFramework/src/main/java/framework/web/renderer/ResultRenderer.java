package framework.web.renderer;

import framework.web.RequestProcessorChain;

public interface ResultRenderer {
    void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
