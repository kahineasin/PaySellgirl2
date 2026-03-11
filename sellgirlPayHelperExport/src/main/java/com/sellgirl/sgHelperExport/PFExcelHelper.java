package com.sellgirl.sgHelperExport;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
//import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFExcelHelper {
    /// <summary>
    /// 获得excel的行数
    /// </summary>
    /// <param name="sheet"></param>
    /// <returns></returns>
    public static int GetExcelRowCount(Sheet sheet)
    {
        //return sheet.Cells.MaxDataRow+1;
        return sheet.getPhysicalNumberOfRows();
    }
    /// <summary>
    /// 获得excel的列数
    /// </summary>
    /// <param name="sheet"></param>
    /// <returns></returns>
    public static int GetExcelColumnCount(Sheet sheet) {
//        return sheet.Cells.MaxDataColumn+1;
    	Row row=sheet.getRow(0);
    	return row.getLastCellNum();
    }
    
    public static Workbook create(InputStream inp) throws IOException,InvalidFormatException {
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
            return new HSSFWorkbook(inp);
        }
        if (POIXMLDocument.hasOOXMLHeader(inp)) {
            return new XSSFWorkbook(OPCPackage.open(inp));
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了");
    }
    /**
     * 第一行是表头
     * @param workbook
     * @return
     */
    //public static List<Map<String, Object>> ExcelToDictList(XSSFWorkbook workbook)
    public static List<Map<String, Object>> ExcelToDictList(Workbook workbook)
    {
    	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	ArrayList<String> cols = new ArrayList<String>();

       // var sheet = workbook.Worksheets[0];
    	//XSSFSheet sheet = workbook.getSheetAt(0);
    	Sheet sheet = workbook.getSheetAt(0);
        //int rowCnt = sheet.Cells.Rows.Count;
        //int colCnt = sheet.Cells.Columns.Count;
        int rowCnt = GetExcelRowCount( sheet);
        int colCnt = GetExcelColumnCount( sheet);
        for (int j = 0; j < colCnt; j++)
        {
            //cols.add(PFDataHelper.ObjectToString(sheet.getRow(0).getCell(j).getRawValue()).trim());
            cols.add(SGDataHelper.ObjectToString(sheet.getRow(0).getCell(j).getStringCellValue()).trim());
        }
        //var telephoneIdx = cols.IndexOf("telephone");
//        if (telephoneIdx < 0)
//        {
//            return null;
//        }
        for (int i = 1; i < rowCnt; i++)
        {
        	HashMap<String, Object> item = new HashMap<String, Object>();
            //var telephone = PFDataHelper.ObjectToString(sheet.Cells[i, telephoneIdx].Value);
//            if (PFDataHelper.StringIsNullOrWhiteSpace(telephone))//只要有一行为空，就返回
//            {
//                return list;
//            }
            for (int j = 0; j < colCnt; j++)
            {
                //item[cols[j]] = sheet.Cells[i, j].Value;
                //item.put(cols.get(j),sheet.getRow(i).getCell(j).getRawValue());
                item.put(cols.get(j),sheet.getRow(i).getCell(j).getStringCellValue());
            }
            list.add(item);
        }
        return list;
    }
}
