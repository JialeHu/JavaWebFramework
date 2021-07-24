package app.demo.controller;

import app.demo.serializer.DataSerializer;
import app.demo.service.MainService;
import framework.core.annotation.Autowired;
import framework.web.annotation.Controller;
import framework.web.annotation.*;
import framework.web.constant.RequestMethod;
import framework.web.constant.StatusCode;
import framework.web.exception.APIException;
import framework.web.exception.WebException;
import lombok.extern.slf4j.Slf4j;

/**
 * Demo controller class for handling requests.
 */
@Slf4j
@Controller
public class MainController {

    /**
     * mainService will be auto injected based on its type (or annotation input string).
     */
    @Autowired
    MainService mainService;

    /**
     * Mapped to GET /, return jsp page name (JSP in templates dir).
     */
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getIndex() {
        return "index.jsp";
    }

    /**
     * Mapped to GET /hello, accept input of request parameters and request body, return body.
     */
    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String getHelloWorld(@RequestParam(name = "a") String[] a,
                                @RequestParam(name = "b", required = true) String b,
                                @RequestParam(name = "c", required = true) String c,
                                @RequestBody String body) {
        log.debug("getHelloWorld({}, {}, {}, {})", a, b, c, body);
        return mainService.handleHelloWorld();
    }

    /**
     * Mapped to POST /data, accept input of body, deserialized by user-defined body serializer,
     * return body, serialized by the same user-defined body serializer.
     */
    @RequestMapping(path = "/data", method = RequestMethod.POST)
    @BodySerializerClass(value = DataSerializer.class)
    public Object getData(@RequestBody int[] body) {
        return body;
    }

    /**
     * Mapped to GET /web-exc, throw WebException, which will be caught and return error page.
     */
    @RequestMapping(path = "/web-exc", method = RequestMethod.GET)
    public void getWebException() {
        throw new WebException("Some Error Message", StatusCode.HTTP_500_INTERNAL_SERVER_ERROR);
    }

    /**
     * Mapped to GET /api-exc, throw APIException, which will be caught and return error body.
     */
    @RequestMapping(path = "/api-exc", method = RequestMethod.GET)
    public void getAPIException() {
        throw new APIException("Some Error Message", StatusCode.HTTP_500_INTERNAL_SERVER_ERROR, "Some Error Body");
    }
}
