package com.sellgirl.sgJavaHelper.sql;

import java.awt.List;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SqlExpressionOperator;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 注意最好不要直接new, 使用sqlExec.getWhereCollection() 获得，这样才能识别不同数据库返回不同的子类
 */
public class SGSqlWhereCollection  extends ArrayList<SqlWhereItem>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Boolean _ignoreNullValue = true;
	public  String FieldQuotationCharacterL (){  return "[";  }
    public  String FieldQuotationCharacterR() {  return "]";  }
    public Boolean getIgnoreNullValue() {
		return _ignoreNullValue;
	}
	public void setIgnoreNullValue(Boolean ignoreNullValue) {
		this._ignoreNullValue = ignoreNullValue;
	}
	public enum WhereOrAnd
    {
        Where ,
        And 
    }
//    /// <summary>
//    /// 分页参数
//    /// </summary>
//    public class Pagination
//    {
//        public Pagination(int start, int end, String sort)
//        {
//            Start = start;
//            End = end;
//            Sort = sort;
//        }
//        public int Start { get; set; }
//        public int End { get; set; }
//        /// <summary>
//        /// 分页参数的排序字段必需指定主键，否则会很慢
//        /// </summary>
//        public String Sort { get; set; }
//    }
    public void Add(String name, Object value, SqlExpressionOperator expressionOperator )
    {
        super.add(new SqlWhereItem(name, value, expressionOperator));
    }
    public void Add(String name, Object value)
    {
        this.Add(name, value,  SqlExpressionOperator.Equal);
    }
    /// <summary>
    /// 加中括号,如 a.typeid 变为 a.[typeid]
    /// </summary>
    private String FormatKey(String key)
    {
        if (key.contains("isnull"))
        {
            return key;
        }
        return key.replaceAll( "([^.]+)$", FieldQuotationCharacterL() + "$1" + FieldQuotationCharacterR());
//    	return key.replaceFirst("([^.]+)$", "[$1]");
    }
    public String ToSql()
    {
    	return ToSql( WhereOrAnd.Where);
    }
    public String ToSql(WhereOrAnd woa)
    {
    	String result = "";
        int count = 0;
        String prev = "";
//        for (SqlWhereItem i : this)
        for (int idx=0;idx<this.size();idx++)
        {
        	SqlWhereItem i=this.get(idx);
            //if (i.Value != null)
              if (i.Value != null || (!_ignoreNullValue))
            {
                prev = count == 0 ?
                    (woa == WhereOrAnd.Where ? " where " : " and ")
                    : " and ";

                Object val = i.Value;
                //if (i.Value instanceof DateTime) { tmpControl.Text = PFDataHelper.ObjectToDateString(val, tmpControl.Attributes["dateFmt"]); }
                if (val instanceof String && (
                		(!_ignoreNullValue)
                		||(!SGDataHelper.StringIsNullOrWhiteSpace(val.toString()))
                	))
                {
                    switch (i.ExpressionOperator)
                    {
                        //既然为空时不进入这里,那么isnull应该是无必要的--wxj20181022
                        case Like:
                            //result += prev + PFDataHelper.FormatString(" isnull({0},'') like '%{1}%' ", i.Key, i.Value);
                            result += prev + SGDataHelper.FormatString(" {0} like '%{1}%' ", FormatKey(i.Key), i.Value);
                            break;
                        case StartWith:
                            //result += prev + PFDataHelper.FormatString(" isnull({0},'') like '{1}%' ", i.Key, i.Value);
                            result += prev + SGDataHelper.FormatString(" {0} like '{1}%' ", FormatKey(i.Key), i.Value);
                            break;
                        case EndWith:
                            //result += prev + PFDataHelper.FormatString(" isnull({0},'') like '%{1}' ", i.Key, i.Value);
                            result += prev + SGDataHelper.FormatString(" {0} like '%{1}' ", FormatKey(i.Key), i.Value);
                            break;
                        case NotEqual:
                            result += prev + SGDataHelper.FormatString(" {0} <> '{1}' ", FormatKey(i.Key), i.Value);
                            break;
                        case Less:
                            result += prev + SGDataHelper.FormatString(" {0} < '{1}' ", FormatKey(i.Key), i.Value);
                            break;
                        case LessOrEqual:
                            result += prev + SGDataHelper.FormatString(" {0} <= '{1}' ", FormatKey(i.Key), i.Value);
                            break;
                        case Greater:
                            result += prev + SGDataHelper.FormatString(" {0} > '{1}' ", FormatKey(i.Key), i.Value);//效率高一些--20200507
                            break;
                        case GreaterOrEqual:
                            result += prev + SGDataHelper.FormatString(" {0} >= '{1}' ", FormatKey(i.Key), i.Value);//效率高一些--20200507
                            break;
                        default:
                            //result += prev + PFDataHelper.FormatString(" isnull({0},'')='{1}' ", i.Key, i.Value);
                            result += prev + SGDataHelper.FormatString(" {0}='{1}' ", FormatKey(i.Key), i.Value);
                            break;
                    }
                    count++;
                }
                else if (val instanceof BigDecimal || val instanceof Integer||val instanceof Long)
                {
                    //result += prev + PFDataHelper.FormatString(" isnull({0},0)={1} ", FormatKey(i.Key), i.Value);
                    switch (i.ExpressionOperator)
                    {
                        //日期的范围比较有些特别,把临界点包含进来比较适合
                        case Less:
                            //result += prev + PFDataHelper.FormatString(" isnull({0},0)<={1} ", FormatKey(i.Key), i.Value);
                            result += prev + SGDataHelper.FormatString(" {0}<{1} ", FormatKey(i.Key), i.Value);
                            break;
                        case LessOrEqual:
                            ////result += prev + string.Format(" isnull({0},'') <= '{1}' ", FormatKey(i.Key), i.Value);
                            //result += prev + string.Format(" isnull({0},0)<={1} ", FormatKey(i.Key), i.Value);
                            result += prev + SGDataHelper.FormatString(" {0}<={1} ", FormatKey(i.Key), i.Value);
                            break;
                        case Greater:
                            //result += prev + PFDataHelper.FormatString(" isnull({0},0)>={1} ", FormatKey(i.Key), i.Value);
                            result += prev +SGDataHelper.FormatString(" {0}>{1} ", FormatKey(i.Key), i.Value);
                            break;
                        case GreaterOrEqual:
                            //result += prev + string.Format(" isnull({0},'') >= '{1}' ", FormatKey(i.Key), i.Value);
                            //result += prev + string.Format(" isnull({0},0)>={1} ", FormatKey(i.Key), i.Value);
                            result += prev +SGDataHelper.FormatString(" {0}>={1} ", FormatKey(i.Key), i.Value);
                            break;
                        default:
                            //result += prev + PFDataHelper.FormatString(" isnull({0},'')='{1}' ", FormatKey(i.Key), i.Value);
                            //result += prev + PFDataHelper.FormatString(" isnull({0},0)={1} ", FormatKey(i.Key), i.Value);
                            result += prev + SGDataHelper.FormatString(" {0}={1} ", FormatKey(i.Key), i.Value);
                            break;
                    }
                    count++;
                }
                else if ((val instanceof Calendar||val instanceof SGDate) && val != null)
                {
                	String dateStr="";
                	   SimpleDateFormat sdf = new SimpleDateFormat(SGDataHelper.DateFormat);
                	if(val instanceof Calendar) {
                	   dateStr = sdf.format(((Calendar)val).getTime());
                	}else if(val instanceof SGDate) {
                       dateStr = sdf.format(((SGDate)val).ToCalendar().getTime());
                	}
                    switch (i.ExpressionOperator)
                    {

                        //日期的范围比较有些特别,把临界点包含进来比较适合
                        case Less:
                            result += prev + SGDataHelper.FormatString(" {0} < '{1}' ", FormatKey(i.Key), dateStr);
                            break;
                        case LessOrEqual:
                            result += prev + SGDataHelper.FormatString(" {0}<= '{1}' ", FormatKey(i.Key), dateStr);
                            break;
                        case Greater:
                            result += prev + SGDataHelper.FormatString(" {0} > '{1}' ", FormatKey(i.Key), dateStr);
                            break;
                        case GreaterOrEqual:
                            result += prev +  SGDataHelper.FormatString(" {0}>= '{1}' ", FormatKey(i.Key), dateStr);
                            break;
                        default:
                            result += prev + SGDataHelper.FormatString(" {0}='{1}' ", FormatKey(i.Key), dateStr);
                            break;
                    }
                    count++;
                }
                else if (val instanceof Boolean && val != null)
                {
                	Boolean b = (Boolean)val;
                    result += prev + SGDataHelper.FormatString(" {0}={1} ", FormatKey(i.Key), b ? 1 : 0);
                    count++;
                }
                else if (val instanceof UUID)
                {
                    result += prev + SGDataHelper.FormatString(" {0}='{1}' ", FormatKey(i.Key), i.Value);
                    count++;
                }
                else if ((val instanceof List||val instanceof Object[]) && i.ExpressionOperator == SqlExpressionOperator.IN)
                {
//                	ArrayList<String> list=PFDataHelper.<String>ObjectToList(val);
//                    //if (list.getItemCount() > 0)
//                        if (list.size() > 0)
//                    {
//                        //result += prev + PFDataHelper.FormatString(" {0} in('{1}') ", FormatKey(i.Key), String.Join("','", list));
//                        result += prev + PFDataHelper.FormatString(" {0} in('{1}') ", FormatKey(i.Key), String.join("','", list));//为了net2.0版本
//                        count++;
//                    }
                    	ArrayList<Object> list=SGDataHelper.<Object>ObjectToList(val);
                        //if (list.getItemCount() > 0)
                            if (list.size() > 0)
                        {
                            ////result += prev + PFDataHelper.FormatString(" {0} in('{1}') ", FormatKey(i.Key), String.Join("','", list));
                            //result += prev + PFDataHelper.FormatString(" {0} in('{1}') ", FormatKey(i.Key), String.join("','", list));//为了net2.0版本
                            result += prev +SGDataHelper.FormatString(" {0} in({1}) ", FormatKey(i.Key), this.JoinList(list)) ;//
                            count++;
                        }
                }
                else if ((val instanceof List||val instanceof Object[]) && i.ExpressionOperator == SqlExpressionOperator.NotIn)
                {
//                    //var list = val as List<String>;
//                    //List<String> list=(List<String>)val;
//                	ArrayList<String> list=PFDataHelper.<String>ObjectToList(val);
//                    if (list.size() > 0)
//                    {
//                        result += prev + PFDataHelper.FormatString(" {0} not in('{1}') ", FormatKey(i.Key), String.join("','", list));
//                        count++;
//                    }
                	ArrayList<Object> list=SGDataHelper.<Object>ObjectToList(val);
                    if (list.size() > 0)
                    {
                       //result += prev + PFDataHelper.FormatString(" {0} not in('{1}') ", FormatKey(i.Key), String.join("','", list));
                        result += prev +SGDataHelper.FormatString(" {0} not in({1}) ", FormatKey(i.Key), this.JoinList(list)) ;//
                        count++;
                    }
                }
            }
        }
        return result;
    }
    public String JoinList(ArrayList<Object> list) {
    	String r="";
    	int cnt=0;
    	for(Object i :list) {
    		if(cnt>0) {
    			r+=",";
    		}
    		if(i instanceof String) {
    			r+="'"+((String)i)+"'";
    		}else {
    			r+=SGDataHelper.ObjectToString(i);
    		}
    		cnt++;
    	}    	
    	return r;
    }
//    public String ToPageSql(Pagination pagination)
//    {
//        return PFDataHelper.FormatString(@"
//With TTTTT AS
//(
//{{0}}
//)
//select * from
//(
//select ROW_NUMBER() OVER (ORDER BY {0}) as rownumber,*,(select count(*) from TTTTT) as rowtotal from TTTTT
//) as T
//where rownumber between {1} and {2}
//", pagination.Sort, pagination.Start, pagination.End);
//    }
}
