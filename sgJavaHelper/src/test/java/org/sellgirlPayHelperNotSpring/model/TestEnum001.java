package org.sellgirlPayHelperNotSpring.model;

import com.sellgirl.sgJavaHelper.PFEnum2;

@SuppressWarnings("deprecation")
public final class TestEnum001 extends PFEnum2<TestEnum001>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3181880742918417727L;
	public final static TestEnum001 Default=new TestEnum001("Default",3);
	public final static TestEnum001 Fgs=new TestEnum001("Fgs",5);
	//public static enum03 Default=new enum03("Default",3);
	protected TestEnum001(String name, int ordinal) {
		super(name,ordinal);
	}
//	@Override
//	public int getValue() {
//		// TODO Auto-generated method stub
//		return _value;
//	}
//
//	@Override
//	public String getText() {
//		// TODO Auto-generated method stub
//		return _text;
//	}
//
//	@Override
//	public boolean hasFlag(PFEnum other) {
//		// TODO Auto-generated method stub
//		return PFDataHelper.EnumHasFlag(this._value, other.getValue());
//	}			

//	private enum02(String text,int value) {
//		_value=value;
//		_text=text;		
//	}	
	//不能复写ordinal
//	public  int ordinal() {
//		
//	}
}