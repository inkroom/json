/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.reader;

import cn.inkroom.json.Token;
import cn.inkroom.json.annotation.JsonConfig;
import cn.inkroom.json.annotation.JsonFeature;
import cn.inkroom.json.exception.JsonException;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * 直接读取字符串的reader
 */
public class StringTokenReader implements TokenReader {


    private JsonConfig config;
    private char[] data;//数据窗口，每次读取这么多数据，把数据窗口的数据使用完之后再读取
    private int cursor = -1;//数据指针，指向即将读取的数据位置，包括换行符之类的空白字符
    private int row = 0;//读取到第几行
    private int col = -1;//读取到第几列


    public StringTokenReader(String data, JsonConfig config) {
        this.config = config;
        this.data = data.toCharArray();
    }


    private boolean isWhiteSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    @Override
    public void setConfig(JsonConfig config) {
        this.config = config;
    }

    @Override
    public Token readNextToken() {
        if (cursor == -1) {
            cursor++;
            return Token.DOCUMENT_START;
        }
        if (!hasMore()) return Token.DOCUMENT_END;

        char datum = next();
        while (isWhiteSpace(datum)) {//去除空白字符，不用担心去除了字符串里的空白字符
            if (!hasMore()) return Token.DOCUMENT_END;
            datum = next();
        }

        if (datum == '{') {
            return Token.OBJECT_START;
        } else if (datum == '}') {
            return Token.OBJECT_END;
        } else if (datum == '[') {
            return Token.ARRAY_START;
        } else if (datum == ']') {
            return Token.ARRAY_END;
        } else if (datum == ',') {
            return Token.SEP_COMMA;
        } else if (datum == ':') {
            return Token.SEP_COLON;
        } else if (datum == '"') {
            return Token.TEXT;
        } else if (datum == 't') {
            return Token.BOOLEAN;
        } else if (datum == 'f') {
            return Token.BOOLEAN;
        } else if (datum == 'n') {
            return Token.NULL;
        } else if (datum <= '9' && datum >= '0') {
            return Token.NUMBER;
        } else if (datum == '-') {//可能是负数，后面应该跟数字
            // cursor已经累加了，所以不用再+1了
            if (peek() <= '9' && peek() >= '0')
                return Token.NUMBER;
            else {
                throwError(null);
            }
        }

        return Token.ILL;
    }

    @Override
    public boolean readBoolean() {
        String except = "";
        char now = now();
        if (now == 't') {
            except = "rue";
        } else if (now == 'f') {
            except = "alse";
        }
        int i = 0;
        for (i = 0; i < except.length(); i++) {
            char c = next();
            if (c != except.charAt(i)) {
                throwError(Token.BOOLEAN);
            }
        }
        //不用考虑可能存在多余的字符，如果有多出的字符，那么在状态机的下一次转换中会认定为状态不合法

        return now == 't';
    }

    @Override
    public Number readNumber() {
        StringBuilder builder = new StringBuilder(Character.toString(now()));
        boolean hasPoint = false;//是否读取到小数点
        boolean hasE = false;
        boolean hasWhiteSpace = false;
        while (hasMore()) {

            char c = peek();
            if (c >= '0' && c <= '9') {
                if (hasWhiteSpace) {//说明在中间存在空白字符
                    throwError(null);
                }
                builder.append(c);
                next();
            } else if (c == '.') {
                if (hasPoint) {
                    throwError(null);
                } else {
                    hasPoint = true;
                    builder.append(c);
                    next();
                }
            } else if (c == ',' || c == '}' || c == ']') {//数字读取完成了
                break;
            } else if (c == 'e' || c == 'E') {
                if (hasE) {
                    throwError(null);
                } else {
                    hasE = true;
                    builder.append('e');//只使用小写字母
                    next();
                }
            } else if (c == '-' || c == '+') {
                if (hasE) {
                    builder.append(c);
                    next();
                } else {
                    throwError(null);
                }
            } else if (isWhiteSpace(c)) {//允许后面有空白字符，但是不允许中间存在；前面的空白字符在之前的获取Token的过程中已经去除了
                hasWhiteSpace = true;
                next();
            } else {//读取到其他非法字符
                throwError(null);
            }

        }

        char last = builder.charAt(builder.length() - 1);
        if (last > '9' || last < '0') {//不正确的结尾
            throwError(null);
        }

        if (hasPoint || hasE) {//有小数点或者使用了科学计数法
            //合法小数转换成double不会报错，但是数据直接不正确，只能直接用高精度，然后在使用的时候再处理成double
            return new BigDecimal(builder.toString());
        }
        try {
            return Integer.parseInt(builder.toString());
        } catch (NumberFormatException e) {
            //大概率就是数字超限，转换成大数
            return new BigInteger(builder.toString());
        }
    }

