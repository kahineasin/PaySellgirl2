package com.sellgirl.sellgirlPayWeb.user;

import com.sellgirl.sgJavaHelper.model.SystemUser;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaMvcHelper.PFBaseWebController;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

//import com.sellgirl.sgJavaSpringHelper.FuncAuthority;
import com.sellgirl.sgJavaHelper.FuncAuthorityClass;
//import com.sellgirl.sgJavaSpringHelper.HtmlHelper;
//import com.sellgirl.sgJavaSpringHelper.HtmlHelperT;
//import com.sellgirl.sgJavaSpringHelper.PFBaseWebController;
import com.sellgirl.sgJavaHelper.SGCaching;
//import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * cache中的SystemUser在这处理
 */
public abstract class YJQueryController extends PFBaseWebController {

	//#region Service
//    private UserService __userService;
//    protected UserService _userService { get { return __userService ?? (__userService = new UserService()); } }//UserService最好别在构造函数里初始化,因为UserDAL里初始化时就已经open了sql的连接了 
    //#endregion
    
    //#region Session
    //public TokenInfo Token
    //{
    //    get
    //    {
    //        return Session["_tokeninfo"] == null ? null : (TokenInfo)Session["_tokeninfo"];
    //    }
    //    set
    //    {
    //        Session["_tokeninfo"] = value;
    //    }
    //}
    //#endregion

	public String GetUserId() {
		return FormsAuth.GetUserData().UserCode;		
	}
	public long GetUserLongId() {
		return SGDataHelper.ObjectToLong0(FormsAuth.GetUserData().UserCode) ;		
	}
	public String GetUserName() {
		return FormsAuth.GetUserData().UserName;		
	}
//    public String UserId
//    {
//        get
//        {
//            return FormsAuth.GetUserData().UserCode;
//        }
//    }
//    public String UserName
//    {
//        get
//        {
//            return FormsAuth.GetUserData().UserName;
//        }
//    }
	
    //#region 用户信息Cache
//	private boolean IsLogined() {//在拦截器中处理更好?
//		return FormsAuth.IsLogined();
//	}
	public  SystemUser GetSystemUser() {
		SystemUser user=FormsAuth.GetUserExData(SystemUser.class);
		
//		//benjamin todo
//		user.OrgList=new ArrayList<UserOrg>();
//		UserOrg org=new UserOrg();
//		org.Org="00099";
//		org.OrgName="电脑部";
//		user.OrgList.add(org);
//		
//		org=new UserOrg();
//		org.Org="00098";
//		org.OrgName="设备部";
//		user.OrgList.add(org);
//		
//		user.Org="00098";
		return user;
	}
	public  void SetSystemUser(SystemUser systemUser) {
      if (!SGDataHelper.StringIsNullOrWhiteSpace(GetUserId())) {
          SGCaching.Set(GetUserId(), systemUser); 
          }
	}
//    /// <summary>
//    /// 想对成员赋值时应该这样：
//    /// var user = SystemUser??new Models.SystemUser();
//    /// user.xx=xx;
//    /// SystemUser=user;
//    /// </summary>
//    public SystemUser SystemUser
//    {
//        get
//        {
//
//
//            return FormsAuth.GetUserExData<SystemUser>();
//        }
//        set
//        {
//            if (!String.IsNullOrWhiteSpace(UserId)) {
//
//                Caching.Set(UserId, value, null); }
//        }
//    }
	  public String GetSf() {
		  SystemUser SystemUser=GetSystemUser(); 
		  return SystemUser == null ? null : SystemUser.Sf;
	  }
	  public String GetSfName() {
		  SystemUser SystemUser=GetSystemUser(); 
		  return SystemUser == null ? null : SystemUser.SfName;
	  }
	  public String GetFgs() {
		  SystemUser SystemUser=GetSystemUser(); 
		  return SystemUser == null ? null : SystemUser.Fgs;
	  }
	  public String GetFgsName() {
		  SystemUser SystemUser=GetSystemUser(); 
		  return SystemUser == null ? null : SystemUser.FgsName;
	  }
	  public String GetOrg() {
		  SystemUser SystemUser=GetSystemUser(); 
		  return SystemUser == null ? null : SystemUser.Org;
	  }
	  public String GetOrgName() {
		  SystemUser SystemUser=GetSystemUser(); 
		  return SystemUser == null ? null : SystemUser.OrgName;
	  }
//    /// <summary>
//    /// 省份编码
//    /// </summary>
//    public String Sf
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.Sf;
//        }
//    }
//    /// <summary>
//    /// 省份名称
//    /// </summary>
//    public String SfName
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.SfName;
//        }
//    }
//    /// <summary>
//    /// 分公司
//    /// </summary>
//    public String Fgs
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.Fgs;
//        }
//    }
//    /// <summary>
//    /// 分公司名称(注意：总公司登入时这属性也是有值的(00099-总公司))
//    /// </summary>
//    public String FgsName
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.FgsName;
//        }
//    }
//    /// <summary>
//    /// 机构
//    /// </summary>
//    public String Org
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.Org;
//        }
//    }
//    /// <summary>
//    /// 机构名称
//    /// </summary>
//    public String OrgName
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.OrgName;
//        }
//    }
	
