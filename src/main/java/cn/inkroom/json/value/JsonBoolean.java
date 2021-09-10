package cn.inkroom.json.value;

import cn.inkroom.json.JsonElement;

/**
 * 封装一下，用来处理可能存在的单个value
 */
public class JsonBoolean implements JsonElement {

    private boolean value;

    public JsonBoolean() {
    }

    public JsonBoolean(Boolean value) {
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
