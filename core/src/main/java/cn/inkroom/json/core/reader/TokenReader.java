/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.core.reader;

import cn.inkroom.json.core.Token;
import cn.inkroom.json.core.annotation.JsonConfig;
import cn.inkroom.json.core.exception.JsonParseException;

public interface TokenReader {

    /**
     * 设置配置项
     *
     * @param config 配置项
     */
    void setConfig(JsonConfig config);

    /**
     * 获取下一个token
     *
     * @return 获取的token
     */
    Token readNextToken();

    boolean readBoolean();

    /**
     * 跳过一行的内容，用于单行注释
     */
    void skipLine();

    /**
     * 跳过多行，用于多行注释
     */
    void skipMultiLine();

    Number readNumber();

    /**
     * 获取key，支持json5
     *
     * @return key
     */
    String readKey();

    String readString();

    /**
     * @return 固定返回null
     */
    Object readNull();

    /**
     * 读取到不合法的数据，抛出异常
     *
     * @param token 当前的token
     * @throws JsonParseException 当出现格式错误时
     */
    void throwError(Token token) throws JsonParseException;

}
