package framework.web.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    @Getter
    private String view;
    @Getter
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView setView(String view) {
        this.view = view;
        return this;
    }

    public ModelAndView addViewData(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    @Override
    public String toString() {
        return "ModelAndView-" + view;
    }
}
