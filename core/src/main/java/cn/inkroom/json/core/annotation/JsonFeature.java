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

/**
 * 用于启停一些功能
 */
public enum JsonFeature {


    /**
     * 允许最后一个元素后面的逗号存在；例如
     * [2,]
     * <br>
     * 默认启用
     */
    ALLOW_LAST_COMMA(true),
    /**
     * 转换unicode码
     * <br>
     * 默认启用
     */
    CONVERT_UNICODE(true),
    /**
     * 不输出值为null的数据
     * <br>
     * 默认启用
     */
    IGNORE_NULL(true),
    /**
     * 将char数组合并成字符串输出；不启用则作为一个数组依次输出为字符串
     */
    COMBINATION_CHAR_ARRAY(true),

    /**
     * 将byte数组转换成base64；禁用情况下将转成大写的16进制
     * <br>
     * 默认启用
     */
    BASE64_BYTE_ARRAY(true),

    /**
     * 使用毫秒时间戳存储时间和日期类数据
     * <br>
     * 默认不启用，将会格式化为{@link JsonConfig} 中指定的格式
     */
    TIME_STAMP(false),
    ;

    /**
     * 是否默认启用
     */
    private boolean enable;

    JsonFeature(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }
}
