package cn.inkroom.json.common.annotation;

import java.lang.annotation.*;

/**
 * 别名；
 * <p>序列化</p>
 * 如果同时加在方法和field上，那么将会进行一个合并操作
 * <p></p>
 * <p>反序列化</p>
 * field优先级最高，其次通过alias读取
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAlias {
    String[] alias() default {};
}
