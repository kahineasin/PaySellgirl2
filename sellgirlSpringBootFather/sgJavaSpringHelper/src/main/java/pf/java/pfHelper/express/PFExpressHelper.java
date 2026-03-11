//package pf.java.pfHelper.express;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.tuple.ImmutablePair;
//import org.apache.commons.lang3.tuple.Pair;
//
////import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
//
//import pf.java.pfHelper.PFRef;
//import pf.java.pfHelper.config.PFDataHelper;
//import static pf.java.pfHelper.express.PFFormulaItem.*;
//
//public class PFExpressHelper {
//	/**
//	 * 运算符
//	 */
//	List<String> marks=null;
//	/**
//	 * 运算
//	 */
//	List<PFFormula> formula=null;
////	public static PFFormulaItem mark(String m) {
////		return PFFormulaItem.mark(m);
////	}
////	public static PFFormulaItem variable(Object v) {
////		return PFFormulaItem.variable(v);
////	}
////	public static PFFormulaItem variable() {
////		return PFFormulaItem.variable();
////	}
//	/**
//	 * 内部运算
//	 */
//	protected PFExpressHelper() {
//	}
//
//	/**
//	 * 裙始化,带一些常用函数
//	 * @return
//	 */
//	public static PFExpressHelper popular() {
//		PFExpressHelper r=new PFExpressHelper();
//
//		r.marks=new ArrayList<String>();
//		r.marks.add("+");
//		r.marks.add("-");
//		r.marks.add("*");
//		r.marks.add("(");
//		r.marks.add(")");
//
//		r.formula=new ArrayList<PFFormula>();
//		//加法
//		r.formula.add(
//				new PFFormula(
//						new PFFormulaItemCollection(variable(),mark("+"),variable()) ,
//						(a,b,c)->{
//							if((a!=null&&a.toString().charAt(0)=='"')
//								||(b!=null&&b.toString().charAt(0)=='"')	) {//字符拼接方式(由于暂时只有加法用得上双引号,所以先不把双引号的判断放到PFFormulaItem的方法里)
//								String as="";
//								if(a!=null&&a.toString().charAt(0)=='"') {
//									as=a.toString().substring(1,a.toString().length()-1);
//								}else if (a!=null){
//									as=a.toString();
//								}
//								String bs="";
//								if(b!=null&&b.toString().charAt(0)=='"') {
//									bs=b.toString().substring(1,b.toString().length()-1);
//								}else if (b!=null){
//									bs=b.toString();
//								}
//								//return as+bs;
//								return "\""+as+bs+"\"";
//							}else {//int计算方式
//								return PFDataHelper.ObjectToInt(a)+PFDataHelper.ObjectToInt(b);
//							}
//							},100
//						)
//				);
//		//减法
//		r.formula.add(
//				new PFFormula(
//						new PFFormulaItemCollection(variable(),mark("-"),variable()) ,
//						(a,b,c)->PFDataHelper.ObjectToInt(a)-PFDataHelper.ObjectToInt(b),100
//						)
//				);
//		//乘法
//		r.formula.add(
//				new PFFormula(
//						new PFFormulaItemCollection(variable(),mark("*"),variable()) ,
//						(a,b,c)->PFDataHelper.ObjectToInt(a)*PFDataHelper.ObjectToInt(b),50
//						)
//				);
//
//		//挌号
//		r.formula.add(
//				new PFFormula(
//						new PFFormulaItemCollection(mark("("),variable(),mark(")")) ,
//						(a,b,c)->PFDataHelper.ObjectToInt(a),100
//						)
//				);
//		return r;
//	}
//	
//	/**
//	 * 加自定义运算
//	 * @param marks
//	 * @param formula
//	 */
//	public void addCustomFormula(PFFormula formula) {
//		for(PFFormulaItem i : formula.format) {
//			if(i.isMark&&(!marks.contains(i.mark))) {
//				marks.add(i.mark);
//			}
//		}
//		this.formula.add(formula);
//	}
//	public int findFirstMark(String s,SGRef<String> mark) {
//		int lastIdx=-1;
//		String r=null;
//		for(String i : marks) {
//			int idx=s.indexOf(i);
//			if(idx>-1) {
//				int quotaIdx=s.indexOf("\"");
//				if(quotaIdx>-1&&quotaIdx<idx) {
//					if(s.substring(quotaIdx).indexOf("\"")>-1) {
//						
//					}else {
//						//左边只有一个双引号时不匹配mark
//						continue;
//					}
//				}
//				if(lastIdx==-1||lastIdx>idx) {
//					r=i;
//					lastIdx=idx;
//				}
//			}
//		}
//		if(lastIdx>-1) {
//			mark.SetValue(r);
//		}
//		return lastIdx;
//	}
//	//public int findBestFormula(SGRef<List<PFFormulaItem>> formula,SGRef<Integer> idx,SGRef<PFFormula> mark) {
//	//public Pair<Integer,Integer> findBestFormula(List<PFFormulaItem> formulaItem,Integer idx,SGRef<PFFormula> formulaRef) {
//	public PFFormula findBestFormula(List<PFFormulaItem> formulaItem,Integer idx,SGRef<Pair<Integer,Integer>> formulaPosRef) {
////		if(idx>formulaItem.size()-1) {
////			return null;
////		}
//		int lastOrder=-1;
//		PFFormula r=null;
//		Pair<Integer,Integer> formulaPos=null;
////		List<PFFormulaItem> left=formulaItem.subList(0,idx);
//		List<PFFormulaItem> left=formulaItem.subList(0,idx+1);
//		List<PFFormulaItem> right=formulaItem.subList(idx,formulaItem.size());//第idx个字符应同时存在于left和right
//		boolean bestIsRight=false;
//		//List<PFFormulaItem> right=formulaItem.subList(idx+1,formulaItem.size());
////		//这样有问题,有可能先匹配了右边的公式
////		for(PFFormula i :formula) {
////			if(i.matchRight(left)) {//匹配到运算
////				if(lastOrder==-1||lastOrder>i.order) {
////					r=i;
////					lastOrder=i.order;
////					formulaPos=ImmutablePair.of(idx-i.format.size()+1, idx);
////				}
////			}
////			if(i.matchLeft(right)) {//匹配到运算
////				if(lastOrder==-1||lastOrder>i.order) {
////					r=i;
////					lastOrder=i.order;
////					formulaPos=ImmutablePair.of(idx,idx+i.format.size()-1);
////				}
////			}
////		}
//		//先找左边的公式
//		for(PFFormula i :formula) {
//			if(i.matchRight(left)) {//匹配到运算
//				if(lastOrder==-1||lastOrder>i.order) {
//					r=i;
//					lastOrder=i.order;
//					formulaPos=ImmutablePair.of(idx-i.format.size()+1, idx);
//				}
//			}
//		}
//		//往右找公式
//		for(PFFormula i :formula) {
//			if(i.matchLeft(right)) {//匹配到运算
//				if(lastOrder==-1||lastOrder>i.order) {
//					r=i;
//					lastOrder=i.order;
//					formulaPos=ImmutablePair.of(idx,idx+i.format.size()-1);
//					bestIsRight=true;
//				}
//			}
//		}
//		//从最优公式的末位开始往右找公式
//		if(bestIsRight&&lastOrder>-1) {
////			List<PFFormulaItem> bRight=formulaItem.subList(formulaPos.getRight(),formulaItem.size());//第idx个字符应同时存在于left和right
////			for(PFFormula i :formula) {
////				if(i.matchLeft(bRight)) {//匹配到运算
////					if(lastOrder>i.order) {
////						r=i;
////						lastOrder=i.order;
////						formulaPos=ImmutablePair.of(formulaPos.getRight(),formulaPos.getRight()+i.format.size()-1);
////					}
////				}
////			}
////			//严紧一点的话,当最优公式在idx右边时,要从left+1开始往右找公式使用到的每一位(找1次不够)
////			for(int i=formulaPos.getLeft()+1;i<=formulaPos.getRight();i++) {
////				List<PFFormulaItem> bRight=formulaItem.subList(i,formulaItem.size());//第idx个字符应同时存在于left和right
////				for(PFFormula j :formula) {
////					if(j.matchLeft(bRight)) {//匹配到运算
////						if(lastOrder>j.order) {
////							r=j;
////							lastOrder=j.order;
////							formulaPos=ImmutablePair.of(formulaPos.getRight(),formulaPos.getRight()+j.format.size()-1);
////						}
////					}
////				}
////			}
//			//严紧一点的话,当最优公式在idx右边时,要从left+1开始往右找公式使用到的每一位
//			boolean rightHasBest=true;
//			while(rightHasBest) {
//				rightHasBest=false;
//				for(int i=formulaPos.getLeft()+1;i<=formulaPos.getRight();i++) {
//					List<PFFormulaItem> bRight=formulaItem.subList(i,formulaItem.size());//第idx个字符应同时存在于left和right
//					for(PFFormula j :formula) {
//						if(j.matchLeft(bRight)) {//匹配到运算
//							if(lastOrder>j.order) {
//								r=j;
//								lastOrder=j.order;
//								formulaPos=ImmutablePair.of(formulaPos.getRight(),formulaPos.getRight()+j.format.size()-1);
//								rightHasBest=true;
//							}
//						}
//					}
//				}
//			}
//		}
//		//往右找公式时之后,
//		if(lastOrder>-1) {
//			//formulaRef.SetValue(r);
//			formulaPosRef.SetValue(formulaPos);
//		}
//		return r;
//	}
//	/**
//	 * 运算表达式
//	 * @param expression
//	 * @return
//	 */
//	public Object eval(String expression) {
//		//第1步 按mark分隔字符串
//		List<PFFormulaItem> r=new ArrayList<PFFormulaItem>();
//        String tmpExpress=expression;
//		while(tmpExpress.length()>0) {
//			boolean hasMark=false;
//
//			SGRef<String> fMark=new SGRef<String>();
//			int fMarkIdx=findFirstMark(tmpExpress,fMark);
//
//			hasMark=fMarkIdx>-1;
//			if(hasMark) {
//				if(fMarkIdx>0) {//符号左边有变量
//					r.add(variable(tmpExpress.substring(0,fMarkIdx)));
//				}
//				r.add(mark(fMark.GetValue()));
//				//tmpExpress=tmpExpress.substring(fMarkIdx+1);
//				tmpExpress=tmpExpress.substring(fMarkIdx+fMark.GetValue().length());
//			}
//			if(!hasMark) {
//				//r.add(tmpExpress);
//				r.add(variable(tmpExpress));
//				tmpExpress="";
//			}
//
//		}
//
//		System.out.println("");
//		System.out.println(expression);
////		for(PFFormulaItem i :r) {
////			System.out.println(i);
////		}
//		
//		
////		//第二步 匹配公式进行运算(这种方式不能计算优先级)
////		SGRef<List<PFFormulaItem>> rr=new SGRef<List<PFFormulaItem>> (r);
////		SGRef<List<PFFormulaItem>> rrr=new SGRef<List<PFFormulaItem>> (new ArrayList<PFFormulaItem>());
////		while(rr.GetValue().size()>0||rrr.GetValue().size()>1) {
////			boolean hasMatch=false;
////			for(PFFormula i :formula) {
////				if(i.match(rrr.GetValue())) {//匹配到运算
////					PFFormulaItem n=variable(i.calculate(rrr));
////					rrr.GetValue().add(n);
////					hasMatch=true;
////				}
////			}
////			if(!hasMatch) {//如果没有匹配到,把rr的前项放到rrr后面
////				rrr.GetValue().add(rr.GetValue().get(0));
////				rr.GetValue().remove(0);
////			}
////		}
//
//		//第二步 匹配公式进行运算
//		SGRef<List<PFFormulaItem>> rr=new SGRef<List<PFFormulaItem>> (r);
//		//SGRef<List<PFFormulaItem>> rrr=new SGRef<List<PFFormulaItem>> (new ArrayList<PFFormulaItem>());
//		SGRef<Integer> curIdx=new SGRef<Integer>(0); 
//		while(rr.GetValue().size()>0||rr.GetValue().size()>1) {
//			boolean hasMatch=false;
//
//			if(curIdx.GetValue()>=rr.GetValue().size()) {
//				break;
//			}
////			//(这种方式不能计算优先级)
////			for(PFFormula i :formula) {
////				if(i.match(rrr.GetValue())) {//匹配到运算
////					PFFormulaItem n=variable(i.calculate(rrr));
////					rrr.GetValue().add(n);
////					hasMatch=true;
////				}
////			}
//			
//			SGRef<Pair<Integer,Integer>> formulaPos=new SGRef<Pair<Integer,Integer>>();
//			PFFormula bestFormula=findBestFormula(rr.GetValue(),curIdx.GetValue(),formulaPos);
//			hasMatch=bestFormula!=null;
//			
//			if(hasMatch) {
//				//PFFormulaItem n=variable(bestFormula.calculate(rr.GetValue().subList(formulaPos.GetValue().getLeft(),formulaPos.GetValue().getRight())));
//				PFFormulaItem n=variable(bestFormula.calculate(rr.GetValue().subList(formulaPos.GetValue().getLeft(),formulaPos.GetValue().getRight()+1)));
//				
//				
////				List<PFFormulaItem> left=rr.GetValue().subList(0, formulaPos.GetValue().getLeft());
////				//List<PFFormulaItem> right=rr.GetValue().subList(formulaPos.GetValue().getRight(),rr.GetValue().size());
////				List<PFFormulaItem> right=rr.GetValue().subList(formulaPos.GetValue().getRight()+1,rr.GetValue().size());				
////				//把公式换
////				rr.SetValue(left);
////				rr.GetValue().add(n);
////				rr.GetValue().addAll(right);//这样会报错:list addAll java.lang.reflect.InvocationTargetException
//				
//				removeElements(rr.GetValue(),formulaPos.GetValue().getLeft(),formulaPos.GetValue().getRight());
//				rr.GetValue().add(formulaPos.GetValue().getLeft(), n);
//				
//				//匹配完之后,要把指针放到left[如:2+(3-1) 时,当(3-1)运算完,指针为4,但还未计算2+2这步]
//				if(curIdx.GetValue()>formulaPos.GetValue().getLeft()) {
//					curIdx.SetValue(formulaPos.GetValue().getLeft());
//				}
//			}
//			
//			if(!hasMatch) {//如果没有匹配到,把rr的前项放到rrr后面
////				rrr.GetValue().add(rr.GetValue().get(0));
////				rr.GetValue().remove(0);
//				curIdx.SetValue(curIdx.GetValue()+1);
//			}
//		}
//		//return rrr.GetValue().get(0);
//		//return rrr.GetValue().get(0).value;
//		return rr.GetValue().get(0).value;
//	}
//	public void removeElements(List list,int start,int end){
//
//if(list!=null&&list.size()>0){
//
//for(int i=end;i>=start;i--){
//
//Object o = list.get(i);
//
//if(o!=null){
//
//list.remove(i);
//
//}
//
//}
//
//}
//
//}
//}
