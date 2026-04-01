package com.sellgirl.sellgirlPayWeb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Swagger只做有此标签的
//@Retention注解指定Login注解可以保留多久
//（元注释后面讲）
@Retention(RetentionPolicy.RUNTIME)
//@Target注解指定注解能修饰的目标(只能是方法或类)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface PayShopSwaggerAttr{
}