package cn.inkroom.json;

import java.util.LinkedHashMap;

public class JsonObject extends LinkedHashMap<String, JsonElement> implements JsonElement {
    @Override
    public JsonObject getValue() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");

        forEach((s, o) -> builder.append(s).append(":").append(o.toString()).append(","));

        if (builder.length() > 1) {//去除最后的逗号
            builder.deleteCharAt(builder.length() - 1);
        }


        builder.append("}");

        return builder.toString();
    }
}
