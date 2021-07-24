package test.packages.controller;

import framework.core.annotation.Autowired;
import framework.web.annotation.Controller;
import test.packages.service.Service2;

@Controller
public class Controller2 {
    @Autowired
    public Service2 service2;
}
