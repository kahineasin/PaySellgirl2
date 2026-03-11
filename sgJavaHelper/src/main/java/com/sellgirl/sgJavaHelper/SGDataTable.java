package com.sellgirl.sgJavaHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import java.util.Map.Entry;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

//import pf.java.pfHelper.config.DataRow;

public class SGDataTable {

	public List<PFDataRow> Rows=new ArrayList<PFDataRow>();
    public PFDataColumnCollection Columns=new PFDataColumnCollection();//NewRow需要用到--benjamin20191114

    public Map<String,Object> ExtendedProperties=new HashMap<String,Object>();
    public SGDataTable() {
 
    }
 
    public SGDataTable(List<PFDataRow> r) {
        Rows = r;
        if(Columns.isEmpty()&&(!r.isEmpty())) {
        	for(PFDataColumn col :r.get(0).getCol()) {
        		Columns.add(col);
        	}
        }
    }
    public SGDataTable(String title,String msg) {
		//List<PFDataColumn> col = null;// 行所有列集合
		PFDataRow r = null; // 单独一行
		
		//col = new ArrayList<PFDataColumn>();
		// 此处开始列循环，每次向一行对象插入一列
		//for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			// String columnName = rsmd.getColumnName(i);
			PFDataColumn c = null;// 单独一列
			String columnName =title;
			String value = msg;
			// 初始化单元列
			c = new PFDataColumn(columnName, value);
			//c.setSqlType(rsmd.getColumnType(i));
			c.setPFDataType(SGSqlFieldTypeEnum.String);
			// 将列信息加入列集合
//			col.add(c);
			Columns.add(c);
		//}
		// 初始化单元行
		r = new PFDataRow(Columns);
		// 将行信息降入行结合
		Rows.add(r);
    }
 
 
    public List<PFDataRow> getRow() {
        return Rows;
    }
 
    public void setRow(List<PFDataRow> row) {
        this.Rows = row;
    }
    public Boolean IsEmpty() {
        return Rows==null||Rows.isEmpty();
    }
    public PFDataRow NewRow() {
    	PFDataRow newRow=new PFDataRow(Columns.TClone());
    	//Rows.add(newRow);//C#中的NewRow()也只是创建,但没有立刻add到Rows里的
        return newRow;
    }

