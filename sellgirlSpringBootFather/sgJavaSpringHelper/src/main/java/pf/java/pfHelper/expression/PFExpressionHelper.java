//package pf.java.pfHelper.expression;
//
//import pf.java.pfHelper.*;
//
//public class PFExpressionHelper {
//	//计算右边连续{注不出现"("为连续} 出现)的数量,
//	void s_cout_end_right_brackets(String s,SGRef<Integer> count)//测试通过 ok!
//	{
//		if(count.GetValue()>20){return;}//防止死循环
//		int p_r=s.lastIndexOf(")");
//		int p_l=s.lastIndexOf("(");
//		if((p_r!=-1)&&(p_l==-1||p_r>p_l))
//		{
////			++count;
//			count.SetValue(count.GetValue()+1);
//			//s=s.substr(0,p_r);//前面没有"s="的话,会无限循环,substr(pos,n)这函数的pos是起始的下标,但是n不是结束的下标,而是截取的长度,要注意
//			s=s.substring(0,p_r);//前面没有"s="的话,会无限循环,substr(pos,n)这函数的pos是起始的下标,但是n不是结束的下标,而是截取的长度,要注意
//			//cout<<s<<"\t"<<count<<"\t"<<p_r<<endl;//引用类型的int类型不能自动转换类型然后使用<<,其实没有这问题,是左边的cout错写成count了
//			s_cout_end_right_brackets(s,count);
//		}
//	}
//	//找出右边第n个"("的位置
//	int s_find_end_left_brackets(String s,SGRef<Integer> n)//测试通过 ok!
//	{
//		if(n.GetValue().intValue()==0)
//		{return 0;}//防止死递归
//		int p=s.lastIndexOf("(");
//		if(p!=-1)
//		{
//			//s=s.substr(0,p);
//			s=s.substring(0,p);
////			--n;
//			n.SetValue(n.GetValue()-1);
//			if(n.GetValue().intValue()>0&&s.lastIndexOf("(")!=-1){
//				return s_find_end_left_brackets(s,n);
//			}
//		}
//		return p;
//	}
//	//用于把 a*(b+c) 分割为 a 和 b+c
//	void s_split_brackets(SGRef<String> s,SGRef<String> l,SGRef<String> r,SGRef<String> mark)//test ok,增加 计算mark功能,test ok
//	{
//		SGRef<Integer> count=new SGRef<Integer>(0);
//		SGRef<Integer> n=new SGRef<Integer>(0);
//		int l_p=0;		
//		//if(s.find_last_of(" ")==s.size()-1){s=string_replace(s," ","");}//去掉最右边空格
//		if(s.GetValue().lastIndexOf(" ")==s.GetValue().length()-1){r.SetValue(r.GetValue().replace(" ",""));}//去掉最右边空格
//		s_cout_end_right_brackets(s.GetValue(),count);
//		n.SetValue(count.GetValue().intValue());//没有这句error
//		if(count.GetValue().intValue()==0){return;}
//		//if(count>0)
//		//{
//		l_p=s_find_end_left_brackets(s.GetValue(),n);
//		//}	
//		//l=s.substr(0,l_p);r=s.substr(l_p+1,s.size()-l_p-2);//未考虑"("左边的符号 ,"-2"是除去两边括号的长度
//		l.SetValue(s.GetValue().substring(0,l_p));
//		//r=s.substr(l_p+1,s.GetValue().length()-l_p-2);//未考虑"("左边的符号 ,"-2"是除去两边括号的长度
//		r.SetValue(s.GetValue().substring(l_p+1,s.GetValue().length()-1));//未考虑"("左边的符号 ,"-2"是除去两边括号的长度  --待测试--benjamin todo
//		if(l.GetValue().lastIndexOf(" ")==l.GetValue().length()-1){l.SetValue(l.GetValue().replace(" ",""));}//去掉最右边空格
//		mark.SetValue(l.GetValue().substring(l.GetValue().length()-1, l.GetValue().length()));
//		//l=l.substr(0,l.GetValue().length()-1);//简单起见直接去掉"("左边的符号
//		l.SetValue(l.GetValue().substring(0,l.GetValue().length()-1));//简单起见直接去掉"("左边的符号
//	}
//	//bool splitable(string &s,string &l,string &r,string &mark)//test ok
//	public boolean splitable(SGRef<String> s,SGRef<String> l,SGRef<String> r,SGRef<String> mark)//test ok
//	{
//		//如果右边第一个符号是),要把这对()的内容赋给r,其余的赋给l
////		if(s.find_last_of(" ")==s.size()-1){s=string_replace(s," ","");}//去掉最右边空格
//		if(s.GetValue().lastIndexOf(" ")==s.GetValue().length()-1){r.SetValue(r.GetValue().replace(" ",""));}//去掉最右边空格
//		//if(s.find_last_of(")")==s.size()-1){s_split_brackets(s,l,r,mark);return true;} //brackets()用于分割最右边的括号,s_split_brackets也需要计算mark
//		if(s.GetValue().lastIndexOf(")")==s.GetValue().length()-1){s_split_brackets(s,l,r,mark);return true;} //brackets()用于分割最右边的括号,s_split_brackets也需要计算mark
//		//string::size_type p;
//		int p;
//		//分割2元操作符号 a&b a|b
//
////		for(int i=0;i!=(sizeof(mark_to_fun<T>::mark_two_name)/sizeof(*mark_to_fun<T>::mark_two_name));++i)//使用模板提示:非法sizeof操作数
//		for(int i=0;i!=mark_to_fun.mark_two_name.length;++i)//使用模板提示:非法sizeof操作数
//		{
//			p=s.GetValue().lastIndexOf(mark_to_fun.mark_two_name[i]);//因为顺序从右到左,所以默认右边开始分割			
//			if(p!=-1)
//			{
//				l.SetValue(s.GetValue().substring(0,p));
//				//r=s.substr(p+1,s.size()-1);
//				r.SetValue(s.GetValue().substring(p+1,s.GetValue().length()+p));
//				mark.SetValue(mark_to_fun.mark_two_name[i]);
//				return true;
//			}
//		}
//		//for(int i=0;i!=(sizeof(mark_to_fun<T>::mark_right_name)/sizeof(*mark_to_fun<T>::mark_right_name));++i)//使用模板提示:非法sizeof操作数
//		for(int i=0;i!=mark_to_fun.mark_right_name.length;++i)//使用模板提示:非法sizeof操作数
//		{
//			p=s.GetValue().lastIndexOf(mark_to_fun.mark_right_name[i]);//因为顺序从右到左,所以默认右边开始分割			
//			if(p!=-1)
//			{
//				l.SetValue("");
//				//r=s.substr(p+1,s.GetValue().length()-1);
//				r.SetValue(s.GetValue().substring(p+1,s.GetValue().length()+p));
//				mark.SetValue(mark_to_fun.mark_right_name[i]);
//				return true;
//			}
//		}
//		return false;
//	}
////	public <T extends Query>  s_eval(SGRef<String> s) {
////		SGRef<String> l=new SGRef<String>(""),
////				r=new SGRef<String>(""),
////				mark=new SGRef<String>("");	//l,r用于保存返回值,mark表示分割符
////		//if(splitable(s,l,r,mark))	//splitable()用于分割字符串,未定义
////		if(splitable(s,l,r,mark))	//splitable()用于分割字符串,未定义
////		{
////			//cout<<"string is: "<<s<<"\nl is: "<<l<<"\nr is: "<<r<<"\nmark is: "<<mark<<endl;
////			////改为统一循环多个符号的方法,需要用到函数指针,暂时不做
////			//for(vector<string>::iterator it=mark_to_fun::mark.begin();it!=mark_to_fun::mark.end();++it){
////			//	if(mark==*it){
////			//		
////			//	}
////			//}
////
////			mark_to_fun<T> om;
////			return om.match(s,l,r,mark);
////
////		}
////		else
////		{
////			s.SetValue(s.GetValue().replace(" ",""));//此函数报错,原因不明,string_replace里面之前不是用string::npos来判断所以错
////			return new T(s);}
////		return new T(s);
////		////判断能否继续split,如果不能返回0,如果可以继续递归把结果赋给q,并返回1
////	}
//	public static void TestQuery() {
//		String ex="~me&(me|~help)";
//		
//	}
//}
