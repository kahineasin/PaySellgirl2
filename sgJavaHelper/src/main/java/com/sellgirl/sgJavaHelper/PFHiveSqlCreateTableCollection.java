package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.sql.SqlCreateTableItem;

public class PFHiveSqlCreateTableCollection extends SGSqlCreateTableCollection {

	/**
	 * 
	 */
	private static final long serialVersionUID = -423843318903818688L;
	@Override
    public  String GetFieldQuotationCharacterL() {
    	return "`";
    }
	@Override
    public  String GetFieldQuotationCharacterR() {
    	return "`";
    }
	@Override
    public SGSqlCommandString ToSql()
    {
//    	List<SqlCreateTableItem> normalField=PFDataHelper.ListWhere(this, a->!Arrays.asList(Partition).contains(a));
//    	List<SqlCreateTableItem> keyField=PFDataHelper.ListWhere(this, a->Arrays.asList(Partition).contains(a));
//    	
//    	List<String> fieldStr=PFDataHelper.ListSelect(normalField, (aaa)->{
//    		return PFDataHelper.FormatString("{0}{1}{2} {3} ",
//    	            GetFieldQuotationCharacterL(),
//    	            aaa.FieldName,
//    	            GetFieldQuotationCharacterR(),
//    	            GetFieldTypeString(aaa)
//    	        );
//    	});
//    	List<String> normalFieldStr=PFDataHelper.ListWhere(fieldStr, a->!Arrays.asList(Partition).contains(a));//非主键
//    	List<String> keyFieldStr=PFDataHelper.ListWhere(fieldStr, a->Arrays.asList(Partition).contains(a));//主键
    	
    	List<String> normalFieldStr=new ArrayList<String>();
    	List<String> keyFieldStr=new ArrayList<String>();
    	for(SqlCreateTableItem aaa : this) {
    		List<String> tmpFieldStr=null;
    		if(Arrays.asList(Partition).contains(aaa.FieldName)) {
    			tmpFieldStr=keyFieldStr;
//    			keyFieldStr.add(PFDataHelper.FormatString("{0}{1}{2} {3} ",
//    	            GetFieldQuotationCharacterL(),
//    	            aaa.FieldName,
//    	            GetFieldQuotationCharacterR(),
//    	            GetFieldTypeString(aaa)
//    	        ));
    		}else {
    			tmpFieldStr=normalFieldStr;
//    			normalFieldStr.add(PFDataHelper.FormatString("{0}{1}{2} {3} ",
//        	            GetFieldQuotationCharacterL(),
//        	            aaa.FieldName,
//        	            GetFieldQuotationCharacterR(),
//        	            GetFieldTypeString(aaa)
//        	        ));
    		}
    		tmpFieldStr.add(SGDataHelper.FormatString("{0}{1}{2} {3} {4} ",
    	            GetFieldQuotationCharacterL(),
    	            aaa.FieldName,
    	            GetFieldQuotationCharacterR(),
    	            GetFieldTypeString(aaa),
    	            aaa.FieldText != null?(" COMMENT '" + aaa.FieldText + "'"):""
    	        ));
    	}

        String result = SGDataHelper.FormatString(
        "CREATE TABLE {0}.{1}  (\r\n"+
         " {2} \r\n"+
        ") \r\n"+
        // "PARTITION BY {3} \r\n"+ 
         " {3} "+ 
        "row format delimited\r\n" + 
        "fields terminated by ','\r\n" + 
        "collection items terminated by '-'\r\n" + 
        "map keys terminated by ':' "
         ,
DbName,
TableName,
String.join(",\r\n",normalFieldStr),
//Partition==null?"":(" PARTITION BY ("+String.join(",",Partition)+") \r\n")
Partition==null?"":(" PARTITIONED BY ("+String.join(",",keyFieldStr)+") \r\n")
);

        return new SGSqlCommandString(result);
    }
	@Override
    public String GetFieldTypeString(PFModelConfig m)
    {
        String typeString = SGDataHelper.GetStringByType(m.FieldType);
        SGSqlFieldTypeEnum pfType =SGSqlFieldTypeEnum.InitByClass(m.FieldType);
        String r = "";
        if ( "int".equals(typeString)
            )
        {
//                r = m.FieldSqlLength == null
//                    ? PFDataHelper.FormatString("int32")
//                    : PFDataHelper.FormatString("int32({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
                    //r = PFDataHelper.FormatString("Int32");
                    r = SGDataHelper.FormatString("BIGINT");
        }
        else if ( "long".equals(typeString))
        {
//            r = m.FieldSqlLength == null
//                ? PFDataHelper.FormatString("int64")
//                : PFDataHelper.FormatString("int64({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
                r =SGDataHelper.FormatString("Int64");
        }
        else if ("string".equals(typeString))
        {
           // r = PFDataHelper.FormatString("String({0})", m.FieldSqlLength==null ? 100:m.FieldSqlLength);
            r="String";
        }
        else if (  "decimal".equals(typeString)
        		||"BigDecimal".equals(typeString)
            )
        {
//            r = PFDataHelper.FormatString("Decimal{0}({1})", (m.FieldSqlLength==null ? 64:64),m.Precision==null?2:m.Precision);
            r = SGDataHelper.FormatString("DOUBLE");
        }
        else if ( SGSqlFieldTypeEnum.DateTime.equals(pfType)
        		|| "DateTime".equals(typeString))
        {
//        	if(m.Required
//        			||(this.Partition!=null
//        			  //&&Arrays.asList(this.Partition).contains(m.FieldName)
//        			 &&PFDataHelper.ArrayAny(Partition, a->a.indexOf(m.FieldName)>-1)
//        			 )
//        			) {
//                r = "DateTime";
//        	}else {
//        		r="Nullable(DateTime)";
//        	}
        	r = "Date";
        }
        else if ("bool".contentEquals(typeString))
        {
            //r = "bit";
            r = "UInt8";
        }
        else
        {
            //r = "varchar(100)";
            r = "String(100)";
        }
//        if (PrimaryKey != null && Arrays.asList(PrimaryKey).contains(m.FieldName))
//        {
//            r += " not null";
//        }
//        if (m.FieldText != null)
//        {
//            r += " COMMENT '" + m.FieldText + "'";
//        }
        return r;
    }
//	public static String GetHiveTypeByCls(PFModelConfig m) {
////        String typeString = PFDataHelper.GetStringByType(cls);
////        PFSqlFieldType pfType =PFSqlFieldType.InitByClass(cls);
//        String typeString = PFDataHelper.GetStringByType(m.FieldType);
//        PFSqlFieldType pfType =PFSqlFieldType.InitByClass(m.FieldType);
//        String r = "";
//        if ( "int".equals(typeString)
//            )
//        {
////                r = m.FieldSqlLength == null
////                    ? PFDataHelper.FormatString("int32")
////                    : PFDataHelper.FormatString("int32({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
//                    //r = PFDataHelper.FormatString("Int32");
//                    r = PFDataHelper.FormatString("BIGINT");
//        }
//        else if ( "long".equals(typeString))
//        {
////            r = m.FieldSqlLength == null
////                ? PFDataHelper.FormatString("int64")
////                : PFDataHelper.FormatString("int64({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
//                r =PFDataHelper.FormatString("Int64");
//        }
//        else if ("string".equals(typeString))
//        {
//           // r = PFDataHelper.FormatString("String({0})", m.FieldSqlLength==null ? 100:m.FieldSqlLength);
//            r="String";
//        }
//        else if (  "decimal".equals(typeString)
//        		||"BigDecimal".equals(typeString)
//            )
//        {
//            //r = PFDataHelper.FormatString("decimal({0},{1})", m.FieldSqlLength==null ? 18:m.FieldSqlLength, m.Precision==null? 2:m.Precision);
////            r = PFDataHelper.FormatString("Decimal{0}", m.FieldSqlLength==null ? 64:64);
//            r = PFDataHelper.FormatString("Decimal{0}({1})", (m.FieldSqlLength==null ? 64:64),m.Precision==null?2:m.Precision);
//        }
//        else if ( PFSqlFieldType.DateTime.equals(pfType)
//        		|| "DateTime".equals(typeString))
//        {
////        	if(m.Required
////        			||(this.Partition!=null
////        			  //&&Arrays.asList(this.Partition).contains(m.FieldName)
////        			 &&PFDataHelper.ArrayAny(Partition, a->a.indexOf(m.FieldName)>-1)
////        			 )
////        			) {
////                r = "DateTime";
////        	}else {
////        		r="Nullable(DateTime)";
////        	}
//        	r = "Date";
//        }
//        else if ("bool".contentEquals(typeString))
//        {
//            //r = "bit";
//            r = "UInt8";
//        }
//        else
//        {
//            //r = "varchar(100)";
//            r = "String(100)";
//        }
////        if (PrimaryKey != null && Arrays.asList(PrimaryKey).contains(m.FieldName))
////        {
////            r += " not null";
////        }
////        if (m.FieldText != null)
////        {
////            r += " COMMENT '" + m.FieldText + "'";
////        }
//        return r;
	//}

}
