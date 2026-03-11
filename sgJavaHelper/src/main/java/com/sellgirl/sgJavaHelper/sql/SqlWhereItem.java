package com.sellgirl.sgJavaHelper.sql;

import java.io.Serializable;

import com.sellgirl.sgJavaHelper.SqlExpressionOperator;

public class SqlWhereItem implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3015315128677978374L;
	public SqlWhereItem(String key, Object value, SqlExpressionOperator expressionOperator)
    {
        Key = key;
        Value = value;
        ExpressionOperator = expressionOperator;
    }
    public SqlWhereItem(String key, Object value)
    {
    	this(key,value,SqlExpressionOperator.Equal);
    }
    public String Key ;
    public Object Value ;
    public SqlExpressionOperator ExpressionOperator ;
}
