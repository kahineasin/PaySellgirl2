package com.sellgirl.sgJavaHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Retention注解指定Login注解可以保留多久
//（元注释后面讲）
/**
 * 表示不拦截此控制器或者控制器的方法
 */
@Retention(RetentionPolicy.RUNTIME)
//@Target注解指定注解能修饰的目标(只能是方法或类)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SGAllowAnonymous{
}