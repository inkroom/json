package cn.inkroom.json.value;

import cn.inkroom.json.JsonElement;

/**
 * 整数
 */
public class JsonInt implements JsonElement {

    private int value;

    public JsonInt() {
    }

    public JsonInt(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
