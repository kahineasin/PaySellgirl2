package com.sellgirl.sgJavaHelper;

/*
 * 因为java没有ref关键字,这样用吧
 * 使用方法:
 * SGRef<Integer> ti = new SGRef<Integer>(0);
 */
public class SGRef<T> {
	private T _value;
	public SGRef(T value) {
		
		_value=value;
	}
	public SGRef() {
		
	}
	public T GetValue() {
		return _value;		
	}
	public void SetValue(T value) {
		_value=value;
	}
	/**
	 * 为了便于FormatString
	 */
	@Override
	public String toString() {
		if(_value==null) {return null;}
		return _value.toString();
	}
}
