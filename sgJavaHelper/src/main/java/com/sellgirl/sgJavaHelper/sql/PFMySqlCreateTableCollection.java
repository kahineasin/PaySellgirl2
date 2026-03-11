package com.sellgirl.sgJavaHelper.sql;

import java.util.Arrays;
import java.util.List;

import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * mysql版本
 */
public class PFMySqlCreateTableCollection extends SGSqlCreateTableCollection{
	private static final long serialVersionUID = 3363284594566796477L;
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
//        "CREATE TABLE `{0}` ("+
//         " {1} "+
//          " {2} "+
//        "){3}; ",
//TableName,
//String.join(",",fieldStr),
//PrimaryKey != null && PrimaryKey.length>0 ? PFDataHelper.FormatString(",PRIMARY KEY({0})", String.join(",", PrimaryKey)) : "",
//TableComment == null ? "" : PFDataHelper.FormatString("COMMENT = '{0}'", TableComment)
//);
//        if (TableIndex != null && TableIndex.length>0)
//        {
//            for(String i : TableIndex)
//            {
//                result += PFDataHelper.FormatString(
//"CREATE INDEX  idx_{2} ON {0} ({1}{2}{3}); "
//, TableName, GetFieldQuotationCharacterL(), i, GetFieldQuotationCharacterR());
//            }
//        }
    	
    	//mysql8.0.23版本的stmt.execute()不支持包含分号分隔的多命令语句--benjamin20210311
    	SGSqlCommandString result =new SGSqlCommandString(SGDataHelper.FormatString(
    	      "CREATE TABLE {0}`{1}` ("+
    	    	       " {2} "+
    	    	        " {3} "+
    	    	      "){4}; ",
    	    	      DbName==null?"":(GetFieldQuotationCharacterL()+DbName+GetFieldQuotationCharacterR()+"."),
    	    	TableName,
    	    	String.join(",",fieldStr),
    	    	PrimaryKey != null && PrimaryKey.length>0 ? SGDataHelper.FormatString(",PRIMARY KEY({0})", String.join(",", PrimaryKey)) : "",
    	    	TableComment == null ? "" : SGDataHelper.FormatString("COMMENT = '{0}'", TableComment)
    	    	)); 
      if (TableIndex != null && TableIndex.length>0)
      {
          for(String i : TableIndex)
          {
              result.add(SGDataHelper.FormatString(
"CREATE INDEX  idx_{2} ON {0} ({1}{2}{3}); "
, TableName, GetFieldQuotationCharacterL(), i, GetFieldQuotationCharacterR()));
          }
      }
        return result;
    }
	@Override
    public String GetFieldTypeString(PFModelConfig m)
    {
        String typeString = SGDataHelper.GetStringByType(m.FieldType);
        SGSqlFieldTypeEnum pfType =SGSqlFieldTypeEnum.InitByClass(m.FieldType);
        String r = "";
        if ( SGSqlFieldTypeEnum.Int.equals(pfType)
        		||"int".equals(typeString)
            )
        {
            //r = String.Format("int");//int(11) ?后面不知道要不要长度
            r = m.FieldSqlLength == null
                ? SGDataHelper.FormatString("int")
                : SGDataHelper.FormatString("int({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
        }
        else if ( SGSqlFieldTypeEnum.Long.equals(pfType)
        		||"long".equals(typeString))
        {
            r = m.FieldSqlLength == null
                ? SGDataHelper.FormatString("bigint")
                : SGDataHelper.FormatString("bigint({0})", m.FieldSqlLength);//int(11) ?后面不知道要不要长度
        }
        else if (  SGSqlFieldTypeEnum.Decimal.equals(pfType)
        		||SGSqlFieldTypeEnum.BigDecimal.equals(pfType)
        		||"decimal".equals(typeString)
            )
        {
            r = SGDataHelper.FormatString("decimal({0},{1})", m.FieldSqlLength==null ? 18:m.FieldSqlLength, m.Precision==null? 2:m.Precision);
        }
        else if ( SGSqlFieldTypeEnum.DateTime.equals(pfType)
        		||"DateTime".equals(typeString))
        {
            r = "datetime";
        }
        else if (SGSqlFieldTypeEnum.Bool.equals(pfType)
        		||typeString == "bool")
        {
            r = "bit";
        }
        else if (SGSqlFieldTypeEnum.String.equals(pfType)
        		||"string".equals(typeString))
        {
            r = SGDataHelper.FormatString("varchar({0})", m.FieldSqlLength==null ? 100:m.FieldSqlLength);//int(11) ?后面不知道要不要长度
        }
        else
        {
            r = "varchar(100)";
        }
        if (PrimaryKey != null && Arrays.asList(PrimaryKey).contains(m.FieldName))
        {
            r += " not null";
        }
        if (m.FieldText != null)
        {
            r += " COMMENT '" + m.FieldText + "'";
        }
        return r;
    }
}
