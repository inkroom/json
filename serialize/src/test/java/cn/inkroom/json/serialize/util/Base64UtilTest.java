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

import cn.inkroom.json.exception.JsonParseException;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

public class Base64UtilTest {

    @Test
    public void encode() {


//        Assert.assertEquals("QQ==", Base64Util.encode("A".getBytes()));
//
//        Assert.assertEquals("anNkcGZ1d2VqaXBzZA==", Base64Util.encode("jsdpfuwejipsd".getBytes()));
        Assert.assertEquals(Base64.encodeBase64String("中文a".getBytes()), Base64Util.encode("中文a".getBytes()));

        Assert.assertEquals("", Base64Util.encode("".getBytes()));
        Assert.assertEquals("Zg==", Base64Util.encode("f".getBytes()));
        Assert.assertEquals("Zm8=", Base64Util.encode("fo".getBytes()));
        Assert.assertEquals("Zm9v", Base64Util.encode("foo".getBytes()));
        Assert.assertEquals("Zm9vYg==", Base64Util.encode("foob".getBytes()));
        Assert.assertEquals("Zm9vYmE=", Base64Util.encode("fooba".getBytes()));
        Assert.assertEquals("Zm9vYmFy", Base64Util.encode("foobar".getBytes()));

        Assert.assertEquals(Base64.encodeBase64String("中速度sw文a".getBytes()), Base64Util.encode("中速度sw文a".getBytes()));
        Assert.assertEquals(Base64.encodeBase64String("中速32度sw文a".getBytes()), Base64Util.encode("中速32度sw文a".getBytes()));
        Assert.assertEquals(Base64.encodeBase64String("中速32度.['sw文a".getBytes()), Base64Util.encode("中速32度.['sw文a".getBytes()));


    }

    @Test
    public void decode() {

        assertArrayEquals(Base64.decodeBase64("Zm9vYmFy"), Base64Util.decode("Zm9vYmFy"));

        assertArrayEquals(Base64.decodeBase64("Zm9vYg=="), Base64Util.decode("Zm9vYg=="));

        assertArrayEquals(Base64.decodeBase64("Zm9vYmE="), Base64Util.decode("Zm9vYmE="));

        assertArrayEquals(Base64.decodeBase64(Base64.encodeBase64String("中文1".getBytes())), Base64Util.decode(Base64Util.encode("中文1".getBytes())));

        //测试错误数据

        try {

            Base64Util.decode("'[]/sd;32");
            Base64Util.decode("Zm9vYmE==");
            Base64Util.decode("Zm9vYmEW");

            Assert.fail("没检测出错误");
        } catch (JsonParseException e) {

        }


    }
}