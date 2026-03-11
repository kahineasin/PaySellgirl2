package com.sellgirl.sgHelperExport;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

//import com.sellgirl.pfHelperNotSpring.ITreeListItem;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.PagingResult;
import com.sellgirl.sgJavaHelper.StoreColumn;
import com.sellgirl.sgJavaHelper.StoreColumnCollection;
import com.sellgirl.sgJavaHelper.TreeListItem;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Iterator;
import java.util.Map.Entry;

public class Exporter //extends Closeable// : IDisposable
{
    final String DEFAULT_EXPORT = "xls";
//    const String DEFAULT_DATAGETTER = "api";
//    const String DEFAULT_COMPRESS = "none";

//    private Dictionary<String, Type> _compress = new Dictionary<String, Type>() { 
//        //{ "zip", typeof(ZipCompress)},
//        {"none",typeof(NoneCompress)}
//    };
//    private Dictionary<String, Type> _dataGetter = new Dictionary<String, Type>() {
//        { "api", typeof(ApiData) }
//    };
    private Map<String, Class<?>> _export = new HashMap<String, Class<?>>() { /**
		 * 
		 */
		private static final long serialVersionUID = -9198969434465949521L;

	{
    	put("xls",XlsxExport1048576.class);
//        { "xlsNoMulti", typeof(XlsExport) },//项目外包使用
//        { "xls", typeof(XlsxExport1048576) },
//        { "doc", typeof(WordExport) }
    }};

    private Map<String, IFormatter> _fieldFormatter = new HashMap<String, IFormatter>();

    private Object _data;
    private ArrayList<ArrayList<StoreColumn>> _title;
    private StoreColumnCollection _columns;//这个类型比_title实用--wxj20181010
    private IExport _exporter;//有这个属性比较容易做后期合并cell操作--wxj20181011
    private PrintPageScheme _printPageScheme;//打印页面样式方案--wxj20181011
    private String _sheetTitle;//打印页面样式方案--wxj20181011
    private String _sheetFoot;
    private Stream<?> _fileStream = null;
    private String _fileName = "";
    //private String _suffix = "";
    //private IController _controller = null;

