/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.core;

public enum Token {
    DOCUMENT_START,
    DOCUMENT_END,
    OBJECT_START,
    OBJECT_END,
    ARRAY_START,
    ARRAY_END,
    BOOLEAN,
    /**
     * 冒号
     */
    SEP_COLON,
    /**
     * 逗号
     */
    SEP_COMMA,
    /**
     * 引号，接下来是个字符串
     */
    TEXT,
    NUMBER,
    NULL,
    /**
     * 单行注释开始
     */
    SINGLE_DESC_START,
    /**
     * 单行注释结束
     */
    SINGLE_DESC_END,
    /**
     * 多行注释开始
     */
    MULTI_DESC_START,
    /**
     * 多行注释结束
     */
    MULTI_DESC_END,
    /**
     * 换行符
     */
    LINE,
    /**
     * 非法
     */
    ILL,

}
