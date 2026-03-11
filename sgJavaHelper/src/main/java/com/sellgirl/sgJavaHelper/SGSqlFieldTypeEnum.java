package com.sellgirl.sgJavaHelper;

/**
 * 数据库的字段类型(为了统一多种数据库的数据类型)
 * 
 * PFSqlFieldTypeEnum比PFSqlFieldType性能更快(UncheckDe001.testEnumSpeed4)
 * @author Administrator
 *
 */
public enum SGSqlFieldTypeEnum  {
	String,
	/**
	 * 32-bit 
	 * ±1.5 × 10^−45 to ±3.4 × 10^38
	 * 
	 * java float 1.4E-45~4.4028235E38
	 */
	Decimal,
	/**
	 * 64-bit
	 * ±5.0 × 10^−324 to ±1.7 × 10^308
	 * 
	 * java double 4.9E-324 ~ 1.7976931348623157E308
	 */
	BigDecimal,
	/**
	 * java int ± 2.147483648e9
	 * 
	 * 2^31 - 1 = 2147483647
	 */
	Int,
	/**
	 * java long ± 9.223372036854775807e18
	 * 
	 * mysql bitint ± 9.223372036854775807e18
	 * 
	 * 2^64 -1
	 */
	Long,
	/**
	 * 建议系统中的时间运算使用Timestamp的long值
	 */
	DateTime,Bool,UUID,Null,Percent//,BigInt
	;	

	public static SGSqlFieldTypeEnum InitByString(String type) {
//	   if(type.equals(type)) {
//		   return Null.class;
//	   }
//	   if(Null.class.equals(type)) {
//		   return Null.class;
//	   }
		switch (type.toLowerCase()) {
		case "bool":
		case "bit":
			return SGSqlFieldTypeEnum.Bool;
//           case "byte":
//               return Byte.class;
//           case "sbyte":
//               return Type.GetType("System.SByte", true, true);
//           case "char":
//               return char.class;
		case "decimal":
			return SGSqlFieldTypeEnum.Decimal;
		case "double":
			return SGSqlFieldTypeEnum.BigDecimal;
		case "float":
			return SGSqlFieldTypeEnum.BigDecimal;
		case "int":
		case "bigint":
		case "smallint":
			return SGSqlFieldTypeEnum.Int;
		case "uint":
			return SGSqlFieldTypeEnum.Int;
		case "long":
			return SGSqlFieldTypeEnum.BigDecimal;
		case "ulong":
			return SGSqlFieldTypeEnum.BigDecimal;
//           case "object":
//               return Object.class;
		case "short":
			return SGSqlFieldTypeEnum.Int;
		case "ushort":
			return SGSqlFieldTypeEnum.Int;
		case "string":
		case "varchar":
			return SGSqlFieldTypeEnum.String;
		case "datetime":
		case "java.util.calendar":
			return SGSqlFieldTypeEnum.DateTime;
		case "guid":
			return SGSqlFieldTypeEnum.UUID;
//           case "percent":
//               return PFPercent.class;
		case "date":
			return SGSqlFieldTypeEnum.DateTime;
		default:
			return SGSqlFieldTypeEnum.String;
		}
	}
	public static SGSqlFieldTypeEnum InitByClass(Class<?> type) {
			// 以后应该先判断长名,找不到再找短名
			if (type == null) {
				return SGSqlFieldTypeEnum.Null;
			}
			switch (type.getSimpleName()) {
			case "System.Boolean":
			case "Boolean":
				return SGSqlFieldTypeEnum.Bool;
//	           case "System.Byte":
//	               return "byte";
//	           case "System.SByte":
//	               return "sbyte";
//	           case "System.Char":
//	               return "char";
			case "System.Decimal":
			case "System.Single":
				return SGSqlFieldTypeEnum.Decimal;
			case "System.Double":
			case "System.BigDecimal":
			case "BigDecimal":
				return SGSqlFieldTypeEnum.BigDecimal;
			case "System.Int32":
			case "Integer":
				return SGSqlFieldTypeEnum.Int;
			case "System.UInt32":
				return SGSqlFieldTypeEnum.Int;
			case "System.Int64":
			case "Long":
				return SGSqlFieldTypeEnum.Long;
			case "System.UInt64":
				return SGSqlFieldTypeEnum.Long;
//	           case "System.Object":
//	               return "object";
			case "System.Int16":
				return SGSqlFieldTypeEnum.Int;
			case "System.UInt16":
				return SGSqlFieldTypeEnum.Int;
			case "System.String":
			case "String":
				return SGSqlFieldTypeEnum.String;
			case "System.DateTime":
			case "Calendar":
			case "Timestamp":
			case "GregorianCalendar":
			case "Perfect.PFDate":
			case "PFDate":
				return SGSqlFieldTypeEnum.DateTime;
			case "System.Guid":
				return SGSqlFieldTypeEnum.UUID;
//	           case "Perfect.PFPercent":
//	               return "percent";
			default:
				return InitByString(type.getSimpleName());//注意这句不能修改,InitByString的case部分是不一样的
				//return PFSqlFieldTypeEnum.String;
			}
	}
}