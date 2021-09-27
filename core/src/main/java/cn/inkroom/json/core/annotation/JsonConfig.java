/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.core.annotation;

import cn.inkroom.json.core.exception.JsonConfigException;

import java.util.LinkedHashSet;
import java.util.Set;

public class JsonConfig {


    /**
     * 启用的feature
     */
    private Set<JsonFeature> enable;

    /**
     * 关闭的feature
     */
    private Set<JsonFeature> disable;

    /**
     * 时间格式化
     */
    private String timeFormat = "HH:mm:ss";

    /**
     * 日期格式化
     */
    private String dateFormat = "YYYY-MM-HH";

    /**
     * 日期时间格式化，如果数据没有同时包括两类元素就不会使用该配置
     */
    private String dateTimeFormat = dateFormat + " " + timeFormat;


    public JsonConfig() {
        enable = new LinkedHashSet<>();
        disable = new LinkedHashSet<>();

        // 设置默认的特性
        JsonFeature[] values = JsonFeature.values();
        for (JsonFeature f : values) {
            if (f.isEnable()) {
                enable(f);
            } else {
                disable(f);
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
        enable.add(feature);
    }

    /**
     * 关闭某项特性
     *
     * @param feature
     */
    public void disable(JsonFeature feature) {
        disable.add(feature);
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        if (dateTimeFormat == null) throw new JsonConfigException("format can not be null");
        this.dateTimeFormat = dateTimeFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        if (timeFormat == null) throw new JsonConfigException("format can not be null");

        this.timeFormat = timeFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        if (dateFormat == null) throw new JsonConfigException("format can not be null");

        this.dateFormat = dateFormat;
    }
}
