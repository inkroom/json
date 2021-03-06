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

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 存储要进行序列化的class对象
 */
public class SerializeClass {

    private Class c;
    /**
     * 可用的get方法
     */
    private List<Property> getters;
    /**
     * 可用的set方法
     */
    private List<Property> setters;


    public SerializeClass(Class c) {
        this.c = c;

        initGetters();


    }

    private void initGetters() {
        if (getters == null) {

            getters = new LinkedList<>();
            setters = new LinkedList<>();
            Method[] methods = c.getDeclaredMethods();

            for (Method m : methods) {
                Property property = Property.convert(c, m);
                if (property != null) {
                    if (property.getGetter())
                        getters.add(property);
                    else setters.add(property);


                }
            }


            Comparator<Property> c = (o1, o2) -> {
                if (o1.getName().length() == o2.getName().length()) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o1.getName().length() > o2.getName().length() ? 1 : -1;
                }
            };

            getters.sort(c);
            setters.sort(c);

        }

    }

    public List<Property> getGetters() {
        return getters;
    }

    public List<Property> getSetters() {
        return setters;
    }


}
