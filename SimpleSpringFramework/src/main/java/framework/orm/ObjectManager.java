package framework.orm;

import java.util.Set;

public interface ObjectManager<M> {

    public M get();

    public Set<M> query();

}
