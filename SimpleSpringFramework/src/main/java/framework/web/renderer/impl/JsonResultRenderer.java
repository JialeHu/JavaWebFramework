package framework.web.renderer.impl;

import com.google.gson.Gson;
import framework.web.RequestProcessorChain;
import framework.web.renderer.ResultRenderer;
import framework.web.constant.MediaType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

@Slf4j
@AllArgsConstructor
public class JsonResultRenderer implements ResultRenderer {
    private final Object data;

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        log.debug("Rendering " + data);
        requestProcessorChain.getResponse().setContentType(MediaType.APPLICATION_JSON.toString());
        requestProcessorChain.getResponse().setCharacterEncoding("UTF-8");

        try (PrintWriter writer = requestProcessorChain.getResponse().getWriter()){
            Gson gson = new Gson();
            writer.write(gson.toJson(data));
            writer.flush();
        }
    }
}
