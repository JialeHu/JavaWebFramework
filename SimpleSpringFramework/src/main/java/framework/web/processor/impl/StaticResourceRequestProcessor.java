package framework.web.processor.impl;

import framework.web.RequestProcessorChain;
import framework.web.processor.RequestProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import static framework.web.WebApplication.DEFAULT_SERVLET;
import static framework.web.WebApplication.STATIC_RESOURCE_PREFIX;

@Slf4j
public class StaticResourceRequestProcessor implements RequestProcessor {
    private final RequestDispatcher defaultDispatcher;

    public StaticResourceRequestProcessor(ServletContext servletContext) {
        defaultDispatcher = servletContext.getNamedDispatcher(DEFAULT_SERVLET);
        if (defaultDispatcher == null) {
            throw new RuntimeException("Servlet with name: " + DEFAULT_SERVLET + " Not Found");
        }
        log.info("Using servlet \"{}\" for static resource at {} ...", DEFAULT_SERVLET, STATIC_RESOURCE_PREFIX);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Processing request: {} {}",
                requestProcessorChain.getRequestMethod(),
                requestProcessorChain.getRequestPath()
        );
        if (requestProcessorChain.getRequestPath().startsWith(STATIC_RESOURCE_PREFIX)) {
            defaultDispatcher.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            return false;
        }
        return true;
    }
}
