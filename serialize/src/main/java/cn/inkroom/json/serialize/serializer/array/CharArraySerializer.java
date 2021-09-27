/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.serialize.serializer.array;

import cn.inkroom.json.annotation.JsonFeature;
import cn.inkroom.json.serialize.JsonSerializer;
import cn.inkroom.json.serialize.JsonWriter;
import cn.inkroom.json.serialize.SerializerProvider;
import cn.inkroom.json.serialize.exception.JsonSerializeException;

public class CharArraySerializer implements JsonSerializer<char[]> {
    @Override
    public void serialize(char[] value, JsonWriter writer, SerializerProvider provider) throws JsonSerializeException {

        if (provider.getConfig().isEnable(JsonFeature.COMBINATION_CHAR_ARRAY)) {
            writer.writeString(new String(value));
        } else
            for (char c : value) {
                writer.flush();
                writer.writeString(String.valueOf(c));
                writer.comma();
            }


    }
}