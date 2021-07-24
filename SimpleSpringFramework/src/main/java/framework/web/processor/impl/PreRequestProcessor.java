package framework.web.processor.impl;

import framework.web.RequestProcessorChain;
import framework.web.processor.RequestProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Processing request: {} {}",
                requestProcessorChain.getRequestMethod(),
                requestProcessorChain.getRequestPath()
        );
        // Set encoding
        requestProcessorChain.getRequest().setCharacterEncoding("UTF-8");
        // Format path
        String path = requestProcessorChain.getRequestPath();
        if (path.length() > 1 && path.endsWith("/")) {
            requestProcessorChain.setRequestPath(path.substring(path.length() - 1));
        }
        return true;
    }
}