	//private String _currentYm=null;
    /// <summary>
    /// 当前系统月份(格式如201801)
    /// </summary>
    public String GetCurrentYm() {
    	SystemUser SystemUser=GetSystemUser();
    	return SystemUser == null ? null : SystemUser.CurrentYm;
	}
    public String GetCurrentMonth() {
    	SystemUser SystemUser=GetSystemUser();
    	return SystemUser == null ? null : SystemUser.GetCurrentMonth();
	}
    public String GetMaxMonth() {
    	SystemUser SystemUser=GetSystemUser();
    	return SystemUser == null ? null : SystemUser.MaxMonth;
	}
    public String GetCurrentDatabase() {
    	SystemUser SystemUser=GetSystemUser();
    	return SystemUser == null ? null : SystemUser.CurrentDatabase;
	}
//	public void setCurrentYm(String currentYm) {
//		this._currentYm = currentYm;
//	}
//	public String CurrentYm
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.CurrentYm;
//        }
//    }
//    /// <summary>
//    /// 当前系统月份(格式如2018.01)
//    /// </summary>
//    public String CurrentMonth
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.CurrentMonth;
//        }
//    }
//    /// <summary>
//    /// 最大系统月份
//    /// </summary>
//    public String MaxMonth
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.MaxMonth;
//        }
//    }
//    /// <summary>
//    /// 当前数据库
//    /// </summary>
//    public String CurrentDatabase
//    {
//        get
//        {
//            return SystemUser == null ? null : SystemUser.CurrentDatabase;
//        }
//    }

    public Map<String, FuncAuthorityClass> GetFuncAuthorities() {
    	return FormsAuth.GetFuncAuthorities();
	}
    public void SetFuncAuthorities(Map<String, FuncAuthorityClass> value) {
    	String UserId=GetUserId();
        if (!SGDataHelper.StringIsNullOrWhiteSpace(GetUserId())) {
        	SGCaching.Set(UserId + "_FuncAuthorities", value);
        }
	}

    
    public Map<String, List<String>> GetOtherFuncAuthorities() {
    	return FormsAuth.GetOtherFuncAuthorities();
	}
    public void SetOtherFuncAuthorities(Map<String, List<String>> value) {
    	String UserId=GetUserId();
        if (!SGDataHelper.StringIsNullOrWhiteSpace(UserId))
        {
            SGCaching.Set(UserId + "_OtherFuncAuthorities", value);
        }
	}
//    public Dictionary<String, FuncAuthority> FuncAuthorities//因为FuncAuthority引用关系,不能移去FormsAuth
//    {
//        get
//        {
//            return FormsAuth.GetFuncAuthorities();
//        }
//        set
//        {
//            if (!String.IsNullOrWhiteSpace(UserId)) {
//                Caching.Set(UserId + "_FuncAuthorities", value, null);
//            }
//        }
//    }

//    public Map<String, List<String>> OtherFuncAuthorities//因为FuncAuthority引用关系,不能移去FormsAuth
//    {
//        get
//        {
//            return FormsAuth.GetOtherFuncAuthorities();
//        }
//        set
//        {
//            if (!String.IsNullOrWhiteSpace(UserId))
//            {
//                Caching.Set(UserId + "_OtherFuncAuthorities", value, null);
//            }
//        }
//    }
    public Boolean HasOtherAuthority(String funcNo, String authority)
    {
    	Map<String, List<String>> other = GetOtherFuncAuthorities();
        return other.containsKey(funcNo) && other.get(funcNo).contains(authority);
    }
    public Boolean IsFgs() {    	
    	return !SGDataHelper.StringIsNullOrWhiteSpace(GetSf());
    }
//    /// <summary>
//    /// 是分公司用户
//    /// </summary>
//    public Boolean IsFgs { get { return !String.IsNullOrWhiteSpace(Sf); } }//暂时决定,如果有sf参数就当作分公司角色
    //#endregion

    //#region old-Session

    ///// <summary>
    ///// 省份编码
    ///// </summary>
    //public String Sf
    //{
    //    get
    //    {
    //        return Session["_sf"] == null ? null : Session["_sf"].ToString();
    //    }
    //    set
    //    {
    //        Session["_sf"] = value;
    //    }
    //}
    ///// <summary>
    ///// 省份名称
    ///// </summary>
    //public String SfName
    //{
    //    get
    //    {
    //        return Session["_sfname"] == null ? null : Session["_sfname"].ToString();
    //    }
    //    set
    //    {
    //        Session["_sfname"] = value;
    //    }
    //}
    ///// <summary>
    ///// 分公司
    ///// </summary>
    //public String Fgs
    //{
    //    get
    //    {
    //        return Session["_fgs"] == null ? null : Session["_fgs"].ToString();
    //    }
    //    set
    //    {
    //        Session["_fgs"] = value;
    //    }
    //}
    ///// <summary>
    ///// 分公司名称
    ///// </summary>
    //public String FgsName
    //{
    //    get
    //    {
    //        return Session["_fgsname"] == null ? null : Session["_fgsname"].ToString();
    //    }
    //    set
    //    {
    //        Session["_fgsname"] = value;
    //    }
    //}

