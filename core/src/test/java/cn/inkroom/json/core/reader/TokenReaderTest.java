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

import cn.inkroom.json.core.JsonFactory;
import cn.inkroom.json.core.Token;
import cn.inkroom.json.core.annotation.JsonConfig;
import cn.inkroom.json.core.annotation.JsonFeature;
import cn.inkroom.json.core.exception.JsonParseException;
import org.junit.Assert;

import java.util.function.Function;

public class TokenReaderTest {

    /**
     * 读取字符串，要求能识别特殊字符
     */
    public void readString(Function<String, TokenReader> function) {

        readString(function, "\"1\"", "1");
        readString(function, "\"1abcd\"", "1abcd");
        readString(function, "\"\\t\"", "\t");
        readString(function, "\"\\rss\\t\\nds\\b32\\f\"", "\rss\t\nds\b32\f");
        readString(function, "\"中文\\u5b66\\u4e60教材15.6\"", "中文学习教材15.6");
        readString(function, "\"中文\\u5b66\\u4e60fw教材15.6\"", "中文学习fw教材15.6");


        //测试错误字符串

        readStringError(function, "\"dssawe");
        readStringError(function, "\"\\u222");
        readStringError(function, "\"\\uwdus\"");
        readStringError(function, "\"\\uwdu433s\"");


        //测试不转换unicode
        JsonConfig config = new JsonConfig();
        config.disable(JsonFeature.CONVERT_UNICODE);

        TokenReader apply = function.apply("\"中文\\u5b66\\u4e60fw教材15.6\"");
        apply.setConfig(config);

        Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
        Assert.assertEquals(Token.TEXT, apply.readNextToken());
        Assert.assertEquals("中文\\u5b66\\u4e60fw教材15.6", apply.readString());
        Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());

