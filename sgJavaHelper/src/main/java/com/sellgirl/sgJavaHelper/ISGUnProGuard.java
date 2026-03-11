package com.sellgirl.sgJavaHelper;

/**
 * 继承此接口的类, 不应对其的public method 和类名等做 proguard 混淆
 * 后来想想, 本接口也没什么用
 * 1. 如果不是一起打包的, 其它库调用 SGDataHelper 时,肯定要求 SGDataHelper不能改类名
 * 2. 就算是一起打包, 但如 knightsasha.jar 和 knightsashax.jar 的分离情况, 也要求 SGDataHelper不能改名
 * 
 * 适合于
 * 1. 只有像apk这种一个包的
 * 2. 单个jar包的情况 此接口才有用; 多个jar这种分散布置的, 此接口没什么用; 但单个jar的情况, 此类有用
 */
public interface ISGUnProGuard {

}
