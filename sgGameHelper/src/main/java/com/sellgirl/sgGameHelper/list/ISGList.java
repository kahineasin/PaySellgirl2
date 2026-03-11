package com.sellgirl.sgGameHelper.list;

/**
 * 为了便于统一多种框架的list的api
 * @param <T>
 */
public interface ISGList<T> extends Iterable<T> {
    T first();
    T get(int i);
    int size();
//    public void clear();
//    public void add(T a);
//    public boolean addAll(Iterable<T> c);
//    public void addAllSG(Iterable<? extends T> c);
//    public boolean addAll(Iterable<? extends T> c);
    //public void addAll(T a);
   // public boolean addAll(Iterable<? extends T> c);
//    public boolean addAll(Collection< T> c);
//    public boolean addAll(Collection<? extends T> c);
}
