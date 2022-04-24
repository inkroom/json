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

import cn.inkroom.json.core.JsonElement;
import cn.inkroom.json.core.annotation.JsonConfig;
import cn.inkroom.json.serialize.serializer.*;
import cn.inkroom.json.serialize.serializer.array.*;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用来提供序列化工具
 */
@SuppressWarnings("all")
public class SerializerProvider {


    private JsonConfig config;
    private Map<Class, JsonSerializer> serializerMap;


    public SerializerProvider(JsonConfig config) {
        this.config = config;
        serializerMap = new LinkedHashMap<>();

        //提前注册
        register(JsonElement.class, new JsonElementSerializer());
        register(Number.class, new NumberSerializer());
        register(CharSequence.class, new StringSerializer());
        register(Character.class, new CharSerializer());
        register(Collection.class, new CollectionSerializer());
        register(Map.class, new MapSerializer());
        register(Array.class, new ArraySerializer());
        register(Boolean.class, new BooleanSerializer());


        // 数组
        register(int[].class, new IntArraySerializer());
        register(double[].class, new DoubleArraySerializer());
        register(float[].class, new FloatArraySerializer());
        register(short[].class, new ShortArraySerializer());
        register(long[].class, new LongArraySerializer());
        register(char[].class, new CharArraySerializer());
        register(boolean[].class, new BooleanArraySerializer());
        register(byte[].class, new ByteArraySerializer());

        // 枚举
        register(Enum.class, new EnumSerializer());

        register(Object[].class, new ObjectArraySerializer());


        //兜底
//        register(BeanJsonSerializer.class, new BeanJsonSerializer());

    }

    public void register(Class c, JsonSerializer serializer) {
        serializerMap.put(c, serializer);
    }

    public JsonSerializer findSerializer(Class c) {
        JsonSerializer jsonSerializer = serializerMap.get(c);

        if (jsonSerializer != null) {
            return jsonSerializer;
        }
        //如果没有完全符合的序列类，就找一下有没有父类匹配的
        Iterator<Class> iterator = serializerMap.keySet().iterator();
        while (iterator.hasNext()) {
            Class next = iterator.next();
            if (next.isAssignableFrom(c)) {
                return serializerMap.get(next);
            }
        }

        // 如果都没有，则使用兜底序列化类
        return new BeanJsonSerializer();
    }


    public JsonConfig getConfig() {
        return config;
    }


}
