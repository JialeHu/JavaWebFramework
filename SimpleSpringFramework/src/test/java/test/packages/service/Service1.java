package test.packages.service;

import framework.core.annotation.Autowired;
import framework.core.annotation.Service;
import test.packages.component.Component1;
import test.packages.repository.Repository1;

@Service
public class Service1 {
    @Autowired
    public Component1 component1;
    @Autowired
    public Repository1 repository1;

    public Object returnNull() {
        return null;
    }

    public Object returnObject() {
        return new Object();
    }
}
