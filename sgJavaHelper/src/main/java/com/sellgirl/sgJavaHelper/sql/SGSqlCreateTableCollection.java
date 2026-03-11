package com.sellgirl.sgJavaHelper.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sellgirl.sgJavaHelper.PFClickHouseSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.PFSqlType;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;


/**
 * sqlserver版本
 */
public class SGSqlCreateTableCollection extends ArrayList<SqlCreateTableItem> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7159062743069521004L;
	//private String _charset = "utf-8";
	/**
	 * 数据库名(clickhouse建表时有用)
	 */
    public String DbName =null;
    public String TableName =null;
    /// <summary>
    /// 表备注
    /// </summary>
    public String TableComment =null;
    //public String Charset { get { return _charset; } set { _charset = value; } }
    public  String GetFieldQuotationCharacterL() {
    	//return "`";
    	return "[";
    }
    public  String GetFieldQuotationCharacterR() {
//    	return "`";
    	return "]";
    }
//    public  String FieldQuotationCharacterL { get { return "`"; } }//[
//    public  String FieldQuotationCharacterR { get { return "`"; } }//]
    public String[] PrimaryKey =null;
    /**
     * 目标表聚集索引
     */
    public String[] ClusteredIndex=null;
    public String[] TableIndex =null;
    /**
     * 大数据表中的分区属性,如clickhouse的PARTITION
     */
    public String[] Partition =null;
    /**
     * 大数据表(如clickhouse)中的分区属性
     */
    public String[] Order =null;
    public SGSqlCreateTableCollection() { }
    
    public static SGSqlCreateTableCollection Init(ISGJdbc jdbc) {
		try {
			//这里不判断ClickHouse了，因为这样如果项目没引用，也可以正常使用PFSqlExecute
			PFSqlType sqlType=jdbc.GetSqlType();
	    	if(sqlType==PFSqlType.ClickHouse) {
				//	Class.forName(jdbc.getDriverClassName());
	            return new PFClickHouseSqlCreateTableCollection();
	    	}else if(sqlType==PFSqlType.MySql||sqlType==PFSqlType.Tidb){
	    		return new PFMySqlCreateTableCollection();
	    	}else {
	    		return new SGSqlCreateTableCollection();
//	            return DriverManager.getConnection(jdbc.getUrl(), jdbc.getUsername(),jdbc.getPassword());
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }   
    public static SGSqlCreateTableCollection Init(ISGJdbc jdbc,List<SGSqlFieldInfo> fields) {
    	SGSqlCreateTableCollection models=SGSqlCreateTableCollection.Init(jdbc);

//		models.DbName = consumer.Databases;
//		models.TableName =consumer.Tables;
		//models.PrimaryKey = table.getIdColumnList().stream().map(a->a.getDstColumn()).toArray(String[]::new);
		models.PrimaryKey = fields.stream().filter(a->a.getIsPrimaryKey()).map(a->a.getFieldName()).toArray(String[]::new);
		//models.ClusteredIndex = tableClusteredIndexArray;
		//models.TableIndex = tableIndexArray;//最好把原表的索引拿过来 -- benjamin todo
		//models.Partition = transfer.DstTablePartition;
		//models.Order = transfer.DstTableOrder;
		//models.TableComment = tableComment;//最好把原表的备注拿过来 -- benjamin todo
		//ISqlExecute srcExec = PFSqlExecute.Init(table.getSrcJdbc());
		for(SGSqlFieldInfo f:fields) {
			SqlCreateTableItem m = new SqlCreateTableItem(f);
			models.add(m);
		}
    	return models;
    }   
//    /**
//     * BaseSqlUpdateCollection可以共用此方法
//     * @param action
//     * @param model
//     * @param names
//     */
//    public static void eachModelName(PFAction<String,Class<?>,Object> action,Object model,  String... names) {
//
//        //_model = model;
//        if (model instanceof Map)
//        {
//        	//Map<String, Object> modelDict = (Map<String, Object>)model;
//        	Map<String, Object> modelDict = PFDataHelper.ObjectAsMap(model);
//            //--benjamin todo
//            if (names != null && names.length > 0)
//            {
//                for (String i : names)
//                {
////                    //Add(i);
////                	SqlUpdateItem item=new SqlUpdateItem();
////                	item.Key=i;
////                	item.Value=modelDict.get(i);
////                	item.VType=item.Value==null?null:item.Value.getClass();
////                    super.put(i, item);
//                    
////                	SqlCreateTableItem item=new SqlCreateTableItem();
////                	item.FieldName=i;
//                	Object v=modelDict.get(i);
////                	item.FieldType=v==null?null:v.getClass();
////                    this.add(item);
//                	action.go(i, v==null?null:v.getClass(), null);
//                }
//            }
//            else
//            {
//         	   Iterator<Entry<String,Object>> iter = modelDict.entrySet().iterator();
//        	   while(iter.hasNext()){
//        		   Entry<String,Object> key=iter.next();
//        		   
////               	SqlUpdateItem item=new SqlUpdateItem();
////               	item.Key=key.getKey();
////               	item.Value=key.getValue();
////               	item.VType=item.Value==null?null:item.Value.getClass();
////                   super.put(item.Key, item);
//
////               	SqlCreateTableItem item=new SqlCreateTableItem();
////               	item.FieldName=key.getKey();
//               	Object v=key.getValue();
////               	item.FieldType=v==null?null:v.getClass();
////                   this.add(item);
//
//               	action.go(key.getKey(), v==null?null:v.getClass(), null);
//        	   }
//
//            }
//        }
//        else
//        {
//            ////_modelProperties = PFDataHelper.GetProperties(model.GetType());
//            //var modelProperties = PFDataHelper.GetProperties(model.GetType());
//            Field[] fields=model.getClass().getFields();
//            if (names != null && names.length > 0)
//            {
//                for (String i : names)
//                {
//                	Field field;
//					try {           
//						field = model.getClass().getField(i);
//						
////	                   	SqlUpdateItem item=new SqlUpdateItem();        	
////	                   	item.Key=i;
////	                   	item.Value=field.get(_model);
////	                   	item.VType=field.getDeclaringClass();
////	                   	item.PInfo=field;
////	                    super.put(item.Key, item);
//	                    
////	                   	SqlCreateTableItem item=new SqlCreateTableItem();
////	                   	item.FieldName=i;
//	                   	Object v=field.get(model);
////	                   	item.FieldType=v==null?null:v.getClass();
////	                       this.add(item);
//	                       
//	                      	action.go(i, v==null?null:v.getClass(), null);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//                    
//                    //base.Add(i, new SqlUpdateItem { Key = i, Value = modelProperties[i].GetValue(_model, null), VType = modelProperties[i].PropertyType, PInfo = modelProperties[i] });
//                }
//            }
//            else
//            {
//                for (Field i : fields)
//                {
////                   	SqlUpdateItem item=new SqlUpdateItem();
////                   	item.Key=i.getName();
////                   	try {
////						item.Value=i.get(model);
////					} catch (IllegalArgumentException e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
////					} catch (IllegalAccessException e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
////					}
////                   	item.VType=i.getDeclaringClass();
////                   	item.PInfo=i;
////                       super.put(item.Key, item);
//
//
//                   action.go(i.getName(), i.getDeclaringClass(), null);
//	                       
//                   // base.Add(i.Key, new SqlUpdateItem { Key = i.Key, Value = i.Value.GetValue(_model, null), VType = i.Value.PropertyType, PInfo = i.Value });
//                }
//            }
//        }
//    }
//    public void generateByModel(Object model,  String... names
//            )
//        {
//    	PFDataHelper.EachObjectPropertyType(row, handle);;
//    	eachModelName((a,b,c)->{
//           	SqlCreateTableItem item=new SqlCreateTableItem();
//           	item.FieldName=a;
//           	item.FieldType=b;
//               this.add(item);
//    	},model,names);
////            //_model = model;
////            if (model instanceof Map)
////            {
////            	//Map<String, Object> modelDict = (Map<String, Object>)model;
////            	Map<String, Object> modelDict = PFDataHelper.ObjectAsMap(model);
////                //--benjamin todo
////                if (names != null && names.length > 0)
////                {
////                    for (String i : names)
////                    {
//////                        //Add(i);
//////                    	SqlUpdateItem item=new SqlUpdateItem();
//////                    	item.Key=i;
//////                    	item.Value=modelDict.get(i);
//////                    	item.VType=item.Value==null?null:item.Value.getClass();
//////                        super.put(i, item);
////                        
////                    	SqlCreateTableItem item=new SqlCreateTableItem();
////                    	item.FieldName=i;
////                    	Object v=modelDict.get(i);
////                    	item.FieldType=v==null?null:v.getClass();
////                        this.add(item);
////                    }
////                }
////                else
////                {
////             	   Iterator<Entry<String,Object>> iter = modelDict.entrySet().iterator();
////            	   while(iter.hasNext()){
////            		   Entry<String,Object> key=iter.next();
////            		   
//////                   	SqlUpdateItem item=new SqlUpdateItem();
//////                   	item.Key=key.getKey();
//////                   	item.Value=key.getValue();
//////                   	item.VType=item.Value==null?null:item.Value.getClass();
//////                       super.put(item.Key, item);
////
////                   	SqlCreateTableItem item=new SqlCreateTableItem();
////                   	item.FieldName=key.getKey();
////                   	Object v=key.getValue();
////                   	item.FieldType=v==null?null:v.getClass();
////                       this.add(item);
////            	   }
////
////                }
////            }
////            else
////            {
////                ////_modelProperties = PFDataHelper.GetProperties(model.GetType());
////                //var modelProperties = PFDataHelper.GetProperties(model.GetType());
////                Field[] fields=model.getClass().getFields();
////                if (names != null && names.length > 0)
////                {
////                    for (String i : names)
////                    {
////                    	Field field;
////    					try {           
////    						field = model.getClass().getField(i);
////    						
//////    	                   	SqlUpdateItem item=new SqlUpdateItem();        	
//////    	                   	item.Key=i;
//////    	                   	item.Value=field.get(_model);
//////    	                   	item.VType=field.getDeclaringClass();
//////    	                   	item.PInfo=field;
//////    	                    super.put(item.Key, item);
////    	                    
////    	                   	SqlCreateTableItem item=new SqlCreateTableItem();
////    	                   	item.FieldName=i;
////    	                   	Object v=field.get(model);
////    	                   	item.FieldType=v==null?null:v.getClass();
////    	                       this.add(item);
////    					} catch (Exception e) {
////    						// TODO Auto-generated catch block
////    						e.printStackTrace();
////    					} 
////                        
////                        //base.Add(i, new SqlUpdateItem { Key = i, Value = modelProperties[i].GetValue(_model, null), VType = modelProperties[i].PropertyType, PInfo = modelProperties[i] });
////                    }
////                }
////                else
////                {
////                    for (Field i : fields)
////                    {
////                       	SqlUpdateItem item=new SqlUpdateItem();
////                       	item.Key=i.getName();
////                       	try {
////    						item.Value=i.get(_model);
////    					} catch (IllegalArgumentException e) {
////    						// TODO Auto-generated catch block
////    						e.printStackTrace();
////    					} catch (IllegalAccessException e) {
////    						// TODO Auto-generated catch block
////    						e.printStackTrace();
////    					}
////                       	item.VType=i.getDeclaringClass();
////                       	item.PInfo=i;
////                           super.put(item.Key, item);
////
////   	                   	SqlCreateTableItem item=new SqlCreateTableItem();
////   	                   	item.FieldName=i;
////   	                   	Object v=field.get(model);
////   	                   	item.FieldType=v==null?null:v.getClass();
////   	                       this.add(item);
////   	                       
////                       // base.Add(i.Key, new SqlUpdateItem { Key = i.Key, Value = i.Value.GetValue(_model, null), VType = i.Value.PropertyType, PInfo = i.Value });
////                    }
////                }
////            }
//        }

//    /// <summary>
//    /// 使用方法:       
//    ///SqlCreateTableCollection sqlCreateTable = new SqlCreateTableCollection(
//    ///    "monthly_finance_statistics",
//    ///    new MySqlInsertCollection(new MonthlyFinanceStatisticsCreate()),
//    ///    new String[] { "create_date" },
//    ///    null,
//    ///    PFDataHelper.GetModelConfig(typeof(MonthlyFinanceStatistics)),
//    ///    "各月财务统计"
//    ///);
//    /// 
//    /// 此方法要求提供insert的原因是:建表字段应该是根据CreateModel生成的,而modelConfig里可能含有一些经过转换后用于显示的字段
//    /// </summary>
//    /// <param name="tableName"></param>
//    /// <param name="insert"></param>
//    /// <param name="primaryKey"></param>
//    /// <param name="tableIndex"></param>
//    /// <param name="modelConfig"></param>
//    /// <param name="tableComment"></param>
//    public SqlCreateTableCollection(String tableName, SqlInsertCollection insert, String[] primaryKey, String[] tableIndex = null, PFModelConfigCollection modelConfig = null, String tableComment = null)
//    {
//        foreach (var i in insert)
//        {
//            var fieldName = i.Key;
//            var m = modelConfig.ContainsKey(fieldName)
//                ? new SqlCreateTableItem(modelConfig[fieldName])
//                : new SqlCreateTableItem
//                {
//                    FieldName = fieldName
//                };
//            m.FieldType = i.Value.VType;
//
//            var fieldAttr = PFFinanceFieldAttribute.GetAttr(i.Value.PInfo);
//            if (fieldAttr != null && m.FieldText == null)
//            {
//                m.FieldText = fieldAttr.GetFieldText();
//            }
//
//            //var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType = rdr.GetFieldType(i) };
//            Add(m);
//        }
//        TableName = tableName;
//        PrimaryKey = primaryKey;
//        TableIndex = tableIndex;
//        TableComment = tableComment;
//
//    }
    
    public SGSqlCommandString ToSql()
    {
    	List<String> fieldStr=SGDataHelper.ListSelect(this, (aaa)->{
    		return SGDataHelper.FormatString("{0}{1}{2} {3} ",
    	            GetFieldQuotationCharacterL(),
    	            aaa.FieldName,
    	            GetFieldQuotationCharacterR(),
    	            GetFieldTypeString(aaa)
    	        );
    	});
//        String result = PFDataHelper.FormatString(
//        "CREATE TABLE {0} ("+
//         " {1} "+
//          " {2} "+
//        "){3};\r\n ",
//TableName,
//String.join(",",fieldStr),
//PrimaryKey != null && PrimaryKey.length>0 ? PFDataHelper.FormatString(",CONSTRAINT [PK_{0}] PRIMARY KEY CLUSTERED({1})", TableName,String.join(",", PrimaryKey)) : "",
//TableComment == null ? "" : PFDataHelper.FormatString("COMMENT = '{0}'", TableComment)
//);
        String result = SGDataHelper.FormatString(
        "CREATE TABLE {0} ("+
         " {1} "+
          " {2} "+
        ");\r\n ",
TableName,
String.join(",",fieldStr),
PrimaryKey != null && PrimaryKey.length>0 ? SGDataHelper.FormatString(",CONSTRAINT [PK_{0}] PRIMARY KEY CLUSTERED({1})", TableName,String.join(",", PrimaryKey)) : ""
);
        if (ClusteredIndex != null && ClusteredIndex.length>0)
        {
            for(String i : ClusteredIndex)
            {
                result += SGDataHelper.FormatString(
"CREATE clustered INDEX  idx_{2} ON {0} ({1}{2}{3}); "
, TableName, GetFieldQuotationCharacterL(), i, GetFieldQuotationCharacterR());
            }
        }
        if (TableIndex != null && TableIndex.length>0)
        {
            for(String i : TableIndex)
            {
                result += SGDataHelper.FormatString(
"CREATE INDEX  idx_{2} ON {0} ({1}{2}{3}); "
, TableName, GetFieldQuotationCharacterL(), i, GetFieldQuotationCharacterR());
            }
        }

        for(SqlCreateTableItem i : this) {
        	if(i.FieldText!=null) {
        		result+=SGDataHelper.FormatString("EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'{0}' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'{1}', @level2type=N'COLUMN',@level2name=N'{2}' \r\n ", i.FieldText,TableName,i.FieldName);
        	}
        }
        if(TableComment != null) {
        	result+=SGDataHelper.FormatString("EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'{0}' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'{1}' \r\n ", TableComment,TableName);
        }
        return new SGSqlCommandString(result);
    }
    public String GetFieldTypeString(PFModelConfig m)
    {
        String typeString = SGDataHelper.GetStringByType(m.FieldType);
//        PFSqlFieldType pfType = PFDataHelper.GetPFTypeByClass(m.FieldType);
        SGSqlFieldTypeEnum pfType =SGSqlFieldTypeEnum.InitByClass(m.FieldType);
        String r = "";
        if (SGSqlFieldTypeEnum.Int.equals(pfType)
        		||"int".equals(typeString)
            )
        {
            r = "int";//int(11)  //sqlserver的int不能加长度
//            r = m.FieldSqlLength == null
//                ? PFDataHelper.FormatString("int")
//                : PFDataHelper.FormatString("int({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
        }
        else if (SGSqlFieldTypeEnum.Long.equals(pfType)
        		||"long".equals(typeString))
        {
//            r = m.FieldSqlLength == null
//                ? PFDataHelper.FormatString("bigint")
//                : PFDataHelper.FormatString("bigint({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
            r = "bigint";//sqlserver的bigint不能指定长度
        }
        else if ( SGSqlFieldTypeEnum.Decimal.equals(pfType)
        		||SGSqlFieldTypeEnum.BigDecimal.equals(pfType)
        		|| "decimal".equals(typeString)
            )
        {
			if(m.FieldSqlLength>38) {//sum(xx)的结果可能长度超出38,sql数据库只支持38
				m.FieldSqlLength=38;
			}
            r = SGDataHelper.FormatString("decimal({0},{1})", m.FieldSqlLength==null ? 18:m.FieldSqlLength, m.Precision==null? 2:m.Precision);
        }
        else if (SGSqlFieldTypeEnum.DateTime.equals(pfType)
        		|| "DateTime".equals(typeString))
        {
            r = "datetime";
        }
        else if (SGSqlFieldTypeEnum.Bool.equals(pfType)
        		||"bool".equals(typeString) )
        {
            r = "bit";
        }
        else if (SGSqlFieldTypeEnum.String.equals(pfType)
        		||"string".equals(typeString))
        {
            r = SGDataHelper.FormatString("{0}varchar({1})",
            		(//m.HasChinese!=null&&
            		(m.HasChinese==true||(null!=m.LowerFieldName&&(m.LowerFieldName.contains("name")||m.LowerFieldName.endsWith("er"))))?"n":""),
            		(m.FieldSqlLength==null ? 100:m.FieldSqlLength)
            				);//int(11) ?后面不知道要不要长度
        }
        else
        {
            r = "varchar(100)";
        }
        if (PrimaryKey != null && Arrays.asList(PrimaryKey).contains(m.FieldName))
        {
            r += " not null";
        }
//        if (m.FieldText != null)
//        {
//            r += " COMMENT '" + m.FieldText + "'";
//        }
        return r;
    }
    /**
     * 获得datax的列json配置,如 [{name:xx,type:xx},...]
     * @return
     */
    public String GetDataxColumnJson() {
		List<String> r=SGDataHelper.ListSelect(this, a->SGDataHelper.FormatString("{\"name\":\"{0}\",\"type\":\"{1}\"}", a.FieldName,this.GetFieldTypeString(a)));
		return "["+String.join(",\r\n", r)+"]";
    }

}
