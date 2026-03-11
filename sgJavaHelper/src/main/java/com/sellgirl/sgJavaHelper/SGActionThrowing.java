package com.sellgirl.sgJavaHelper;


@FunctionalInterface
public interface SGActionThrowing<T1,T2,T3,TE extends Exception> {
     void go(T1 t1,T2 t2,T3 t3) throws TE;
}
