package app.demo.service;

import framework.core.annotation.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * Demo service class for handling request logic, to be auto injected into controller.
 */
@Slf4j
@Service
public class MainService {

    /**
     * Define logic for an endpoint, sleep to simulate processing.
     */
    public String handleHelloWorld() {
        log.info("Handling HelloWorld");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello World!";
    }
}
