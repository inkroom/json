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

import cn.inkroom.json.core.annotation.JsonConfig;
import cn.inkroom.json.core.exception.JsonTypeException;
import cn.inkroom.json.core.value.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

/**
 * 在执行获取转换时需要特别注意，如果类型不完全匹配的情况下，每次get都是一个全新对象，每个对象存储的数据是一致的
 */
public interface JsonElement {

    /**
     * 返回当前数据格式类型
     *
     * @return
     */
    Type getType();

    Object getValue();

    default String toString(JsonConfig config) {
        return toString();
    }

    default JsonString getAsJsonString() {
        if (getType() != Type.String)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.String);
        return ((JsonString) this);
    }

    default JsonBoolean getAsJsonBoolean() {
        if (getType() != Type.Boolean)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.Boolean);
        return ((JsonBoolean) this);
    }

    default JsonArray getAsJsonArray() {
        if (getType() != Type.Array)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.Array);
        return ((JsonArray) this);
    }

    default JsonInt getAsJsonInt() {
        if (getType() != Type.Int)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.Int);
        return ((JsonInt) this);
    }

    default JsonLong getAsJsonLong() {
        if (getType() != Type.Long)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.Long);
        return (JsonLong) this;
    }

    default JsonDouble getAsJsonDouble() {
        if (getType() != Type.Double)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.Double);
        return ((JsonDouble) this);
    }

    default JsonNull getAsJsonNull() {
        if (getType() != Type.Null)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.Null);
        return ((JsonNull) this);
    }

    default JsonObject getAsJsonObject() {
        if (getType() != Type.Object)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.Object);
        return ((JsonObject) this);
    }

    default JsonBigInteger getAsJsonBigInteger() {
        if (getType() != Type.BigInteger && getType() != Type.Int && getType() != Type.Long)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.BigInteger);
        if (this instanceof JsonInt) {
            return new JsonBigInteger(((JsonInt) this).getValue());
        } else if (this instanceof JsonLong) {
            return new JsonBigInteger(((JsonLong) this).getValue());
        }
        return ((JsonBigInteger) this);
    }

    default JsonBigDecimal getAsJsonBigDecimal() {
        if (getType() != Type.BigDecimal && getType() != Type.Double)
            throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.BigDecimal);
        if (this instanceof JsonDouble) {
            return new JsonBigDecimal(((JsonDouble) this).getValue());
        }
        return ((JsonBigDecimal) this);
    }

    default JsonDate getAsJsonDate() {
        if (getType() == Type.Date) return ((JsonDate) this);
        if (getType() == Type.Long) return new JsonDate(getAsJsonLong().getValue());

        throw new JsonTypeException("unsupported type cast: " + getType() + " to " + Type.Date);
    }

    default JsonDate getAsJsonDate(DateFormat f) {
        try {
            return getAsJsonDate();
        } catch (JsonTypeException e) {
            if (getType() == Type.String) {
                Date parse = null;
                try {
                    parse = f.parse(getAsJsonString().getValue());
                    return new JsonDate(parse);
                } catch (ParseException ex) {
                    throw new JsonTypeException(ex);
                }
            }
        }
        throw new JsonTypeException("unsupported type cast");
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
        Date,


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
