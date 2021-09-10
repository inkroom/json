package cn.inkroom.json.value;

import cn.inkroom.json.JsonElement;

/**
 * 浮点数
 */
public class JsonDouble implements JsonElement {

    private double value;

    public JsonDouble() {
    }

    public JsonDouble(double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
