/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json;

import cn.inkroom.json.annotation.JsonConfig;
import cn.inkroom.json.annotation.JsonFeature;
import cn.inkroom.json.reader.SingleLineTokenReader;
import cn.inkroom.json.reader.TokenReader;
import cn.inkroom.json.value.JsonString;

import java.util.Stack;

public class JsonParser {


    private JsonConfig config;

    public JsonParser() {
        config = new JsonConfig();
    }

    public JsonParser(JsonConfig config) {
        this.config = config;
    }

    public JsonElement parse(String json) {

        TokenReader reader = new SingleLineTokenReader(json);

        Stack<JsonElement> stack = new Stack<>();


        /*
        token 和 状态是两个东西，一个状态应该对应多个token。意思是可能读取到不同的token，但是都会转向相同的状态
         */
        int exceptStatus = STATUS_EXPECT_SINGLE_VALUE | STATUS_EXPECT_BEGIN_OBJECT | STATUS_EXPECT_BEGIN_ARRAY;//期望下一个状态
        for (; ; ) {

            Token t = reader.readNextToken();
            switch (t) {
                case DOCUMENT_START:
                    exceptStatus = STATUS_EXPECT_SINGLE_VALUE | STATUS_EXPECT_BEGIN_OBJECT | STATUS_EXPECT_BEGIN_ARRAY;
                    continue;
                case DOCUMENT_END:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_END_DOCUMENT)) {
                        if (stack.size() == 1) {
                            return stack.pop();
                        }
                    }
                    reader.throwError(t);
                case OBJECT_START:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_BEGIN_OBJECT)) {//是否是一个正确的状态
                        stack.push(JsonFactory.createObject());
                        exceptStatus = STATUS_EXPECT_END_OBJECT | STATUS_EXPECT_OBJECT_KEY;

                        continue;
                    }
                    reader.throwError(t);
                case ARRAY_START:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_BEGIN_ARRAY)) {
                        //开始读取一个Array，后续可以是结束这个Array，或者直接一个Value，或者是一个Object开始，再或者嵌套一个Array

                        stack.push(JsonFactory.createArray());
                        exceptStatus = STATUS_EXPECT_END_ARRAY | STATUS_EXPECT_ARRAY_VALUE | STATUS_EXPECT_BEGIN_OBJECT | STATUS_EXPECT_BEGIN_ARRAY;
                        continue;

                    }
                    reader.throwError(t);
                case ARRAY_END:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_END_ARRAY)) {//结束一个array
                        JsonElement v = stack.pop();//此时应该是一个Array
                        if (stack.isEmpty()) {
                            stack.push(v);
                            exceptStatus = STATUS_EXPECT_END_DOCUMENT;
                            continue;
                        }
                        //栈内还有数据，那么代表读取完了这个Array，这个Array可能是前一个Object中某个key的value，也可以是前一个Array中的一个value

                        JsonElement pre = stack.pop();

                        if (pre instanceof JsonString) {//此时是某个key的value
                            //取栈顶的map，设置进去
                            JsonObject map = (JsonObject) stack.peek();
                            map.put(pre.toString(), v);

                            //读取完object中的一个k-v，那么后续要么是一个逗号，继续kv；要么直接结束这个Object
                            exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_OBJECT;

                            continue;
                        }
                        if (pre instanceof JsonArray) {//是Array中的一个value

                            ((JsonArray) stack.peek()).add(pre);

                            //读取完Array的一个value，那么后续要么是一个逗号，继续value；要么直接结束这个Array
                            exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_ARRAY;
                            continue;
                        }
                    }
                    reader.throwError(t);
                case OBJECT_END:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_END_OBJECT)) {

                        //当栈内只有一个元素时（map，list，或者单个value），json可能已经读取完了
                        JsonElement v = stack.pop();//此时v应该是一个Object
                        if (stack.isEmpty()) {
                            stack.push(v);
                            exceptStatus = STATUS_EXPECT_END_DOCUMENT;
                            continue;
                        }
                        //栈里还有数据，那么代表当前这个Object已经读取完了，这个Object可能是前一个Object中某个key的value，也可以是前一个Array中的一个value
                        JsonElement pre = stack.pop();
                        if (pre instanceof JsonString) {//此时是某个key的value
                            //取栈顶的map，设置进去
                            JsonObject map = ((JsonObject) stack.peek());
                            map.put(pre.toString(), v);

                            //读取完object中的一个k-v，那么后续要么是一个逗号，继续kv；要么直接结束这个Object
                            exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_OBJECT;

                            continue;
                        }
                        if (pre instanceof JsonArray) {//是Array中的一个value

                            ((JsonArray) pre).add(v);
                            stack.push(pre);
                            //读取完Array的一个value，那么后续要么是一个逗号，继续value；要么直接结束这个Array
                            exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_ARRAY;
                            continue;
                        }
                    }
                    reader.throwError(t);
                case TEXT:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_SINGLE_VALUE)) {//整行只有一个数据
                        stack.push(JsonFactory.createValue(reader.readString()));
                        exceptStatus = STATUS_EXPECT_END_DOCUMENT;
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_OBJECT_KEY)) {// object的key
                        stack.push(JsonFactory.createValue(reader.readString()));
                        exceptStatus = STATUS_EXPECT_COLON;//后面应该是冒号
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_OBJECT_VALUE)) {
                        String key = stack.pop().toString();
                        String value = reader.readString();
                        JsonObject peek = (JsonObject) stack.peek();
                        peek.put(key, JsonFactory.createValue(value));

                        exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_OBJECT;

                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_ARRAY_VALUE)) {
                        String value = reader.readString();
                        ((JsonArray) stack.peek()).add(JsonFactory.createValue(value));
                        exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_ARRAY;
                        continue;
                    }
                    reader.throwError(t);
                case NUMBER:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_SINGLE_VALUE)) {//整行只有一个数据
                        stack.push(JsonFactory.createValue(reader.readNumber()));
                        exceptStatus = STATUS_EXPECT_END_DOCUMENT;
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_OBJECT_KEY)) {// object的key
                        stack.push(JsonFactory.createValue(reader.readNumber()));
                        exceptStatus = STATUS_EXPECT_COLON;//后面应该是冒号
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_OBJECT_VALUE)) {
                        String key = stack.pop().toString();
                        Number value = reader.readNumber();
                        JsonObject peek = (JsonObject) stack.peek();
                        peek.put(key, JsonFactory.createValue(value));
                        exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_OBJECT;
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_ARRAY_VALUE)) {
                        ((JsonArray) stack.peek()).add(JsonFactory.createValue(reader.readNumber()));
                        exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_ARRAY;
                        continue;
                    }
                    reader.throwError(t);
                case NULL:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_SINGLE_VALUE)) {//整行只有一个数据
                        stack.push(JsonFactory.createValue(JsonFactory.createValue(reader.readNull())));
                        exceptStatus = STATUS_EXPECT_END_DOCUMENT;
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_OBJECT_VALUE)) {
                        String key = stack.pop().toString();
                        JsonObject peek = (JsonObject) stack.peek();
                        peek.put(key, JsonFactory.createValue(reader.readNull()));
                        exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_OBJECT;
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_ARRAY_VALUE)) {
                        ((JsonArray) stack.peek()).add(JsonFactory.createValue(reader.readNull()));
                        exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_ARRAY;
                        continue;
                    }
                    reader.throwError(t);
                case BOOLEAN:
                    if (hasStatus(exceptStatus, STATUS_EXPECT_SINGLE_VALUE)) {//整行只有一个数据
                        stack.push(JsonFactory.createValue(reader.readBoolean()));
                        exceptStatus = STATUS_EXPECT_END_DOCUMENT;
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_OBJECT_VALUE)) {
                        String key = stack.pop().toString();
                        JsonObject peek = (JsonObject) stack.peek();
                        peek.put(key, JsonFactory.createValue(reader.readBoolean()));
                        exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_OBJECT;
                        continue;
                    }
                    if (hasStatus(exceptStatus, STATUS_EXPECT_ARRAY_VALUE)) {
                        ((JsonArray) stack.peek()).add(JsonFactory.createValue(reader.readBoolean()));
                        exceptStatus = STATUS_EXPECT_COMMA | STATUS_EXPECT_END_ARRAY;
                        continue;
                    }
                    reader.throwError(t);
                case SEP_COLON://冒号
                    if (hasStatus(exceptStatus, STATUS_EXPECT_COLON)) {
                        exceptStatus = STATUS_EXPECT_OBJECT_VALUE | STATUS_EXPECT_BEGIN_OBJECT | STATUS_EXPECT_BEGIN_ARRAY;
                        continue;
                    }
                    reader.throwError(t);
                case SEP_COMMA://逗号
                    if (hasStatus(exceptStatus, STATUS_EXPECT_COMMA)) {
                        if (hasStatus(exceptStatus, STATUS_EXPECT_END_OBJECT)) {//之前已经读取了一个object的key，现在读取到了逗号，后面就应该跟一个object的key
                            exceptStatus = STATUS_EXPECT_OBJECT_KEY;
                            if (config.isEnable(JsonFeature.ALLOW_LAST_COMMA)) {//允许最后一个元素后面可以存在逗号，那么下一个token就可以是key或者object end
                                exceptStatus |= STATUS_EXPECT_END_OBJECT;
                            }
                            continue;
                        }
                        if (hasStatus(exceptStatus, STATUS_EXPECT_END_ARRAY)) {//之前已经读取了array的一个value，现在读取到了逗号，后面可以跟一个新的value，或者一个新的Object，再或者一个新的Array
                            exceptStatus = STATUS_EXPECT_ARRAY_VALUE | STATUS_EXPECT_BEGIN_OBJECT | STATUS_EXPECT_BEGIN_ARRAY;
                            if (config.isEnable(JsonFeature.ALLOW_LAST_COMMA)) {//允许最后一个元素后面可以存在逗号，那么下一个token就可以是array end
                                exceptStatus |= STATUS_EXPECT_END_ARRAY;
                            }
                            continue;
                        }
                    }
                    reader.throwError(t);
                case ILL:
                    reader.throwError(t);
            }


        }


    }

    /**
     * 新的状态是否符合期望
     *
     * @param except 期望状态
     * @param actual 实际状态
     * @return
     */
    private boolean hasStatus(int except, int actual) {
        return ((except & actual) > 0);
    }

    /**
     * Should read EOF for next token.
     */
    static final int STATUS_EXPECT_END_DOCUMENT = 0x0002;

    /**
     * Should read "{" for next token.
     */
    static final int STATUS_EXPECT_BEGIN_OBJECT = 0x0004;

    /**
     * Should read "}" for next token.
     */
    static final int STATUS_EXPECT_END_OBJECT = 0x0008;

    /**
     * Should read object key for next token.
     */
    static final int STATUS_EXPECT_OBJECT_KEY = 0x0010;

    /**
     * Should read object value for next token.
     */
    static final int STATUS_EXPECT_OBJECT_VALUE = 0x0020;

    /**
     * Should read ":" for next token.
     */
    static final int STATUS_EXPECT_COLON = 0x0040;

    /**
     * Should read "," for next token.
     */
    static final int STATUS_EXPECT_COMMA = 0x0080;

    /**
     * Should read "[" for next token.
     */
    static final int STATUS_EXPECT_BEGIN_ARRAY = 0x0100;

    /**
     * Should read "]" for next token.
     */
    static final int STATUS_EXPECT_END_ARRAY = 0x0200;

    /**
     * Should read array value for next token.
     */
    static final int STATUS_EXPECT_ARRAY_VALUE = 0x0400;

    /**
     * Should read a single value for next token (must not be "{" or "[").
     */
    static final int STATUS_EXPECT_SINGLE_VALUE = 0x0800;

}
