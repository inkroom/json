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

import cn.inkroom.json.core.annotation.JsonFeature;
import cn.inkroom.json.serialize.JsonSerializer;
import cn.inkroom.json.serialize.JsonWriter;
import cn.inkroom.json.serialize.SerializerProvider;
import cn.inkroom.json.serialize.exception.JsonSerializeException;

import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("all")
public class MapSerializer implements JsonSerializer<Map<String, ?>> {
    @Override
    public void serialize(Map<String, ?> value, JsonWriter writer, SerializerProvider provider) throws JsonSerializeException {


        writer.startObject();

        Iterator<String> iterator = value.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object v = value.get(key);
            if (v != null || !provider.getConfig().isEnable(JsonFeature.IGNORE_NULL)) {
                writer.flush();
                writer.field(key);
                provider.findSerializer(v.getClass()).serialize(v, writer, provider);
                writer.comma();
            }
        }

        writer.endObject();


    }
}
