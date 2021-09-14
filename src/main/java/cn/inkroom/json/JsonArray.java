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

import java.util.ArrayList;

public class JsonArray extends ArrayList<JsonElement> implements JsonElement {
    @Override
    public JsonArray getValue() {
        return this;
    }

    @Override
    public Type getType() {
        return Type.Array;
    }

    public JsonString getAsJsonString(int index) {
        return get(index).getAsJsonString();
    }

    public JsonBoolean getAsJsonBoolean(int index) {
        return get(index).getAsJsonBoolean();
    }

    public JsonArray getAsJsonArray(int index) {
        return get(index).getAsJsonArray();
    }

    public JsonInt getAsJsonInt(int index) {
        return get(index).getAsJsonInt();
    }

    public JsonLong getAsJsonLong(int index) {
        return get(index).getAsJsonLong();
    }

    public JsonDouble getAsJsonDouble(int index) {
        return get(index).getAsJsonDouble();
    }

    public JsonNull getAsJsonNull(int index) {
        return get(index).getAsJsonNull();
    }

    public JsonObject getAsJsonObject(int index) {
        return get(index).getAsJsonObject();
    }

    public JsonBigInteger getAsJsonBigInteger(int index) {
        return get(index).getAsJsonBigInteger();
    }

    public JsonBigDecimal getAsJsonBigDecimal(int index) {
        return get(index).getAsJsonBigDecimal();
    }

    /***********************/

    public String getAsString(int index) {
        return getAsJsonString(index).getValue();
    }

    public Boolean getAsBoolean(int index) {
        return getAsJsonBoolean(index).getValue();
    }

    public int getAsInt(int index) {
        return getAsJsonInt(index).getValue();
    }

    public Long getAsLong(int index) {
        return getAsJsonLong(index).getValue();
    }

    public Double getAsDouble(int index) {
        return getAsJsonDouble(index).getValue();
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
