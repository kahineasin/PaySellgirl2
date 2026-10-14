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

    /**
	 * 
	 */
	private static final long serialVersionUID = 6085669720561852497L;

	@Override
    public T first() {
        return get(0);
    }

	@Override
	public T removeIndex(int index) {		
		return super.remove(index);
	}
@Override
    public void add2(T a) {
    	super.add(a);
    }

//    @Override
//    public void addAll(T... c) {
//        super.addAll(c);
//    }
}
