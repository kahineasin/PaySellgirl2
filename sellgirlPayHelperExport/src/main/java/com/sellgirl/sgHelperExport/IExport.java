package com.sellgirl.sgHelperExport;

import java.io.OutputStream;
import java.util.stream.Stream;

public interface IExport //: IDisposable
{
    String getSuffix();
    /// <summary>
    /// 
    /// </summary>
    /// <param name="x"></param>
    /// <param name="px">网页的px单位</param>
    void SetColumnWidth(int x, String px);
    void SetRowHeight(int y, String px);
    /// <summary>
    /// 设置自动换行
    /// </summary>
    /// <param name="x1">列</param>
    /// <param name="y1">行</param>
    /// <param name="x2"></param>
    /// <param name="y2"></param>
     void SetTextWrapped(int x1, int y1, int x2, int y2);
    /// <summary>
    /// 合并单元格，序号均从0开始
    /// </summary>
    /// <param name="x1">左上角点的列</param>
    /// <param name="y1">左上角点的行</param>
    /// <param name="x2">右下角点的列</param>
    /// <param name="y2">右下角点的行</param>
    void MergeCell(int x1, int y1, int x2, int y2);
    void FillData(int x, int y,// String field,
    		Object data);

    //void Init(Object data, PrintPageScheme scheme);
    void Init(PrintPageScheme scheme);
    Stream<?> SaveAsStream();
    void WriteToStream(OutputStream stream);

    ///// <summary>
    ///// 设置打印页面格式
    ///// </summary>
    ///// <param name="scheme"></param>
    //void SetPrintPageScheme(PrintPageScheme scheme);

    void SetHeadStyle(int x1, int y1, int x2, int y2);
    void SetRowsStyle(int x1, int y1, int x2, int y2);
    void SetTitleStyle(int x1, int y1, int x2, int y2);
    void SetFootStyle(int x1, int y1, int x2, int y2);

    void SetFont(int x1, int y1, int x2, int y2, String fontName);

    /// <summary>
    /// 为了实现Excel多个sheet合并的情况(title为sheet的标签名)--benjamin20190409
    /// </summary>
    /// <param name="export"></param>
    void AddExport(IExport export,String title
        );
}