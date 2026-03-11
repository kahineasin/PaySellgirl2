//package pf.java.pfHelper.express;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import pf.java.pfHelper.SGFunc;
//import pf.java.pfHelper.PFRef;
//
///**
// * 运算
// * @author Administrator
// *
// */
//public class PFFormula {
//	/**
//	 * 运算的格式
//	 */
//	public List<PFFormulaItem>  format;
//	/**
//	 * 运算的函数
//	 */
//	public SGFunc<Object,Object,Object,Object> formula=null;
//	/**
//	 * 运算的优先级(如乘法高于加法)
//	 */
//	public int order=100;
////	public PFFormula(List<PFFormulaItem> format, SGFunc<Object, Object, Object, Object> formula) {
////		super();
////		this.format = format;
////		this.formula = formula;
////	}
//	public PFFormula(List<PFFormulaItem> format, SGFunc<Object, Object, Object, Object> formula,int order) {
//		super();
//		this.format = format;
//		this.formula = formula;
//		this.order = order;
//	}
////	public boolean match(List<PFFormulaItem> t) {
////		if(t.size()) {
////			return t.mark.equals(this.mark);
////		}else if((!t.isMark)&&(!this.isMark)) {
////			return true;
////		}
////		return false;
////	}
//	public boolean matchLeft(List<PFFormulaItem> t) {
//		if(t.size()>=format.size()) {
//			for(int i=0;i<format.size();i++) {
//				if(!t.get(i).match(format.get(i))) {
//					return false;
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//	/**
//	 * 从右边开始匹配
//	 * @param t
//	 * @return
//	 */
//	public boolean match(List<PFFormulaItem> t) {
//		return matchRight(t);
//	}
//	/**
//	 * 从右边开始匹配
//	 * @param t
//	 * @return
//	 */
//	public boolean matchRight(List<PFFormulaItem> t) {
//		int beginIdx=t.size()-format.size();
//		if(t.size()>=format.size()) {
//			for(int i=0;i<format.size();i++) {
////				if(!t.get(i).match(format.get(i))) {
////					return false;
////				}
//				if(!t.get(i+beginIdx).match(format.get(i))) {
//					return false;
//				}
//			}
//			return true;
//		}
//		return false;
//	}
////	/**
////	 * 执行运算(要match的才能运算)
////	 * @param t
////	 * @return
////	 */
////	public Object calculate(SGRef<List<PFFormulaItem>> t) {
////		Object t1=null;
////		Object t2=null;
////		Object t3=null;
////		List<Object> v=new ArrayList<Object>();
////		for(int i=0;i<format.size();i++) {
////			if(!format.get(i).isMark) {
////				//v.add(t.get(i).value);
////				v.add(t.GetValue().get(i).value);
////			}
////		}
////		int cnt=v.size();
////		//t=t.subList(cnt,t.size());
//////		t.SetValue(t.GetValue().subList(cnt+1,t.GetValue().size()+1));
////		//t.SetValue(t.GetValue().subList(cnt+1,t.GetValue().size()+1));
////		t.SetValue(t.GetValue().subList(format.size(),t.GetValue().size()));
////		if(cnt>0) {t1=v.get(0);}
////		if(cnt>1) {t2=v.get(1);}
////		if(cnt>2) {t3=v.get(2);}
////		return formula.go(t1, t2, t3);
////	}
//	/**
//	 * 执行运算(要match的才能运算)(从右边开始匹配)
//	 * @param t
//	 * @return
//	 */
//	public Object calculate(SGRef<List<PFFormulaItem>> t) {
//		int beginIdx=t.GetValue().size()-format.size();
//		Object t1=null;
//		Object t2=null;
//		Object t3=null;
//		List<Object> v=new ArrayList<Object>();
//		for(int i=0;i<format.size();i++) {
//			if(!format.get(i).isMark) {
//				//v.add(t.get(i).value);
//				v.add(t.GetValue().get(i+beginIdx).value);
//			}
//		}
//		int cnt=v.size();
//		//t.SetValue(t.GetValue().subList(format.size(),t.GetValue().size()));//左边匹配
//		t.SetValue(t.GetValue().subList(0,t.GetValue().size()-format.size()));//右边匹配
//		if(cnt>0) {t1=v.get(0);}
//		if(cnt>1) {t2=v.get(1);}
//		if(cnt>2) {t3=v.get(2);}
//		return formula.go(t1, t2, t3);
//	}
//	/**
//	 * 执行运算(要match的才能运算)(从右边开始匹配)
//	 * @param t
//	 * @return
//	 */
//	public Object calculate(List<PFFormulaItem> t) {
//		Object t1=null;
//		Object t2=null;
//		Object t3=null;
//		List<Object> v=new ArrayList<Object>();
//		for(int i=0;i<format.size();i++) {
//			if(!format.get(i).isMark) {
//				//v.add(t.get(i).value);
//				v.add(t.get(i).value);
//			}
//		}
//		int cnt=v.size();
//		//t.SetValue(t.GetValue().subList(format.size(),t.GetValue().size()));//左边匹配
//		//t.SetValue(t.GetValue().subList(0,t.GetValue().size()-format.size()));//右边匹配
//		if(cnt>0) {t1=v.get(0);}
//		if(cnt>1) {t2=v.get(1);}
//		if(cnt>2) {t3=v.get(2);}
//		return formula.go(t1, t2, t3);
//	}
//}
