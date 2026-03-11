package com.sellgirl.sgJavaSpringHelper;

import com.sellgirl.sgJavaHelper.FuncAuthorityClass;

/// <summary>
/// 系统按钮
/// </summary>
public class SysBtn
{
	public SysBtn() {}
	public SysBtn(String name,String text,FuncAuthorityClass auth) {
		this(name,text);
		Authority = auth;
	}
	public SysBtn(String name,String text,FuncAuthorityClass auth,Boolean alwayShow) {
		this(name,text, auth);
		AlwayShow=alwayShow;
	}
	public SysBtn(String name,String text) {
		Name = name; Text = text; 
	}
    /// <summary>
    /// 所需权限
    /// </summary>
    public  FuncAuthorityClass Authority ;
    /// <summary>
    /// 名称(英)
    /// </summary>
    public  String Name ;
    /// <summary>
    /// 显示的文字
    /// </summary>
    public  String Text ;
    /// <summary>
    /// 总是显示（包含无数据时）
    /// </summary>
    public  Boolean AlwayShow ;
}