    public static PrintPageScheme FinancialScheme = new PrintPageScheme()
    {{
        TopMargin = 0.4;
        RightMargin = 0.3;
        BottomMargin = 0.4;
        LeftMargin = 0.3;

        DataRowHeight = 13.5;
        DataFontSize = 9;

        HeadRowHeight = 13.5;
        //HeadForegroundColor=null; //默认是rgb(0;0;0);但显示为无色，原因未明
        HeadFontSize = 9;

        TitleRowHeight = 25.5;//20;
        TitleFontSize = 16;
        TitleFontIsBold = true;
        TitleHorizontalAlignment = PFTextAlignmentType.Center;

        FootRowHeight = 36.75;
        FootFontSize = 11;
    }}
    ;

//    public static Exporter Instance(PFBaseWebController controller)
//    {
//        var export = new Exporter();
//        export._controller = controller;
//        var context = HttpContext.Current;
//
//        //if (context.Request.Form["titles"] != null)
//        //    export.Title(JsonConvert.DeserializeObject<List<List<StoreColumn>>>(context.Request.Form["titles"]));//原本的title是从前端传过来,现改为统一用_data里的columns
//
//        if (context.Request.Form["dataGetter"] != null)
//        {
//            export.Data(context.Request.Form["dataGetter"]);
//            var columns = (export._data as PagingResult).columns as StoreColumnCollection;//这里可以扩展为多表头
//            ////export.Title(new List<List<StoreColumn>> { columns });//树型必需转2维数组
//            //var title = new List<List<StoreColumn>>();
//            //var maxDepth = new StoreColumn { Children = columns }.GetDepth()-1;
//            //StoreColumnCollection.StoreColumnTo2DArray(ref title, columns,ref maxDepth);
//            //export.Title(title);
//            export.Title(columns);
//        }
//
//        if (context.Request.Form["fileType"] != null)
//            export.Export(context.Request.Form["fileType"]);
//
//        if (context.Request.Form["compressType"] != null)
//            export.Compress(context.Request.Form["compressType"]);
//
//        return export;
//    }
//    public static Exporter Instance(PagingResult pagingResult, ExporterOption opts)
//    {
//        var export = new Exporter();
//        //export._controller = controller;
//        var context = HttpContext.Current;
//
//        String fileType = opts.FileType; PrintPageScheme scheme = opts.Scheme; String sheetTitle = opts.SheetTitle; String sheetFoot = opts.SheetFoot;
//
//        export._printPageScheme = scheme;
//        export._sheetTitle = sheetTitle;
//        export._sheetFoot = sheetFoot;
//
//        //if (context.Request.Form["titles"] != null)
//        //    export.Title(JsonConvert.DeserializeObject<List<List<StoreColumn>>>(context.Request.Form["titles"]));//原本的title是从前端传过来,现改为统一用_data里的columns
//
//        //if (context.Request.Form["dataGetter"] != null)
//        //{
//        //export.Data(context.Request.Form["dataGetter"]);
//        export.Data(pagingResult);
//        var columns = (export._data as PagingResult).columns as StoreColumnCollection;//这里可以扩展为多表头
//                                                                                      ////export.Title(new List<List<StoreColumn>> { columns });//树型必需转2维数组
//                                                                                      //var title = new List<List<StoreColumn>>();
//                                                                                      //var maxDepth = new StoreColumn { Children = columns }.GetDepth() - 1;
//                                                                                      //StoreColumnCollection.StoreColumnTo2DArray(ref title, columns, ref maxDepth);
//                                                                                      //export.Title(title);
//        export.Title(columns);
//        //}
//
//        export.Export(fileType);
//
//        if (context != null && context.Request.Form["compressType"] != null)
//            export.Compress(context.Request.Form["compressType"]);
//
//        return export;
//    }
    public static Exporter Instance(
    		PagingResult pagingResult,
//             List data,StoreColumnCollection pagingResult, 
             ExporterOption opts)
    {
    	Exporter export = new Exporter();
//        //export._controller = controller;
//        var context = HttpContext.Current;

        String fileType = opts.FileType; PrintPageScheme scheme = opts.Scheme; String sheetTitle = opts.SheetTitle; String sheetFoot = opts.SheetFoot;

        export._printPageScheme = scheme;
        export._sheetTitle = sheetTitle;
        export._sheetFoot = sheetFoot;

        //if (context.Request.Form["titles"] != null)
        //    export.Title(JsonConvert.DeserializeObject<List<List<StoreColumn>>>(context.Request.Form["titles"]));//原本的title是从前端传过来,现改为统一用_data里的columns

        //if (context.Request.Form["dataGetter"] != null)
        //{
        //export.Data(context.Request.Form["dataGetter"]);
        export.Data(pagingResult);

        StoreColumnCollection columns = pagingResult.columns;//这里可以扩展为多表头
                                                                                      ////export.Title(new List<List<StoreColumn>> { columns });//树型必需转2维数组
                                                                                      //var title = new List<List<StoreColumn>>();
                                                                                      //var maxDepth = new StoreColumn { Children = columns }.GetDepth() - 1;
                                                                                      //StoreColumnCollection.StoreColumnTo2DArray(ref title, columns, ref maxDepth);
                                                                                      //export.Title(title);
        export.Title(columns);
        //}

        export.Export(fileType);

//        if (context != null && context.Request.Form["compressType"] != null)
//            export.Compress(context.Request.Form["compressType"]);

        return export;
    }
    //private static Exporter Instance()//旧方法,反射controller会导致方便调用时session为空
    //{
    //    var export = new Exporter();
    //    var context = HttpContext.Current;

    //    //if (context.Request.Form["titles"] != null)
    //    //    export.Title(JsonConvert.DeserializeObject<List<List<StoreColumn>>>(context.Request.Form["titles"]));//原本的title是从前端传过来,现改为统一用_data里的columns

    //    if (context.Request.Form["dataGetter"] != null)
    //    {
    //        export.Data(context.Request.Form["dataGetter"]);
    //        var columns = (export._data as PagingResult).columns as StoreColumnCollection;//这里可以扩展为多表头
    //        export.Title(new List<List<StoreColumn>> { columns });
    //    }

    //    if (context.Request.Form["fileType"] != null)
    //        export.Export(context.Request.Form["fileType"]);

    //    if (context.Request.Form["compressType"] != null)
    //        export.Compress(context.Request.Form["compressType"]);

