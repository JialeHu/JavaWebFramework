package framework.web.renderer.impl;

import framework.web.RequestProcessorChain;
import framework.web.renderer.ResultRenderer;
import framework.web.type.ModelAndView;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class ViewResultRenderer implements ResultRenderer {
    public static final String VIEW_PATH = "/templates/";
    private final ModelAndView modelAndView;

    public ViewResultRenderer(Object modelAndView) {
        if (modelAndView instanceof ModelAndView){
            this.modelAndView = (ModelAndView) modelAndView;
        } else if (modelAndView instanceof String){
            this.modelAndView = new ModelAndView().setView((String) modelAndView);
        } else {
            throw new RuntimeException("Unsupported result type: " + modelAndView.getClass().getName());
        }
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Rendering " + modelAndView);
        HttpServletRequest request = requestProcessorChain.getRequest();
        HttpServletResponse response = requestProcessorChain.getResponse();
        String path = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        //JSP
        request.getRequestDispatcher(VIEW_PATH + path).forward(request, response);
    }
}
