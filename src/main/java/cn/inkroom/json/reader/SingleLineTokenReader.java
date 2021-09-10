package cn.inkroom.json.reader;

import cn.inkroom.json.Token;
import cn.inkroom.json.exception.JsonException;


/**
 * 只支持单行json数据的reader
 */
public class SingleLineTokenReader implements TokenReader {


    private char[] data;//数据窗口，每次读取这么多数据，把数据窗口的数据使用完之后再读取
    private int cursor = -1;//数据指针，指向即将读取的数据位置

    public SingleLineTokenReader(String data) {
        this.data = data.toCharArray();
    }


    private boolean isWhiteSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
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
            } else if (isWhiteSpace(c)) {//允许后面有空白字符，但是不允许中间存在；前面的空白字符在之前的获取Token的过程中已经去除了
                hasWhiteSpace = true;
                next();
            } else {//读取到其他非法字符
                throwError(null);
            }

        }
        int i = builder.indexOf(".");
        if (i == builder.length() - 1) {//最后一位是小数点，数字非法
            throwError(null);
        }
        if (i != -1) {
            return Double.parseDouble(builder.toString());
        }
        return Integer.parseInt(builder.toString());
    }

    @Override
    public String readString() {

        StringBuilder builder = new StringBuilder();
        for (; ; ) {
            char c = next();
            if (c == '"') {
                return builder.toString();
            }

            builder.append(c);
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
        if (token != null) {
            throw new JsonException("unexcept character " + token + " col number: " + cursor);
        }
        throw new JsonException("error json, col number: " + cursor);

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
        return data[cursor++];
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
