package com.sellgirl.sgJavaHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
//@Target注解指定注解能修饰的目标(只能是方法)
@Target(ElementType.FIELD)
public @interface PFSqlFieldAttribute{
  String FieldText();//default "zhangsan";
  String FieldDescription()  default "";
  boolean HasChinese()  default false;
}