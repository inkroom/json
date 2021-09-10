package cn.inkroom.json;

import cn.inkroom.json.value.*;

public interface JsonElement {


    Object getValue();

    default JsonString getAsJsonString() {
        return ((JsonString) this);
    }

    default JsonBoolean getAsJsonBoolean() {
        return ((JsonBoolean) this);
    }

    default JsonArray getAsJsonArray() {
        return ((JsonArray) this);
    }

    default JsonInt getAsJsonInt() {
        return ((JsonInt) this);
    }

    default JsonDouble getAsJsonDouble() {
        return ((JsonDouble) this);
    }

    default JsonNull getAsJsonNull() {
        return ((JsonNull) this);
    }

    default JsonObject getAsJsonObject() {
        return ((JsonObject) this);
    }
}
