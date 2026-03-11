package com.sellgirl.sellgirlPayWeb.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

//import com.sellgirl.sgJavaSpringHelper.FuncAuthority;
import com.sellgirl.sgJavaHelper.FuncAuthorityClass;
//import com.sellgirl.sgJavaSpringHelper.MvcHtmlString;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.SysBtn;
import com.sellgirl.sgJavaHelper.SysBtnCollection;
import com.sellgirl.sgJavaHelper.TreeListItem;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.PFModelConfig;
//import pf.springBoot.springBootMetabaseLogin.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaMvcHelper.MvcHtmlString;
import com.sellgirl.sellgirlPayWeb.shop.model.DefaultMenuIcon;
import com.sellgirl.sellgirlPayWeb.shop.model.WebMenu;

/*
 * 用于替代C#里的WebViewPage基类
 */
public class WebViewPageT<TModel> {

    private YJQueryController controller;
    //public Dictionary<String, FuncAuthority> FuncAuthorities;
    public String UserId ;
    public String UserName;
    /// <summary>
    /// 当前系统月份(格式如201801)
    /// </summary>
    public String CurrentYm;
    /// <summary>
    /// 当前系统月份(格式如2018.01)
    /// </summary>
    public String CurrentMonth ;
    public String Fgs ;
    public String FgsName ;
    public String Org ;
    public FuncAuthorityClass Authorities ;
    public FuncAuthorityClass RequestAuthorities ;
    public FuncAuthorityClass SysAuthorities ;
    /// <summary>
    /// 功能按钮
    /// </summary>
    public com.sellgirl.sgJavaHelper.SysBtnCollection FuncBtns ;
    protected SysBtn AddBtn = new SysBtn (   "Add",  "新增" , FuncAuthorityClass.Add);
    protected SysBtn EditBtn = new SysBtn ( "Edit", "编辑",FuncAuthorityClass.Edit );
    protected SysBtn DeleteBtn = new SysBtn (    "Delete",  "删除" ,FuncAuthorityClass.Delete);
    protected SysBtn ImportBtn = new SysBtn (    "Import",  "导入" ,FuncAuthorityClass.Import,true);
    protected SysBtn ExportBtn = new SysBtn (    "Export",  "导出" ,FuncAuthorityClass.Export);
    protected SysBtn SaveBtn = new SysBtn (    "saveBtn",  "保存" ,FuncAuthorityClass.Edit);
    protected SysBtn BatchBtn = new SysBtn (    "Batch",  "批改" ,FuncAuthorityClass.Edit);
    protected SysBtn BatchAllBtn = new SysBtn (    "BatchAll",  "批改全部" ,FuncAuthorityClass.Edit);
    protected SysBtn SetVisibleColumnBtn = new SysBtn (   "SetVisibleColumn",  "设置可见列" );
    protected SysBtn SetGroupColumnBtn = new SysBtn (  "SetGroupColumn",  "设置汇总列" );
    /// <summary>
    /// 用户类型
    /// </summary>
    public UserTypeClass UserType ;
    /// <summary>
    /// 省份(表示登陆用户的省份(分公司的))(如果是系统本来的参数传递,最好用SfNo这个参数名以区分开)
    /// </summary>
    public String Sf ;
    public String SfName ;