    @Override
    public String readString() {

        StringBuilder builder = new StringBuilder();
        for (; ; ) {
            char c = next();
            if (c == '\\') {
                char n = next();
                switch (n) {
                    case '"':
                        builder.append(n);
                        break;
                    case 't':
                        builder.append("\t");
                        break;
                    case 'n':
                        builder.append("\n");
                        break;
                    case 'r':
                        builder.append("\r");
                        break;
                    case '\\':
                        builder.append("\\");
                        break;
                    case '/':
                        builder.append("/");
                        break;
                    case 'b':
                        builder.append("\b");
                        break;
                    case 'f':
                        builder.append("\f");
                        break;
                    case 'u':
                        // unicode码

                        if (config.isEnable(JsonFeature.CONVERT_UNICODE)) {
                            //将十六进制转成十进制
                            int r = 0;
                            for (int i = 0; i < 4; i++) {
                                char code = next();
                                if (code >= '0' && code <= '9') {
                                    r = (r << 4) + (code - '0');
                                } else if (code >= 'a' && code <= 'f') {
                                    r = (r << 4) + (code - 'a') + 10;
                                } else if (code >= 'A' && code <= 'F') {
                                    r = (r << 4) + (code - 'a') + 10;
                                } else
                                    throwError(null);
                            }
                            builder.append((char) r);
                        } else {
                            builder.append("\\u");
                            for (int i = 0; i < 4; i++) {
                                char next = next();
                                if ((next >= '0' && next <= '9') || (next >= 'a' && next <= 'f') || (next >= 'A' && next <= 'F'))
                                    builder.append(next);
                                else throwError(null);
                            }
                        }


                        break;
                    default:
                        throwError(null);
                }
            } else if (c == '"') {
                return builder.toString();
            } else {
                builder.append(c);
            }
        }
    }

    @Override
    public Object readNull() {
        String except = "ull";
        int i = 0;
        for (i = 0; i < except.length(); i++) {
            char c = next();
            if (c != except.charAt(i)) {
                throwError(Token.NULL);
            }
        }
        //不用考虑可能存在多余的字符，如果有多出的字符，那么在状态机的下一次转换中会认定为状态不合法
        return null;
    }

    @Override
    public void throwError(Token token) throws RuntimeException {
        throw new JsonException("Unexpected character " + now() + " row: " + row + ", col: " + col);

    }

    /**
     * 取下一个字符，但是指针不动
     *
     * @return
     */
    private char peek() {
        if (!hasMore()) throwError(null);
        return data[cursor];
    }

    private boolean hasMore() {
        return cursor < data.length;
    }

    /**
     * 下一个元素，指针移动
     *
     * @return
     */
    private char next() {
        if (!hasMore()) throwError(null);
        char datum = data[cursor++];
        col++;
        switch (datum) {
            case '\r':
            case '\n':
                row++;
                col = 0;
        }
        return datum;
    }

    /**
     * 当前指针指向的数据
     *
     * @return
     */
    private char now() {
        return data[cursor - 1];
    }

}
