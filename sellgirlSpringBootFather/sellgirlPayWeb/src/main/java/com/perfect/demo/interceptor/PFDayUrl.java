package com.perfect.demo.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为了防扫黄,把日期加入url里
 * @author Administrator
 *测试地址:https://localhost:44303/20210107/getmobiledeskimgname
 */
//@Retention注解指定Login注解可以保留多久
//（元注释后面讲）
@Retention(RetentionPolicy.RUNTIME)
//@Target注解指定注解能修饰的目标(只能是方法或类)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PFDayUrl{
	String value() default "";
}
