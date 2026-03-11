package com.sellgirl.sgGameHelper.list;

import com.badlogic.gdx.utils.Array;
//import com.mygdx.game.share.ISGList;

public class Array2<T> extends Array<T> implements ISGList<T> {
//    @Override
//    public void addAllSG(Iterable<T > c) {
//        super.addAll(c);
//    }
//    @Override
//    public boolean addAll(Iterable<T> c) {
//        return super.addAll(c);
//    }
//    @Override
//    public void addAll(T... c) {
//         super.addAll(c);
//    }
//    @Override
//    public boolean addAll(Collection<T> c) {
//        return super.addAll(c);
//    }
//    public boolean addAll(Collection<? extends T> c) {
////        Object[] a = c.toArray();
////        modCount++;
////        int numNew = a.length;
////        if (numNew == 0)
////            return false;
////        Object[] elementData;
////        final int s;
////        if (numNew > (elementData = this.elementData).length - (s = size))
////            elementData = grow(s + numNew);
////        System.arraycopy(a, 0, elementData, s, numNew);
////        size = s + numNew;
//        return true;
//    }
    public int size(){
        return super.size;
    }
}