    /// <summary>
    /// 是分公司用户
    /// </summary>
    public Boolean IsFgs() { return UserType == UserTypeClass.Fgs;  }
    /// <summary>
    /// 跨iframe访问系统
    /// </summary>
    public Boolean IsCrossIframeVisit (){  return IsFgs(); } 
    //public override void InitHelpers()
    public WebViewPageT(YJQueryController Controller)
    {
        //base.InitHelpers();
//        controller = (YJQueryController)(this.ViewContext.Controller);
        controller=Controller;
       // FuncBtns = new SysBtnCollection { ExportBtn };//默认有导出功能
        FuncBtns=new com.sellgirl.sgJavaHelper.SysBtnCollection(Arrays.asList(ExportBtn));
//        FuncBtns=new SysBtnCollection(){{
//        	add(ExportBtn);
//        }};
        UserId = controller.GetUserId();
        UserName = controller.GetUserName();
        CurrentYm = controller.GetCurrentYm();
        CurrentMonth = controller.GetCurrentMonth();
        Fgs = controller.GetFgs();
        FgsName = controller.GetFgsName();
        Org = controller.GetOrg();
        //FuncAuthorities = controller.FuncAuthorities;

//        RequestAuthorities = GetRequestAuthority();
//        SysAuthorities = GetSysAuthority();
//        Authorities = RequestAuthorities | SysAuthorities;
        Authorities =  SysAuthorities;
//
//        if ((!String.IsNullOrWhiteSpace(Request["sf"]))
//            //||(!String.IsNullOrWhiteSpace(controller.Fgs))
//            || controller.IsFgs)
//        {
//            UserType = UserType.Fgs;
//            Sf =String.IsNullOrWhiteSpace( Request["sf"])?controller.Sf: Request["sf"];
//            SfName = controller.SfName;
//        }
    }

//    public override void Write(object value)
//    {
//        Output.Write(value);
//    }

    /// <summary>
    /// 设置FieldSets.xml里面的节点名
    /// </summary>
    /// <param name="dataSet"></param>
    public void SetModelConfig( String... dataSet)
    {
    	PFModelConfigCollection m = new PFModelConfigCollection();
        int idx = 0;
        for(String i : dataSet)
        {
            if (idx == 0)
            {
                m = SGDataHelper.GetModelConfig(i, null);
            }else
            {
            	PFModelConfigCollection other= SGDataHelper.GetModelConfig(i, null);
            	Iterator<String> iter = other.keySet().iterator();
            	  while(iter.hasNext()){
            	   String key=iter.next();
            	   PFModelConfig value = other.get(key);

                   if (!m.containsKey(key))
                   {
                       m.put(key, value);
                   }
            	  }
            }
            //m.AddRange(SGDataHelper.GetModelConfig(i, null).Where(a=>!m.Any(b=>b.Equals(a))));
            idx++;
        }
        controller.ViewData.put("modelConfig", m);
    }
//    /// <summary>
//    /// 本来用于接收跨平台传过来的权限,但现在如果使用AutoLogin自动登陆的话,此方法能够删除
//    /// </summary>
//    /// <returns></returns>
//    private FuncAuthorityClass GetRequestAuthority()
//    {
//        String authority = Request["authority"];
//        if (String.IsNullOrWhiteSpace(authority) || authority == "null") { return FuncAuthority.Default; }
//        var t = typeof(FuncAuthority);
//        var names = Enum.GetNames(t);
//        var btns = authority.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
//        FuncAuthority r = FuncAuthority.Default;
//        foreach(var name in names)
//        {
//            if (btns.Contains(name))
//            {
//                r |= (FuncAuthority)Enum.Parse(t, name);
//            }
//        }
//        return r;
//    }
    private FuncAuthorityClass GetSysAuthority()
    {
        if (controller.ViewData.get("authorities") == null) { return FuncAuthorityClass.Default; }
        return (FuncAuthorityClass)controller.ViewData.get("authorities");
    }

    /// <summary>
    /// 获得功能按钮(有权限的)
    /// </summary>
    /// <returns></returns>
    public List<SysBtn> GetButtons()
    {
        return GetButtons(Authorities);
    }
    /// <summary>
    /// 获得功能按钮(有权限的)
    /// (这个重载是非常有必要的,因为可以用于多个子功能分开权限的情况,手动get权限再调用此方法来生成按钮)
    /// (如果不控制权限,用All做参数(不要想着后台无权限拦截时自动当作all,那样和request权限合并后会有问题))
    /// </summary>
    /// <param name="funcAuthority">RequestAuthorities和SysAuthorities取其一</param>
    /// <returns></returns>
    public List<SysBtn> GetButtons(FuncAuthorityClass funcAuthority)
    {
        return GetButtons(FuncBtns, funcAuthority);
        //if (funcAuthority.HasFlag(FuncAuthority.All)) { return FuncBtns; }
        //return FuncBtns.Where(a => funcAuthority.HasFlag(a.Authority)).ToList();
    }
    public List<SysBtn> GetButtons(List<SysBtn> btns,FuncAuthorityClass funcAuthority)
    {
        if (funcAuthority.HasFlag(FuncAuthorityClass.All)) { return btns; }
        return btns.stream().filter(a -> funcAuthority.HasFlag(a.Authority))
                .collect(Collectors.toList());
        //return btns.Where(a => funcAuthority.HasFlag(a.Authority)).ToList();
    }

