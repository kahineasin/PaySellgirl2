package com.sellgirl.sgHelperExport;

import java.awt.Color;

/// <summary>
/// 打印页样式方案（注意title是指标题，head是指表头.但Exporter里的title指表头）
/// </summary>
public class PrintPageScheme
{
    //#region 页边距
    public double TopMargin ;
    public double RightMargin ;
    public double BottomMargin ;
    public double LeftMargin ;

    //#endregion
    //#region 数据格式
    public double DataRowHeight ;
    public int DataFontSize ;
    //#endregion
    //#region 表头格式(列头)
    public double HeadRowHeight ;
    public Color HeadForegroundColor ;
    public int HeadFontSize ;
    //#endregion
    //#region 标题格式
    public double TitleRowHeight ;
    public int TitleFontSize ;
    public Boolean TitleFontIsBold ;
    public PFTextAlignmentType TitleHorizontalAlignment ;
    //#endregion
    //#region Foot格式
    public double FootRowHeight ;
    public int FootFontSize ;
    //#endregion
}