    /// <summary>
    /// DataTable 转换为List 集合
    /// </summary>
    /// <typeparam name="TResult">类型</typeparam>
    /// <param name="dt">DataTable</param>
    /// <param name="eachRow">(o,dr)=></param>
    /// <returns></returns>
    public  <T> List<T> ToList(Class<T> cls, SGAction<T, PFDataRow,Object> eachRow 
        )
    {
    	
        //创建一个属性的列表
        List<Field> prlist = new ArrayList<Field>();
    	Map<String, Field> prMap =SGDataHelper.GetProperties(cls);
        //获取TResult的类型实例  反射的入口
        //Type t = typeof(T);
        //获得TResult 的所有的Public 属性 并找出TResult属性和DataTable的列名称相同的属性(PropertyInfo) 并加入到属性列表 
        //Array.ForEach<PropertyInfo>(t.GetProperties(), p => { if (dt.Columns.IndexOf(p.Name) != -1) prlist.Add(p); });
        

 	   Iterator<Entry<String, Field>> iter = prMap.entrySet().iterator();
 	   while(iter.hasNext()){
 		   Entry<String, Field> key=iter.next();
 		   if(this.Columns.contains(key.getKey())) {
 			  prlist.add(key.getValue());
 		   }
 	   }
        //创建返回的集合
        List<T> oblist = new ArrayList<T>();
        String currentColumnName = "";
        try
        {
            for (PFDataRow row : Rows)
            {
                //创建TResult的实例
                T ob = cls.getConstructor().newInstance();
                //找到对应的数据  并赋值
                for(Field p :prlist)
                {
                	Object v=row.getColumn(p.getName());
                    if (v != null && v != null)
                    {
                        currentColumnName = p.getName();
                        //var pt = PFDataHelper.GetPropertyType(p);
                        Class<?> pt = p.getType();
                        //PFSqlFieldType pfType=PFSqlFieldType.InitByClass(pt);
                        if (pt.isEnum())
                        {
                            //p.set(ob, PFDataHelper.ObjectToEnum((Class<Enum>)pt, v));

                    		@SuppressWarnings({ "unchecked", "rawtypes" })
                    		Object ev=SGDataHelper.ObjectToEnum((Class<Enum>)pt, v);
                            p.set(ob, ev);
                        }
                        //else if (pt.IsSubclassOf(typeof(PFCustomStringType)))
                        //{
                        //    p.SetValue(ob, row[p.Name].ToString(), null);
                        //}
                        else
                        {
                            //if (pt == typeof(decimal) && dt.Columns[p.Name].DataType == typeof(int))
                            //{
                            //    //由于某些数据库版本不统一的问题会导致,inv表的pv是decimal的，但tc_inv表的pv是int的，于是建model时tc_inv都采用decimal就行了
                            //}

                            //p.set(ob,PFDataHelper.ConvertObjectByType(v, Columns.get(p.getName()).getDataType(), pt));
                            //p.set(ob,PFDataHelper.ConvertObjectToPFTypeBySqlType(v, v.getClass(), pfType));
                        	Object v2=null;
                        	boolean b=false;
//                        	try {
                        		v2=SGDataHelper.ConvertObjectByType(v, v.getClass(), pt);
//                        		 b=p.isAccessible(); 
                            p.set(ob,v2);
//                        	}catch(Exception e) {
//                        		int a=1;
//                        	}

                        }
                    }
                };
                if (eachRow != null) { eachRow.go(ob, row,null); }
                //放入到返回的集合中.
                oblist.add(ob);
            }
        }
        catch (Exception e)
        {
            String msg = SGDataHelper.FormatString("DataTableToList方法报错,当前字段:{0},错误信息:{1}", currentColumnName, e);
            //PFDataHelper.WriteError(new Exception(msg,e));
            //PFDataHelper.WriteError(new Throwable(),new Exception(msg,e));
            SGDataHelper.WriteErrors(Arrays.asList(e,new Exception(msg)));
        }
        return oblist;
    }
    
    /// <summary>
    /// KVP比SelectList更容易操作item
    /// </summary>
    /// <param name="dt"></param>
    /// <param name="dataValueField"></param>
    /// <param name="dataTextField"></param>
    /// <returns></returns>
    public List<Pair<Object, Object>> ToKVList( String dataValueField, String dataTextField)
    {
        //创建返回的集合
        List<Pair<Object, Object>> oblist = new ArrayList<Pair<Object, Object>>();
//        if (dt != null)
//        {
            for(PFDataRow row : Rows)
            {
            	
                //创建TResult的实例
               
            	//Map<Object, Object> ob = new HashMap<Object, Object>(row.getColumn(dataValueField), row.getColumn(dataTextField));
            	//Entry<Object, Object> ob=new  Entry<Object, Object>();
            	//Pair<Object, Object> keyValue = new ImmutablePair(row.getColumn(dataValueField), row.getColumn(dataTextField));
            	Pair<Object, Object> p=ImmutablePair.of(row.getColumn(dataValueField), row.getColumn(dataTextField)); 
                oblist.add(p);
            }
//        }
        return oblist;
    }
    public ArrayList<LinkedHashMap<String,Object>> ToDictList()
    {
        //创建返回的集合
    	ArrayList<LinkedHashMap<String,Object>> oblist = new ArrayList<LinkedHashMap<String,Object>>();
//        if (dt != null)
//        {
            for(PFDataRow row : Rows)
            {
            	
//                //创建TResult的实例
//               
//            	//Map<Object, Object> ob = new HashMap<Object, Object>(row.getColumn(dataValueField), row.getColumn(dataTextField));
//            	//Entry<Object, Object> ob=new  Entry<Object, Object>();
//            	//Pair<Object, Object> keyValue = new ImmutablePair(row.getColumn(dataValueField), row.getColumn(dataTextField));
//            	Pair<Object, Object> p=ImmutablePair.of(row.getColumn(dataValueField), row.getColumn(dataTextField)); 
//                oblist.add(p);
                
                LinkedHashMap<String,Object> dictionary = new LinkedHashMap<String,Object>();
                for (PFDataColumn dataColumn : Columns)
                {
                    dictionary.put(
                    		(dataColumn.getKey().indexOf(".") > -1 
                    		? dataColumn.getKey().replace(".", "_") 
                    				: dataColumn.getKey())
//                        , useStringValue ? dataRow[dataColumn.ColumnName].ToString() : dataRow[dataColumn.ColumnName])
                    		, row.getColumn(dataColumn.getKey())
                    )
                    ;
                }
                oblist.add(dictionary);
            }
//        }
        return oblist;
    }