    //    return export;
    //}


//    public Exporter Data(IDataGetter data)
//    {
//        //_data = data.GetData(HttpContext.Current);
//        _data = data.GetData(_controller, HttpContext.Current);
//        return this;
//    }
//
//    public Exporter Data(String type)
//    {
//        var dataGetter = GetActor<IDataGetter>(_dataGetter, DEFAULT_DATAGETTER, type);
//        return Data(dataGetter);
//    }

    public Exporter Data(Object data)
    {
        _data = data;
        return this;
    }

//    public Exporter AddFormatter(String field, IFormatter formatter)
//    {
//        _fieldFormatter[field] = formatter;
//        return this;
//    }

    public Exporter Title(ArrayList<ArrayList<StoreColumn>> title)
    {
        _title = title;
        return this;
    }
    public Exporter Title(StoreColumnCollection columns)
    {
        columns.forEach(a ->
        {
            if ((!a.GetChildren().isEmpty()) && a.GetAllLeafCount(b -> b.get_visible()) < 1)//如果不是叶节点且所有子叶节点都隐藏,那么本父节点也隐藏
            {
                a.set_visible(false);
            }
        });
        int maxDepth = new StoreColumn (){{ 
        	SetChildren(columns); 
        			}}.GetDepth() - 1;
        			SGRef<Integer> maxDepthRef=new SGRef<Integer>(maxDepth); 
        			ArrayList<ArrayList<StoreColumn>> title = new ArrayList<ArrayList<StoreColumn>>();
        	    	//SGRef<List<List<StoreColumn>>> titleRef = new SGRef<List<List<StoreColumn>>>(title);
        	    	//SGRef<List<List<StoreColumn>>> titleRef = new SGRef<List<List<StoreColumn>>>();
        	    	SGRef<ArrayList<ArrayList<StoreColumn>>> titleRef = new SGRef<ArrayList<ArrayList<StoreColumn>>>(title);
        StoreColumnCollection.StoreColumnTo2DArray( titleRef, columns, maxDepthRef);
        _title = title;
        _columns = columns;
        return this;
    }

    /// <summary>
    /// 下载的文件名
    /// </summary>
    /// <param name="fileName"></param>
    /// <returns></returns>
    public Exporter FileName(String fileName)
    {
        _fileName = fileName;
        return this;
    }

    public Exporter Export(String type)
    {
    	IExport export = this.<IExport>GetActor(_export, DEFAULT_EXPORT, type);
        return Export(export);
    }

