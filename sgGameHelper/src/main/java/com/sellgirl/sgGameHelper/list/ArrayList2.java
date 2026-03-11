package com.sellgirl.sgGameHelper.list;

import com.sellgirl.sgGameHelper.list.ISGList;

import java.util.ArrayList;

/**
 *
 * @deprecated render中用ArrayList.foreach不太合适, 因为每次是new iter的
 * @param <T>
 */
@Deprecated
public class ArrayList2<T> extends ArrayList<T> implements ISGList<T> {

    @Override
    public T first() {
        return get(0);
    }

//    @Override
//    public void addAll(T... c) {
//        super.addAll(c);
//    }
}
