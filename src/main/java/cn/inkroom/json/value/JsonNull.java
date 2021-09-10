package cn.inkroom.json.value;

import cn.inkroom.json.JsonElement;

public class JsonNull implements JsonElement {


    public Object getValue() {
        return "null";
    }

    @Override
    public String toString() {
        return "null";
    }
}