    /// <summary>
    /// Table汇总方法，如果不提供valueFields，默认对group之外的所有int或decimal字段汇总
    /// </summary>
    /// <param name="dt"></param>
    /// <param name="groupFields"></param>
    /// <param name="valueFields"></param>
    /// <returns></returns>
    public  SGDataTable DataTableGroupBy( String[] groupFields, PFKeyValueCollection<SummaryType> valueFields)
    {
        try
        {
        	SGDataTable dt=this;
        	String[][] groupFieldsF=new String[][] { groupFields};
        	PFDataColumnCollection columns = dt.Columns;
        	//HashMap<String, Type> columnType = new HashMap<String, Type>();
            //var valueColumnType = new Dictionary<String, Type>();
            Boolean allValue = valueFields == null;
            if (allValue) { valueFields = new PFKeyValueCollection<SummaryType>(); }
            SGDataTable result = new SGDataTable();
            List<PFDataRow> rowList = dt.Rows;
            //加列
            List<String> srcColumnNames = new ArrayList<String>();
            for (PFDataColumn dataColumn : columns)
            {
                if (Arrays.asList(groupFieldsF[0]).contains(dataColumn.getKey()))
                {
                    CopyTableColumn(dataColumn, result);
                    //result.Columns.Add(dataColumn.ColumnName, dataColumn.DataType).ExtendedProperties=dataColumn.ExtendedProperties;
                }
                else if (SGSqlFieldTypeEnum.Decimal.equals(dataColumn.getPFDataType()) 
                		|| SGSqlFieldTypeEnum.Int.equals(dataColumn.getPFDataType())
                    ||SGSqlFieldTypeEnum.Long.equals(dataColumn.getPFDataType()) 
                    )//其实字符串也可以汇总的，如Max之类，但好像没什么意义
                {
                    if (allValue)
                    {
                        valueFields.Add(dataColumn.getKey(), SummaryType.Sum);
                    }
                    //if (//allValue||
                    //    valueFields.Keys.Contains(dataColumn.ColumnName))
                    if (//allValue||
                        valueFields.ContainsKey(dataColumn.getKey()))
                    {
                        CopyTableColumn(dataColumn, result);
                        //result.Columns.Add(dataColumn.getKey(), dataColumn.DataType);
                    }
                }
                srcColumnNames.add(dataColumn.getKey());
            }
            //因为dt有可能经过其它代码groupby的，就会出现groupField的列不存在于dt的情况
            groupFieldsF[0] =SGDataHelper.ArrayWhere(String.class,groupFieldsF[0], a -> srcColumnNames.contains(a)) ;
//            valueFields = new PFKeyValueCollection<SummaryType>(valueFields.Where(a -> srcColumnNames.Contains(a.Key)).Select(b -> b).ToList());
            valueFields = new PFKeyValueCollection<SummaryType>(valueFields.stream().filter(a -> srcColumnNames.contains(a.Key)).collect(Collectors.toList()));

//            List<IGrouping<String, PFDataRow>> group = rowList
//                .GroupBy<PFDataRow, String>(dr ->
//                {
//                    var g = "";
//                    foreach (var i in groupFieldsF[0])
//                    {
//                        //g += ObjectToString(dr[i]) ?? "";//太慢
//                        g += (dr[i] ?? "").ToString();
//                    }
//                    return g;
//                }).ToList();//按A分组  
            Map<String,List<PFDataRow>> group = rowList.stream()
            		.collect(Collectors.groupingBy(dr->{
                        String g = "";
                        for (String i : groupFieldsF[0])
                        {
                            //g += ObjectToString(dr[i]) ?? "";//太慢
//                            g += (dr.getColumn()[i] ?? "").ToString();
                            Object c=dr.getColumn(i);
                            g += c==null?"":c.toString();
                        }
                        return g;
            		}));
     	   Iterator<Entry<String,List<PFDataRow>>> iter = group.entrySet().iterator();
    	   while(iter.hasNext()){
    		   Entry<String,List<PFDataRow>> ig=iter.next();
    	   //}
            //for (IGrouping<String, PFDataRow> ig : group){
    		   
                //创建一个PFDataRow实例
                PFDataRow row = result.NewRow();
                //分组列
                PFDataRow f = ig.getValue().get(0);//用于得到分组
                for (String i : groupFieldsF[0])
                {
                    //row[i]=ig.Key[i]//这样写要改上面group的对象为Dictionary
                    row.set(i,f.getColumn(i));
                }
                //值列
                for (PFKeyValue<SummaryType> i : valueFields)
                {
                    if (!IsColumnCompute(result.Columns.get(i.Key)))//不是计算列才设置值
                    {
                    	SGSqlFieldTypeEnum dataType = columns.get(i.Key).getPFDataType();
                        if (i.Value == SummaryType.Sum)
                        {
                            if (SGSqlFieldTypeEnum.Decimal.equals(dataType)
                            	||SGSqlFieldTypeEnum.Long.equals(dataType)	)
                            {
                            	Long s=ig.getValue().stream().mapToLong(r->SGDataHelper.ObjectToLong(r.getColumn(i.Key))).sum();
                                row.set(i.Key,s);
                            }
                            else if (SGSqlFieldTypeEnum.Int.equals(dataType) )
                            {
                                int s=ig.getValue().stream().mapToInt(r->SGDataHelper.ObjectToInt(r.getColumn(i.Key))).sum();
                                row.set(i.Key,s);
                            }
//                            else if (dataType == typeof(double))
//                            {
//                                row[i.Key] = ig.Sum(delegate (PFDataRow r)
//                                {
//                                    return PFDataHelper.ObjectToDouble(r[i.Key]);
//                                });
//                            }
//                            else if (dataType == typeof(long))
//                            {
//                                row[i.Key] = ig.Sum(delegate (PFDataRow r)
//                                {
//                                    return PFDataHelper.ObjectToLong(r[i.Key]);
//                                });
//                            }
                        }
                        if (i.Value == SummaryType.Average)
                        {
                            if (SGSqlFieldTypeEnum.Decimal.equals(dataType)
                                	||SGSqlFieldTypeEnum.Long.equals(dataType))
                            {
                                double s=ig.getValue().stream().mapToLong(r->SGDataHelper.ObjectToLong(r.getColumn(i.Key))).average().getAsDouble();
                                row.set(i.Key,s);
                            }
                            else if (SGSqlFieldTypeEnum.Int.equals(dataType) )
                            {
                            	double s=ig.getValue().stream().mapToInt(r->SGDataHelper.ObjectToInt(r.getColumn(i.Key))).average().getAsDouble();
                                row.set(i.Key,s);
                            }
//                            else if (dataType == typeof(double))
//                            {
//                                row[i.Key] = ig.Average(delegate (PFDataRow r)
//                                {
//                                    return PFDataHelper.ObjectToDouble(r[i.Key]);
//                                });
//                            }
//                            else if (dataType == typeof(long))
//                            {
//                                row[i.Key] = ig.Average(delegate (PFDataRow r)
//                                {
//                                    return PFDataHelper.ObjectToLong(r[i.Key]);
//                                });
//                            }
                        }
                    }

                }

                //加入到PFDataTable
                result.Rows.add(row);
            }
            //ExtProp
            for (String key : dt.ExtendedProperties.keySet())
            {
                result.ExtendedProperties.put(key,dt.ExtendedProperties.get(key));
            }

            return result;
        }
        catch (Exception e)
        {
            //PFDataHelper.WriteError(e);
            //PFDataHelper.WriteError(new Throwable(),e);
            SGDataHelper.WriteError(e);
        }
        return null;
    }
    private static void CopyTableColumn(PFDataColumn srcColumn, SGDataTable dst)
    {
    	PFDataColumn dstColumn = dst.Columns.add(srcColumn.getKey(), srcColumn.getDataType());
        for (String key : srcColumn.ExtendedProperties.keySet())
        {
            dstColumn.ExtendedProperties.put(key,srcColumn.ExtendedProperties.get(key));
        }
        //benjamintodo
        //if (srcColumn.Expression != null) { dstColumn.Expression = srcColumn.Expression; }
    }
    public static Boolean IsColumnCompute( PFDataColumn col)
    {
    	if(col.ExtendedProperties.containsKey("compute")) {
            Object p = col.ExtendedProperties.get("compute");
            return p != null && ((Boolean)p).equals(true);
    	}
    	return false;
    }
    
