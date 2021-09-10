package cn.inkroom.json.reader;

import cn.inkroom.json.Token;
import cn.inkroom.json.exception.JsonException;

public interface TokenReader {

    /**
     * 获取下一个token
     *
     * @return
     */
    Token readNextToken();

    boolean readBoolean();

    Number readNumber();

    String readString();

    /**
     * @return 固定返回null
     */
    Object readNull();

    /**
     * 读取到不合法的数据，抛出异常
     *
     * @param token 当前的token
     * @throws JsonException
     */
    void throwError(Token token) throws JsonException;

}
