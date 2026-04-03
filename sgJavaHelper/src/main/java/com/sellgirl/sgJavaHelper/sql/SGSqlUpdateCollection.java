package com.sellgirl.sgJavaHelper.sql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public  class SGSqlUpdateCollection extends BaseSqlUpdateCollection//<SqlUpdateCollection>
    //where TWhereCollection : SqlWhereCollection, new()
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -9069358574569037330L;
//	@Override
//	protected PFSqlType sqlType() {
//		return PFSqlType.MySql;
//	}
	

	protected List<String> _updateFields;
    //protected IList<string> _keyFields;

    protected SGSqlWhereCollection _where;
    //public TWhereCollection Where { get { return _where; } }
    public Map<String, SqlWhereItem> PrimaryFields=null;
    protected  SGSqlWhereCollection GetWhereCollection() {
        return new SGSqlWhereCollection ();
    }
    public SGSqlUpdateCollection()
    {
    	super();
    }
    public SGSqlUpdateCollection(Object model, String... names
        )
    {
    	super(model,names);
    }
    /// <summary>
    /// 更新的字段(用于生成set语句)
    /// </summary>
    /// <param name="names"></param>
    /// <returns></returns>
    public  SGSqlUpdateCollection UpdateFields( String... names)
    {
        _updateFields =Arrays.asList( names);
        return this;
    }
    /**
     * 主键字段(用于生成where语句)
     * @param names
     * @return
     */
    public  SGSqlUpdateCollection PrimaryKeyFields(String... names)
    {
        return PrimaryKeyFields(true, names);

    }
    public  SGSqlUpdateCollection PrimaryKeyFields(Boolean checkWhereNotNull, String... names)
    {
        ////_keyFields = new List<string>();
        //if (_where == null)
        //{
        //    _where = new SqlWhereCollection { };
        //}
        _where=GetWhereCollection();
        _where.setIgnoreNullValue(false);
        PrimaryFields = new HashMap<String, SqlWhereItem>();
        for(String i : names)
        {
            Object v = this.get(i).Value;
            if (checkWhereNotNull && SGDataHelper.StringIsNullOrWhiteSpace(v)) { try {
				throw new Exception("更新时where条件不能为空!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
            ////_where.Add(i, this[i].Value);//这样不保险，因为names有可能是大写的，但this[i].Key有可能是小写的
            //_where.Add(this[i].Key, this[i].Value);
            SqlWhereItem whereItem = new SqlWhereItem(this.get(i).Key, this.get(i).Value);
            _where.add(whereItem);
            PrimaryFields.put(this.get(i).Key, whereItem);
        }
        //_primaryKeyFields = names;
        if (_updateFields == null)//一般来讲,除去主键之外的字段都应该要更新
        {
        	_updateFields= SGDataHelper.MapSelect(this, (a,b)->{
        		return a;
        	}).stream().filter(a->!Arrays.asList(names).contains(a)).collect(Collectors.toList());
        	
            //_updateFields = this.Select(a => a.Key).Where(a => !names.Contains(a)).ToList();
        }
        return this;
    }
    @Override
    public  void Set(String key, Object value)
    {
        if (
        		null!=PrimaryFields//20260318
        		&&PrimaryFields.containsKey(key))
        {
        	SGDataHelper.ListFirst(_where, a->a.Key.equals(key)).Value=value;
        }
        super.Set(key, value);
    }

//    //这样做原子更新不值得,1.性能;2.原子更新通常是单列的,直接getFormat()就行了
//    private HashMap<String,Boolean> atomicMap=null;
//    public  void Set(String key, Object value,boolean Atomic)
//    {
//    	if(Atomic) {
//    		if(null==atomicMap) {
//    			atomicMap=new HashMap<String,Boolean>();
//    		}
//    		atomicMap.put(key, Atomic);
//    	}
//    }
    /// <summary>
    /// 格式如:name1='value1',name2='value2',name3=time3,...
    /// </summary>
    /// <returns></returns>
    public String ToSetSql()
    {
        int count = 0;
        String s1 = "";
        for (String i : _updateFields)
        {
            if (count != 0) { s1 += ","; }
//            //s1 += ("[" + i + "]" + "=");
//            if(null!=atomicMap&&atomicMap.containsKey(i)) {
//            	s1 += SGDataHelper.FormatString("{0}{1}{2}={0}{1}{2}", GetFieldQuotationCharacterL(), i, GetFieldQuotationCharacterR());
//            	Object val=this.get(i);//正数加符号+
//                s1 += GetFormatValue();
//            }else {
//            	s1 += SGDataHelper.FormatString("{0}{1}{2}=", GetFieldQuotationCharacterL(), i, GetFieldQuotationCharacterR());
//                s1 += GetFormatValue(this.get(i));
//            }
            s1 += SGDataHelper.FormatString("{0}{1}{2}=", GetFieldQuotationCharacterL(), i, GetFieldQuotationCharacterR());
//            //s1 += GetFormatValue(this.get(i).Value, this.get(i).VType);
//            //s1 += GetFormatValue(this.get(i).Value, this.get(i).VType,this.get(i).VPFType);
            s1 += GetFormatValue(this.get(i));
            count++;
        }

        return s1;
    }
    
    /**
     * Atomic原子更新时就用此方法拼接吧
     * @param key
     * @return
     */
    public String GetFormatKey(String key) {
    	return SGDataHelper.FormatString("{0}{1}{2}", GetFieldQuotationCharacterL(), key, GetFieldQuotationCharacterR());
    }
    /// <summary>
    /// 返回格式如: where xx=xx and ...
    /// </summary>
    /// <returns></returns>
    public String ToWhereSql(SGSqlWhereCollection.WhereOrAnd woa )
    {
        return _where.ToSql(woa);
    }
    public String ToWhereSql()
    {
        return _where.ToSql(SGSqlWhereCollection.WhereOrAnd.Where);
    }

    /**
     * 此方法并非不能用,但版本太旧了,可能需要根据父类方法来更新判断类型的方式
     * @param model
     */
    @Override
	@Deprecated
    public  void UpdateModelValue(Object model)
    {
        super.UpdateModelValue(model);
        if (model instanceof Map<?, ?>)
        {
        	//Map<String, Object> modelDict = (Map<String, Object>)model ;
        	Map<String, Object> modelDict =SGDataHelper.<Map<String, Object>>ObjectAs(model);
            for (SqlWhereItem i : _where)
            {
                if (modelDict.containsKey(i.Key))
                {
                    i.Value = modelDict.get(i.Key);
                }
                else
                {
                    i.Value = null;
                }
            }
        }
        else
        {
            for(SqlWhereItem i : _where)
            {
                try {
					i.Value = this.get(i.Key).PInfo.get(model);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
    @Override
    public  void UpdateModelValueAutoConvert(Object model)
    {
        //super.UpdateModelValue(model);
        super.UpdateModelValueAutoConvert(model);
        if (model instanceof Map<?, ?>)
        {
        	//Map<String, Object> modelDict = (Map<String, Object>)model ;
        	Map<String, Object> modelDict =SGDataHelper.<Map<String, Object>>ObjectAs(model);
            for (SqlWhereItem i : _where)
            {
                if (modelDict.containsKey(i.Key))
                {
                    //i.Value = modelDict.get(i.Key);
                    i.Value = this.get(i.Key).Value;
                }
                else
                {
                    i.Value = null;
                }
            }
        }else if(model instanceof PFDataRow) {
			PFDataRow dataRow= SGDataHelper.ObjectAs(model);            
			for (SqlWhereItem i : _where)
            {
				Object col=dataRow.getColumnObject(i.Key);
                //if (dataRow.containsKey(i.Key))
                if (null!=col)
                {
                    i.Value = this.get(i.Key).Value;
                }
                else
                {
                    i.Value = null;
                }
            }
		}
        else
        {
            for(SqlWhereItem i : _where)
            {
                try {
					i.Value = this.get(i.Key).PInfo.get(model);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
}