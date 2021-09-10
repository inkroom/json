package cn.inkroom.json.reader;

import cn.inkroom.json.Token;
import cn.inkroom.json.exception.JsonException;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class TokenReaderTest {

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


        // 测试科学计数法


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
        } catch (JsonException e) {
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
        } catch (JsonException e) {
            return;
        }
        Assert.fail("应该出现的异常没有出现 " + json);
    }

    private void readNumber(Function<String, TokenReader> function, String json, String except) {
        TokenReader apply = function.apply(json);
        Assert.assertEquals(Token.DOCUMENT_START, apply.readNextToken());
        Assert.assertEquals(Token.NUMBER, apply.readNextToken());
        Assert.assertEquals(except, apply.readNumber().toString());
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
        } catch (JsonException e) {
            return;
        }
        Assert.fail("应该出现的异常没有出现 " + json);
    }


}