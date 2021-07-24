package framework.web.processor;

import framework.web.RequestProcessorChain;

public interface RequestProcessor {
    /**
     * Process request
     * @param requestProcessorChain chain of request processor
     * @return false to stop further processing
     * @throws Throwable
     */
    boolean process(RequestProcessorChain requestProcessorChain) throws Throwable;
}
