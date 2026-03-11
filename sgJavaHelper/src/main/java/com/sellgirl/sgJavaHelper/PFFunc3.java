package com.sellgirl.sgJavaHelper;

/*
 * 三个参数的委托,和BiFunction一样
 */
@FunctionalInterface
public interface PFFunc3<T1,T2,T3,TReturn> {
     TReturn go(T1 t1,T2 t2,T3 t3) throws Exception;
}
