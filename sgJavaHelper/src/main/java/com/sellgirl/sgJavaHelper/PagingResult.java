package com.sellgirl.sgJavaHelper;

import java.util.List;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/// <summary>
/// 分页查询结果
/// </summary>
public class PagingResult implements ISGDisposable
{
    public List<?> data ;
    public Object exData ;//为了减少前端多次请求,便于放其它数据
    public StoreColumnCollection columns ;
    public int total ;
    public String TableCacheKey ;

    public void dispose()
    {
        //data.Clear();
        data = null;//2329-700 =1662
        exData = null;
        columns = null;
        SGDataHelper.GCCollect();
        //GC.Collect();
        //var aa = "aa";
        //throw new NotImplementedException();
    }
}