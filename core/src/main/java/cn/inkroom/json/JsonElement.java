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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

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


        ;

        /**
         * 将class对象映射到json类型
         *
         * @param c
         * @return
         */
        public static Type convertClass2Type(Class c) {
            if (int.class.isAssignableFrom(c) || Integer.class.isAssignableFrom(c)) {
                return Int;
            } else if (char.class.isAssignableFrom(c) || java.lang.CharSequence.class.isAssignableFrom(c)) {
                return String;
            } else if (boolean.class.isAssignableFrom(c) || java.lang.Boolean.class.isAssignableFrom(c)) {
                return Boolean;
            } else if (double.class.isAssignableFrom(c) || float.class.isAssignableFrom(c) || Float.class.isAssignableFrom(c) || java.lang.Double.class.isAssignableFrom(c)) {
                return Double;
            } else if (long.class.isAssignableFrom(c) || java.lang.Long.class.isAssignableFrom(c)) {
                return Long;
            } else if (java.math.BigDecimal.class.isAssignableFrom(c)) {
                return BigDecimal;
            } else if (java.math.BigInteger.class.isAssignableFrom(c)) {
                return BigInteger;
            } else if (c.isArray()) {
                return Array;
            } else if (Collection.class.isAssignableFrom(c)) {
                return Array;
            } else {
                return Object;
            }


        }
    }

}
