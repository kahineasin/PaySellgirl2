package com.sellgirl.sgJavaHelper;


@FunctionalInterface
public interface SGAction<T1,T2,T3> {
     void go(T1 t1,T2 t2,T3 t3);
}
