/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.serialize;

import cn.inkroom.json.common.Property;
import cn.inkroom.json.common.SerializeClass;
import cn.inkroom.json.core.JsonElement;
import cn.inkroom.json.core.annotation.JsonConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 序列化核心，由该类负责统筹，开发者直接调用该类
 */
public class JsonMapper {
    /**
     * 基本的一些配置
     */
    private JsonConfig config = null;

    private SerializerProvider provider;

    public JsonMapper() {
        this.config = new JsonConfig();
        provider = new SerializerProvider(this.config);
    }

    public JsonMapper(JsonConfig config) {
        this.config = config;
        provider = new SerializerProvider(this.config);
    }

    public JsonMapper(JsonConfig config, SerializerProvider provider) {
        this.config = config;
        this.provider = provider;
    }

    public JsonConfig getConfig() {
        return config;
    }

    public void setConfig(JsonConfig config) {
        this.config = config;
    }

    /**
     * 将对象转换成json
     *
     * @param java
     * @return
     */
    public String write(Object java) {
        if (java == null) {
            return "null";
        }
        if (java instanceof JsonElement) {
            return java.toString();
        }

        JsonWriter writer = new JsonWriter();
        provider.findSerializer(java.getClass()).serialize(java, writer, provider);

        return writer.toString();
    }


    /**
     * 转换普通对象
     *
     * @param java
     * @return
     */
    private String writeObject(Object java) throws InvocationTargetException, IllegalAccessException {


        StringBuilder builder = new StringBuilder("{");
        SerializeClass c = getSerializeClass(java);

        for (Property p : c.getGetters()) {
            builder.append("\"").append(p.getName()).append("\":");

            switch (p.getType()) {
                case Int:
                    builder.append(writeInt(p, java));
                    break;
                case String:
                    builder.append("\"").append(writeString(p, java)).append("\"");
                    break;
                case Boolean:
                    builder.append(writeBoolean(p, java));
                    break;
                case Long:
                    builder.append(writeLong(p, java));
                    break;
                case Double:
                    builder.append(writeDouble(p, java));
                    break;
                case BigInteger:
                    builder.append(writeBigInteger(p, java));
                    break;
                case BigDecimal:
                    builder.append(writeBigDecimal(p, java));
                    break;
                case Array:
                    builder.append(writeList(p, java));
                    break;
                case Object:
                    builder.append(writeObject(p, java));
            }

            builder.append(",");

        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append("}");
        return builder.toString();
    }

    private String writeObject(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        if (java instanceof Map) {
            return writeMap(((Map<?, ?>) java));
        }
        Object invoke = p.getMethod().invoke(java);
        if (invoke == null) {
            return "null";
        }
        if (invoke instanceof Map) {
            return writeMap(((Map<?, ?>) invoke));
        }
        return writeObject(invoke);
    }

    private String writeList(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        Object invoke = p.getMethod().invoke(java);
        if (invoke == null) {
            return "null";
        }

        Collection collection = (Collection) invoke;

        StringBuilder builder = new StringBuilder("[");
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Object next = iterator.next();
            builder.append(write(next));
            builder.append(",");
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append("]");

        return builder.toString();
    }

    private SerializeClass getSerializeClass(Object next) {
        return new SerializeClass(next.getClass());
    }

    private String writeBigDecimal(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        return writeInt(p, java);
    }

    private String writeBigInteger(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        return writeInt(p, java);
    }

    private String writeDouble(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        return writeInt(p, java);
    }

    private String writeLong(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        return writeInt(p, java);
    }

    private String writeBoolean(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        return writeInt(p, java);
    }

    private String writeString(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        Object invoke = p.getMethod().invoke(java);
        if (invoke == null) {
            return "null";
        }
        return invoke.toString();
    }

    private String writeInt(Property p, Object java) throws InvocationTargetException, IllegalAccessException {
        Object invoke = p.getMethod().invoke(java);
        if (invoke == null) {
            return "null";
        }
        return invoke.toString();
    }


    private String writeList(Collection<?> java) throws InvocationTargetException, IllegalAccessException {

        StringBuilder builder = new StringBuilder("[");

        for (Object next : java) {
            builder.append(writeObject(next));
            builder.append(",");
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }

    private String writeMap(Map<?, ?> java) {
        StringBuilder builder = new StringBuilder("{");

        java.forEach((key, value) -> {
            builder.append("\"").append(key).append("\":");
            try {
                builder.append(writeObject(value)).append(",");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("}");

        return builder.toString();
    }
}
