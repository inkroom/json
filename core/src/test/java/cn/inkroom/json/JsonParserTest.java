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

import cn.inkroom.json.core.JsonElement;
import cn.inkroom.json.core.JsonParser;
import cn.inkroom.json.core.annotation.JsonConfig;
import cn.inkroom.json.core.annotation.JsonFeature;
import cn.inkroom.json.core.exception.JsonException;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonParserTest {

    /**
     * 测试转换一个Object
     */
    @Test
    public void parseObject() {

        parse("");
        parse("{}");
        parse("{\"1\":1}");
        parse("{\"1\":1.0}");
        parse("{\"1\":0.1}");
        parse("{\"1\":-1}");
        parse("{\"1\":-1.3}");
        parse("{\"1\":true}");
        parse("{\"1\":false}");
        parse("{\"1\":null}");
        parse("{\"1\":1,\"2\":1.0}");
        parse("{\"1\":1,\"2\":2.0,\"3\":true}");
        parse("{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false}");
        parse("{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null}");
        parse("{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null,\"6\":-6,\"7\":-7.67}");
        parse("{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null,\"6\":-6,\"7\":-7.67,\"8\":\"ink json lib\"}");

        parse("{\"1\":\"ink json lib\"}");
        parse("{\"1\":\"\"}");
        parse("{\"1\":1,}", "{\"1\":1}");

        //测试带有换行符的json
        parse("{\n}", "{}");
        parse("{\n\"1\":1}", "{\"1\":1}");
        parse("{\"1\"\n:1.0}", "{\"1\":1.0}");
        parse("{\"1\":0.1\n}", "{\"1\":0.1}");
        parse("{\"1\"\n:\n-1\n}", "{\"1\":-1}");
        parse("{\"1\":-1.3\r}", "{\"1\":-1.3}");
        parse("{\"1\":\rtrue\t}", "{\"1\":true}");
        parse("{\"1\"\t:false\r}", "{\"1\":false}");
        parse("{\"1\"\t:null\n}", "{\"1\":null}");
        parse("{\t\"1\"\n:\r1,\"2\":1.0}", "{\"1\":1,\"2\":1.0}");
        parse("{\"1\":1,\"2\":2.0,\"3\":true}", "{\"1\":1,\"2\":2.0,\"3\":true}");
        parse("{\"1\":1,\r\"2\":2.0,\"3\":true\n,\"4\":false}", "{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false}");
        parse("{\"1\":1,\"2\":2.0\r,\"3\":true,\"4\":false,\"5\":null}", "{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null}");
        parse("{\"1\":1,\"2\":2.0\n,\"3\":true,\"4\":false,\"5\":null,\"6\":-6,\"7\":-7.67}", "{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null,\"6\":-6,\"7\":-7.67}");
        parse("{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null,\"6\":-6,\"7\":-7.67,\"8\":\"ink json lib\"\n,\"9\":\"json lib\"}", "{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null,\"6\":-6,\"7\":-7.67,\"8\":\"ink json lib\",\"9\":\"json lib\"}");
        parse("{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null,\"6\":-6,\"7\":-7.67,\"8\":\"ink json lib\"\n,\"9\":\"json lib\"}", "{\"1\":1,\"2\":2.0,\"3\":true,\"4\":false,\"5\":null,\"6\":-6,\"7\":-7.67,\"8\":\"ink json lib\",\"9\":\"json lib\"}");


        parse("{\"1\":\"ink json lib\"}");
        parse("{\"1\":\"\"}");
        parse("{\"1\":1,}", "{\"1\":1}");


        //测试错误数据

        parseError("{");
        parseError("}");
        parseError("{\"");
        parseError("{\"\"}");
        parseError("{\"\"");
        parseError("{\"d\"");

        parseError("{\"dd\":}");
        parseError("{\"dd\":\"}");
        parseError("{\"dd\":n}");
        parseError("{\"dd\":t}");
        parseError("{\"dd\":1,");

        //测试多行错误
        try {
            new JsonParser(new JsonConfig()).parse("{\nw");
        } catch (JsonException e) {
            Assert.assertEquals("Unexpected character [w] row: 1, col: 1", e.getMessage());
        }

        //不允许最后一个逗号存在
        JsonConfig config = new JsonConfig();
        config.disable(JsonFeature.ALLOW_LAST_COMMA);

        parseError(config, "{\"1\":1,}");

    }

    /**
     * 测试转化Array
     */
    @Test
    public void parseArray() {
        parse("[]");
        parse("[3]");
        parse("[true]");
        parse("[false]");
        parse("[null]");
        parse("[1.0]");
        parse("[-54]");
        parse("[483.594]");
        parse("[-8934.23478]");
        parse("[\"sd\"]");
        parse("[\"\"]");

        parse("[1,2.0,-3,4.4,true,false,null,\"\",\"ink json lib\"]");


        // 标准json最后不允许有逗号，提供特性允许放开
        parse("[32,43,]", "[32,43]");

        //测试错误数据
        parseError("[");
        parseError("]");
        parseError("[,]");
        parseError("[sds]");

        //不允许最后一个逗号存在
        JsonConfig config = new JsonConfig();
        config.disable(JsonFeature.ALLOW_LAST_COMMA);

        parseError(config, "[32,43,]");


    }

    private void parse(String json, String except) {
        parse(new JsonConfig(), json, except);

    }

    /**
     * 测试单个value
     */
    @Test
    public void singleValue() {
        parse("1");
        parse("true");
        parse("false");
        parse("\"wwww\"");
        parse(new JsonConfig(), "\"ww\\\nww\"", "\"ww\nww\"");// 测试json5 多行文本
        parse(new JsonConfig(), "'wwww2'", "\"wwww2\"");
        parse("-1");
        parse("0.23");
        parse(new JsonConfig(), ".3", "0.3");//测试json5数字写法
        parse(new JsonConfig(), "3.", "3.0");//测试json5数字写法
        parse(new JsonConfig(), "+3", "3");//测试json5数字写法
        parse(new JsonConfig(), "+3", "3");//测试json5数字写法
        parse(new JsonConfig(), "0x17F7b6b5", "402110133");//测试json5数字写法
        parse(new JsonConfig(), "0x7fffffffffffffff", Long.toString(Long.MAX_VALUE));//测试json5数字写法


        //测试非法数据
        parseError("-");//只有一个－

        parseError("www");//字符串未包裹

        parseError(".");//非法小数
        parseError("0.3.3");//非法小数

        parseError("3o");//数字开头，混合其他字符
        parseError("30.d");//数字开头，混合其他字符
        parseError("843.290re");//数字开头，混合其他字符
        parseError("843 290");//数字开头，混合其他字符

        parseError("trso");//不正确的布尔值
        parseError("trus");//不正确的布尔值
        parseError("tosu");//不正确的布尔值
        parseError("falsp");//不正确的布尔值
        parseError("falie");//不正确的布尔值
        parseError("fapid");//不正确的布尔值
        parseError("fpw");//不正确的布尔值


    }

    @Test
    public void com() {

        String json = "{\"1\":{\"2\":true,\"3\":false},\"4\":null,\"5\":[12,38.53,\"value\",{\"6\":null,\"7\":[54,65,true,null]}]}";

        parse(json);

    }

    /**
     * 测试json5支持
     * <p>单行注释</p>
     */
    @Test
    public void json5SingDesc() throws Exception {

// 单行注释，允许出现在除 key和value内部的以外的任何位置
        json5("singleDesc");

    }

    /**
     * 测试json5支持
     * <p>多行注释</p>
     */
    @Test
    public void json5MultiDesc() throws Exception {

// 多行注释，允许出现在除 key和value内部的以外的任何位置

        json5("multiDesc");

    }

    /**
     * 测试json5支持
     * <p>单双引号</p>
     */
    @Test
    public void json5Quotation() throws Exception {

// 允许key-value使用单双引号

        json5("quotation");

    }

    /**
     * 测试json5支持
     * <p>数字写法</p>
     */
    @Test
    public void json5Number() throws Exception {

// json5的数字写法

        json5("number");

    }

    /**
     * 测试json5支持
     * <p>object-Key写法</p>
     */
    @Test
    public void json5Key() throws Exception {

// json5的object key

        json5("key");

    }

    private void json5(String prefix) throws Exception {
        URL resource = getClass().getResource("/json5/" + prefix);

        File file = new File(resource.getFile());
        String[] list = file.list();

        for (String f : list) {
            System.out.println("测试文件 = " + f);
            if (f.endsWith(".json5"))
                parse(IOUtils.resourceToString("/json5/" + prefix + "/" + f, StandardCharsets.UTF_8), IOUtils.resourceToString("/json5/" + prefix + "/" + f + ".except", StandardCharsets.UTF_8));
        }
    }

    public void parse(JsonConfig config, String json, String except) {
        JsonElement element = new JsonParser(config).parse(json);

        if (element != null)
            Assert.assertEquals(except, element.toString());

    }

    public void parseError(JsonConfig config, String json) {
        try {
            JsonElement element = new JsonParser(config).parse(json);
        } catch (JsonException e) {
            return;
        }
        Assert.fail("应该有的错误没有出现," + json);


    }


    public void parse(String json) {
        parse(new JsonConfig(), json, json);
    }

    public void parseError(String json) {
        parseError(new JsonConfig(), json);


    }
}