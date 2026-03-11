package com.sellgirl.sgJavaHelper.express;

import java.util.ArrayList;
import java.util.List;

import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.SGRef;

/**
 * 运算
 * @author Administrator
 *
 */
public class PFFormula {
	/**
	 * 运算的格式
	 */
	public List<PFFormulaItem>  format;
	/**
	 * 运算的函数
	 */
	public SGFunc<Object,Object,Object,Object> formula=null;
	/**
	 * 运算的优先级(如乘法高于加法)(越小优先级越高)
	 */
	public int order=100;
//	public PFFormula(List<PFFormulaItem> format, PFFunc<Object, Object, Object, Object> formula) {
//		super();
//		this.format = format;
//		this.formula = formula;
//	}
	public PFFormula(List<PFFormulaItem> format, SGFunc<Object, Object, Object, Object> formula,int order) {
		super();
		this.format = format;
		this.formula = formula;
		this.order = order;
	}
//	public boolean match(List<PFFormulaItem> t) {
//		if(t.size()) {
//			return t.mark.equals(this.mark);
//		}else if((!t.isMark)&&(!this.isMark)) {
//			return true;
//		}
//		return false;
//	}
	public boolean matchLeftOld(List<PFFormulaItem> t) {
		if(t.size()>=format.size()) {
			for(int i=0;i<format.size();i++) {
				if(!t.get(i).match(format.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public boolean matchLeft(List<PFFormulaItem> t,SGRef<Integer> length) {
		//return doMatchAt(t,0);
		if(t.size()>=format.size()) {
			int i2=0;//t的遍历索引,原本t和format的i是对应一项项比的,但为了适应多参数的情况,改一下
			//boolean isEven=false;
			for(int i=0;i<format.size();) {
//				if(format.get(i).isMultiVariable&&!t.get(i).isMark) {//格式是多变量 且 第i项是变量
//					boolean isEven=true;
//					for(int j=i+1;j<format.size();j++) {
//						if(t.get(j).match(format.get(i+1))) {//是结束符时
//							return true;
//						}else if()
//						isEven=!isEven;
//					}
//				}
				PFFormulaItem ti=t.get(i2);
				if(format.get(i).isMultiVariable) {//格式是多变量 且 第i项是变量
					if((!ti.isMark)
							||(ti.isMark&&",".equals(ti.mark))) {//多变量时,如果第i2项是变量或者逗号都继续
						i2++;
						continue;
					}else if(ti.isMark) {//多变量时,如果第i2项是符号时,回到正常的比较方式
						i++;
						continue;
					}
					//isEven=true;					
				}
//				else {
//					//isEven=false;
//				}
				if(!t.get(i2).match(format.get(i))) {
					return false;
				}
				i++;
				i2++;
			}
		    length.SetValue(i2);
			return true;
		}
		return false;
	}
	/**
	 * 从右边开始匹配
	 * @param t
	 * @return
	 */
	public boolean match(List<PFFormulaItem> t,SGRef<Integer> length) {
		return matchRight(t,length);
	}
//	/**
//	 * 从右边开始匹配
//	 * @param t
//	 * @return
//	 */
//	public boolean matchRightOld(List<PFFormulaItem> t) {
//		int beginIdx=t.size()-format.size();//和matchLeft的区别只是多了beginIndex
//		if(t.size()>=format.size()) {
//			for(int i=0;i<format.size();i++) {
//				if(!t.get(i+beginIdx).match(format.get(i))) {
//					return false;
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//	/**
//	 * @deprecated
//	 * @param t
//	 * @param beginIdx
//	 * @return
//	 */
//	private boolean doMatchAt(List<PFFormulaItem> t,int beginIdx) {
//		//int beginIdx=t.size()-format.size();
//		if(t.size()>=format.size()) {
//			int i2=0;//t的遍历索引,原本t和format的i是对应一项项比的,但为了适应多参数的情况,改一下
//			for(int i=0;i<format.size()
//					//&&i2+beginIdx<t.size()//不能在这里限制,否则匹配不到变成true了
//					;) {
////				if(!t.get(i).match(format.get(i))) {
////					return false;
////				}
//				if(i2+beginIdx>=t.size()) {
//					return false;
//				}
//				PFFormulaItem ti=t.get(i2+beginIdx);
//				if(format.get(i).isMultiVariable) {//格式是多变量 且 第i项是变量
//					if((!ti.isMark)
//							||(ti.isMark&&",".equals(ti.mark))) {//多变量时,如果第i2项是变量或者逗号都继续
//						i2++;
//						continue;
//					}else if(ti.isMark) {//多变量时,如果第i2项是符号时,回到正常的比较方式
//						i++;
//						continue;
//					}
//					//isEven=true;					
//				}
//				if(!ti.match(format.get(i))) {
//					return false;
//				}
//				i++;
//				i2++;
//			}
//			return true;
//		}
//		return false;
//	}
	/**
	 * 从右边开始匹配
	 * @param t
	 * @return
	 */
	public boolean matchRight(List<PFFormulaItem> t,SGRef<Integer> length) {
//		int beginIdx=t.size()-format.size();
//		return doMatchAt(t,beginIdx);
		
//		//当参数个数固定时,才能用beginIdx转换为和matchLeft相同的方式计算,但如果有multi参数时,实际表达式长度比format更长,这样就匹配不到了
//		int beginIdx=t.size()-format.size();
//		if(t.size()>=format.size()) {
//			int i2=0;//t的遍历索引,原本t和format的i是对应一项项比的,但为了适应多参数的情况,改一下
//			for(int i=0;i<format.size()
//					//&&i2+beginIdx<t.size()//不能在这里限制,否则匹配不到变成true了
//					;) {
////				if(!t.get(i).match(format.get(i))) {
////					return false;
////				}
//				if(i2+beginIdx>=t.size()) {
//					return false;
//				}
//				PFFormulaItem ti=t.get(i2+beginIdx);
//				if(format.get(i).isMultiVariable) {//格式是多变量 且 第i项是变量
//					if((!ti.isMark)
//							||(ti.isMark&&",".equals(ti.mark))) {//多变量时,如果第i2项是变量或者逗号都继续
//						i2++;
//						continue;
//					}else if(ti.isMark) {//多变量时,如果第i2项是符号时,回到正常的比较方式
//						i++;
//						continue;
//					}
//					//isEven=true;					
//				}
//				if(!ti.match(format.get(i))) {
//					return false;
//				}
//				i++;
//				i2++;
//			}
//			return true;
//		}
//		return false;

		//int beginIdx=t.size()-format.size();
		if(t.size()>=format.size()) {
			int i2=t.size()-1;//t的遍历索引,原本t和format的i是对应一项项比的,但为了适应多参数的情况,改一下
//			for(int i=0;i<format.size()
					for(int i=format.size()-1;i>=0
					//&&i2+beginIdx<t.size()//不能在这里限制,否则匹配不到变成true了
					;) {

//				if(i2+beginIdx>=t.size()) {
				if(i2<0) {
					return false;
				}
				PFFormulaItem ti=t.get(i2);
				if(format.get(i).isMultiVariable) {//格式是多变量 且 第i项是变量
					if((!ti.isMark)
							||(ti.isMark&&",".equals(ti.mark))) {//多变量时,如果第i2项是变量或者逗号都继续
						i2--;
						continue;
					}else if(ti.isMark) {//多变量时,如果第i2项是符号时,回到正常的比较方式
						i--;
						continue;
					}
					//isEven=true;					
				}
				if(!ti.match(format.get(i))) {
					return false;
				}
				i--;
				i2--;
			}
		    length.SetValue(t.size()-i2-1);
			return true;
		}
		return false;
	}
//	/**
//	 * 执行运算(要match的才能运算)
//	 * @param t
//	 * @return
//	 */
//	public Object calculate(SGRef<List<PFFormulaItem>> t) {
//		Object t1=null;
//		Object t2=null;
//		Object t3=null;
//		List<Object> v=new ArrayList<Object>();
//		for(int i=0;i<format.size();i++) {
//			if(!format.get(i).isMark) {
//				//v.add(t.get(i).value);
//				v.add(t.GetValue().get(i).value);
//			}
//		}
//		int cnt=v.size();
//		//t=t.subList(cnt,t.size());
////		t.SetValue(t.GetValue().subList(cnt+1,t.GetValue().size()+1));
//		//t.SetValue(t.GetValue().subList(cnt+1,t.GetValue().size()+1));
//		t.SetValue(t.GetValue().subList(format.size(),t.GetValue().size()));
//		if(cnt>0) {t1=v.get(0);}
//		if(cnt>1) {t2=v.get(1);}
//		if(cnt>2) {t3=v.get(2);}
//		return formula.go(t1, t2, t3);
//	}
	/**
	 * 执行运算(要match的才能运算)(从右边开始匹配)
	 * @param t
	 * @return
	 */
	public Object calculate(SGRef<List<PFFormulaItem>> t) {
		int beginIdx=t.GetValue().size()-format.size();
		Object t1=null;
		Object t2=null;
		Object t3=null;
		List<Object> v=new ArrayList<Object>();
		for(int i=0;i<format.size();i++) {
			if(!format.get(i).isMark) {
				//v.add(t.get(i).value);
				v.add(t.GetValue().get(i+beginIdx).value);
			}
		}
		int cnt=v.size();
		//t.SetValue(t.GetValue().subList(format.size(),t.GetValue().size()));//左边匹配
		t.SetValue(t.GetValue().subList(0,t.GetValue().size()-format.size()));//右边匹配
		if(cnt>0) {t1=v.get(0);}
		if(cnt>1) {t2=v.get(1);}
		if(cnt>2) {t3=v.get(2);}
		return formula.go(t1, t2, t3);
	}
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
	/**
	 * 执行运算(要match的才能运算)(从右边开始匹配)
	 * 改为支持多参数--benjamin 20210903
	 * @param t
	 * @return
	 */
	public Object calculate(List<PFFormulaItem> t) {
		Object t1=null;
		Object t2=null;
		Object t3=null;
		List<Object> v=new ArrayList<Object>();
		int i2=0;//t的索引
		boolean beginMulti=false;
		List<Object> tmpMultiP=new ArrayList<Object>();
		for(int i=0;i<format.size();) {
			PFFormulaItem ti=t.get(i2);
			if(format.get(i).isMultiVariable) {//格式是多变量 且 第i项是变量
				if(!beginMulti) {
					beginMulti=true;
					//tmpMultiP.clear();
					tmpMultiP=new ArrayList<Object>();
				}
				if((!ti.isMark)
						||(ti.isMark&&",".equals(ti.mark))) {//多变量时,如果第i2项是变量或者逗号都继续
					if(!ti.isMark) {
						tmpMultiP.add(ti.value);
					}
					i2++;
					continue;
				}else if(ti.isMark) {//多变量时,如果第i2项是符号时,回到正常的比较方式
					v.add(tmpMultiP);
					beginMulti=false;
					i++;
					continue;
				}			
			}
			if(!format.get(i).isMark) {
				v.add(ti.value);
			}
			i++;
			i2++;
		}
		int cnt=v.size();
		//t.SetValue(t.GetValue().subList(format.size(),t.GetValue().size()));//左边匹配
		//t.SetValue(t.GetValue().subList(0,t.GetValue().size()-format.size()));//右边匹配
		if(cnt>0) {t1=v.get(0);}
		if(cnt>1) {t2=v.get(1);}
		if(cnt>2) {t3=v.get(2);}
		return formula.go(t1, t2, t3);
	}
}
