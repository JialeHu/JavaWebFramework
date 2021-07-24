package test.packages.controller;

import framework.core.annotation.Autowired;
import framework.web.annotation.Controller;
import test.packages.service.Service1;

@Controller
public class Controller1 {
    @Autowired
    public Service1 service1;
}
