package framework.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

public class PointcutLocator {

    private final PointcutParser pointcutParser =
            PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
                    PointcutParser.getAllSupportedPointcutPrimitives()
            );

    private final PointcutExpression pointcutExpression;

    public PointcutLocator(String expression) {
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * Check if a class could be a match
     * @param targetClass class to be checked
     * @return if possibly matched
     */
    public boolean couldMatch(Class<?> targetClass) {
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     * Check if a method matches the Pointcut expression
     * @param targetMethod method to be checked
     * @return if matched
     */
    public boolean isMatch(Method targetMethod) {
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(targetMethod);
        return shadowMatch.alwaysMatches();
    }

    @Override
    public String toString() {
        return pointcutExpression.getPointcutExpression();
    }
}
