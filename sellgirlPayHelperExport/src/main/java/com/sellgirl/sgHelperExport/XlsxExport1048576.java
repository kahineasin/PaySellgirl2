package com.sellgirl.sgHelperExport;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/// <summary>
/// 注意：x为列,y为行。但在Aspose.Cells里习惯了把y放在x前面
/// xls2003默认一个sheet最多65535行，所以新建这个类，暂时把多出的行分到其它sheet
/// </summary>
public class XlsxExport1048576 implements IExport
{
//    private FileFormatType _excelFormat
//    {
//        get
//        {
//            //return FileFormatType.CSV;
//            //return FileFormatType.Excel97To2003;
//            return FileFormatType.Xlsx;
//        }
//    }
    //public String suffix { get { return "xls"; } }//C# 导出的excel只有65536行（2003版本是这样的，改为2007就行了）
    //public String suffix { get { return "xlsx"; } }
    public String getSuffix()
    {
    	return  "xlsx";
//        get
//        {
//            //return "csv";
//            //return "xlsx";
//            var result = "";
//            switch (_excelFormat)
//            {
//                case FileFormatType.Xlsx://直接toString不行，因为FileFormatType为6的项是有两个的
//                    result = "xlsx";
//                    break;
//                case FileFormatType.Excel97To2003://直接toString不行，因为FileFormatType为6的项是有两个的
//                    result = "xls";
//                    break;
//                default:
//                    result = "xls";
//                    break;
//            }
//            return result;
//        }
    }

    //public Workbook workbook;//待改为private--benjamin todo
    public XSSFWorkbook  workbook;//待改为private--benjamin todo
    //private XSSFSheet sheet;
    private Sheet sheet;
    private List<Sheet> sheets = new ArrayList<Sheet>();
    private XSSFCellStyle dataStyle;
    private PrintPageScheme _printPageScheme;//打印页面样式方案--wxj20181011

    /// <summary>
    /// 报错:(初步怀疑是破解盗版时产生的问题)
    /// time:[2019/5/22 15:13:47]
    ///      System.NullReferenceException: Object reference not set to an instance of an Object.
    /// at ح.ⴗ.⴦(Stream Ԡ)
    /// at ح.ⴗ.⴦(String ⴧ, Assembly ⴨)
    /// at Perfect.XlsExport.Init(Object data, PrintPageScheme scheme)
    /// at Perfect.Exporter.Export(IExport export)
    /// at Perfect.Exporter.Export(String type)
    /// at Perfect.Exporter.Instance(PagingResult pagingResult, ExporterOption opts)
    /// at YJQuery.Web.Areas.ProjOut.Controllers.ProjectRequirementController.GetExporter(String cmonthff, String fgsno, String fgsname, String hr, String fbatch, String SfName)
    /// </summary>
    /// <param name="data"></param>
    /// <param name="scheme"></param>
    public void Init(//Object data, 
    		PrintPageScheme scheme)
    {
        //try
        //{
        //    XlsExport.InitializeAsposeCells();
        //    //Aspose.Cells.License l = new Aspose.Cells.License();
        //    ////l.SetLicense(Path.Combine(HttpRuntime.AppDomainAppPath, "lib/Aid/License.lic"));
        //    //l.SetLicense(Path.Combine(PFDataHelper.BaseDirectory, "lib/Aid/License.lic"));
        //}
        //catch (Exception e)
        //{
        //    PFDataHelper.WriteError(e);
        //}
        //workbook = new Workbook();
    	
//        workbook = new Workbook(_excelFormat);
//        sheet = workbook.Sheets[0];
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Sheet1");
        //sheet = workbook.getSheetAt(0);//和C#版本不一样,C#默认创建了第一个sheet
        sheets.add(sheet);

        if (scheme != null) { SetPrintPageScheme(scheme); }
    }
    //#region Excel专用方法(非通用)
    public Sheet GetSheet()
    {
        return sheet;
    }
    public void SetSheetTabName(String name)
    {
        //sheet..Name = name;//好像没有setName方法
    }
    //#endregion