        apply = function.apply("\"中文\\u5M66\\u4e60fw教材15.6\"");
        apply.setConfig(config);
        Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
        Assert.assertEquals(Token.TEXT, apply.readNextToken());
        try {
            apply.readString();
            Assert.fail("没有检测出非法的unicode码");
        } catch (JsonParseException e) {
            Assert.assertEquals("Unexpected character M row: 0, col: 6", e.getMessage());
        }

    }


    private void readString(Function<String, TokenReader> function, String json, String except) {

        TokenReader apply = function.apply(json);

        Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
        Assert.assertEquals(Token.TEXT, apply.readNextToken());
        Assert.assertEquals(except, apply.readString());
        Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());

    }

    private void readStringError(Function<String, TokenReader> function, String json) {
        try {
            TokenReader apply = function.apply(json);
            Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
            Assert.assertEquals(Token.TEXT, apply.readNextToken());
            apply.readString();
            Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());
        } catch (JsonParseException e) {
            return;
        }
        Assert.fail("应该出现的异常没有出现 " + json);
    }


    /**
     * 测试数字读取，保证能检测到非法数据
     */
    public void readNumber(Function<String, TokenReader> function) {

        //测试正常数据
        readNumber(function, "123");
        readNumber(function, "123.0");
        readNumber(function, "-1");
        readNumber(function, "1");
        readNumber(function, "-1.43");
        readNumber(function, "1.4");
        readNumber(function, "5.49");

        //测试带有空格的数据
        readNumber(function, "  48934", "48934");
        readNumber(function, "\t9328", "9328");
        readNumber(function, " \t9328", "9328");

        readNumber(function, "9328  ", "9328");
        readNumber(function, "9328\t", "9328");
        readNumber(function, "9328  \t", "9328");


        //测试超限数据
        readNumber(function, "2389234345473348297429", "2389234345473348297429");
        readNumber(function, "2389234345473348297429.2345354333", "2389234345473348297429.2345354333");

        // 测试科学计数法

        readNumber(function, "3e3", "3000");
        readNumber(function, "4E3", "4000");
        readNumber(function, "3.3e3", "3300");
        readNumber(function, "9.4E32", "940000000000000000000000000000000");
        readNumber(function, "-9.4E12", "-9400000000000");
        readNumber(function, "9.4E-12", "0.0000000000094");
        readNumber(function, "-8.7E-43", "-0.00000000000000000000000000000000000000000087");

        //测试错误数据
        readNumberError(function, "-");
        readNumberError(function, "1.");
        readNumberError(function, "1.d");
        readNumberError(function, "8w");
        readNumberError(function, "382spw");
        readNumberError(function, "-ls");
        readNumberError(function, "-38x");
        readNumberError(function, "-28.p");
        readNumberError(function, "28.372.8");
        readNumberError(function, "28.372.8ds");
        readNumberError(function, "-.54");
        readNumberError(function,"32e33e");
        readNumberError(function,"7ee");
        readNumberError(function,"7.e");
        readNumberError(function,"7.4e-");

        // 中间不允许有空格
        readNumberError(function, "93 28");
        readNumberError(function, "93\t28");
        readNumberError(function, " 93\t28");

        //测试超出精度范围


    }

    /**
     * 测试boolean的读取
     *
     * @param function
     */
    public void readBoolean(Function<String, TokenReader> function) {

        readBoolean(function, "true");
        readBoolean(function, "false");

        // 测试错误数据
        readBooleanError(function, "trul");
        readBooleanError(function, "trle");
        readBooleanError(function, "twue");
        readBooleanError(function, "twu");
        readBooleanError(function, "t");


        readBooleanError(function, "falsd");
        readBooleanError(function, "fwosl");
        readBooleanError(function, "fals");
        readBooleanError(function, "fal");
        readBooleanError(function, "f");

    }

    /**
     * 测试null的读取
     *
     * @param function
     */
    public void readNull(Function<String, TokenReader> function) {

        readNull(function, "null");

        // 测试错误数据
        readNullError(function, "nulw");
        readNullError(function, "nu");
        readNullError(function, "nwll");

    }

    private void readNull(Function<String, TokenReader> function, String json) {

        TokenReader apply = function.apply(json);

        Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
        Assert.assertEquals(Token.NULL, apply.readNextToken());
        Assert.assertNull(apply.readNull());
        Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());

    }

    private void readNullError(Function<String, TokenReader> function, String json) {
        try {
            TokenReader apply = function.apply(json);
            Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
            Assert.assertEquals(Token.NULL, apply.readNextToken());
            Assert.assertNull(apply.readNull());
            Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());
        } catch (JsonParseException e) {
            return;
        }
        Assert.fail("应该出现的异常没有出现 " + json);
    }

    private void readBoolean(Function<String, TokenReader> function, String json) {

        TokenReader apply = function.apply(json);

        Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
        Assert.assertEquals(Token.BOOLEAN, apply.readNextToken());
        Assert.assertEquals(json, String.valueOf(apply.readBoolean()));
        Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());

    }

    private void readBooleanError(Function<String, TokenReader> function, String json) {
        try {
            TokenReader apply = function.apply(json);
            Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
            Assert.assertEquals(Token.BOOLEAN, apply.readNextToken());
            apply.readBoolean();
            Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());
        } catch (JsonParseException e) {
            return;
        }
        Assert.fail("应该出现的异常没有出现 " + json);
    }

    private void readNumber(Function<String, TokenReader> function, String json, String except) {
        TokenReader apply = function.apply(json);
        Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
        Assert.assertEquals(Token.NUMBER, apply.readNextToken());
        Assert.assertEquals(except, JsonFactory.createValue(apply.readNumber()).toString());
        Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());

    }

    private void readNumber(Function<String, TokenReader> function, String json) {
        readNumber(function, json, json);
    }

    private void readNumberError(Function<String, TokenReader> function, String json) {
        try {
            TokenReader apply = function.apply(json);
            Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
            Assert.assertEquals(Token.NUMBER, apply.readNextToken());
            apply.readNumber();
            Assert.assertEquals(Token.DOCUMENT_END, apply.readNextToken());
        } catch (JsonParseException e) {
            return;
        }
        Assert.fail("应该出现的异常没有出现 " + json);
    }


}