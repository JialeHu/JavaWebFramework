package test.packages.service;

import framework.core.annotation.Autowired;
import framework.core.annotation.Service;
import test.packages.component.Component2;
import test.packages.repository.Repository2;

@Service
public class Service2 {
    @Autowired
    public Component2 component2;
    @Autowired
    public Repository2 repository2;

    public Object returnNull() {
        throw new RuntimeException("Some Runtime Exception");
    }
}
