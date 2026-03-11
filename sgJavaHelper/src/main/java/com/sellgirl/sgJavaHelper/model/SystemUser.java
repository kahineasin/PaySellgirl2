package com.sellgirl.sgJavaHelper.model;

import java.util.List;

import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 系统用户,在cookie中的登陆信息
 * @author Administrator
 *
 */
public class SystemUser {

    public String UserCode;

    public String UserName;
//    public String inviteCode;
    public boolean isInvited=false;
    public int signDay;
    public SGDate lastSign;
    /**
     * 
     */
    public String Pwd;

    public String Sf ;
    public String SfName ;
    public String Fgs ;
    public String FgsName ;    
        
    /// <summary>
    /// 年月:如201801
    /// </summary>
    public String CurrentYm ;

    public String GetCurrentMonth() {
    	return  SGDataHelper.StringIsNullOrWhiteSpace(CurrentYm) 
    			? "" 
    					: SGDataHelper.StringInsert(CurrentYm,4, "."); 
    }
//    /// <summary>
//    /// 年月:如2018.01
//    /// </summary>
//    public String CurrentMonth { get {return  string.IsNullOrWhiteSpace(CurrentYm) ? "" : CurrentYm.Insert(4, "."); } }
    public String MaxMonth ;
    public String CurrentDatabase ;


    //public String EmployeeName
    //{
    //    get;
    //    set;
    //}

    public String Email;

    public String Telephone;

    public String Precinct;

    public String Org;
    public String OrgName;

    /// <summary>
    /// 机构列表，因为权限系统中不能准确得到机构编码和名称，所以登陆后转化后保存下来
    /// 如果是分公司： key 02000 name 广东分公司
    /// 如果非分公司： key OrgNumber name OrgName
    /// </summary>
    //public List<UserOrg> OrgList;
    public List<UserOrg> OrgList;
}