    public Exporter Export(IExport export)
    {
        int[] i = new int[] {0};
        
        //如果要这段,移到初始化那样(可以根据实际data类型来)
//        if (_title == null)
//        {
//            _title = new ArrayList<ArrayList<StoreColumn>>();
//            _title.add(new ArrayList<StoreColumn>());
//            //PFDataHelper.EachListHeader(_data, (i, field, type) => _title[0].Add(new StoreColumn() { title = field, field = field, rowspan = 1, colspan = 1 }));
//            PFDataHelper.EachListHeader(_data, (a, field, type) -> _title[0].Add(new StoreColumn()
//            {{
//                title = field,
//                data = field
//                //, rowspan = 1, colspan = 1
//            }}));
//        }

        Map<Integer, Integer> currentHeadRow = new HashMap<Integer, Integer>();
        Map<String, List<Integer>> fieldIndex = new HashMap<String, List<Integer>>();
        //SGRef<Map<String, List<Integer>>> fieldIndexRef = new SGRef<Map<String, List<Integer>>>(fieldIndex);

        int[] titleRowCount = new int[] {0};
        if (!SGDataHelper.StringIsNullOrWhiteSpace(_sheetTitle))
        {
            titleRowCount[0]++;
        }
        Function<Integer, Integer> GetCurrentHeadRow = cell -> currentHeadRow.containsKey(cell) ? currentHeadRow.get(cell) : titleRowCount[0];
//        int currentRow = 0;
//        SGRef<Integer> currentRowRef = new SGRef<Integer>(0);
        int[] currentRow = new int[] {0};
        int currentCell = 0;

        //export.Init(_data, _printPageScheme);
        export.Init( _printPageScheme);

        //标题--wxj20181011
        StoreColumn temp = new StoreColumn() {{ SetChildren( _columns );}};
        int columnCount = temp.GetAllLeafCount(a -> a.get_visible());
        //String firstData = temp.FirstLeaf(a -> true).data;
        if (!SGDataHelper.StringIsNullOrWhiteSpace(_sheetTitle))
        {
            export.FillData(0, 0, //firstData,
            		_sheetTitle);

            export.SetTitleStyle(0, 0, columnCount - 1, 0);
            currentRow[0]++;
        }

        //Stopwatch sw = new Stopwatch();
        //sw.Start();
        //int resultCount = 0;
        //#region 3秒(后来发现只有调试时特别慢,原因未明)
        ////生成多行题头
        for (i[0] = 0; i[0] < _title.size(); i[0]++)
        {
            currentCell = 0;

            ArrayList<StoreColumn> titleI=_title.get(i[0]);
            for (int j = 0; j < titleI.size(); j++)
            {
            	StoreColumn item = titleI.get(j);
                if (item.get_visible() == false) { continue; }//隐藏列不导出--wxj20181009

                while (currentRow[0] < GetCurrentHeadRow.apply(currentCell))
                { currentCell++;}

                //export.FillData(currentCell, currentRow, "title_" + item.data, item.title);
                export.FillData(currentCell, currentRow[0], //"title_" + item.data, 
                		item.title==null? item.data:item.title);//e:\svn\businesssys2018\yjquery.web\areas\bonus\views\reportquery05\treegrid.cshtml里的title是null

                if (item.get_rowspan() + item.get_colspan() > 2)
                    {
                	export.MergeCell(currentCell, currentRow[0], currentCell + item.get_colspan() - 1, currentRow[0] + item.get_rowspan() - 1);
                    }

                if (!SGDataHelper.StringIsNullOrWhiteSpace(item.data))
                {
                    if (!fieldIndex.containsKey(item.data))
                        {fieldIndex.put(item.data,new ArrayList<Integer>());}
                    //fieldIndex[item.data].Add(currentCell);
                    fieldIndex.get(item.data).add(currentCell);
                }

                for (int k = 0; k < item.get_colspan(); k++)
                {
                    currentHeadRow.put(currentCell, GetCurrentHeadRow.apply(currentCell++) + item.get_rowspan());
                }
                //resultCount++;
            }
            currentRow[0]++;
        }
        //#endregion
        //#region 一样是3秒
        ////生成多行题头
        //foreach (var ii in _title)
        //{
        //    currentCell = 0;

        //    foreach (var j in ii)
        //    {
        //        //var item = _title[i][j];
        //        var item = j;
        //        if (item.visible == false) { continue; }//隐藏列不导出--wxj20181009
        //        //if (item.hidden) continue;

        //        while (currentRow < GetCurrentHeadRow(currentCell))
        //            currentCell++;

        //        //export.FillData(currentCell, currentRow, "title_" + item.data, item.title);
        //        export.FillData(currentCell, currentRow, "title_" + item.data, item.title ?? item.data);//e:\svn\businesssys2018\yjquery.web\areas\bonus\views\reportquery05\treegrid.cshtml里的title是null

        //        if (item.rowspan + item.colspan > 2)
        //            export.MergeCell(currentCell, currentRow, currentCell + item.colspan - 1, currentRow + item.rowspan - 1);

        //        if (!PFDataHelper.StringIsNullOrWhiteSpace(item.data))
        //        {
        //            if (!fieldIndex.ContainsKey(item.data))
        //                fieldIndex[item.data] = new List<int>();
        //            fieldIndex[item.data].Add(currentCell);
        //        }

        //        for (var k = 0; k < item.colspan; k++)
        //            currentHeadRow[currentCell] = GetCurrentHeadRow(currentCell++) + item.rowspan;
        //    }
        //    currentRow++;
        //} 
        //#endregion

        //sw.Stop();
        //var aa = String.Format("插入{0}条记录共花费{1}毫秒，{2}分钟", resultCount, sw.ElapsedMilliseconds, sw.ElapsedMilliseconds / 1000 / 60);

        //设置题头样式
        //export.SetHeadStyle(0, 0, currentCell - 1, currentRow - 1);
        export.SetHeadStyle(0, titleRowCount[0], currentHeadRow.size() - 1, currentRow[0] - 1);//上面那样,当后面的列不是多表头时,背景色只填到最后一个多表头为止

        ////设置数据样式
        //int dataCount = 0;
        SGRef<Integer> dataCount = new SGRef<Integer>(0);
        if (_data instanceof PagingResult)
        {
        	PagingResult data =SGDataHelper.< PagingResult>ObjectAs(_data);
//        	List<TreeListItem> list1 = PFDataHelper.<List<TreeListItem>>ObjectAs(data.data);
//            if (list1 != null)
//            {
//                //(new TreeListItem() {{ SetChildren(list1);} }).EachChild(a -> dataCount++);
//                (new TreeListItem() {{ SetChildren(list1);} }).EachChild(a -> dataCount.SetValue(dataCount.GetValue()+1));
//            }else {
//                for (int rowIndex = 0; rowIndex < data.data.size(); rowIndex++)
//                {
////                    dataCount++;
//                	dataCount.SetValue(dataCount.GetValue()+1);
//                }
//            }

            if (data.data != null&&data.data.size()>0&&data.data.get(0) instanceof TreeListItem)
            {
            	List<TreeListItem> list1 = SGDataHelper.<List<TreeListItem>>ObjectAs(data.data);
                //(new TreeListItem() {{ SetChildren(list1);} }).EachChild(a -> dataCount++);
                (new TreeListItem() {{ SetChildren(list1);} }).EachChild(a -> dataCount.SetValue(dataCount.GetValue()+1));
            }else {
                for (int rowIndex = 0; rowIndex < data.data.size(); rowIndex++)
                {
//                    dataCount++;
                	dataCount.SetValue(dataCount.GetValue()+1);
                }
            }
        }
        else
        {
            //PFDataHelper.EachListRow(_data, (a, r) => dataCount++);//原版
        }
        //export.SetRowsStyle(0, currentRow, currentCell - 1, currentRow + dataCount - 1);
        export.SetRowsStyle(0, currentRow[0], currentHeadRow.size() - 1, currentRow[0] + dataCount.GetValue() - 1);//上面那样,当后面的列不是多表头时,边框不见了

        //填充数据
        if (_data instanceof PagingResult)
        {
            // var data = _data as PagingResult;
        	PagingResult data =SGDataHelper.< PagingResult>ObjectAs(_data);
            //if (data.data is List<TreeListItem>)
            if (data.data!=null&&(!data.data.isEmpty())&& data.data.get(0) instanceof TreeListItem)
            {
                export.SetFont(0, currentRow[0], 0, currentRow[0] + dataCount.GetValue() - 1, "宋体");//默认的Arial字体中,树型的┝等符号对不齐--benjamin20190711
                TreeListItem tree = new TreeListItem();
//                tree.SetChildren(data.data as List<TreeListItem>);
                tree.SetChildren(SGDataHelper.< List<TreeListItem>>ObjectAs(data.data) );
//                int rowIndex = 0;
                SGRef<Integer> rowIndex = new SGRef<Integer>(0);
                //int colIndex = 0;
                com.sellgirl.sgJavaHelper.TreeMatrix matrix = new com.sellgirl.sgJavaHelper.TreeMatrix(SGDataHelper.ListSelect(tree.GetChildren(), a->a));
                tree.EachChild((a, deep) ->
                {
                    //colIndex = 0;
                    SGDataHelper.EachObjectProperty(a.GetData(), (b, name, value) ->
                    {
                        if (fieldIndex.containsKey(name))
                        {
                            for(int cellIndex : fieldIndex.get(name))
                            {
                                if (_fieldFormatter.containsKey(name))
                                {
                                    value = _fieldFormatter.get(name).Format(value);
                                }
                                //if (colIndex == 0)
                                if (cellIndex == 0)
                                {
                                    String line = "";
                                    for (int j = 0; j < deep - 2; j++)
                                    {
                                        //line += String.Format("<div class='{0} {1}'></div>", "tree-tr-linearea ", GetClassByTreeMatrixNetLine(matrix.GetNetLine(j, rowIdx)));
                                        line += matrix.GetNetLineString(j, rowIndex.GetValue());
                                    }
                                    value = line + SGDataHelper.ObjectToString(value);
                                    //var line = GetClassByTreeMatrixNetLine(matrix.GetNetLine(cellIndex, currentRow))
                                }
                                export.FillData(cellIndex, currentRow[0],// name,
                                		value);
                            }
                            //colIndex++;
                        }
                    });
//                    rowIndex++;
                    //currentRow++;
                    rowIndex.SetValue(rowIndex.GetValue()+1);
                    currentRow[0]++;
                });
            }
            else
            {
                List<?> list = data.data;
                for (int rowIndex = 0; rowIndex < list.size(); rowIndex++)
                {
                	Map<String, Object> rowData =SGDataHelper.ObjectAs(list.get(rowIndex)) ;

             	   Iterator<Entry<String, Object>> iter = rowData.entrySet().iterator();
             	   while(iter.hasNext()){
             		   Entry<String, Object> key=iter.next();
             		  String name = key.getKey();
                       Object value = key.getValue();

                       if (fieldIndex.containsKey(name))
                           for(int cellIndex : fieldIndex.get(name))
                           {
                               if (_fieldFormatter.containsKey(name))
                               { value = _fieldFormatter.get(name).Format(value);}
                               export.FillData(cellIndex, currentRow[0], //name, 
                            		   value);
                           }
             	   }

             	   
//                    for (i = 0; i < rowData.size(); i++)
//                    {
//                        var name = rowData.get(i).Key;
//                        var value = rowData.ElementAt(i).Value;
//
//                        if (fieldIndex.ContainsKey(name))
//                            for(int cellIndex : fieldIndex.get(name))
//                            {
//                                if (_fieldFormatter.ContainsKey(name))
//                                    value = _fieldFormatter[name].Format(value);
//                                export.FillData(cellIndex, currentRow, name, value);
//                            }
//                    }
                    currentRow[0]++;
                }
            }
        }
        else
        {
//            //原版
//            PFDataHelper.EachListRow(_data, (rowIndex, rowData) ->
//            {
//                PFDataHelper.EachObjectProperty(rowData, (a, name, value) ->
//                {
//                    if (fieldIndex.ContainsKey(name))
//                        for (int cellIndex : fieldIndex.get(name))
//                        {
//                            if (_fieldFormatter.ContainsKey(name))
//                                value = _fieldFormatter[name].Format(value);
//                            export.FillData(cellIndex, currentRow, name, value);
//                        }
//                });
//                currentRow++;
//            });
        }

        //汇总行
        Boolean[] hasSummary = new Boolean[] {false};
        i[0] = 0;
        int[] firstSummary =new int[] { 0};//第一个有汇总的格的位置
        String[] firstSummaryField =new String[] { ""};
        (new StoreColumn (){{ SetChildren (_columns); }}).EachLeaf(a ->
        {
            //设置列宽--wxjtodo20190417
            if (!SGDataHelper.StringIsNullOrWhiteSpace(a.width))
            {
                //export.SetColumnWidth(i, PFDataHelper.WebWidthToExcel(a.width).Value);
                export.SetColumnWidth(i[0], a.width);
            }
            //if (a.excelWidth.HasValue)
            //{
            //    export.SetColumnWidth(i, double.Parse(a.width.Replace("px", "")));
            //}
            //var column = _title[_title.Count - 1][i];
            if (a.get_visible())
            {
            	StoreColumn column = a;
                if (!hasSummary[0]) { firstSummary[0] = i[0]; firstSummaryField[0] = column.data; }
                if (column.summary != null)
                {
                    hasSummary[0] = true;
                    export.FillData(i[0], currentRow[0], //column.data, 
                    		column.summary);
                }
                i[0]++;
            }
        });
        if (hasSummary[0])
        {
            export.FillData(firstSummary[0] - 1, currentRow[0], //firstSummaryField[0], 
            		"合计：");
            export.SetRowsStyle(0, currentRow[0], columnCount - 1, currentRow[0]);//上面那样,当后面的列不是多表头时,边框不见了
            currentRow[0]++;
        }

        //Foot--wxj20181011
        if (!SGDataHelper.StringIsNullOrWhiteSpace(_sheetFoot))
        {
            export.FillData(0, currentRow[0],// firstData,
            		_sheetFoot);

            export.SetFootStyle(0, currentRow[0], columnCount - 1, currentRow[0]);

            //titleRowCount++;
            currentRow[0]++;
        }

        _exporter = export;
        //_fileStream = export.SaveAsStream();

        //_suffix = export.getSuffix();
        if (SGDataHelper.StringIsNullOrWhiteSpace(_fileName))
            _fileName = SGDate.Now().toString("yyyyMMddHHmmss");

        return this;
    }
    public IExport GetExport()//--wxj20181011
    {
        return _exporter;
    }
    //public Exporter SaveStream()//--wxj20181011
    //{
    //    _fileStream = _exporter.SaveAsStream();
    //    return this;
    //}

