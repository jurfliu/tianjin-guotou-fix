package com.bonc.tianjin.guotou.annotaion;

import java.lang.annotation.*;

/**
 *
 *
 * 被应用与此注解的类不会被执行，防止误操作
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Expired {
}
