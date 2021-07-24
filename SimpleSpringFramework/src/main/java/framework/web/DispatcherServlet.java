package framework.web;

import framework.web.processor.RequestProcessor;
import framework.web.processor.impl.ControllerRequestProcessor;
import framework.web.processor.impl.JspRequestProcessor;
import framework.web.processor.impl.PreRequestProcessor;
import framework.web.processor.impl.StaticResourceRequestProcessor;
import lombok.extern.slf4j.Slf4j;

//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
//@WebServlet(name = "dispatcherServlet", urlPatterns = {"/*"})
public class DispatcherServlet extends HttpServlet {
    private final List<RequestProcessor> requestProcessors = new ArrayList<>();

    @Override
    public void init() {
        log.info("Initializing DispatcherServlet ...");
        // Set RequestProcessorChain
        requestProcessors.add(new PreRequestProcessor());
        requestProcessors.add(new StaticResourceRequestProcessor(getServletContext()));
        requestProcessors.add(new JspRequestProcessor(getServletContext()));
        requestProcessors.add(new ControllerRequestProcessor());
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Serving: {} {} {} from {}", request.getMethod(), request.getPathInfo(), request.getProtocol(), request.getRemoteAddr());

        // Start Processor Chain
        RequestProcessorChain requestProcessorChain = new RequestProcessorChain(requestProcessors.iterator(), request, response);
        // Process Request
        requestProcessorChain.doRequestProcessorChain();
        // Render Response
        requestProcessorChain.doRender();

        log.info("{} {} {} -- {} -- {}",
                request.getMethod(),
                request.getPathInfo(),
                request.getProtocol(),
                response.getStatus(),
                request.getRemoteAddr()
        );
    }

    @Override
    public void destroy() {
        log.info("Destroying DispatcherServlet");
    }
}
