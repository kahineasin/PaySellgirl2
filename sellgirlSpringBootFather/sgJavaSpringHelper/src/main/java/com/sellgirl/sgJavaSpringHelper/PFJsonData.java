// package com.sellgirl.sgJavaSpringHelper;
// 
// 
// 
// public class PFJsonData
// {
//   public String Message;
//   public Boolean Result;
//   public String Url;
//   public String HtmlPartial;
//   public String HtmlPartial2;
//   public String HtmlPartial3;
//   
//   public static PFJsonData SetSuccess(String message) {
//     PFJsonData r = new PFJsonData();
//     r.Message = message;
//     r.Result = Boolean.valueOf(true);
//     return r;
//   }
//   public static PFJsonData SetSuccess() {
//     PFJsonData r = new PFJsonData();
//     r.Result = Boolean.valueOf(true);
//     return r;
//   }
//   
//   public static PFJsonData SetFault(String message) {
//     PFJsonData r = new PFJsonData();
//     r.Message = message;
//     r.Result = Boolean.valueOf(false);
//     return r;
//   }
// }