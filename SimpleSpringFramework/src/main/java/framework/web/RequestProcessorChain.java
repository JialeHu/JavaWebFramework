package framework.web;

import framework.web.constant.StatusCode;
import framework.web.exception.APIException;
import framework.web.exception.WebException;
import framework.web.processor.RequestProcessor;
import framework.web.renderer.ResultRenderer;
import framework.web.renderer.impl.DefaultResultRenderer;
import framework.web.renderer.impl.InternalErrorResultRenderer;
import framework.web.renderer.impl.RawResultRenderer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

@Data
@Slf4j
public class RequestProcessorChain {
    private Iterator<RequestProcessor> requestProcessorIterator;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String requestMethod;
    private String requestPath;
    private int responseCode;
    private ResultRenderer resultRenderer;

    public RequestProcessorChain(Iterator<RequestProcessor> iterator, HttpServletRequest request, HttpServletResponse response) {
        this.requestProcessorIterator = iterator;
        this.request = request;
        this.response = response;
        this.requestMethod = request.getMethod();
        this.requestPath = request.getPathInfo();
        this.responseCode = StatusCode.HTTP_200_OK.getCode();
    }

    public void doRequestProcessorChain() {
        // Run through all processors
        try {
            // Process until false
            while (requestProcessorIterator.hasNext()) {
                if (!requestProcessorIterator.next().process(this)) break;
            }
        } catch (APIException e) {
            // Handle internal error API
            this.resultRenderer = new RawResultRenderer(e.getBody(), e.getStatusCode(), e.getMediaType());
            log.error("Error running request processor chain: {}", e.toString());
        } catch (WebException e) {
            // Handle internal error VIEW
            this.resultRenderer = new InternalErrorResultRenderer(e.getMessage(), e.getStatusCode());
            log.error("Error running request processor chain: {}", e.toString());
        } catch (Throwable e) {
            // Unhandled error
            throw new RuntimeException(e);
        }
    }

    public void doRender() {
        // Set default renderer
        if (resultRenderer == null) {
            resultRenderer = new DefaultResultRenderer();
        }
        // Render
        try {
            resultRenderer.render(this);
        } catch (Exception e) {
            log.error("Error rendering: ", e);
            throw new RuntimeException(e);
        }
    }
}
