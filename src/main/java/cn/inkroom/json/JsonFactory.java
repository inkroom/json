package cn.inkroom.json;

import cn.inkroom.json.value.*;

class JsonFactory {


    public static JsonElement createValue(Object value) {
        if (value == null) {
            return new JsonNull();
        }
        if (value instanceof String) {
            return new JsonString(value.toString());
        }
        if (value instanceof Integer) {
            return new JsonInt((Integer) value);
        }
        if (value instanceof Double) {
            return new JsonDouble(((Double) value));
        }
        if (value instanceof Boolean) {
            return new JsonBoolean(((Boolean) value));
        }

        return null;
    }

    public static JsonElement createObject() {
        return new JsonObject();
    }

    public static JsonElement createArray() {
        return new JsonArray();

    }

}
