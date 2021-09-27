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
import cn.inkroom.json.serialize.util.Base64Util;

public class ByteArraySerializer implements JsonSerializer<byte[]> {
    @Override
    public void serialize(byte[] value, JsonWriter writer, SerializerProvider provider) throws JsonSerializeException {
        if (provider.getConfig().isEnable(JsonFeature.BASE64_BYTE_ARRAY)) {
            writer.writeString(Base64Util.encode(value));
        }
        //否则转换成16进制表示
        else
            writer.writeString(toHex(value));
    }

    private String toHex(byte[] b) {

        char[] src = "0123456789ABCDEF".toCharArray();

        StringBuilder builder = new StringBuilder();
        for (byte value : b) {
            builder.append(src[(value & 0xf0) >> 4]).append(src[(value & 0xf]);
        }
        return builder.toString();
    }


}