    /**
     * @功能描述 双表根据两表关联字段连接，要求两表必须包含公告字段，并且每一行数据公共字段相等 。dt1对应colname1 ,dt2
     *       对应colName2
     * @可能的错误 未完成
     * @作者 叶小钗
     * @修改说明
     * @修改人
     */
    public static SGDataTable joinTable(SGDataTable dt1, SGDataTable dt2, String colName1,
            String colName2) {
        List<PFDataRow> newRows = new ArrayList<PFDataRow>();
 
        List<PFDataRow> rows1 = dt1.getRow();
        List<PFDataRow> rows2 = dt2.getRow();
 
        int i1 = rows1.size();
        int i2 = rows2.size();
 
        List<PFDataRow> temp = new ArrayList<PFDataRow>();
        String tempC = "";
        if (i1 > i2) {
            temp = rows1;
            rows1 = rows2;
            rows2 = temp;
            tempC = colName1;
            colName1 = colName2;
            colName2 = tempC;
        }
        for (PFDataRow r1 : rows1) {
            String value1 = r1.eval(colName1);
            for (PFDataRow r2 : rows2) {
                String value2 = r2.eval(colName2);
                if (value1.equals(value2)) {
                    List<PFDataColumn> cols = new ArrayList<PFDataColumn>();
                    for (PFDataColumn c : r1.getCol()) {
                        cols.add(c);
                    }
                    for (PFDataColumn c : r2.getCol()) {
                        cols.add(c);
                    }
                    PFDataRow rr = new PFDataRow(cols);
                    newRows.add(rr);
                }
            }
        }
        SGDataTable dt = new SGDataTable(newRows);
        return dt;
    }
 
    public static void outTable(SGDataTable dt) {
        for (PFDataRow r : dt.getRow()) {
            for (PFDataColumn c : r.getCol()) {
                System.out.print(c.getKey() + ":" + c.getValue() + "  ");
            }
            wl("");
        }
    }
 
    public static void wl(String s) {
        System.out.println(s);
    }
}
