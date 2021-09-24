/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.serialize.serializer;

import cn.inkroom.json.annotation.JsonFeature;
import cn.inkroom.json.serialize.*;
import cn.inkroom.json.serialize.exception.JsonSerializeException;

import java.lang.reflect.InvocationTargetException;

/**
 * 兜底的工具，一般负责自定义bean的序列化
 */
public class BeanJsonSerializer implements JsonSerializer<Object> {


    private SerializeClass getSerializeClass(Object next) {
        return new SerializeClass(next.getClass());
    }

    @Override
    public void serialize(Object value, JsonWriter writer, SerializerProvider provider) throws JsonSerializeException {

        SerializeClass c = getSerializeClass(value);

        writer.startObject();
        for (Property p : c.getGetters()) {
            Object v = null;
            try {
                v = p.getMethod().invoke(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new JsonSerializeException(e);
            }
            if (v == null && provider.getConfig().isEnable(JsonFeature.IGNORE_NULL)) {
                continue;
            }
            writer.flush();

            writer.field(p.getName());
            if (v == null) {
                writer.writeNull();
            } else
                provider.findSerializer(v.getClass()).serialize(v, writer, provider);
            writer.comma();
        }
        writer.endObject();

    }
}
