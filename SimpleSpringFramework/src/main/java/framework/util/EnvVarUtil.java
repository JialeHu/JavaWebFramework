package framework.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnvVarUtil {

    public static Map<String, String> getEnvVars(List<String> names) {
        Map<String, String> varMap = new LinkedHashMap<>();
        for (String name : names) {
            varMap.put(name, System.getenv(name));
        }
        return varMap;
    }

}
