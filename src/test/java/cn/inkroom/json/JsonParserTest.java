package cn.inkroom.json;

import cn.inkroom.json.annotation.JsonConfig;
import cn.inkroom.json.annotation.JsonFeature;
import cn.inkroom.json.exception.JsonException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonParserTest {

    /**
     * 测试转换一个Object
     */
    @Test
    public void parseObject() {


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
        parse("{\"1\":1,}","{\"1\":1}");

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
        parse("-1");
        parse("0.23");


        //测试非法数据
        parseError("-");//只有一个－

        parseError("www");//字符串未包裹

        parseError(".");//非法小数
        parseError("0.");//非法小数
        parseError("0.3.3");//非法小数

        parseError("3o");//数字开头，混合其他字符
        parseError("30.d");//数字开头，混合其他字符
        parseError("843.290re");//数字开头，混合其他字符

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

    public void parse(JsonConfig config, String json, String except) {
        JsonElement element = new JsonParser(config).parse(json);

        assertEquals(except, element.toString());

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