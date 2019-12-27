package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    public static class Null {
        @Override
        public String toString() {
            return null;
        }
    }

    public static void main(String[] args) {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String json = "{\"number\":123,\"floating\":45.6,\"name\":\"Ann\",\"isOk\":true,\"nothing\":null,\"other\":[]}";                   //Test section
        for (Map.Entry<String, Object> entry : flatten(json).entrySet()) {
            System.out.println(entry.getKey() + "\t: " + "\t(" + entry.getValue().getClass().getName() + ") " + entry.getValue());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("name", "Ann");
        data.put("score", 123.45);
        System.out.println(toJson(data));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String toJson(Map<String, Object> data) {
        List<String> items = new ArrayList<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = "\"" + entry.getKey() + "\"";
            Object value = entry.getValue();
            if (value instanceof String) {
                value = "\"" + value + "\"";
            }
            items.add(key + ':' + value.toString());
        }
        String json = String.join(",", items);
        return "{" + json + "}";
    }

    public static Map<String, Object> flatten(String json) {

        Map<String, Object> data = new HashMap<>();

        if (json == null || json.isEmpty()) {
            return data;
        }
        if (!(json.charAt(0) == '{' && json.charAt(json.length() - 1) == '}')) {
            return data;
        }

        json = json.substring(1, json.length() - 1);
        for (String pair : json.split(",")) {
            String[] keyValue = pair.split(":");
            if (keyValue.length != 2) {
                return data;
            }
            String key = keyValue[0];
            String value = keyValue[1];

            key = key.substring(1, key.length() - 1);

            if (isNull(value)) {
                data.put(key, new Null());
                continue;
            }

            Boolean b = parseBoolean(value);
            if (b != null) {
                data.put(key, b);
                continue;
            }

            Integer i = parseInteger(value);
            if (i != null) {
                data.put(key, i);
                continue;
            }

            Double d = parseDouble(value);
            if (d != null) {
                data.put(key, d);
                continue;
            }

            String s = parseString(value);
            if (s != null) {
                data.put(key, s);
                continue;
            }

            data.put(key, value);
        }

        return data;
    }

    private static Integer parseInteger(String json) {
        try {
            return Integer.parseInt(json);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double parseDouble(String json) {
        try {
            return Double.parseDouble(json);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String parseString(String json) {
        if (json.length() < 2) {
            return null;
        }
        if (!(json.charAt(0) == '"' && json.charAt(json.length() - 1) == '"')) {
            return null;
        }
        return json.substring(1, json.length() - 1);
    }

    private static Boolean parseBoolean(String json) {
        if ("true".equals(json)) return true;
        if ("false".equals(json)) return false;
        return null;
    }

    private static boolean isNull(String json) {
        return "null".equals(json);
    }
}