    public void SetColumnWidth(int x, String px)
    {
        double width = SGDataHelper.WebWidthToExcel(px).doubleValue();
        sheet.setColumnWidth(x, (int) width);
        //sheet.Cells.SetColumnWidth(x, width);  //
    }
    public void SetRowHeight(int y, String px)
    {
//        var tmpY = 0;
//        Sheet tmpSheet = null;
//        GetSheetByY(y, out tmpY, out tmpSheet);
//        double height = PFDataHelper.WebWidthToExcel(px).Value;
//        tmpSheet.Cells.SetRowHeight(tmpY, height);
    }
    /// <summary>
    /// 设置自动换行
    /// </summary>
    /// <param name="x1">列</param>
    /// <param name="y1">行</param>
    /// <param name="x2"></param>
    /// <param name="y2"></param>
    public void SetTextWrapped(int x1, int y1, int x2, int y2)
    {
//        var style = sheet.Cells[y1, x1].GetStyle();
//        style.IsTextWrapped = true;
//        for (var y = y1; y <= y2; y++)
//        {
//            var tmpY = 0;
//            Sheet tmpSheet = null;
//            GetSheetByY(y, out tmpY, out tmpSheet);
//
//            for (var x = x1; x <= x2; x++)
//            {
//                //var cell = sheet.Cells[y, x];
//                var cell = tmpSheet.Cells[tmpY, x];
//                cell.SetStyle(style);
//            }
//        }
    }
    public void MergeCell(int x1, int y1, int x2, int y2)
    { 
    	//int firstRow, int lastRow, int firstCol, int lastCol
    	CellRangeAddress region = new CellRangeAddress(y1,y2, x1, x2);
    sheet.addMergedRegion(region);

    //C#里
//    //firstRow, int firstColumn, int totalRows, int totalColumns
//        Range range = sheet.Cells.CreateRange(y1, x1, y2 - y1 + 1, x2 - x1 + 1);
//        range.Merge(); //benjamin todo

    }


    //public virtual void FillData(int x, int y, String field, Object value)
    //{

    //    //if (!field.StartsWith("title_"))
    //    //    cell.SetStyle(GetDataStyle());

    //    switch ((value ?? String.Empty).GetType().Name.ToLower())
    //    {
    //        case "int32":
    //        case "int64":
    //        case "decimal":
    //            sheet.Cells[y, x].PutValue(PFDataHelper.ObjectToType<double>(value, 0));
    //            break;
    //        //case "System.String[]":
    //        //    var s = String.Join(",", (String[])value);
    //        //    sheet.Cells[y, x].PutValue(s);
    //        //    break;
    //        default:
    //            if (value is String[])
    //            {
    //                var s = String.Join(",", value as String[]);
    //                sheet.Cells[y, x].PutValue(s);
    //            }
    //            else
    //            {
    //                sheet.Cells[y, x].PutValue(PFDataHelper.ObjectToString(value));
    //            }
    //            break;
    //    }
    //}


    private static void setCellValue(Cell cell, Object object) {
        if (object == null)
            return;
        if (object instanceof Integer)
            cell.setCellValue(((Integer) object).doubleValue());
        else if (object instanceof Double)
            cell.setCellValue((Double) object);
        else if (object instanceof Long)
            cell.setCellValue(((Long) object).doubleValue());
        else if (object instanceof BigDecimal)
            cell.setCellValue(((BigDecimal) object).doubleValue());
        else if (object instanceof String)
            cell.setCellValue((String) object);
        else if (object instanceof Date) {
            cell.setCellValue((Date) object);}
        else
            cell.setCellValue(object.toString());
    }
    private void CreateXY(SGRef<Sheet> tmpSheet ,int x,int y) {
//        if(tmpSheet.GetValue().getRow(tmpY.GetValue())==null) {
//        	tmpSheet.GetValue().createRow(tmpY.GetValue());
//        }
//        if(tmpSheet.GetValue().getRow(tmpY.GetValue()).getCell( x)==null) {
//        	tmpSheet.GetValue().getRow(tmpY.GetValue()).createCell(x);
//        }
        if(tmpSheet.GetValue().getRow(y)==null) {
        	tmpSheet.GetValue().createRow(y);
        }
        if(tmpSheet.GetValue().getRow(y).getCell( x)==null) {
        	tmpSheet.GetValue().getRow(y).createCell(x);
        }
    }
    public  void FillData(int x, int y,// String field, 
    		Object value)
    {

        //if (!field.StartsWith("title_"))
        //    cell.SetStyle(GetDataStyle());

        SGRef<Integer> tmpY = new SGRef<Integer>(0);
        SGRef<Sheet> tmpSheet = new SGRef<Sheet>(null);
        GetSheetByY(y,  tmpY,  tmpSheet);

        CreateXY(tmpSheet,x,tmpY.GetValue());
//        if(tmpSheet.GetValue().getRow(tmpY.GetValue())==null) {
//        	tmpSheet.GetValue().createRow(tmpY.GetValue());
//        }
//        if(tmpSheet.GetValue().getRow(tmpY.GetValue()).getCell( x)==null) {
//        	tmpSheet.GetValue().getRow(tmpY.GetValue()).createCell(x);
//        }
        if(value!=null) {
        	setCellValue(tmpSheet.GetValue().getRow(tmpY.GetValue()).getCell( x),value);
        }
    }
    

