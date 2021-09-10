package cn.inkroom.json;

import java.util.ArrayList;

public class JsonArray extends ArrayList<JsonElement> implements JsonElement {
    @Override
    public JsonArray getValue() {
        return this;
    }


    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("[");

        forEach((v) -> builder.append(v.toString()).append(","));
        if (builder.length() > 1) {//去除最后的逗号
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append("]");
        return builder.toString();
    }
}
