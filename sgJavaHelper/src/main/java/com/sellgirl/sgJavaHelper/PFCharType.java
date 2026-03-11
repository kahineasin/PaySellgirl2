package com.sellgirl.sgJavaHelper;


@Deprecated
public final class PFCharType extends PFEnumClass{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5806352607245687879L;
	public PFCharType(String text, int value) {
		super(text, value);
		// TODO Auto-generated constructor stub
	}
	
	public static final PFCharType  Default=new PFCharType("Default",0);
	public static final PFCharType  Chinese=new PFCharType("Chinese",1);
	public static final PFCharType  English=new PFCharType("English",1<<1);
	public static final PFCharType   Number=new PFCharType("Number",1<<2);
	public static final PFCharType FullChar=new PFCharType("FullChar",1<<3);
	public static final PFCharType HalfChar=new PFCharType("HalfChar",1<<4);
}