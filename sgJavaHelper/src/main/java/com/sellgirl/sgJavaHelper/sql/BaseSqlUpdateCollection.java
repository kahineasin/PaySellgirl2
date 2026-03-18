package com.sellgirl.sgJavaHelper.sql;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sellgirl.sgJavaHelper.PFDataColumn;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.PFSqlType;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public abstract class BaseSqlUpdateCollection extends LinkedHashMap<String, SqlUpdateItem> {
    /**
     * 便于提示一些tidb等兼容性的异常内容(此属性在SqlExecute.getInsertCollection时设置
     */
    private PFSqlType sqlType = PFSqlType.MySql;
    // protected abstract PFSqlType sqlType();
    /**
     *
     */
    private static final long serialVersionUID = -166162807314904847L;
    private Object _model = null;

    public String GetFieldQuotationCharacterL() {
        return "[";
    }

    public String GetFieldQuotationCharacterR() {
        return "]";
    }

    public SqlUpdateItem Get(String name) {
        if (this.containsKey(name)) {
            return super.get(name);
        } else if (!SGDataHelper.StringIsNullOrWhiteSpace(name)) {
            return super.get(name.toLowerCase());
        } // 初始化时，GetProperties的Key是ToLower之后的，原来就这么用，所以就那方法了
        return null;
    }


    public BaseSqlUpdateCollection() {
    }

    public void setSqlType(PFSqlType sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * 此方法和
     *
     * @param model
     * @param names
     */
    public void InitItemByModel(Object model, String... names) {
        _model = model;
        if (model instanceof Map) {
            // Map<String, Object> modelDict = (Map<String, Object>)model;
            Map<String, Object> modelDict = SGDataHelper.ObjectAsMap(model);
            // --benjamin todo
            if (names != null && names.length > 0) {
                for (String i : names) {
                    // Add(i);
                    SqlUpdateItem item = new SqlUpdateItem();
                    item.Key = i;
                    item.Value = modelDict.get(i);
                    item.VType = item.Value == null ? null : item.Value.getClass();
                    super.put(i, item);
                }
            } else {
                Iterator<HashMap.Entry<String, Object>> iter = modelDict.entrySet().iterator();
                while (iter.hasNext()) {
                    HashMap.Entry<String, Object> key = iter.next();
                    SqlUpdateItem item = new SqlUpdateItem();
                    item.Key = key.getKey();
                    item.Value = key.getValue();
                    item.VType = item.Value == null ? null : item.Value.getClass();
                    super.put(item.Key, item);
                }

            }
        } else {
            //// _modelProperties = PFDataHelper.GetProperties(model.GetType());
            // var modelProperties = PFDataHelper.GetProperties(model.GetType());
            Field[] fields = model.getClass().getFields();
            if (names != null && names.length > 0) {
                for (String i : names) {
                    Field field;
                    try {
                        field = model.getClass().getField(i);
                        SqlUpdateItem item = new SqlUpdateItem();
                        item.Key = i;
                        item.Value = field.get(_model);
                        item.VType = field.getDeclaringClass();
                        item.PInfo = field;
                        super.put(item.Key, item);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // base.Add(i, new SqlUpdateItem { Key = i, Value =
                    // modelProperties[i].GetValue(_model, null), VType =
                    // modelProperties[i].PropertyType, PInfo = modelProperties[i] });
                }
            } else {
                for (Field i : fields) {
                    SqlUpdateItem item = new SqlUpdateItem();
                    item.Key = i.getName();
                    try {
                        item.Value = i.get(_model);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    // item.VType = i.getDeclaringClass();
                    item.VType = i.getType();// 之前好像写错了,getDeclaringClass获得的实际上是field的所属类的类型-- benjamin 20220624
                    item.PInfo = i;
                    super.put(item.Key, item);

                    // base.Add(i.Key, new SqlUpdateItem { Key = i.Key, Value =
                    // i.Value.GetValue(_model, null), VType = i.Value.PropertyType, PInfo = i.Value
                    // });
                }
            }
        }
    }

    public void InitItemByDataColumn(List<PFDataColumn> model) {
        for (PFDataColumn i : model) {
            SqlUpdateItem item = new SqlUpdateItem();
            item.Key = i.getKey();
            if ("LR".equals(item.Key)) {
                continue;
            }
            try {
                item.Value = i.getValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            item.VType = i.getDataType();
            // item.PInfo = i;
            // item.VPFType=i.getPFDataType();
            item.setSrcDataPFType(i.getPFDataType());
            super.put(item.Key, item);

        }
    }

    public void InitItemByModel(Object model) {
        InitItemByModel(model, new String[]{});
    }

    public BaseSqlUpdateCollection(Object model, String... names) {

        this.InitItemByModel(model, names);
    }

    public <VType> void Add(String name, VType value) {
        SqlUpdateItem item = new SqlUpdateItem();
        item.Key = name;
        item.Value = value;
        item.VType = value.getClass();
        item.setSrcDataPFType(SGSqlFieldTypeEnum.InitByClass(value.getClass()));
        super.put(item.Key, item);
        // base.Add(name, new SqlUpdateItem { Key = name, Value = value, VType =
        // typeof(VType) });
    }

    public void Add(SqlUpdateItem item) {
        super.put(item.Key, item);
    }

    /**
     * /// 批量更新时为了减少应用反射的次数,只更新value /// 执行5120次共花费11毫秒,重新构造整个对象的花费是此方法的2倍 ///
     * 注意使用此方法的前提是:初始化时提供了model
     * <p>
     * 改用UpdateModelValueAutoConvert(Object model)?
     *
     * @param model
     */
    public void UpdateModelValue(Object model) {
        if (model instanceof ResultSet) {
            UpdateByDataReader((ResultSet) model);
        } else if (model instanceof Map) {
            Map<String, Object> modelDict = SGDataHelper.ObjectAsMap(model);
            Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry<String, SqlUpdateItem> key = iter.next();

//				if (modelDict.containsKey(key.getKey())) {
//					SqlUpdateItem item = key.getValue();
//					item.Value = modelDict.get(key.getKey());
////                   i.Value.Value = modelDict[i.Key];
//					if (key.getValue().VType == null && item.Value != null) {
//						key.getValue().VType = item.Value.getClass();
//					}
//					//key.getValue().Value=getValueByMapI(modelDict,key.getValue());
//				} else {
//					key.getValue().Value = null;
//				}
                SqlUpdateItem updateItem = key.getValue();
//				String matchKey=getMatchKey(modelDict,key.getKey());
//				if (null!=matchKey) {
//					updateItem.Value = modelDict.get(matchKey);
//					if (updateItem.VType == null && modelDict.get(matchKey) != null) {
//						updateItem.VType = modelDict.get(matchKey).getClass();
//					}
//				} else {
//					updateItem.Value = null;
//				}
                updateItem.Value = this.getValueByMapI(modelDict, updateItem);
            }
        } else if (model instanceof PFDataRow) {
            PFDataRow dataRow = SGDataHelper.ObjectAs(model);
            Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry<String, SqlUpdateItem> key = iter.next();

                SqlUpdateItem updateItem = key.getValue();
                updateItem.Value = getValueByDataRowI(dataRow, updateItem);

            }
        } else {
            Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry<String, SqlUpdateItem> key = iter.next();
                try {
                    SqlUpdateItem updateItem = key.getValue();
//					if (key.getValue().PInfo == null) {
//						try {
//							key.getValue().PInfo = model.getClass().getDeclaredField(key.getKey());
//							key.getValue().PInfo.setAccessible(true);
//						} catch (NoSuchFieldException | SecurityException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					key.getValue().Value = key.getValue().PInfo.get(model);
                    updateItem.Value = getValueByClassI(model, updateItem);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * key:dstField,value:srcField
     */
    private Map<String, String> lowerKeyMap = null;

    /**
     * 为了处理来源目标字段大小写不一样的情况
     */
    public void UpdateLowerKeyMapBySrcField(List<String> fields) {

        Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
        while (iter.hasNext()) {
            HashMap.Entry<String, SqlUpdateItem> key = iter.next();
            for (String f : fields) {
                if (f.equals(key.getKey())) {

                } else if (f.toLowerCase().equals(key.getKey().toLowerCase())) {
                    if (null == lowerKeyMap) {
                        lowerKeyMap = new HashMap<String, String>();
                    }
                    lowerKeyMap.put(key.getKey(), f);
                }
            }
        }
    }

    public void UpdateModelValueAutoConvert(Object model) {
        if (model instanceof ResultSet) {
            UpdateByDataReaderAutoConvert((ResultSet) model);
        } else if (model instanceof Map) {
            // Map<String, Object> modelDict = (Map<String, Object>)model;
            Map<String, Object> modelDict = SGDataHelper.ObjectAsMap(model);
            Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry<String, SqlUpdateItem> key = iter.next();

                SqlUpdateItem updateItem = key.getValue();

//				if (modelDict.containsKey(key.getKey())
//						|| (lowerKeyMap != null && modelDict.containsKey(lowerKeyMap.get(key.getKey())))) {
//					String matchKey = modelDict.containsKey(key.getKey()) ? key.getKey()
//							: lowerKeyMap.get(key.getKey());
//					updateItem.Value = modelDict.get(matchKey);
////                   i.Value.Value = modelDict[i.Key];
//					if (updateItem.VType == null && modelDict.get(matchKey) != null) {
//						updateItem.VType = modelDict.get(matchKey).getClass();
//					}
//				} else {
//					updateItem.Value = null;
//				}
//				String matchKey=getMatchKey(modelDict,key.getKey());
//				if (null!=matchKey) {
//					updateItem.Value = modelDict.get(matchKey);
////                   i.Value.Value = modelDict[i.Key];
//					if (updateItem.VType == null && modelDict.get(matchKey) != null) {
//						updateItem.VType = modelDict.get(matchKey).getClass();
//					}
//				} else {
//					updateItem.Value = null;
//				}
                updateItem.Value = this.getValueByMapI(modelDict, updateItem);

//				if (updateItem.ConvertFrom == null) {
//					updateItem.ConvertFrom = PFDataHelper.GetObjectToPFTypeBySqlTypeConverter0(updateItem.Value, -1,
//							updateItem.getSrcDataPFType());
//				}
//				if (null == updateItem.ConvertFrom) {
//					if (null != updateItem.Value) {
//						PFDataHelper.WriteError(new Throwable(), new Exception(
//								"GetObjectToPFTypeBySqlTypeConverter0 有问题, value不为空, 却不能得到converter"));
//					}
//				} else {
//					updateItem.Value = updateItem.ConvertFrom.convert(updateItem.Value);
//				}
                convertField(updateItem);
            }
        } else if (model instanceof PFDataRow) {
            PFDataRow dataRow = SGDataHelper.ObjectAs(model);
            Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry<String, SqlUpdateItem> key = iter.next();

                SqlUpdateItem updateItem = key.getValue();
//				PFDataColumn col = dataRow.getColumnObject(key.getKey());
//				if (null != col) {
//					updateItem.Value = col.getValue();
////                   i.Value.Value = modelDict[i.Key];
//					if (updateItem.VType == null && updateItem.Value != null) {
//						updateItem.VType = updateItem.Value.getClass();
//					}
//				} else {
//					updateItem.Value = null;
//				}
                updateItem.Value = getValueByDataRowI(dataRow, updateItem);

//				if (updateItem.ConvertFrom == null) {
//					updateItem.ConvertFrom = PFDataHelper.GetObjectToPFTypeBySqlTypeConverter0(updateItem.Value, -1,
//							updateItem.getSrcDataPFType());
//				}
//				if (null == updateItem.ConvertFrom) {
//					if (null != updateItem.Value) {
//						PFDataHelper.WriteError(new Throwable(), new Exception(
//								"GetObjectToPFTypeBySqlTypeConverter0 有问题, value不为空, 却不能得到converter"));
//					}
//				} else {
//					updateItem.Value = updateItem.ConvertFrom.convert(updateItem.Value);
//				}
                convertField(updateItem);
            }
        } else {
            Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry<String, SqlUpdateItem> key = iter.next();
                try {
//					if (key.getValue().PInfo == null) {
//						try {
//							key.getValue().PInfo = model.getClass().getDeclaredField(key.getKey());
//							key.getValue().PInfo.setAccessible(true);
//						} catch (NoSuchFieldException | SecurityException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					key.getValue().Value = key.getValue().PInfo.get(model);
                    SqlUpdateItem updateItem = key.getValue();
                    updateItem.Value = getValueByClassI(model, updateItem);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 批量更新时为了减少应用反射的次数,只更新value
     * 执行5120次共花费11毫秒,重新构造整个对象的花费是此方法的2倍
     *
     * @param dr
     * @param converter
     */
    public void UpdateByDataReader(ResultSet dr, SGFunc<Object, SqlUpdateItem, Integer, Object> converter) {

        try {
            ResultSetMetaData md = dr.getMetaData();
            for (int i = 0; i < md.getColumnCount(); i++) {
                String fieldName = md.getColumnLabel(i + 1);

                if (this.containsKey(fieldName)) {
                    int mdIdx = i + 1;
//					Object v = null;
//					if (md.getColumnType(mdIdx) == java.sql.Types.TIMESTAMP) {// timestamp(数据库中是DateTime)一定要用getDate,否则会多一天,暂不知道怎么解决(用getObject和getTimestamp得到的值都多一天)--benjamin
//																				// todo
//						v = dr.getDate(fieldName);// 这样会丢了分和秒
//						v = dr.getTimestamp(fieldName, Calendar.getInstance());// 如果不传Calendar会多了一天
//					} else {
//						v = dr.getObject(fieldName);
//					}
                    //Object v=getValueByDataReaderI(dr,md,mdIdx,this.get(fieldName));
                    Object v = getValueByDataReaderI(dr, md, mdIdx);
                    if (converter != null) {
                        this.get(fieldName).Value = converter.go(v, this.get(fieldName), md.getColumnType(i + 1));
                    } else {
                        this.get(fieldName).Value = v;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//	public static void UpdateMapByDataReader(Map<String, SqlUpdateItem> map,ResultSet dr, PFFunc<Object, SqlUpdateItem, Integer, Object> converter) {
//
//		try {
//			ResultSetMetaData md = dr.getMetaData();
//			for (int i = 0; i < md.getColumnCount(); i++) {
//				String fieldName = md.getColumnLabel(i + 1);
//
//				if (map.containsKey(fieldName)) {
//					int mdIdx = i + 1;
//					//Object v=getValueByDataReaderI(dr,md,mdIdx,map.get(fieldName));
//					Object v=getValueByDataReaderI2(dr,md,mdIdx);
//					if (converter != null) {
//						map.get(fieldName).Value = converter.go(v, map.get(fieldName), md.getColumnType(i + 1));
//					} else {
//						map.get(fieldName).Value = v;
//					}
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

    public void UpdateByDataReader(ResultSet dr) {
        UpdateByDataReader(dr, null);
    }

    /**
     * @param dr
     * @deprecated 感觉没有必要在读的时候就转换 -- benjamin 20221026
     */
    public void UpdateByDataReaderAutoConvert(ResultSet dr) {

        try {
            ResultSetMetaData md = dr.getMetaData();
            for (int i = 0; i < md.getColumnCount(); i++) {
                String fieldName = md.getColumnLabel(i + 1);

                try {
                    if (this.containsKey(fieldName)) {
                        SqlUpdateItem updateItem = this.get(fieldName);
                        int mdIdx = i + 1;

                        //Object v=getValueByDataReaderI(dr,md,mdIdx,updateItem);
                        Object v = getValueByDataReaderI(dr, md, mdIdx);

                        updateItem.Value = v;

                        convertField(updateItem);
                        // }
                    }
                } catch (Exception e) {
                    if (PFSqlType.Tidb == sqlType
                            && "java.sql.SQLException: Invalid length (10) for type TIMESTAMP".equals(e.toString())) {
                        SGDataHelper.WriteError(new Throwable(), new Exception(SGDataHelper.FormatString(
                                "UpdateByDataReaderAutoConvert[{0}]报错,原因是不支持 date_add产生的10位Timestamp(如date_add(STR_TO_DATE('2021.10.01','%Y.%m.%d'), INTERVAL -1 year))",
                                fieldName), e));
                    } else {
                        SGDataHelper.WriteError(new Throwable(),
                                new Exception("UpdateByDataReaderAutoConvert[" + fieldName + "]报错", e));
                    }
                }

            }
        } catch (Exception e) {
            SGDataHelper.WriteError(new Throwable(), e);
        }

    }

    public void SetItem(String name, SqlUpdateItem value) {
        super.put(name, value);
    }

    /**
     * 注意不要和Set()方法同名重载,有风险.
     *
     * @param key
     * @param value
     */
    public void Set(String key, Object value) {
        if (this.containsKey(key)) {
            this.get(key).Value = value;
        }
    }

    // protected String GetFormatValue(Object val)
    // {
    // if (val != null&&val.GetType().IsEnum) { return String.Format(" {0} ",
    // (int)val); }
    // if (val is String) { return String.Format(" '{0}' ", val); }
    // if (val is decimal || val is int) { return String.Format(" {0} ", val); }
    // return String.Format(" '{0}' ", val);
    // }

    /**
     * 考虑共用此方法,改为静态方法
     *
     * @return
     */
    public static String GetFormatValue(IPFSqlUpdateField item// ,Object val, Class<?> vtype, PFSqlFieldType pfType
    ) {

        String typeString = item.getDstDataTypeLowercaseName();
        SGSqlFieldTypeEnum srcPFType = item.getSrcDataPFType();
        SGSqlFieldTypeEnum dstPFType = item.getDstDataPFType();
        Object val = item.getValue();
		boolean isDateTime=false;//目标列的类型
        boolean isNumber =false;
        if(( null!=dstPFType  && SGSqlFieldTypeEnum.DateTime == dstPFType)
        ||"datetime".equals(typeString)){
            isDateTime=true;
        }else if((null != dstPFType && (SGSqlFieldTypeEnum.Int == dstPFType || SGSqlFieldTypeEnum.Decimal == dstPFType || SGSqlFieldTypeEnum.BigDecimal == dstPFType || SGSqlFieldTypeEnum.Long == dstPFType))
        || "bigdecimal".equals(typeString) || "decimal".equals(typeString) || "int".equals(typeString) || "double".equals(typeString) || "long".equals(typeString)
        ){
            isNumber=true;
        }else if( null != srcPFType && (SGSqlFieldTypeEnum.Int == srcPFType || SGSqlFieldTypeEnum.Decimal == srcPFType || SGSqlFieldTypeEnum.BigDecimal == srcPFType || SGSqlFieldTypeEnum.Long == srcPFType)
				){
			isNumber=true;
		}else if( srcPFType != null && SGSqlFieldTypeEnum.DateTime == srcPFType
				){
			isDateTime=true;
		}


        if (val == null
            // || val == DBNull.Value //benjamin20190910
        ) {
            if (isNumber || "bool".equals(typeString)
            ) {
                return " null ";
            } else if (isDateTime ) {
                return " null ";
            } else if ("string".equals(typeString)) {
                return " '' ";
            } else if ("system.dbnull".equals(typeString)) {
                return " null ";
            } else {
                return " '' ";
            }
        }
//        var nonnullType = PFDataHelper.GetNonnullType(vtype);
//        if (nonnullType.IsEnum) { return String.Format(" {0} ", (int)val); }
        if (val instanceof String) {
            // return String.Format(" '{0}' ", val);
            return SGDataHelper.FormatString(" '{0}' ", (val.toString()).replace("'", "''").replace("\\", "\\\\"));// 如果字符串有单引号,会报错--benjamin20200311
        }
        if (isNumber) {
            return SGDataHelper.FormatString(" {0} ", val);
        }
        if ("bool".equals(typeString)
        		||SGSqlFieldTypeEnum.Bool==dstPFType//mysql 中tinyint(1) 插入 'true' 报错，所以加此条件 --benjamin 20260319
        		) {
            return SGDataHelper.FormatString(" {0} ", SGDataHelper.ObjectToBool0(val) == true ? 1 : 0);
        }
//        if (val instanceof List)//支持String[]的成员
//        {
//            var list = val as IList<String>;
//            return String.Format(" '{0}' ", String.Join(",", list.ToArray()));
//        }
//        if (nonnullType == typeof(MySql.Data.Types.MySqlDateTime))
//        //if(val is MySql.Data.Types.MySqlDateTime)
//        {
//            return String.Format(" '{0}' ", ((MySql.Data.Types.MySqlDateTime)val).Value.ToString(PFDataHelper.DateFormat));
//        }
        if (isDateTime)
        // if(val is MySql.Data.Types.MySqlDateTime)
        {
            return SGDataHelper.FormatString(" '{0}' ", SGDataHelper.ObjectToDateString(val, SGDataHelper.DateFormat));
        }

        return SGDataHelper.FormatString(" '{0}' ", val);
    }

    private Object getValueByMapI(Map<String, Object> modelDict, IPFSqlUpdateField field) {
        Object v = null;
        String matchKey = getMatchKey(modelDict, field.getKey());
        if (null != matchKey) {
            //v = modelDict.get(field.getKey());
            v = modelDict.get(matchKey);
            if (null == field.getSrcDataPFType() && v != null) {
                //field.VType = v.getClass();
                field.setSrcDataPFType(SGSqlFieldTypeEnum.InitByClass(v.getClass()));
            }
        } else {
            //updateItem.Value = null;
        }
        return v;
    }

    private Object getValueByClassI(Object model, SqlUpdateItem field) throws Exception {
        Object v = null;
        if (field.PInfo == null) {
            try {
                field.PInfo = model.getClass().getDeclaredField(field.getKey());
                field.PInfo.setAccessible(true);
            } catch (NoSuchFieldException | SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        v = field.PInfo.get(model);
        return v;
    }

    //	/**
//	 * 这个方法应该有问题,getValue应该是根据src的类型,而不是dst的类型-- benjamin todo
//	 * @param dr
//	 * @param md
//	 * @param mdIdx
//	 * @param field
//	 * @return
//	 * @throws SQLException
//	 */
//	private Object getValueByDataReaderI(ResultSet dr,ResultSetMetaData md,
//			int mdIdx,IPFSqlUpdateField field) throws SQLException {
//		Object v = null;
////        if("qx_day".equals(field.getKey())) {
////        	String aa="aa";
////        }
//		if (java.sql.Types.TIMESTAMP==field.getDstDataType()) {// timestamp(数据库中是DateTime)一定要用getDate,否则会多一天,暂不知道怎么解决(用getObject和getTimestamp得到的值都多一天)--benjamin
//																	// todo
//			//v = dr.getDate(mdIdx);// 这样会丢了分和秒
//			v = dr.getTimestamp(mdIdx, Calendar.getInstance());// 如果不传Calendar会多了一天
//		} else if (java.sql.Types.DATE == field.getDstDataType()) {
//			v = dr.getDate(mdIdx, Calendar.getInstance());
//		} else {
//			v = dr.getObject(mdIdx);
//		}
//		if(null==field.getSrcDataType()) {
//			field.setSrcDataType( md.getColumnType(mdIdx));
//		}
//		return v;
//	}
    public static Object getValueByDataReaderI(ResultSet dr, ResultSetMetaData md,
                                               int mdIdx) throws SQLException {
        Object v = null;
//        if("qx_day".equals(field.getKey())) {
//        	String aa="aa";
//        }
        int dstDataType = md.getColumnType(mdIdx);
        if (java.sql.Types.TIMESTAMP == dstDataType) {// timestamp(数据库中是DateTime)一定要用getDate,否则会多一天,暂不知道怎么解决(用getObject和getTimestamp得到的值都多一天)--benjamin
            // todo
            //v = dr.getDate(mdIdx);// 这样会丢了分和秒
            v = dr.getTimestamp(mdIdx, Calendar.getInstance());// 如果不传Calendar会多了一天
        } else if (java.sql.Types.DATE == dstDataType) {
            v = dr.getDate(mdIdx, Calendar.getInstance());
        } else {
            v = dr.getObject(mdIdx);
        }
//		if(null==field.getSrcDataType()) {
//			field.setSrcDataType( md.getColumnType(mdIdx));
//		}
        return v;
    }

    private Object getValueByDataRowI(PFDataRow dataRow,//ResultSetMetaData md,
                                      IPFSqlUpdateField field) {
        Object v = null;
        PFDataColumn col = dataRow.getColumnObject(field.getKey());
        if (null != col) {
            v = col.getValue();
//           i.Value.Value = modelDict[i.Key];
//			if (updateItem.VType == null && updateItem.Value != null) {
//				updateItem.VType = updateItem.Value.getClass();
//			}
            if (null == field.getSrcDataPFType() && null != v) {
                field.setSrcDataPFType(SGSqlFieldTypeEnum.InitByClass(v.getClass()));
            }
        } else {
            //updateItem.Value = null;
        }
        return v;
    }

    private String getMatchKey(Map<String, Object> modelDict, String key) {
        if (modelDict.containsKey(key)) {
            return key;
        } else if (lowerKeyMap != null && modelDict.containsKey(lowerKeyMap.get(key))) {
            return lowerKeyMap.get(key);
        }
        return null;
    }

    private void convertField(SqlUpdateItem field) {
        if (field.convertFrom == null) {
            field.convertFrom = SGDataHelper.GetObjectToPFTypeBySqlTypeConverter0(field.Value, field.getSrcDataType(),
                    field.getSrcDataPFType());
        }
        if (null == field.convertFrom) {
            if (null != field.Value) {
////				throw new Exception(
////						"GetObjectToPFTypeBySqlTypeConverter0 有问题, value不为空, 却不能得到converter");
//				PFDataHelper.WriteError(new Throwable(), new Exception(
//						"GetObjectToPFTypeBySqlTypeConverter0 有问题, value不为空, 却不能得到converter"));
                SGDataHelper.WriteError(new Exception(
                        "GetObjectToPFTypeBySqlTypeConverter0 有问题, value不为空, 却不能得到converter"));
            }
        } else {
            field.Value = field.convertFrom.convert(field.Value);
        }
    }
}
