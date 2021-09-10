package cn.inkroom.json.value;

import cn.inkroom.json.JsonElement;

public class JsonString implements JsonElement {

    private String value;

    public JsonString() {
    }

    public JsonString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + getValue() + "\"";
    }
}
