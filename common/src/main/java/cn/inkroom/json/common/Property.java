/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.common;

import cn.inkroom.json.common.annotation.JsonAlias;
import cn.inkroom.json.core.JsonElement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Property {
    /**
     * 对应的method
     */
    private Method method;
    /**
     * 是否是get方法，false就是set方法,null 代表这是一个普通方法
     */
    private Boolean isGetter;
    /**
     * 对应的field name
     */
    private String name;
    /**
     * 输出的别名，不包括field name
     */
    private String[] alias;

    /**
     * 对应field的类型，使用json type存储
     */
    private JsonElement.Type type;

    private Class realClass;

    public String[] getAlias() {
        return alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Boolean getGetter() {
        return isGetter;
    }

    public void setGetter(Boolean getter) {
        isGetter = getter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonElement.Type getType() {
        return type;
    }

    public void setType(JsonElement.Type type) {
        this.type = type;
    }

    public Class getRealClass() {
        return realClass;
    }

    public void setRealClass(Class realClass) {
        this.realClass = realClass;
    }

    /**
     * 从get、set方法中获取field name
     *
     * @param name
     * @return
     */
    private static String getFieldName(String name) {

        if (name.startsWith("is")) {
            if (name.length() < 3) return null;
            name = name.substring(2);
        }
        if (name.startsWith("get") || name.startsWith("set")) {
            if (name.length() < 4)
                return null;
            name = name.substring(3);
        }

        //首字母小写
        if (name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') {
            char[] chars = name.toCharArray();
            chars[0] += 32;
            return String.valueOf(chars);
        }

        return name;

    }

    static Property convert(Class c, Method method) {

        String name = method.getName();
        switch (name) {//屏蔽通用方法
            case "getClass":
            case "isNaN":
            case "isInfinite":
            case "isFinite":
                return null;
        }
        if (!name.startsWith("get") && !name.startsWith("set") && !name.startsWith("is")) {
            return null;
        }

        String field = getFieldName(name);
//        System.out.println(name + " " + field);
        if (field != null) {

            if (!name.startsWith("set")) {
                //get方法返回值为void
                if (method.getReturnType().getName().equals("void")) {
                    return null;
                }
                if (method.getParameterCount() != 0) {//有参数的get方法
                    return null;
                }
            }
            Property property = new Property();
            property.method = method;
            property.name = field;
            property.isGetter = !name.startsWith("set");
            if (property.isGetter) {
                //处理返回值类型
                property.type = JsonElement.Type.convertClass2Type(method.getReturnType());

//                System.out.println(method.getName() + " " + method.getReturnType().getName() + "  " + property.type);

            }
            // 处理可能存在的别名
            HashSet<String> alias = new HashSet<>();
            //　判断是否存在指定名字的field
            try {
                Field f = c.getDeclaredField(field);
                JsonAlias annotation = f.getAnnotation(JsonAlias.class);

                if (annotation != null) {
                    alias.addAll(Arrays.stream(annotation.alias()).collect(Collectors.toSet()));
                }
            } catch (NoSuchFieldException e) {
            }

            JsonAlias annotation = method.getAnnotation(JsonAlias.class);
            if (annotation != null) {
                alias.addAll(Arrays.stream(annotation.alias()).collect(Collectors.toSet()));
            }
            if (!alias.isEmpty()) {
                String[] r = new String[alias.size()];
                alias.toArray(r);
                property.setAlias(r);
            }
            return property;
        }
        return null;
    }
}
