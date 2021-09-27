/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.core;

import cn.inkroom.json.core.value.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonFactory {


    public static JsonElement createValue(Object value) {
        if (value == null) {
            return new JsonNull();
        }
        if (value instanceof String) {
            return new JsonString(value.toString());
        }
        if (value instanceof Integer) {
            return new JsonInt((Integer) value);
        }
        if (value instanceof Double) {
            return new JsonDouble(((Double) value));
        }
        if (value instanceof Boolean) {
            return new JsonBoolean(((Boolean) value));
        }
        if (value instanceof BigDecimal) {
            return new JsonBigDecimal(((BigDecimal) value));
        }
        if (value instanceof BigInteger) {
            return new JsonBigInteger(((BigInteger) value));
        }
        if (value instanceof Long) {
            return new JsonLong(((Long) value));
        }
        return null;
    }

    public static JsonElement createObject() {
        return new JsonObject();
    }

    public static JsonElement createArray() {
        return new JsonArray();

    }

}
