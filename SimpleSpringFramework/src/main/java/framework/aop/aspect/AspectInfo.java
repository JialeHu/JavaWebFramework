package framework.aop.aspect;

import framework.aop.PointcutLocator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AspectInfo {
    private int aspectOrder;
    private AbstractAspect aspectInstance;
    private PointcutLocator pointcutLocator;

    @Override
    public String toString() {
        return "(" + aspectInstance.getClass().getTypeName() +
                " for " + pointcutLocator + " at order " + aspectOrder + ")";
    }
}
