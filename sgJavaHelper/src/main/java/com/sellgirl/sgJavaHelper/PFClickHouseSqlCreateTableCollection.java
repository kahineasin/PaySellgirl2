package com.sellgirl.sgJavaHelper;

//import java.util.Arrays;
import java.util.List;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;

public class PFClickHouseSqlCreateTableCollection extends SGSqlCreateTableCollection {

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
//        ")ENGINE = MergeTree() "
//        + "ORDER BY ({2}) ",
//TableName,
//String.join(",",fieldStr),
//String.join(",", PrimaryKey)//,
////TableComment == null ? "" : PFDataHelper.FormatString("COMMENT = '{0}'", TableComment)
//);
        String result = SGDataHelper.FormatString(
        "CREATE TABLE {0}.{1} ON CLUSTER default_cluster (\r\n"+
         " {2} \r\n"+
        ")ENGINE = ReplicatedMergeTree('/clickhouse/tables/{layer}-{shard}/{0}/{1}','{replica}') \r\n"+
        // "PARTITION BY {3} \r\n"+ 
         " {3} "+ 
        "ORDER BY ({4}) \r\n ",
DbName,
TableName,
String.join(",",fieldStr),
//ClusteredIndex==null?"":String.join(",",ClusteredIndex),
Partition==null?"":(" PARTITION BY "+String.join(",",Partition)+" \r\n"),
//String.join(",", PrimaryKey)//,
		Order==null?"":String.join(",", Order)
//TableComment == null ? "" : PFDataHelper.FormatString("COMMENT = '{0}'", TableComment)
);
//        if (TableIndex != null && TableIndex.length>0)
//        {
//            for(String i : TableIndex)
//            {
//                result += PFDataHelper.FormatString(
//"CREATE INDEX  idx_{2} ON {0} ({1}{2}{3}); "
//, TableName, GetFieldQuotationCharacterL(), i, GetFieldQuotationCharacterR());
//            }
//        }
        
        String r2=SGDataHelper.FormatString("CREATE TABLE {0}.{1}_all ON CLUSTER default_cluster\r\n" + 
        		"AS {0}.{1}\r\n" + 
        		"ENGINE = Distributed(default_cluster, {0}, {1}, rand())"	, 
        		DbName,
        		TableName);

        return new SGSqlCommandString(result,r2);
    }
	@Override
    public String GetFieldTypeString(PFModelConfig m)
    {
        String typeString = SGDataHelper.GetStringByType(m.FieldType);
        //PFSqlFieldType pfType =PFSqlFieldType.InitByClass(m.FieldType);
        SGSqlFieldTypeEnum pfType =SGSqlFieldTypeEnum.InitByClass(m.FieldType);
        String r = "";
        if ( "int".equals(typeString)
            )
        {
//                r = m.FieldSqlLength == null
//                    ? PFDataHelper.FormatString("int32")
//                    : PFDataHelper.FormatString("int32({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
                    r = SGDataHelper.FormatString("Int32");
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
//            r = PFDataHelper.FormatString("varchar({0})", m.FieldSqlLength==null ? 100:m.FieldSqlLength);//int(11) ?后面不知道要不要长度
//            //r = PFDataHelper.FormatString("String({0})", m.FieldSqlLength==null ? 100:m.FieldSqlLength);//int(11) ?后面不知道要不要长度
            r = SGDataHelper.FormatString("String({0})", m.FieldSqlLength==null ? 100:m.FieldSqlLength);
        }
        else if (  "decimal".equals(typeString)
        		||"BigDecimal".equals(typeString)
            )
        {
            //r = PFDataHelper.FormatString("decimal({0},{1})", m.FieldSqlLength==null ? 18:m.FieldSqlLength, m.Precision==null? 2:m.Precision);
//            r = PFDataHelper.FormatString("Decimal{0}", m.FieldSqlLength==null ? 64:64);
            r = SGDataHelper.FormatString("Decimal{0}({1})", (m.FieldSqlLength==null ? 64:64),m.Precision==null?2:m.Precision);
        }
        else if ( SGSqlFieldTypeEnum.DateTime.equals(pfType)
        		|| "DateTime".equals(typeString))
        {
        	if(m.Required
        			||(this.Partition!=null
        			  //&&Arrays.asList(this.Partition).contains(m.FieldName)
        			 &&SGDataHelper.ArrayAny(Partition, a->a.indexOf(m.FieldName)>-1)
        			 )
        			) {
                r = "DateTime";
        	}else {
        		r="Nullable(DateTime)";
        	}
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
        if (m.FieldText != null)
        {
            r += " COMMENT '" + m.FieldText + "'";
        }
        return r;
    }
}