    /// <summary>
    /// 生成表单按钮工具栏
    /// </summary>
    /// <returns></returns>
    public MvcHtmlString BuildFormToolbar() {
        return BuildFormToolbar(GetButtons());
//        StringBuilder sb = new StringBuilder();
//        foreach (var btn in GetButtons())
//        {
//sb.AppendFormat("<a href=\"javascript:;\" class=\"sapar-btn sapar-btn-recom\" id=\"{0}\">{1}</a>",btn.Name,btn.Text);                                
//        }
//        return MvcHtmlString.Create(sb.ToString());
    }
    /// <summary>
    /// 生成表单按钮工具栏
    /// </summary>
    /// <returns></returns>
    public MvcHtmlString BuildFormToolbar(List<SysBtn> btns)
    {
        StringBuilder sb = new StringBuilder();
        for(SysBtn btn : btns)
        {
            sb.append(SGDataHelper.FormatString("<a href=\"javascript:;\" class=\"sapar-btn sapar-btn-recom\" id=\"{0}\">{1}</a>", btn.Name, btn.Text));
        }
        return MvcHtmlString.Create(sb.toString());
    }
    
    private static String GetMenuUlByChild(List<TreeListItem> menu) {
    	StringBuilder sb = new StringBuilder();
        sb.append("<ul class=\"subnav\">");
        for (TreeListItem fun : menu)
        {
            Boolean hasChild = fun.GetChildren() != null && fun.GetChildren().size() > 0;
            sb.append(SGDataHelper.FormatString("<li {2}><a href=\"javascript:; \" data-src=\"{0}\">{1}</a>", ((WebMenu)fun.GetData()).Url, ((WebMenu)fun.GetData()).Menuname, hasChild? "class=\"triangleRed\"" : ""));
            if(hasChild)
            {
                sb.append(GetMenuUlByChild(fun.GetChildren()));
            }
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
    public String BuildModuleLeftMenu( List<TreeListItem> Model)
    {
    	StringBuilder sb = new StringBuilder();
        if (Model != null)
        {
        	DefaultMenuIcon icons = new DefaultMenuIcon();
            for (TreeListItem item : Model)
            {
                if (((WebMenu)item.GetData()).Url != null || (item.GetChildren() != null && item.GetChildren().size() > 0))//如果是功能或者有子功能权限才显示
                {
                    Boolean hasChild = item.GetChildren() != null && item.GetChildren().size() > 0;
                    //sb.AppendFormat("<li class=\"{0}\">", item.Icon ?? icons.NextIcon());//阿伟不要图标--benjamin20190621
                    sb.append("<li>");
                    sb.append(SGDataHelper.FormatString("<div class=\"nav-header\"><a href=\"javascript:;\" {0} class=\"clearfix\">{2}<span>{1}</span></a></div>", ((WebMenu)item.GetData()).Url == null ? "" : "data-src='" + ((WebMenu)item.GetData()).Url + "'", ((WebMenu)item.GetData()).Menuname, hasChild ? "<i class=\"icon\" ></i>" : ""));
                    if (hasChild)
                    {
                        sb.append(GetMenuUlByChild(item.GetChildren()));
                        //sb.AppendFormat("<ul class=\"subnav\">");
                        //foreach (WebMenu fun in item.MenuItems)
                        //{
                        //    sb.AppendFormat("<li><a href=\"javascript:; \" data-src=\"{0}\">{1}</a></li>", fun.Url, fun.Menuname);
                        //}
                        //sb.AppendFormat("</ul>");
                    }
                    sb.append("</li>");
                }
            }
        }
        //return MvcHtmlString.Create(sb.toString());
        return sb.toString();
    }
}