    //public virtual void SetHeadStyle(int x1, int y1, int x2, int y2)
    //{
    //    var style = GetHeadStyle();
    //    for (var y = y1; y <= y2; y++)
    //    {                
    //        for (var x = x1; x <= x2; x++)
    //        {
    //            var cell = sheet.Cells[y,x];
    //            cell.SetStyle(style);
    //        }
    //        if (_printPageScheme != null) { sheet.Cells.SetRowHeight(y, _printPageScheme.HeadRowHeight); }
    //    }
    //}

    public void SetHeadStyle(int x1, int y1, int x2, int y2)
    {
    	CellStyle style = GetHeadStyle();
        for (int y = y1; y <= y2; y++)
        {
        	SGRef<Integer> tmpY = new SGRef<Integer> (0);
        	
            //Sheet tmpSheet = null;
        	SGRef<Sheet> tmpSheet = new SGRef<Sheet> (null);
            GetSheetByY(y,  tmpY,  tmpSheet);


            CreateXY(tmpSheet,0,tmpY.GetValue());
            Row row=tmpSheet.GetValue().getRow(tmpY.GetValue());
            for (int x = x1; x <= x2; x++)
            {
                CreateXY(tmpSheet,x,tmpY.GetValue());
                
                Cell cell =row.getCell( x);
                cell.setCellStyle(style);
            }
            //行高怎么设置-- benjamin 
            if (_printPageScheme != null) {
            	row.setHeight( (short) _printPageScheme.HeadRowHeight);
            	//tmpSheet.Cells.SetRowHeight(tmpY, _printPageScheme.HeadRowHeight); 
            	}
        }
    }

    public  void SetRowsStyle(int x1, int y1, int x2, int y2)
    {
    	CellStyle style = GetDataStyle();
        for (int y = y1; y <= y2; y++)
        {
        	//int tmpY = 0;
            //Sheet tmpSheet = null;
        	SGRef<Integer> tmpY = new SGRef<Integer> (0);
        	SGRef<Sheet> tmpSheet = new SGRef<Sheet> (null);
            GetSheetByY(y,  tmpY,  tmpSheet);

            CreateXY(tmpSheet,0,tmpY.GetValue());
            Row row=tmpSheet.GetValue().getRow(tmpY.GetValue());
            for (int x = x1; x <= x2; x++)
            {
                CreateXY(tmpSheet,x,tmpY.GetValue());
                
                //var cell = tmpSheet.Cells[tmpY, x];
                //cell.SetStyle(style);
                Cell cell =row.getCell( x);
                cell.setCellStyle(style);
            }
            if (_printPageScheme != null) { 
            	row.setHeight( (short) _printPageScheme.DataRowHeight);
            	//tmpSheet.Cells.SetRowHeight(tmpY, _printPageScheme.DataRowHeight); 
            	}
        }
    }


    public  void SetTitleStyle(int x1, int y1, int x2, int y2)
    {
    	CellStyle style = GetTitleStyle();
    	
//        //var cell = sheet.Cells[x1, y1];
//        var cell = sheet.Cells[y1, x1];
//        cell.SetStyle(style);
        
    	SGRef<Integer> tmpY = new SGRef<Integer> (0);
    	SGRef<Sheet> tmpSheet = new SGRef<Sheet> (null);
        GetSheetByY(y1,  tmpY,  tmpSheet);
        tmpSheet.GetValue().getRow(tmpY.GetValue()).getCell( x1).setCellStyle(style);
        
        MergeCell(x1, y1, x2, y2);
    	
        if (_printPageScheme != null)
        {
            for (int y = y1; y <= y2; y++)
            {
//                var tmpY = 0;
//                Sheet tmpSheet = null;
                GetSheetByY(y,  tmpY,  tmpSheet);
                tmpSheet.GetValue().getRow(tmpY.GetValue()).setHeight( (short) _printPageScheme.TitleRowHeight);
                //tmpSheet.Cells.SetRowHeight(tmpY, _printPageScheme.TitleRowHeight);
            }
        }
    }


