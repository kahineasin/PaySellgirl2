package com.sellgirl.sgJavaHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 感觉此类没必要,下面那样写也不复杂:
Arrays.asList(_follower).stream().map(a->a.getTelephone()).filter(a->PFDataHelper.StringIsNullOrWhiteSpace(a)).toArray(String[]::new);
 * @author Administrator
 *
 * @param <T>
 */
public class PFLinq<T> {
	private Class<T> cls;
	private T[] list;
	public PFLinq(Class<T> cls, T[] list) {
		
	}
	public PFLinq<T> Where(//Class<T> cls, T[] list, 
			Function<T, Boolean> selectAction) {
		List<T> result = new ArrayList<T>();
		for (T i : list) {
			if (selectAction.apply(i)) {
				result.add(i);
			}
		}
		//return result.toArray(PFDataHelper.ObjectAs(Array.newInstance(cls, result.size())));
		T[] o=SGDataHelper.<T[]>ObjectAs(Array.newInstance(cls, result.size()));
		list=result.toArray(o);
		return this;
	}
	public <TR> PFLinq<TR> Select(//Class<T> cls, T[] list, 
			Class<TR> cls,Function<T, TR> selectAction) {

		List<TR> result = new ArrayList<TR>();
		for (T i : list) {
			result.add(selectAction.apply(i));
		};
//		return result.toArray(PFDataHelper.ObjectAs(Array.newInstance(cls, result.size())));
		TR[] o=SGDataHelper.ObjectAs(Array.newInstance(cls, result.size()));
		return new PFLinq<TR>(cls,result.toArray(o));
	}
	public T[] getArray() {
		return list;
	}
}
