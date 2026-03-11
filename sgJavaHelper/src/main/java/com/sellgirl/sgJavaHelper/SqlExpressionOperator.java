package com.sellgirl.sgJavaHelper;

//public class SqlExpressionOperator extends PFEnumClass{
//
//	public SqlExpressionOperator(String text, int value) {
//		super(text, value);
//		// TODO Auto-generated constructor stub
//	}
//	
//	public static SqlExpressionOperator Equal=new SqlExpressionOperator("Equal",0);
//	public static SqlExpressionOperator Less=new SqlExpressionOperator("Less",1);
//	public static SqlExpressionOperator Greater=new SqlExpressionOperator("Greater",1<<1);
//	public static SqlExpressionOperator GreaterAndEqual=new SqlExpressionOperator("GreaterAndEqual",1<<2);
//	public static SqlExpressionOperator Like=new SqlExpressionOperator("Like",1<<3);
//	public static SqlExpressionOperator IN=new SqlExpressionOperator("IN",1<<4);
//	public static SqlExpressionOperator NotIn=new SqlExpressionOperator("NotIn",1<<5);
//	public static SqlExpressionOperator Exists=new SqlExpressionOperator("Exists",1<<6);
//	public static SqlExpressionOperator NotExists=new SqlExpressionOperator("NotExists",1<<7);
//	public static SqlExpressionOperator NotEqual=new SqlExpressionOperator("NotEqual",1<<8);
//	public static SqlExpressionOperator StartWith=new SqlExpressionOperator("StartWith",1<<9);
//	public static SqlExpressionOperator EndWith=new SqlExpressionOperator("EndWith",1<<10);
//}
public enum SqlExpressionOperator{

    Equal,
    /// <summary>
    /// 数据库的值小于输入值
    /// </summary>
    Less ,
    LessOrEqual,
    /// <summary>
    /// 数据库的值大于输入值
    /// </summary>
    Greater,
    GreaterOrEqual,
    Like ,
    IN ,
    NotIn,
    //Exists = 8,
    //NotExists = 9,
    NotEqual,
    StartWith ,
    EndWith,
}
