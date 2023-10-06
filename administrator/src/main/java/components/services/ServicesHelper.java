package components.services;

import java.util.Map;

public class ServicesHelper {

    protected String[] getParams(Map<String, String> param) {
        return param.entrySet()
                .stream()
                .findFirst()
                .map(entry -> new String[]{entry.getKey(), entry.getValue()})
                .orElse(new String[2]);
    }
}
