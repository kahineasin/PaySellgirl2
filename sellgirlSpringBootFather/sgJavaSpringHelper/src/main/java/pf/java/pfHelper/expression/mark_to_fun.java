//package pf.java.pfHelper.expression;
//
//import java.util.Vector;
//
//import pf.java.pfHelper.SGFunc;
//import pf.java.pfHelper.PFRef;
//
//public class mark_to_fun<T> {
//
//	T and(SGRef<String> l,SGRef<String> r){
//		return PFExpressionHelper.<T>s_eval(l)&s_eval<T>(r);
//		}//测试,在外部定义此函数会导致match()调用此函数时报错:无法解析的外部符号
//	T or(const string& l,const string&r){return s_eval<T>(l)|s_eval<T>(r);}//测试
//	T not(const string& l,const string&r){return ~s_eval<T>(r);}//测试
////	void test(){};
////
////	typedef T (mark_to_fun<T>::*fun)(const string&,const string&);
////	static fun menu[];
//	static SGFunc[] menu=new SGFunc[]{&mark_to_fun<T>::and,//and是函数却不用加(),因为这是用成员函数的地址给成员函数指针定义的方法,p657
//			&mark_to_fun<T>::or,
//			&mark_to_fun<T>::not,		
//		};
//	static String[] mark_name=new String[] {"&",
//			"|",
//			"~"};
//	static String[] mark_two_name=new String[] {"&","|"};//两个操作数的符号,如 a&b a|b
////	static char* mark_right_name[1];//右操作数的符号,如 ~a
//	static String[] mark_right_name=new String[] {"~"};//两个操作数的符号,如 a&b a|b
//	
//	/**
//	 *  匹配函数
//	 * @return
//	 */	
//	T match(SGRef<String> s,SGRef<String> l,SGRef<String> r,SGRef<String> mark)
//	{
//		//for(int i=0;i!=(sizeof(mark_name)/sizeof(*mark_name));++i)//使用模板提示:非法sizeof操作数
//		for(int i=0;i!=mark_to_fun.mark_name.length;++i)//使用模板提示:非法sizeof操作数
//			//for(int i = 0; i < strlen(mark_name); ++i)  //MenuName成员一定要在这里前面定义,否则sizeof是会报错:非法的sizeof操作数
//				//for(int i=0;i!=2;++i)
//		{
//			if(strcmp(mark.data(),mark_name[i])==0)
//			{
//				/*			(this->*menu[i])();*/
//				return (this->*menu[i])(l,r);//方法2.这个方法使用MenuName代替了enum
//				//and(l,r);
//				//test();
//				//break;
//			}
//		}
//		return T(s);
//	}
////	////static string mark[5];//数组方法未实现,注意:在.cc文件中定义初始化时前面要加类型
//	//static vector<string> mark;//p400
//	static Vector<String> mark=new Vector<String>() {{
//		add("&");add("|");
//		}};//p400
//}