    public  void SetFootStyle(int x1, int y1, int x2, int y2)
    {
//        var style = GetFootStyle();
//        //var cell = sheet.Cells[x1, y1];
//        var cell = sheet.Cells[y1, x1];
//        cell.SetStyle(style);
//        MergeCell(x1, y1, x2, y2);
//        if (_printPageScheme != null)
//        {
//            for (int y = y1; y <= y2; y++)
//            {
//                var tmpY = 0;
//                Sheet tmpSheet = null;
//                GetSheetByY(y, out tmpY, out tmpSheet);
//
//                tmpSheet.Cells.SetRowHeight(tmpY, _printPageScheme.FootRowHeight);
//            }
//        }
    }

    //public void SetFont(int x1, int y1, int x2, int y2, String fontName)
    //{
    //    for (var y = y1; y <= y2; y++)
    //    {
    //        for (var x = x1; x <= x2; x++)
    //        {
    //            var cell = sheet.Cells[y, x];
    //            var style = cell.GetStyle();
    //            style.Font.Name = fontName;
    //            cell.SetStyle(style);
    //        }
    //    }
    //}
    public void SetFont(int x1, int y1, int x2, int y2, String fontName)
    {
//        for (var y = y1; y <= y2; y++)
//        {
//            for (var x = x1; x <= x2; x++)
//            {
//                var tmpY = 0;
//                Sheet tmpSheet = null;
//                GetSheetByY(y, out tmpY, out tmpSheet);
//
//                var cell = tmpSheet.Cells[tmpY, x];
//                var style = cell.GetStyle();
//                style.Font.Name = fontName;
//                cell.SetStyle(style);
//            }
//        }
    }

