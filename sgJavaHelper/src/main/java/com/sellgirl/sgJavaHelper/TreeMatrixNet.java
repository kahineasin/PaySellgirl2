package com.sellgirl.sgJavaHelper;

//public enum TreeMatrixNet {
//
//    Up ,//= 1,
//    Right ,//= 2,
//    Down ,//= 4,
//    Left// = 8
//}
public final class TreeMatrixNet extends PFEnumClass{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6120947144798859653L;
	protected TreeMatrixNet() {
		super();
	}
	protected TreeMatrixNet(String text, int value) {
		super(text, value);
	}
	
	//public static final FuncAuthorityClass Up=new FuncAuthorityClass("Up",0);
	public static final TreeMatrixNet Up=new TreeMatrixNet("Up",1);
	public static final TreeMatrixNet Right=new TreeMatrixNet("Right",1<<1);
	public static final TreeMatrixNet Down=new TreeMatrixNet("Down",1<<2);
	public static final TreeMatrixNet Left=new TreeMatrixNet("Left",1<<3);
	public static final TreeMatrixNet None=new TreeMatrixNet("None",1<<4);
}                  
