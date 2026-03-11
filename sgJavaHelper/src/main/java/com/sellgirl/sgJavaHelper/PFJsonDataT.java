/*   */ package com.sellgirl.sgJavaHelper;
/*   */ 
/*   */ public class PFJsonDataT<T>
/*   */   extends PFJsonData {
/*   */   public PFJsonDataT<T> SetSuccess(T data) {
/* 6 */     this.Result = Boolean.valueOf(true);
/* 7 */     this.Data = data;
/* 8 */     return this;
/*   */   }
/*   */   
/*   */   public T Data;
/*   */ }


/* Location:              D:\mylib\sellgirlPayHelper-0.0.1-SNAPSHOT.jar!\pf\java\pfHelper\PFJsonDataT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */