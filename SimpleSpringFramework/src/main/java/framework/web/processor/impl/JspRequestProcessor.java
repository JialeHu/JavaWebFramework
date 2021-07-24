package framework.web.processor.impl;

import framework.web.RequestProcessorChain;
import framework.web.processor.RequestProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import static framework.web.WebApplication.JSP_RESOURCE_PREFIX;
import static framework.web.WebApplication.JSP_SERVLET;

@Slf4j
public class JspRequestProcessor implements RequestProcessor {
    private final RequestDispatcher jspDispatcher;

    public JspRequestProcessor(ServletContext servletContext) {
        jspDispatcher = servletContext.getNamedDispatcher(JSP_SERVLET);
        if (jspDispatcher == null) {
            throw new RuntimeException("Servlet with name: " + JSP_SERVLET + " Not Found");
        }
        log.info("Using servlet \"{}\" for JSP at {} ...", JSP_SERVLET, JSP_RESOURCE_PREFIX);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Processing request: {} {}",
                requestProcessorChain.getRequestMethod(),
                requestProcessorChain.getRequestPath()
        );
        if (requestProcessorChain.getRequestPath().startsWith(JSP_RESOURCE_PREFIX)) {
            jspDispatcher.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            return false;
        }
        return true;
    }
}
