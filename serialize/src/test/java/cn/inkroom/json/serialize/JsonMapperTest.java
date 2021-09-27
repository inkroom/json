/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.serialize;

import cn.inkroom.json.core.JsonParser;
import cn.inkroom.json.core.annotation.JsonConfig;
import cn.inkroom.json.core.annotation.JsonFeature;
import cn.inkroom.json.serialize.example.Demo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class JsonMapperTest {

    @Test
    public void t() throws Exception {

        ObjectMapper om = new ObjectMapper();

        byte[] b = new byte[]{0,2,3,4,5,6,7,8};

        System.out.println(new String(b));
        String s = om.writeValueAsString(b);



    }

    @Test
    public void write() throws Exception {

        Demo d = new Demo();
        d.setV15(new int[]{93, 483, 3872});
        d.setV9(Arrays.asList(new Demo(), new Demo()));
        Map<String, Demo> v = new HashMap<>();
        v.put("v12", new Demo());
        d.setV10(v);

        JsonConfig config = new JsonConfig();
        config.disable(JsonFeature.IGNORE_NULL);

        JsonMapper mapper = new JsonMapper(config);
        ObjectMapper om = new ObjectMapper();

        //输出复合类型
        String write = mapper.write(d);

        Assert.assertEquals(om.writeValueAsString(d), om.writeValueAsString(om.readValue(write, Demo.class)));

        new JsonParser().parse(write);

        //测试map类型
        Assert.assertEquals(v.toString(), om.readValue(mapper.write(v), new TypeReference<HashMap<String, Demo>>() {
        }).toString());

        //测试数组类型

    }
}