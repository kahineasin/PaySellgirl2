package com.sellgirl.sgJavaHelper;

/**
 * 数据库的字段类型(为了统一多种数据库的数据类型)
 * @Deprecated 改用性能更好的PFSqlFieldTypeEnum
 * @author Administrator
 *
 */
@Deprecated
public final class PFSqlFieldType extends PFEnumClass{

	/**
	 * 
	 */
	private static final long serialVersionUID = 996473725331131064L;

	public PFSqlFieldType() {
		super();
	}
	protected PFSqlFieldType(String text, int value) {
		super(text, value);
	}
	
	public final static PFSqlFieldType  String=new PFSqlFieldType("String",0);
	/**
	 * 32-bit 
	 * ±1.5 × 10^−45 to ±3.4 × 10^38
	 */
	public final static PFSqlFieldType  Decimal=new PFSqlFieldType("Decimal",1);
	/**
	 * 64-bit
	 * ±5.0 × 10^−324 to ±1.7 × 10^308
	 * 
	 * java double 4.9E-324 ~ 1.7976931348623157E308
	 */
	public final static PFSqlFieldType  BigDecimal=new PFSqlFieldType("BigDecimal",1<<1);
	/**
	 * java int ± 2.147483648e9
	 * 
	 * 
	 */
	public final static PFSqlFieldType   Int=new PFSqlFieldType("Int",1<<2);
	/**
	 * java long ± 9.223372036854775807e18
	 * 
	 * mysql bitint ± 9.223372036854775807e18
	 */
	public final static PFSqlFieldType   Long=new PFSqlFieldType("Long",1<<3);
	public final static PFSqlFieldType DateTime=new PFSqlFieldType("DateTime",1<<4);
	public final static PFSqlFieldType Bool=new PFSqlFieldType("Bool",1<<5);
	public final static PFSqlFieldType UUID=new PFSqlFieldType("UUID",1<<6);
	public final static PFSqlFieldType Null=new PFSqlFieldType("Null",1<<7);
	public final static PFSqlFieldType Percent=new PFSqlFieldType("Percent",1<<8);
	//public final static PFSqlFieldType   BigInt=new PFSqlFieldType("BigInt",1<<9);//重复了
	
	public static PFSqlFieldType InitByClass(Class<?> type) {
//		   String s=GetStringByType(cl);
//		   return GetPFTypeByString(s);

			// 以后应该先判断长名,找不到再找短名
			// switch (type.toString())
			if (type == null) {
				return PFSqlFieldType.Null;
			}
			switch (type.getSimpleName()) {
			case "System.Boolean":
			case "Boolean":
				return PFSqlFieldType.Bool;
//	           case "System.Byte":
//	               return "byte";
//	           case "System.SByte":
//	               return "sbyte";
//	           case "System.Char":
//	               return "char";
			case "System.Decimal":
			case "System.Single":
				return PFSqlFieldType.Decimal;
			case "System.Double":
			case "System.BigDecimal":
			case "BigDecimal":
				return PFSqlFieldType.BigDecimal;
			case "System.Int32":
			case "Integer":
				return PFSqlFieldType.Int;
			case "System.UInt32":
				return PFSqlFieldType.Int;
			case "System.Int64":
			case "Long":
				return PFSqlFieldType.Long;
			case "System.UInt64":
				return PFSqlFieldType.Long;
//	           case "System.Object":
//	               return "object";
			case "System.Int16":
				return PFSqlFieldType.Int;
			case "System.UInt16":
				return PFSqlFieldType.Int;
			case "System.String":
			case "String":
				return PFSqlFieldType.String;
			case "System.DateTime":
			case "Calendar":
			case "Timestamp":
			case "GregorianCalendar":
			case "Perfect.PFDate":
			case "PFDate":
				return PFSqlFieldType.DateTime;
			case "System.Guid":
				return PFSqlFieldType.UUID;
//	           case "Perfect.PFPercent":
//	               return "percent";
			default:
				// return type.toString();
				return InitByString(type.getSimpleName());
			}
	}
	public static PFSqlFieldType InitByString(String type) {
		// String.class.getComponentType();
//	   if(type.equals(type)) {
//		   return Null.class;
//	   }
//	   if(Null.class.equals(type)) {
//		   return Null.class;
//	   }
		switch (type.toLowerCase()) {
		case "bool":
		case "bit":
			return PFSqlFieldType.Bool;
//           case "byte":
//               return Byte.class;
//           case "sbyte":
//               return Type.GetType("System.SByte", true, true);
//           case "char":
//               return char.class;
		case "decimal":
			return PFSqlFieldType.Decimal;
		case "double":
			return PFSqlFieldType.BigDecimal;
		case "float":
			return PFSqlFieldType.BigDecimal;
		case "int":
		case "bigint":
		case "smallint":
			return PFSqlFieldType.Int;
		case "uint":
			return PFSqlFieldType.Int;
		case "long":
			return PFSqlFieldType.BigDecimal;
		case "ulong":
			return PFSqlFieldType.BigDecimal;
//           case "object":
//               return Object.class;
		case "short":
			return PFSqlFieldType.Int;
		case "ushort":
			return PFSqlFieldType.Int;
		case "string":
		case "varchar":
			return PFSqlFieldType.String;
		case "datetime":
		case "java.util.calendar":
			return PFSqlFieldType.DateTime;
		case "guid":
			return PFSqlFieldType.UUID;
//           case "percent":
//               return PFPercent.class;
		case "date":
			return PFSqlFieldType.DateTime;
		default:
			return PFSqlFieldType.String;
		}
	}
	
	public SGSqlFieldTypeEnum toPFEnum() {
		return SGSqlFieldTypeEnum.valueOf(this.getText()); 
	}
	public static PFSqlFieldType initByPFEnum(SGSqlFieldTypeEnum pfEnum) {
		return PFEnumClass.EnumParseByInt(PFSqlFieldType.class, pfEnum.ordinal()); 
	}
	
}