    ///// <summary>
    ///// 当前系统月份
    ///// </summary>
    //public String CurrentMonth
    //{
    //    get
    //    {
    //        return Session["_cmonth"] == null ? null : Session["_cmonth"].ToString();
    //    }
    //    set
    //    {
    //        Session["_cmonth"] = value;
    //    }
    //}
    ///// <summary>
    ///// 最大系统月份
    ///// </summary>
    //public String MaxMonth
    //{
    //    get
    //    {
    //        return Session["_maxMonth"] == null ? null : Session["_maxMonth"].ToString();
    //    }
    //    set
    //    {
    //        Session["_maxMonth"] = value;
    //    }
    //}
    ///// <summary>
    ///// 当前数据库
    ///// </summary>
    //public String CurrentDatabase
    //{
    //    get
    //    {
    //        return Session["_currentDatabase"] == null ? null : Session["_currentDatabase"].ToString();
    //    }
    //    set
    //    {
    //        Session["_currentDatabase"] = value;
    //    }
    //} 
    //#endregion

    //#region 业务类

    ///// <summary>
    ///// 报表服务
    ///// </summary>
    //protected IBonusBLL BonusBLL
    //{
    //    get
    //    {
    //        return BLLFactory.CreateInstance<IBonusBLL>("BonusBLL");
    //    }
    //}

    //#endregion

    //#region 返回对象型store