    public Stream<?> SaveAsStream()
    {
    	return null;
//        var ms = new MemoryStream();
//        ms = workbook.SaveToStream();
//
//        ms.Flush();
//        ms.Position = 0;
//
//        //workbook = null;//为了便于后期合并cell,先不清空--wxj20181011
//        //sheet = null;
//        return ms;
    }
    public void WriteToStream(OutputStream stream)
    {
    	try {
			workbook.write(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        var ms = new MemoryStream();
//        ms = workbook.SaveToStream();
//
//        ms.Flush();
//        ms.Position = 0;
//
//        //workbook = null;//为了便于后期合并cell,先不清空--wxj20181011
//        //sheet = null;
//        return ms;
    }

    private void SetPrintPageScheme(PrintPageScheme scheme)
    {
//        _printPageScheme = scheme;
//
//        sheet.PageSetup.TopMargin = scheme.TopMargin;
//        sheet.PageSetup.RightMargin = scheme.RightMargin;
//        sheet.PageSetup.BottomMargin = scheme.BottomMargin;
//        sheet.PageSetup.LeftMargin = scheme.LeftMargin;
//        //sheet.Cells.SetRowHeight()     
    }

    
    private static short GetColorByAwtColor(java.awt.Color color) {
    	return HSSFColor.LIGHT_GREEN.index;
    }
    
    private CellStyle GetHeadStyle()
    {
        //表头样式
    	//CellStyle headStyle = workbook.createCellStyle();
    	XSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setWrapText(true);
        
        headStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setTopBorderColor(HSSFColor.BLACK.index);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setRightBorderColor(HSSFColor.BLACK.index);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setLeftBorderColor(HSSFColor.BLACK.index);



        if (_printPageScheme != null)
        {
            headStyle.setFillForegroundColor(GetColorByAwtColor(_printPageScheme.HeadForegroundColor));
            XSSFFont font = workbook.createFont();
          font.setFontHeightInPoints((short) _printPageScheme.HeadFontSize);//设置字体大小
          headStyle.setFont(font);
//            headStyle.Font.Size = _printPageScheme.HeadFontSize;// 10;
        }
        return headStyle;
    }

    private CellStyle GetDataStyle()
    {
        if (dataStyle == null) {
            //表头样式
        	dataStyle = workbook.createCellStyle();
//            dataStyle.setAlignment(HorizontalAlignment.CENTER);
//            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//            dataStyle.setWrapText(true);
//            
//            dataStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
//            dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setTopBorderColor(HSSFColor.BLACK.index);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setRightBorderColor(HSSFColor.BLACK.index);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBottomBorderColor(HSSFColor.BLACK.index);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setLeftBorderColor(HSSFColor.BLACK.index);



            if (_printPageScheme != null)
            {
                XSSFFont font = workbook.createFont();
              font.setFontHeightInPoints((short) _printPageScheme.DataFontSize);//设置字体大小
              dataStyle.setFont(font);
//                headStyle.Font.Size = _printPageScheme.HeadFontSize;// 10;
            }
        }        
        
        return dataStyle;
    }
    
    private CellStyle GetTitleStyle()
    {
    	//CellStyle headStyle = workbook.createCellStyle();
    	XSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setWrapText(true);
        
        headStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setTopBorderColor(HSSFColor.BLACK.index);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setRightBorderColor(HSSFColor.BLACK.index);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setLeftBorderColor(HSSFColor.BLACK.index);



        if (_printPageScheme != null)
        {
            //headStyle.setFillForegroundColor(GetColorByAwtColor(_printPageScheme.HeadForegroundColor));
            XSSFFont font = workbook.createFont();
          font.setFontHeightInPoints((short) _printPageScheme.TitleFontSize);//设置字体大小
          font.setBold(_printPageScheme.TitleFontIsBold);
          headStyle.setFont(font);
//            headStyle.Font.Size = _printPageScheme.HeadFontSize;// 10;

          switch (_printPageScheme.TitleHorizontalAlignment)
          {
              case Center:
            	  headStyle.setAlignment(HorizontalAlignment.CENTER);//左对齐
                  break;
              default:
                  break;
          }
        }
        
  

        return headStyle;
    }
    
//    private Style GetFootStyle()
//    {
//        //if (dataStyle == null)
//        //{
//        var footStyle = workbook.CreateStyle();
//        //数据样式
//        footStyle = workbook.CreateStyle();
//        //footStyle.Alignment = HorizontalAlignment.LEFT;//左对齐
//        //////数据单元格的边框
//        //footStyle.Borders[BorderType.TopBorder].LineStyle = CellBorderType.Thin;
//        //footStyle.Borders[BorderType.TopBorder].Color = Color.Black;
//        //footStyle.Borders[BorderType.RightBorder].LineStyle = CellBorderType.Thin;
//        //footStyle.Borders[BorderType.RightBorder].Color = Color.Black;
//        //footStyle.Borders[BorderType.BottomBorder].LineStyle = CellBorderType.Thin;
//        //footStyle.Borders[BorderType.BottomBorder].Color = Color.Black;
//        //footStyle.Borders[BorderType.LeftBorder].LineStyle = CellBorderType.Thin;
//        //footStyle.Borders[BorderType.LeftBorder].Color = Color.Black;
//
//        if (_printPageScheme != null)
//        {
//            //var f = new Aspose.Cells.Font(
//            //{
//            //    Size = _printPageScheme.FootFontSize
//            //};
//            footStyle.Font.Size = _printPageScheme.FootFontSize;
//            //footStyle.Font.IsBold = _printPageScheme.FootFontIsBold;
//        }
//        //////数据的字体
//        ////var datafont = workbook.CreateFont();
//        ////datafont.FontHeightInPoints = 11;//字号
//        ////footStyle.SetFont(datafont);
//        //}
//
//        return footStyle;
//    }

    public void Dispose()
    {
        workbook = null;//为了便于后期合并cell,先不清空--wxj20181011
        sheet = null;
    }

    public void AddExport(IExport export, String title
        )
    {
//        var other =PFDataHelper.<XlsExport>ObjectAs( export) ;
//        if (other != null)
//        {
//            var otherSheet = other.GetSheet();
//            var s = workbook.Sheets.Add(title ?? otherSheet.Name);
//            s.Copy(otherSheet);
//            //workbook.Sheets[1] = other.GetSheet();
//            //s = other.GetSheet();
//        }
    }

    //#region static
    public static void Crack()
    {

//        try
//        {
//            //XlsExport.InitializeAsposeCells();
//            Aspose.Cells.License l = new Aspose.Cells.License();
//            //l.SetLicense(Path.Combine(HttpRuntime.AppDomainAppPath, "lib/Aid/License.lic"));
//            l.SetLicense(Path.Combine(PFDataHelper.BaseDirectory, "lib/Aid/License.lic"));
//        }
//        catch (Exception e)
//        {
//            PFDataHelper.WriteError(e);
//        }
    }
//    public static void InitializeAsposeCells()
//    {
//        const BindingFlags BINDING_FLAGS_ALL = BindingFlags.Public | BindingFlags.NonPublic | BindingFlags.Static | BindingFlags.Instance;
//
//        const String CLASS_LICENSER = "\u0092\u0092\u0008.\u001C";
//        const String CLASS_LICENSERHELPER = "\u0011\u0001\u0006.\u001A";
//        const String ENUM_ISTRIAL = "\u0092\u0092\u0008.\u001B";
//
//        const String FIELD_LICENSER_CREATED_LICENSE = "\u0001";     // static
//        const String FIELD_LICENSER_EXPIRY_DATE = "\u0002";         // instance
//        const String FIELD_LICENSER_ISTRIAL = "\u0001";             // instance
//
//        const String FIELD_LICENSERHELPER_INT128 = "\u0001";        // static
//        const String FIELD_LICENSERHELPER_BOOLFALSE = "\u0001";     // static
//
//        const int CONST_LICENSER_ISTRIAL = 1;
//        const int CONST_LICENSERHELPER_INT128 = 128;
//        const Boolean CONST_LICENSERHELPER_BOOLFALSE = false;
//
//        //- Field setter for convinient
//        Action<FieldInfo, Type, String, Object, Object> setValue =
//            delegate (FieldInfo field, Type chkType, String chkName, Object obj, Object value)
//            {
//                if ((field.FieldType == chkType) && (field.Name == chkName))
//                {
//                    field.SetValue(obj, value);
//                }
//            };
//
//
//        //- Get types
//        Assembly assembly = Assembly.GetAssembly(typeof(Aspose.Cells.License));
//        Type typeLic = null, typeIsTrial = null, typeHelper = null;
//        foreach (Type type in assembly.GetTypes())
//        {
//            if ((typeLic == null) && (type.FullName == CLASS_LICENSER))
//            {
//                typeLic = type;
//            }
//            else if ((typeIsTrial == null) && (type.FullName == ENUM_ISTRIAL))
//            {
//                typeIsTrial = type;
//            }
//            else if ((typeHelper == null) && (type.FullName == CLASS_LICENSERHELPER))
//            {
//                typeHelper = type;
//            }
//        }
//        if (typeLic == null || typeIsTrial == null || typeHelper == null)
//        {
//            throw new Exception();
//        }
//
//        //- In class_Licenser
//        Object license = Activator.CreateInstance(typeLic);
//        foreach (FieldInfo field in typeLic.GetFields(BINDING_FLAGS_ALL))
//        {
//            setValue(field, typeLic, FIELD_LICENSER_CREATED_LICENSE, null, license);
//            setValue(field, typeof(DateTime), FIELD_LICENSER_EXPIRY_DATE, license, DateTime.MaxValue);
//            setValue(field, typeIsTrial, FIELD_LICENSER_ISTRIAL, license, CONST_LICENSER_ISTRIAL);
//        }
//
//        //- In class_LicenserHelper
//        foreach (FieldInfo field in typeHelper.GetFields(BINDING_FLAGS_ALL))
//        {
//            setValue(field, typeof(int), FIELD_LICENSERHELPER_INT128, null, CONST_LICENSERHELPER_INT128);
//            setValue(field, typeof(Boolean), FIELD_LICENSERHELPER_BOOLFALSE, null, CONST_LICENSERHELPER_BOOLFALSE);
//        }
//    }
    //#endregion static

    //#region Private
    /// <summary>
    /// 当超过65536行后不能导出（可能是excel版本问题，暂时采用分sheet解决）--benjamin20191216
    /// </summary>
    /// <param name="y"></param>
    /// <param name="newY"></param>
    /// <param name="sheet"></param>
    private void GetSheetByY(int y, SGRef<Integer> newY, SGRef<Sheet>  newSheet)
    {
        int pageSize = 1000000;// 60000;//新版本最大行数是1048576
        if (y < pageSize)
        {
            newY.SetValue( y);
            newSheet.SetValue(sheet);
            return;
        }
        else
        {
            //10
            //0~9:0
            //10~19:1

            //page从0开始的
            //Integer page = PFDataHelper.ObjectToInt(Math.Floor(y / pageSize));
            Integer page = SGDataHelper.ObjectToInt(y / pageSize);
            //var page=int.Parse(Math.Ceiling(y / pageSize).ToString())+1;
            if (page >= sheets.size())
            {
                //count=2,page=4, 2~4
                for (int i = sheets.size(); i <= page; i++)
                {
                    sheets.add(workbook.createSheet("Sheet" + String.valueOf(i + 1)));
                }
            }
            newY.SetValue( y % pageSize);
            newSheet.SetValue( sheets.get(page));
        }
    }
    //#endregion
}