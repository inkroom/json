package cn.inkroom.json.serialize.annotation;

import java.lang.annotation.*;

/**
 * 输出时指定别名；如果同时加在get方法和field上，那么将会进行一个合并操作
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAlias {
    String[] alias() default {};
}
