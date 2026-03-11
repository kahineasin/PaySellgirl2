package com.sellgirl.sgJavaHelper;


import com.sellgirl.sgJavaHelper.sql.SGSqlWhereCollection;

public class PFMySqlWhereCollection extends SGSqlWhereCollection
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
    public  String FieldQuotationCharacterL()
    {
            return "`";
    }
	@Override
    public  String FieldQuotationCharacterR()
    {
            return "`";
    }
	
}