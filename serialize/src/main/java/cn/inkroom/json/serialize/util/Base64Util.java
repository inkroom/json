/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.serialize.util;

import cn.inkroom.json.core.exception.JsonParseException;

/**
 * base64工具类
 */
public class Base64Util {

    /**
     * base64元字符
     */
    private static char[] src = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static char[] urlSrc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();

    /**
     * base64的url安全传输版本，去除url中的元字符，保证可直接用于url
     *
     * @param bytes
     * @return
     */
    public static String encodeURLSave(byte[] bytes) {

        return _encode(bytes, urlSrc);

    }

    public static String _encode(byte[] bytes, char[] table) {

        //用于存储字符的缓冲区，只使用低24 bit
        int buffer = 0;

        StringBuilder builder = new StringBuilder();
        for (int i = 0, t = 1; i < bytes.length; i++, t++) {
            buffer = buffer << 8;
            // 负值需要去除 符号位
            buffer = buffer | (bytes[i] < 0 ? (bytes[i] + 256) : bytes[i]);

            if (t == 3) {//完成一组
                buffer = buffer << 8;//再移八位，方便处理，低八位就不用了
                for (int j = 0; j < 4; j++) {
                    builder.append(_indexChar(buffer, table));
                    buffer = buffer << 6;//补充用掉的位
                }
                buffer = 0;
                t = 0;
            }
        }

        if (buffer != 0) {//说明需要补=，此时buffer中低位有8bit或者16bit数据，依次从高位取6位，直到有效位被用完
            if (bytes.length % 3 == 1) {//此时有8bit
                //把有效位移到高位
                buffer = buffer << 24;
                for (int i = 0; i < 2; i++) {
                    builder.append(_indexChar(buffer, table));
                    buffer = buffer << 6;
                }
                builder.append("==");
            } else {//有16bit
                buffer = buffer << 16;
                for (int i = 0; i < 3; i++) {
                    builder.append(_indexChar(buffer, table));
                    buffer = buffer << 6;
                }
                builder.append("=");
            }

        }

        return builder.toString();
    }


    /**
     * base64编码
     *
     * @param bytes 数据
     * @return base64
     */
    public static String encode(byte[] bytes) {

        return _encode(bytes, src);

    }

    public static byte[] decode(String value) {

        char[] chars = value.toCharArray();
        byte[] bytes = new byte[value.length()];//实际上用不完
        int c = 0;
        int buffer = 0;
        int t = 0;

        if (chars.length % 4 != 0) {
            throw new JsonParseException("Illegal base64");
        }


        // 取4个字符index的低6位组成一个3 * 8 = 24 bit，得到3个byte
        for (int i = 0; i < chars.length; i++, t++) {
            if (chars[i] == '=') break;
            //获取字符对应的index，然后每8bit一组，取出byte
            int index = _charIndex(chars[i]);
            if (index == -1) {
                throw new JsonParseException("ill base64");
            }
            buffer = buffer << 6;
            buffer = buffer | index;

            if (t == 3) {//得到了24bit
                bytes[c++] = (byte) (buffer >>> 16);
                bytes[c++] = (byte) ((buffer & 0xff00) >>> 8);
                bytes[c++] = (byte) (buffer & 0xff);

                buffer = 0;
                t = -1;
            }
        }

        if (buffer != 0) {//代表不是整数，还有12、18个bit未处理
            if (t == 2) {//还有12bit 8 + 4 最后4bit是补位的0，不需要
                _validBit(0xf, buffer);
                buffer = buffer >>> 4;
                bytes[c++] = (byte) (buffer & 0xff);
            } else if (t == 3) { //还有18bit，8 + 8 + 2 最后2bit是补位的0，不需要
                _validBit(0x3, buffer);
                buffer = buffer >>> 2;
                bytes[c++] = (byte) ((buffer & 0xff00) >>> 8);
                bytes[c++] = (byte) (buffer & 0xff);
            }
        }

        byte[] r = new byte[c];
        System.arraycopy(bytes, 0, r, 0, c);
        return r;
    }


    private static void _validBit(int mask, int value) {
        if ((value & mask) != 0) throw new JsonParseException("Illegal base64");
    }


    /**
     * 定位数据
     *
     * @param value 高6位为有效数据
     * @return
     */
    private static char _indexChar(int value, char[] table) {
        //每次从高位取出6位
        int index = value & 0xfc000000;
        index = index >>> 26;//将高6位移到低六位
        return table[index];
    }

    /**
     * 获取字符在表中的下标
     *
     * @param c
     * @return
     */
    private static int _charIndex(char c) {
        for (int i = 0; i < src.length; i++) {
            if (src[i] == c) {
                return i;
            }
        }
        //可能是url安全的
        if (c == '-') {
            return 62;
        } else if (c == '_') return 63;
// 不应该出现
        return -1;

    }
}
