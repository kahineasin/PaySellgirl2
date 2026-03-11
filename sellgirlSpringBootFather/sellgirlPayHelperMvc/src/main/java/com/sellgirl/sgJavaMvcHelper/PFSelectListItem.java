package com.sellgirl.sgJavaMvcHelper;

/**
 * 对应C#的 System.Web.Mvc.SelectListItem
 * @author Administrator
 *
 */
public class PFSelectListItem {

    //
    // 摘要:
    //     获取或设置一个值，该值指示是否禁用此 System.Web.Mvc.SelectListItem。
    public boolean Disabled =false;
//    //
//    // 摘要:
//    //     表示此项已包装到的选项组 HTML 元素。在选择列表中，支持多个同名组。它们与引用相等性进行比较。
//    public SelectListGroup Group ;
    //
    // 摘要:
    //     获取或设置一个值，该值指示是否选择此 System.Web.Mvc.SelectListItem。
    //
    // 返回结果:
    //     如果选定此项，则为 true；否则为 false。
    public boolean Selected=false ;
    //
    // 摘要:
    //     获取或设置选定项的文本。
    //
    // 返回结果:
    //     文本。
    public String Text ;
    //
    // 摘要:
    //     获取或设置选定项的值。
    //
    // 返回结果:
    //     值。
    public String Value ;
}
