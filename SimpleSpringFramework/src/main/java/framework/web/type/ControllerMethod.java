package framework.web.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerMethod {
    private Class<?> controllerClass;
    private Method controllerMethod;
}