    //#region old把modelConfig移动到StoreColumnCollection之前
    ////jquery datatables不能用作data的特殊字符
    //private List<KeyValuePair<String, String>> _specDataChar = new List<KeyValuePair<String, String>>() {
    //        new KeyValuePair<String, String>(".","_")
    //    ,
    //        new KeyValuePair<String, String>("[","_lz_"),
    //        new KeyValuePair<String, String>("]","_rz_")
    //    };
    //private String EncodeDataChar(String s)
    //{
    //    _specDataChar.ForEach(a =>
    //    {
    //        s = s.Replace(a.Key, a.Value);
    //    });
    //    return s;
    //}
    //private String DecodeDataChar(String s) {
    //    _specDataChar.ForEach(a =>
    //    {
    //        s = s.Replace(a.Value, a.Key);
    //    });
    //    return s;
    //}
    /// <summary>
    /// 返回DataTables的数据源(改为支持树型结构的Header,多表头)
    /// </summary>
    /// <param name="dataTable"></param>
    /// <param name="p"></param>
    /// <param name="header"></param>
    /// <param name="withHeader"></param>
    /// <param name="setWidthByHeaderWord">根据列头的字数修改列宽(如果设为false,前端是优先根据列内容来适应列宽)</param>
    /// <param name="xmlDataSetName">如果header有值,此参数不起作用,请在new StoreColumnCollection(modelConfig)时提供此参数</param>
    /// <returns></returns>
//    private JsonResult PagingStore(DataTable dataTable, MvcPagingParameters p, StoreColumnCollection header = null, bool setWidthByHeaderWord = true, String xmlDataSetName = null)
//    {
//        DataTable dt = null;
//        if (p.ShowAllColumn!=true)
//        {
//            var groupColumn = DoGetGroupColumnCache();
//            dt = groupColumn == null ? dataTable : SGDataHelper.DataTableGroupBy(dataTable, groupColumn.groupCols, groupColumn.valueCols);
//        }else
//        {
//            dt = dataTable;
//        }
//        var r = SGDataHelper.PagingStore(dt, p, header ,setWidthByHeaderWord , xmlDataSetName);
//        if (r != null)
//        {
//            var jsResult = new PFJsonResult();
//            jsResult.Data = r;
//            jsResult.JsonRequestBehavior = JsonRequestBehavior.AllowGet;
//            return jsResult;
//        }
//        return Json(false, JsonRequestBehavior.AllowGet);
//        #region 放到SGDataHelper之前
//        //var sc = new List<KeyValuePair<String, String>>() {//jquery不能用作data的特殊字符
//        //    new KeyValuePair<String, String>(".","_")
//        //};
//        //if (dataTable != null)
//        //{
//        //    if (!p.PageSize.HasValue) { p.PageSize = dataTable.Rows.Count; }
//        //    var all = dataTable;
//        //    var columns = dataTable.Columns;
//        //    bool isPageSql = all.Rows.Count > 0 && all.Columns["rowtotal"] != null;
//        //    int total = 0;
//        //    DataTable dt = null;
//        //    if (isPageSql)
//        //    {
//        //        total = (int)all.Rows[0]["rowtotal"];
//        //        dt = all;
//        //    } else
//        //    {
//        //        if (!String.IsNullOrWhiteSpace(p.Sort))
//        //        {
//        //            //all.DefaultView.Sort = p.Sort.Replace(sc[0].Value, sc[0].Key);
//        //            all.DefaultView.Sort = DecodeDataChar(p.Sort);
//        //            all = all.DefaultView.ToTable();
//        //        }
//        //        if (!String.IsNullOrWhiteSpace(p.FilterValue))
//        //        {
//        //            all = all.DataTableFilter(p.FilterValue);
//        //        }
//        //        total = all.Rows.Count;//一定要过滤完再计算total
//        //        dt =SGDataHelper.DataPager(all,p.PageIndex.Value, p.PageSize.Value);//注意分页后的columns丢了ExtendedProperties
//        //    }
//        //    //int total = all.Rows.Count>0&&all.Rows[0]["rowtotal"]!=null? (int)all.Rows[0]["rowtotal"] : all.Rows.Count;
//        //    if (dt != null)
//        //    {
//        //        ArrayList arrayList = new ArrayList();
//        //        foreach (DataRow dataRow in dt.Rows)
//        //        {
//        //            Dictionary<String, object> dictionary = new Dictionary<String, object>();
//        //            foreach (DataColumn dataColumn in columns)
//        //            {
//        //                ////dictionary.Add(dataColumn.ColumnName.IndexOf(".") > -1 ? dataColumn.ColumnName.Replace(".", "_") : dataColumn.ColumnName
//        //                ////    , dataRow[dataColumn.ColumnName].ToString());
//        //                //dictionary.Add(EncodeDataChar(dataColumn.ColumnName)
//        //                //    , dataRow[dataColumn.ColumnName].ToString());
//        //                dictionary.Add(EncodeDataChar(dataColumn.ColumnName)
//        //                    , dataRow[dataColumn.ColumnName]);
//        //            }
//        //            arrayList.Add(dictionary);
//
//        //        }
//        //        if (header == null)
//        //        {
//        //            header = new StoreColumnCollection();
//        //            //header =xmlDataSetName==null?new StoreColumnCollection(): new StoreColumnCollection(xmlDataSetName);
//        //            //var modelConfig = SGDataHelper.GetMultiModelConfig(xmlDataSetName);
//        //            var modelConfig = xmlDataSetName == null ? null : SGDataHelper.GetMultiModelConfig(xmlDataSetName);
//        //            foreach (DataColumn dataColumn in columns)
//        //            {
//        //                StoreColumn dictionary = modelConfig == null ? new StoreColumn(dataColumn) : new StoreColumn(dataColumn, modelConfig[dataColumn.ColumnName]);
//        //                //if (dataColumn.ColumnName.IndexOf(sc[0].Key) > -1)
//        //                //{
//        //                //    dictionary.title = dataColumn.ColumnName;//前端jqueryDatables不支持.
//        //                //    dictionary.data = dataColumn.ColumnName.Replace(sc[0].Key,sc[0].Value);
//        //                //}
//        //                //else
//        //                //{
//        //                //    dictionary.data = dataColumn.ColumnName;
//        //                //}
//        //                if (_specDataChar.Any(a => dataColumn.ColumnName.IndexOf(a.Key) > -1))
//        //                {
//        //                    dictionary.title = dataColumn.ColumnName;//前端jqueryDatables不支持.
//        //                    dictionary.data = EncodeDataChar(dataColumn.ColumnName);
//        //                }
//        //                else
//        //                {
//        //                    dictionary.data = dataColumn.ColumnName;
//        //                }
//
//        //                //宽度的优先级:setWidthByHeaderWord<modelConfig<ExtendedProperties
//        //                //PFModelConfig config = modelConfig == null ? null : modelConfig[dataColumn.ColumnName];
//        //                //dictionary.SetPropertyByModelConfig(config);
//
//        //                if (dataColumn.ExtendedProperties.ContainsKey("title")) { dictionary.title = dataColumn.ExtendedProperties["title"].ToString(); }//20180803
//        //                if (String.IsNullOrWhiteSpace(dictionary.title)) { dictionary.title = dataColumn.ColumnName; }
//        //                if (dataColumn.ExtendedProperties != null && dataColumn.ExtendedProperties.Contains("width"))
//        //                {
//        //                    dictionary.width = dataColumn.ExtendedProperties["width"].ToString();
//        //                }
//        //                else
//        //                if (setWidthByHeaderWord && String.IsNullOrWhiteSpace(dictionary.width))//设置为中文后进入这里才有意义
//        //                {
//        //                    dictionary.SetWidthByTitleWords();
//
//        //                }
//
//        //                if (dataColumn.ExtendedProperties != null && dataColumn.ExtendedProperties.Contains("dataType"))
//        //                {
//        //                    dictionary.dataType = dataColumn.ExtendedProperties["dataType"].ToString();
//        //                }
//        //                if (dataColumn.ExtendedProperties != null && dataColumn.ExtendedProperties.Contains("visible"))
//        //                {
//        //                    dictionary.visible = bool.Parse(dataColumn.ExtendedProperties["visible"].ToString());
//        //                }
//        //                if (dataColumn.ExtendedProperties != null)
//        //                {
//        //                    if (dataColumn.ExtendedProperties.Contains("summary"))
//        //                    {
//        //                        dictionary.summary = dataColumn.ExtendedProperties["summary"].ToString();
//        //                    }
//        //                    else if (dataColumn.ExtendedProperties.Contains("hasSummary") && bool.Parse(dataColumn.ExtendedProperties["hasSummary"].ToString()) == true)
//        //                    {
//        //                        //dictionary.summary = SGDataHelper.Thousandth(CommonFun.ColumnTotal(all, dictionary.data));//dictionary.data已替换了特殊字符，不准确
//        //                        //dictionary.summary = SGDataHelper.Thousandth(CommonFun.ColumnTotal(all, dataColumn.ColumnName));
//        //                        //dictionary.summary = SGDataHelper.ColumnTotal(all, dataColumn.ColumnName);
//        //                        dictionary.summary = GetColumnSummary(all, dataColumn.ColumnName, dictionary.summaryType);
//        //                    }
//        //                }
//        //                header.Add(dictionary);
//        //            }
//        //        }
//        //        else//head不为null时
//        //        {
//        //            //如果有head的情况下,visible到底以head的为准还是以xml为准?所以xml配置应该在初始化时就尽量加入--wxj20180815
//        //            //var modelConfig = SGDataHelper.GetMultiModelConfig(xmlDataSetName);
//
//        //            var tree = new StoreColumn { Children = header };
//        //            tree.EachLeaf(column =>
//        //            {
//        //                //if (column.data!=null&&column.data.IndexOf(".") > -1)
//        //                //{
//        //                //    column.data=column.data.Replace(".", "_");
//        //                //}
//        //                if (column.data != null && _specDataChar.Any(a => column.data.IndexOf(a.Key) > -1))
//        //                {
//        //                    column.data = EncodeDataChar(column.data);
//        //                }
//
//        //                ////宽度的优先级:setWidthByHeaderWord<modelConfig<column.width
//        //                ////PFModelConfig config = modelConfig == null ? null : modelConfig[column.data];
//        //                ////column.SetPropertyByModelConfig(config);//先设置了中文才能计算中文字符长度
//        //                //if (setWidthByHeaderWord && String.IsNullOrWhiteSpace(column.width))
//        //                //{
//        //                //    column.SetWidthByTitleWords();
//        //                //}
//
//        //                if (column.hasSummary)
//        //                {
//        //                    //column.summary = SGDataHelper.Thousandth(CommonFun.ColumnTotal(all, column.data));
//        //                    column.summary = GetColumnSummary(all, column.data, column.summaryType);
//        //                }
//        //            });
//        //        }
//
//        //        var r = new PagingResult { data = arrayList, columns = header, total = total };
//        //        if (dataTable.ExtendedProperties.ContainsKey("exData"))
//        //        {
//        //            r.exData = dataTable.ExtendedProperties["exData"];
//        //        }
//        //        var jsResult = new PFJsonResult();
//        //        jsResult.Data = r;
//        //        jsResult.JsonRequestBehavior = JsonRequestBehavior.AllowGet;
//        //        return jsResult;
//        //        //return Json(r, JsonRequestBehavior.AllowGet); //返回一个json字符串
//
//        //    }
//        //}
//        //return Json(false, JsonRequestBehavior.AllowGet); 
//        #endregion
//    }
//    //private object GetColumnSummary(DataTable dt, String columnName, SummaryType summaryType) {
//    //    switch (summaryType)
//    //    {
//    //        case SummaryType.Average:
//    //            return dt.Rows.Count < 1 ? 0 : (SGDataHelper.ColumnTotal(dt, columnName) / dt.Rows.Count);
//    //        default:
//    //            return SGDataHelper.ColumnTotal(dt, columnName);
//    //    }
//    //}
//    public JsonResult PagingStore(DataTable dataTable, MvcPagingParameters p, StoreColumnCollection header)
//    {
//        return PagingStore(dataTable, p, header, false, null);
//    }
//    public JsonResult PagingStore(DataTable dataTable, MvcPagingParameters p, bool setWidthByHeaderWord = true, String xmlDataSetName = null)
//    {
//        return PagingStore(dataTable, p, null, setWidthByHeaderWord, xmlDataSetName);
//    }
//    #endregion
//    #endregion
//    #region 返回树型store
//    private JsonResult TreeStore(List<TreeListItem> list, StoreColumnCollection columns = null, String xmlDataSetName = null)
//    {
//        if (list != null && list.Count > 0)//少于1的话，就算显示列头也无意义的
//        {
//            if (columns == null && list != null && list.Count > 0)
//            {
//                //columns = new StoreColumnCollection();
//                columns = String.IsNullOrWhiteSpace(xmlDataSetName) ? new StoreColumnCollection() : new StoreColumnCollection(xmlDataSetName);
//                SGDataHelper.EachObjectProperty(list[0].Data, (i, name, value) => {
//                    columns.Add(name, a => { if (value != null) {
//                            //这样获得类型的风险是:如果某decimal?的列中,第一行为null,就不能获得实际类型
//                            a.dataType = SGDataHelper.GetStringByType(value.GetType());
//                        }
//                    });
//                });
//            }
//            return Json(new PagingResult { data = list, columns = columns }, JsonRequestBehavior.AllowGet); //返回一个json字符串
//
//        }
//        return Json(false, JsonRequestBehavior.AllowGet);
//    }
//    public JsonResult TreeStore(List<TreeListItem> list, StoreColumnCollection columns = null)
//    {
//        return TreeStore(list, columns, null);
//    }
//    public JsonResult TreeStore(List<TreeListItem> list, String xmlDataSetName = null)
//    {
//        return TreeStore(list, null, xmlDataSetName);
//    }
//
//    #endregion
//    #region 返回一行的对象
//    private ActionResult GetModelByDataRow(DataRow row)
//    {
//        DataTable dt = row.Table;
//        Dictionary<String, object> dictionary = new Dictionary<String, object>(); //实例化一个参数集合
//        foreach (DataColumn dataColumn in dt.Columns)
//        {
//            dictionary.Add(dataColumn.ColumnName, row[dataColumn.ColumnName].ToString());
//        }
//        rdto = JsonData.SetSuccess(dictionary);
//        return Json(rdto, JsonRequestBehavior.AllowGet);
//    }
//    public ActionResult ModelResult(DataRow[] rows)
//    {
//        foreach (DataRow row in rows)
//        {
//            return GetModelByDataRow(row);
//        }
//        return Json(false, JsonRequestBehavior.AllowGet);
//    }
//    public ActionResult ModelResult(DataRowCollection rows)
//    {
//        foreach (DataRow row in rows)
//        {
//            return GetModelByDataRow(row);
//        }
//        return Json(false, JsonRequestBehavior.AllowGet);
//    }
//    #endregion
//
//    #region 文件导出
//    protected virtual String DownloadFileName { get; }
    protected  String GetDownloadFileName() {
    	return null;
    }
//    /// <summary>
//    /// 文件导出(暂支持excel)
//    /// </summary>
//    public void Download()
//    {
//        var e1 = Exporter.Instance(this);
//        if (!String.IsNullOrWhiteSpace(DownloadFileName))
//        {
//            e1.FileName(DownloadFileName + "_" + DateTime.Now.ToString("yyyyMMddHHmmss"));
//        }
//        e1.Download();
//    }
//    #endregion 文件导出
//    #region 设置可见列
//    protected virtual bool ShowSetVisibleColumn { get; }//显示设置可见列的按钮
//    /// <summary>
//    /// 设置显示列(可共用)
//    /// </summary>
//    public ActionResult SetVisibleColumnPopups(bool? useLocalCache, CacheType? cacheType)
//    {
//        useLocalCache = useLocalCache ?? false;//默认使用服务端cache
//        ViewData["useLocalCache"] = useLocalCache;
//        ViewData["cacheType"] = cacheType?? CacheType.VisibleColumn;
//        var pagingResult = new ApiData().GetData(this, System.Web.HttpContext.Current,p=>p.ShowAllColumn=true) as PagingResult;
//        return View("~/Areas/Hyzl/Views/Home/SetVisibleColumnPopups.cshtml", pagingResult);
//    }
//    public enum CacheType {
//        VisibleColumn=1,
//        GroupColumn=2
//    }
//    /// <summary>
//    /// 设置显示列
//    /// </summary>
//    public ActionResult SetVisibleColumnCache(String[] column, CacheType? cacheType,String cacheKey=null)
//    {
//        if (!SGDataHelper.StringIsNullOrWhiteSpace(cacheKey)) { SetTableCacheKey(cacheKey); }
//        //var cacheKey = GetTableCachePrev();
//        var prev = GetTableCachePrev(cacheType);
//        if (column==null|| column.Length == 0)
//        {
//            Caching.Remove(prev);
//        }else
//        {
//            Caching.Set(prev, column, null);
//        }
//        return Json(JsonData.SetSuccess(), JsonRequestBehavior.AllowGet);
//    }
//    protected String[] DoGetVisibleColumnCache( CacheType? cacheType=null)
//    {
//        //var cacheKey = GetTableCachePrev();
//        var prev = GetTableCachePrev(cacheType);
//        return Caching.Get(prev) as String[];
//    }
//    public ActionResult GetVisibleColumnCache(CacheType? cacheType = null)
//    {
//        var arr = DoGetVisibleColumnCache(cacheType);
//        var r = arr == null || arr.Length < 1 ?
//            JsonData.SetFault() :
//            JsonData.SetSuccess(arr);
//        return Json(r, JsonRequestBehavior.AllowGet);
//    }
//    #endregion 设置可见列
//
//    #region 设置分组列
//
//    /// <summary>
//    /// 设置显示列(可共用)
//    /// </summary>
//    public ActionResult SetGroupColumnPopups(bool? useLocalCache,String cacheKey)
//    {
//        useLocalCache = useLocalCache ?? false;//默认使用服务端cache
//        ViewData["useLocalCache"] = useLocalCache;
//        ViewData["tableCacheKey"] = cacheKey;
//        var pagingResult = new ApiData().GetData(this, System.Web.HttpContext.Current, p => p.ShowAllColumn = true) as PagingResult;
//        return View("~/Areas/Hyzl/Views/Home/SetGroupColumnPopups.cshtml", pagingResult);
//    }
//
//    //public class GroupValueColumSetting
//    //{
//    //}
//    public class GroupColumSetting
//    {
//        public String[] groupCols { get; set; }
//        public PFKeyValueCollection<SummaryType> valueCols { get; set; }
//    }
//    /// <summary>
//    /// 设置汇总列
//    /// </summary>
//    public ActionResult SetGroupColumnCache(String[] groupCols, PFKeyValueCollection<SummaryType> valueCols, CacheType? cacheType,String cacheKey=null)
//    {
//        if (!SGDataHelper.StringIsNullOrWhiteSpace(cacheKey)) { SetTableCacheKey(cacheKey); }
//        var prev = GetTableCachePrev(CacheType.GroupColumn);
//        if (groupCols == null || groupCols.Length == 0)
//        {
//            Caching.Remove(prev);
//        }
//        else
//        {
//            Caching.Set(prev, new GroupColumSetting { groupCols=groupCols,valueCols=valueCols}, null);
//        }
//        return Json(JsonData.SetSuccess(), JsonRequestBehavior.AllowGet);
//    }
//    protected GroupColumSetting DoGetGroupColumnCache()
//    {
//        var prev = GetTableCachePrev(CacheType.GroupColumn);
//        return Caching.Get(prev) as GroupColumSetting;
//    }
//    public ActionResult GetGroupColumnCache(String cacheKey = null)
//    {
//        if (!SGDataHelper.StringIsNullOrWhiteSpace(cacheKey)) { SetTableCacheKey(cacheKey); }
//        var arr = DoGetGroupColumnCache();
//        var r = arr == null ?
//            JsonData.SetFault() :
//            JsonData.SetSuccess(arr);
//        return Json(r, JsonRequestBehavior.AllowGet);
//    }
//    #endregion
//
//    #region Table缓存
//    private String _tableCacheKey = "";
//    private String GetTableCachePrev(CacheType? cacheType=null)
//    {
//        return "yj_" + (UserId ?? Session.SessionID) + "_" + this.GetType().Name + "_table" + _tableCacheKey
//            +(cacheType==null?"":((int)cacheType).ToString());
//    }
//    /// <summary>
//    /// 查询参数是否有变化
//    /// </summary>
//    /// <param name="table"></param>
//    /// <param name="requestParams"></param>
//    /// <returns></returns>
//    //private bool IsQueryStringChange(DateTime cacheTime, params object[] requestParams)
//    private bool IsQueryStringChange(params object[] requestParams)
//    {
//        var prev = GetTableCachePrev() + "_lqs";
//        var str = "";
//        var current = "";
//        foreach (var p in requestParams)
//        {
//            if (p is List<String>)
//            {
//                current += String.Join(",", p as List<String>) ?? "";
//            } else
//            {
//                current += p ?? "";
//            }
//        }
//        //str = Session[prev]==null?null: Session[prev].ToString();
//        ////if (String.IsNullOrWhiteSpace(str) || str != current)//这样的话，空参数都不会缓存了
//        //if (Session[prev]==null||str != current)
//        //    {
//        //    Session[prev] = current;
//        //    return true;
//        //}
//        str = Caching.Get(prev) as String;
//        //if (String.IsNullOrWhiteSpace(str) || str != current)//这样的话，空参数都不会缓存了
//        if (str == null || str != current)
//        {
//            Caching.Set(prev, current, null);
//            return true;
//        }
//        return false;
//    }
//    protected void SetCacheTable<T>(T table)
//    {
//        var cacheTime = DateTime.Now.AddHours(1);
//        var prev = GetTableCachePrev();
//        //Session[prev] = table;
//        Caching.Set(prev, table, null);
//    }
//    protected void RemoveCacheTable()
//    {
//        var prev = GetTableCachePrev();
//        Caching.Remove(prev);
//        Caching.Remove(prev + "_lqs");//清参数缓存
//    }
//    protected T GetCacheTable<T>()
//    {
//        var prev = GetTableCachePrev();
//        //var result = Session[prev];
//        var result = Caching.Get(prev);
//        if (result == null) { return default(T); }
//        return (T)result;
//    }
//    /// <summary>
//    /// 查询cache的table
//    /// </summary>
//    /// <typeparam name="T"></typeparam>
//    /// <param name="unCacheAction">无cache时</param>
//    /// <param name="cacheAction">有cache时,实际上这个action可以不要的,待删除</param>
//    /// <param name="requestParams">查询参数</param>
//    /// <returns></returns>
//    [Obsolete("This function is obsolete.尽量使用无cacheAction参数的重载", false)]
//    public T GetCacheTable<T>(Func<T> unCacheAction, Action<T> cacheAction, params object[] requestParams)
//    {
//        //var cacheTime = DateTime.Now.AddHours(1);
//        //if (IsQueryStringChange(cacheTime, requestParams))
//        if (IsQueryStringChange(requestParams))
//        {
//            //var table = unCacheAction();//unCacheAction是查询数据库的,当sql报错时,可能下次会查到上次的cache
//            try
//            {
//                var table = unCacheAction();
//                //SetCacheTable(table, cacheTime);
//                SetCacheTable(table);
//                //if (table != null) { SetCacheTable(table, cacheTime); }                    
//                return table;
//            }
//            catch (Exception e)
//            {
//                Session[GetTableCachePrev() + "_lqs"] = "error";
//                SGDataHelper.WriteError(e);
//                return default(T);
//            }
//        }
//        else
//        {
//            var table = GetCacheTable<T>();
//            if (cacheAction != null) { cacheAction(table); }
//            return table;
//        }
//    }
//    public T GetCacheTable<T>(Func<T> unCacheAction, params object[] requestParams)
//    {
//        return GetCacheTable(unCacheAction, null, requestParams);
//    }
//
//    /// <summary>
//    /// 设置缓存key,当一个控制器内有多个table需要缓存时,需要为每个设置不同的key,在调用GetCacheTable之前调用此方法
//    /// </summary>
//    /// <param name="key"></param>
//    public void SetTableCacheKey(String key)
//    {
//        _tableCacheKey = key;
//    }
//    #endregion
//
//    #region 批量查询
//
//    public List<String> Hybhs
//    {
//        get
//        {
//            var c = Caching.Get(UserId + "_hybhs");
//            return c == null ? null : c as List<String>;
//        }
//        set
//        {
//            Caching.Set(UserId + "_hybhs", value, null);
//        }
//    }
//
//    public ActionResult GetHybhByFile()
//    {
//        DoGetHybhByFile();
//        return Json(JsonData.SetSuccess(), JsonRequestBehavior.AllowGet);
//    }
//    protected void DoGetHybhByFile()
//    {
//        var hybhs = new List<String>();
//        if (Request.Files != null && Request.Files.Count > 0)
//        {
//            StreamReader sreader = new System.IO.StreamReader(Request.Files[0].InputStream, System.Text.Encoding.GetEncoding("utf-8"));
//
//            String hybh = String.Empty;
//            String tel = String.Empty;
//            while (!String.IsNullOrEmpty(hybh = sreader.ReadLine()))
//            {
//                hybhs.Add(hybh);
//            }
//            sreader.Close();
//        }
//        if (hybhs.Count > 0 && hybhs[0].IndexOf(",") > -1) {//如果是一行用逗号分隔的方式
//            var newHybhs= new List<String>();
//            hybhs.ForEach(a =>
//            {
//                newHybhs.AddRange(a.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries).ToList());
//            });
//            hybhs = newHybhs;
//            //hybhs = hybhs[0].Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries).ToList();
//        }
//        Hybhs = hybhs;//数据量很大，所以保存到session是最好的，不要每次翻页都传回来
//    }
//    protected void ClearHybhs() {
//        if (Hybhs != null) { Hybhs.Clear(); Hybhs = null; }
//    }
//    [HttpPost]
//    public ActionResult PostClearHybhs()
//    {
//        ClearHybhs();
//        return Json(JsonData.SetSuccess());
//    }
    //#endregion 批量查询
    
    @Override
    public ModelAndView View(String viewName) {
    	ModelAndView result=super.View(viewName);
    	//考虑把C#中的WebViewPage里面的东西写到这里
    	result.addObject("wvp",new WebViewPageT<Object>(this));
    	return result;
    }
    @Override
    public <TModel> ModelAndView View(TModel model,String viewName) {
    	ModelAndView result=super.View(model,viewName);
    	result.addObject("wvp",new WebViewPageT<TModel>(this));
    	return result;
    }
    @Override
    public <TModel> ModelAndView View(TModel model) {
    	ModelAndView result=super.View(model);
    	result.addObject("wvp",new WebViewPageT<TModel>(this));
    	return result;
    }
}
