package com.sellgirl.sgJavaHelper;

public final class FuncAuthorityClass extends PFEnumClass{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9068437471196025822L;
	protected FuncAuthorityClass() {
		super();
	}
	protected FuncAuthorityClass(String text, int value) {
		super(text, value);
		// TODO Auto-generated constructor stub
	}
	
	public static final FuncAuthorityClass Default=new FuncAuthorityClass("Default",0);
	public static final FuncAuthorityClass All=new FuncAuthorityClass("All",1);
	public static final FuncAuthorityClass Add=new FuncAuthorityClass("Add",1<<1);
	public static final FuncAuthorityClass Edit=new FuncAuthorityClass("Edit",1<<2);
	public static final FuncAuthorityClass Delete=new FuncAuthorityClass("Delete",1<<3);
	public static final FuncAuthorityClass Import=new FuncAuthorityClass("Import",1<<4);
	public static final FuncAuthorityClass Export=new FuncAuthorityClass("Export",1<<5);
}                  
