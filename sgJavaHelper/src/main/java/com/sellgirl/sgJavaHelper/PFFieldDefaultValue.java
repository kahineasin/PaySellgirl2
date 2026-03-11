package com.sellgirl.sgJavaHelper;
//package pf.java.pfHelper;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
//PFJsonIgnoreDefaultFilter没办法拿到PFFieldDefaultValue自定义的默认值
// * 为了实现C#里面下面的用法
// *  [DefaultValue(1)]
//        [JsonProperty(DefaultValueHandling = DefaultValueHandling.Ignore)]
//        
//        
// * @author Administrator
// *
// */
////@Retention注解指定Login注解可以保留多久
////（元注释后面讲）
//@Retention(RetentionPolicy.RUNTIME)
////@Target注解指定注解能修饰的目标(只能是方法或类)
//@Target({ElementType.FIELD})
//public @interface PFFieldDefaultValue{
//	String value() default "";
//}