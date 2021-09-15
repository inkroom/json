/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json;

import cn.inkroom.json.value.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;

public class JsonObject extends LinkedHashMap<String, JsonElement> implements JsonElement {
    @Override
    public Type getType() {
        return Type.Object;
    }

    @Override
    public JsonObject getValue() {
        return this;
    }

    public JsonString getAsJsonString(String key) {
        return get(key).getAsJsonString();
    }

    public JsonBoolean getAsJsonBoolean(String key) {
        return get(key).getAsJsonBoolean();
    }

    public JsonArray getAsJsonArray(String key) {
        return get(key).getAsJsonArray();
    }

    public JsonInt getAsJsonInt(String key) {
        return get(key).getAsJsonInt();
    }

    public JsonLong getAsJsonLong(String key) {
        return get(key).getAsJsonLong();
    }

    public JsonDouble getAsJsonDouble(String key) {
        return get(key).getAsJsonDouble();
    }

    public JsonNull getAsJsonNull(String key) {
        return get(key).getAsJsonNull();
    }

    public JsonObject getAsJsonObject(String key) {
        return get(key).getAsJsonObject();
    }

    public JsonBigInteger getAsJsonBigInteger(String key) {
        return get(key).getAsJsonBigInteger();
    }

    public JsonBigDecimal getAsJsonBigDecimal(String key) {
        return get(key).getAsJsonBigDecimal();
    }

    /***********************/

    public String getAsString(String key) {
        return getAsJsonString(key).getValue();
    }

    public Boolean getAsBoolean(String key) {
        return getAsJsonBoolean(key).getValue();
    }


    public int getAsInt(String key) {
        return getAsJsonInt(key).getValue();
    }

    public Long getAsLong(String key) {
        return getAsJsonLong(key).getValue();
    }

    public Double getAsDouble(String key) {
        return getAsJsonDouble(key).getValue();
    }



    /**
     * 返回null
     *
     * @param key
     * @return
     */
    public boolean isNull(String key) {
        return getAsJsonNull(key).getValue().equals("null");
    }

    public BigInteger getAsBigInteger(String key) {
        return getAsJsonBigInteger(key).getValue();
    }

    public BigDecimal getAsBigDecimal(String key) {
        return getAsJsonBigDecimal(key).getValue();
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