    //public Exporter PrintPageScheme()
    //{
    //    _fileStream = _exporter.SaveAsStream();
    //    return this;
    //}

//    public Exporter Compress(String type)
//    {
//        var compress = GetActor<ICompress>(_compress, DEFAULT_COMPRESS, type);
//        return Compress(compress);
//    }

//    public Exporter Compress(ICompress compress)
//    {
//        _fileStream = compress.Compress(_fileStream, String.Format("{0}.{1}", _fileName, _suffix));
//        _suffix = compress.Suffix(_suffix);
//        return this;
//    }

    //private void SaveToLocal(String fileName)
    //{
    //    //测试xlsx下载后打不开的问题，暂保存到本地试试--benjamin todo
    //    var path = Path.Combine(PFDataHelper.BaseDirectory, "output", fileName);
    //    var directoryName = Path.GetDirectoryName(path);
    //    PFDataHelper.DeleteFile(path);
    //    PFDataHelper.CreateDirectory(directoryName);
    //    var tmpEx = _exporter as XlsxExport;
    //    if (tmpEx != null)
    //    {
    //        tmpEx.workbook.Save(path);
    //    }
    //}
    public void WriteToStream(OutputStream stream) {
    	_exporter.WriteToStream(stream);
    }
    public void Download()
    {
//        ////var tmpEx = _exporter as XlsxExport;
//        //var tmpEx = _exporter as XlsxExport1048576;
//        Aspose.Cells.Workbook book = null;
//        if (_exporter is XlsxExport1048576) { book = (_exporter as XlsxExport1048576).workbook; }
//        if (_exporter is XlsxExport) { book = (_exporter as XlsxExport).workbook; }
//        if (book != null)
//        {
//            //if (PFDataHelper.IsDebug)
//            //{
//            //    var fileName = String.Format("test_{0}.{1}", _fileName, _suffix);
//            //    var tmpFileName = Guid.NewGuid().ToString("N") + DateTime.Now.ToString("yyyyMMddHHmmss") + fileName;
//            //    var path = Path.Combine(PFDataHelper.BaseDirectory, "TempFile", tmpFileName);
//            //    var directoryName = Path.GetDirectoryName(path);
//            //    PFDataHelper.CreateDirectory(directoryName);
//            //    //book.Save(path, Aspose.Cells.FileFormatType.Xlsx);
//            //    book.Save(path, Aspose.Cells.SaveFormat.Xlsx);
//            //}
//            PFDataHelper.DownloadExcel(HttpContext.Current, book, String.Format("{0}.{1}", _fileName, _suffix), PFDataHelper.GetConfigMapper().GetNetworkConfig().DownloadSpeed);
//        }
//        else
//        {
//            //SaveToLocal("excelPo.xlsx");
//
//            if (_fileStream == null)
//            {
//                _fileStream = _exporter.SaveAsStream();
//                //SaveToLocal("excelPoAfterSaveStream.xlsx");
//            }
//            if (_fileStream != null && _fileStream.Length > 0)
//            {
//
//                //PFDataHelper.DownloadFile(HttpContext.Current, _fileStream, String.Format("{0}.{1}", _fileName, _suffix), 1024 * 1024 * 10);
//                PFDataHelper.DownloadFile(HttpContext.Current, _fileStream, String.Format("{0}.{1}", _fileName, _suffix), PFDataHelper.GetConfigMapper().GetNetworkConfig().DownloadSpeed);
//            }
//        }
//
//        _exporter.Dispose();
    }

    private <T> T GetActor(Map<String, Class<?>> dict, String defaultKey, String key)
    {
        if (!dict.containsKey(key)) {
            key = defaultKey;}

        try {
			return SGDataHelper.<T>ObjectAs(dict.get(key).newInstance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return null;
    }

    public void Dispose()
    {
        if (_fileStream != null)
        {
            _fileStream.close();                
        }
        //2848 kbs
        //_data = null;
        //PFDataHelper.DisaposeObject<PagingResult>(_data);
        SGDataHelper.DisaposeObject(_data);
        _data = null;
        //if(_data!=null&&_data is PagingResult)
        //{
        //    (_data as PagingResult).Dispose();
        //    _data = null;
        //}
        SGDataHelper.GCCollect();
        //GC.Collect(); //2848-500=2329
    }
}
