/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.annotation;

import java.util.LinkedList;
import java.util.List;

public class JsonConfig {


    /**
     * 启用的feature
     */
    private List<JsonFeature> enable;
    /**
     * 关闭的feature
     */
    private List<JsonFeature> disable;


    public JsonConfig() {
        enable = new LinkedList<>();
        disable = new LinkedList<>();

        // 设置默认的特性
        JsonFeature[] values = JsonFeature.values();
        for (JsonFeature f : values) {
            if (f.isEnable()) {
                enable.add(f);
            } else {
                disable.add(f);
            }
        }

    }

    /**
     * 判断某项feature是否启用
     *
     * @param feature 特性
     * @return 是否启用
     */
    public boolean isEnable(JsonFeature feature) {
        return enable.contains(feature);
    }

    /**
     * 启用某项特性
     *
     * @param feature
     */
    public void enable(JsonFeature feature) {
        disable.remove(feature);
        if (!enable.contains(feature))
            enable.add(feature);
    }

    /**
     * 关闭某项特性
     *
     * @param feature
     */
    public void disable(JsonFeature feature) {

        if (!disable.contains(feature))
            disable.add(feature);
        enable.remove(feature);

    }


}
