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

import cn.inkroom.json.JsonElement;
import cn.inkroom.json.serialize.exception.JsonSerializeException;

import java.util.Stack;

/**
 * 负责实际写入json数据
 */
public class JsonWriter {
    private StringBuilder json = new StringBuilder();
    private StringBuilder temp = new StringBuilder();

    private Stack<Context> context = new Stack<>();


    public void startObject() {
        json.append("{");

        Context item = new Context();
        item.type = JsonElement.Type.Object;
        item.parent = context.isEmpty() ? null : context.peek();
        context.push(item);
    }

    public void endObject() {
        json.append("}");
        context.pop();
        clear();
    }


    public void startArray() {
        json.append("[");

        Context item = new Context();
        item.type = JsonElement.Type.Array;
        item.parent = context.peek();
        context.push(item);
    }

    public void endArray() {
        json.append("]");
        context.pop();
        clear();
    }

    public void writeString(CharSequence s) {
        json.append("\"").append(s).append("\"");
    }

    public void writeDouble(double v) {
        json.append(v);
    }

    public void writeLong(long v) {
        json.append(v);
    }

    /**
     * 写一个null
     */
    public void writeNull() {
        json.append("null");
    }

    /**
     * 逗号,但是不会直接写入，需要调用 {@link #flush()} 才会写入
     */
    public void comma() {
        temp.append(",");
    }

    /**
     * 将一些缓存的写入
     */
    public void flush() {

        json.append(temp);
        clear();
    }

    /**
     * 清空缓存，不写入
     */
    public void clear() {
        temp.delete(0, temp.length());
    }

    /**
     * 填充某个属性
     *
     * @param name 属性名称
     */
    public void field(String name) {
        if (context.isEmpty() || context.peek().type != JsonElement.Type.Object) {
            throw new JsonSerializeException("not a object, can not write field");
        }
        context.peek().key = name;
        json.append("\"").append(name).append("\"").append(":");
    }

    /**
     * 原样输出
     *
     * @param c
     */
    public void raw(CharSequence c) {
        json.append(c);
    }

    public Context getContext() {
        return context.peek();
    }

    @Override
    public String toString() {
        return json.toString();
    }

    /**
     * 输出的一个上下文环境
     */
    public static class Context {
        /**
         * 当前输出的环境
         */
        private JsonElement.Type type;
        /**
         * 可能存在的Object-key
         */
        private String key;
        /**
         * 上一级
         */
        private Context parent;


        public JsonElement.Type getType() {
            return type;
        }

        public void setType(JsonElement.Type type) {
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Context getParent() {
            return parent;
        }

        public void setParent(Context parent) {
            this.parent = parent;
        }
    }

}
