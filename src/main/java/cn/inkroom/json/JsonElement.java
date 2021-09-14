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

import cn.inkroom.json.exception.JsonTypeException;
import cn.inkroom.json.value.*;

public interface JsonElement {

    /**
     * 返回当前数据格式类型
     *
     * @return
     */
    Type getType();

    Object getValue();

    default JsonString getAsJsonString() {
        if (getType() != Type.String)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成String");
        return ((JsonString) this);
    }

    default JsonBoolean getAsJsonBoolean() {
        if (getType() != Type.Boolean)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成Boolean");
        return ((JsonBoolean) this);
    }

    default JsonArray getAsJsonArray() {
        if (getType() != Type.Array)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成Array");
        return ((JsonArray) this);
    }

    default JsonInt getAsJsonInt() {
        if (getType() != Type.Int)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成String");
        return ((JsonInt) this);
    }

    default JsonLong getAsJsonLong() {
        if (getType() != Type.String)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成Int");
        return (JsonLong) this;
    }

    default JsonDouble getAsJsonDouble() {
        if (getType() != Type.Double)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成Double");
        return ((JsonDouble) this);
    }

    default JsonNull getAsJsonNull() {
        if (getType() != Type.Null)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成Null");
        return ((JsonNull) this);
    }

    default JsonObject getAsJsonObject() {
        if (getType() != Type.Object)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成Object");
        return ((JsonObject) this);
    }

    default JsonBigInteger getAsJsonBigInteger() {
        if (getType() != Type.BigInteger && getType() != Type.Int && getType() != Type.Long)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成BigInteger");
        if (this instanceof JsonInt) {
            return new JsonBigInteger(((JsonInt) this).getValue());
        } else if (this instanceof JsonLong) {
            return new JsonBigInteger(((JsonLong) this).getValue());
        }
        return ((JsonBigInteger) this);
    }

    default JsonBigDecimal getAsJsonBigDecimal() {
        if (getType() != Type.BigDecimal && getType() != Type.Double)
            throw new JsonTypeException("当前类型为：" + getType().name() + " , 不能转成BigDecimal");
        if (this instanceof JsonDouble) {
            return new JsonBigDecimal(((JsonDouble) this).getValue());
        }
        return ((JsonBigDecimal) this);
    }

    enum Type {
        Object,
        Array,
        Int,
        Long,
        Null,
        Double,
        String,
        BigInteger,
        BigDecimal,
        Boolean,

    }

}
