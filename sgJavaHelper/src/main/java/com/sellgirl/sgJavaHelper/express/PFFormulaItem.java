package com.sellgirl.sgJavaHelper.express;

/**
 * 公式组成部分
 * @author Administrator
 *
 */
public class PFFormulaItem {
	/**
	 * 是运算符
	 */
	protected boolean isMark=false;
	/**
	 * 是多变量,如表达式 and(a,b,c,...)里面的a b c变量一起运算
	 */
	protected boolean isMultiVariable=false;
	protected Object value=null;
	protected String mark=null;
	public static PFFormulaItem mark(String m) {
		PFFormulaItem r=new PFFormulaItem();
		r.isMark=true;
		r.mark=m;
		return r;
	}
	public static PFFormulaItem variable(Object v) {
		PFFormulaItem r=new PFFormulaItem();
		r.isMark=false;
		r.value=v;
		return r;
	}
	public static PFFormulaItem variable() {
		PFFormulaItem r=new PFFormulaItem();
		r.isMark=false;
		return r;
	}
	public static PFFormulaItem multiVariable() {
		PFFormulaItem r=new PFFormulaItem();
		r.isMark=false;
		r.isMultiVariable=true;
		return r;
	}
	public boolean match(PFFormulaItem t) {
		if(t.isMark&&this.isMark) {
			return t.mark.equals(this.mark);
		}else if((!t.isMark)&&(!this.isMark)) {
			return true;
		}
		return false;
	}
	@Override
	public String toString() {
		if(isMark) {return mark;}
		return value.toString();
	}
}
