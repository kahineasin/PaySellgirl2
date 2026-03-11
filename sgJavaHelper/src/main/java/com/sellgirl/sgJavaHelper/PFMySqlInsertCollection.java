package com.sellgirl.sgJavaHelper;

import com.sellgirl.sgJavaHelper.sql.PFSqlInsertCollection;

/*
 * ClickHouse也用这个
 */
public class PFMySqlInsertCollection extends PFSqlInsertCollection {
/**
	 * 
	 */
	private static final long serialVersionUID = -1138527804081458390L;
@Override
    public  String GetFieldQuotationCharacterL()
    {
            return "`";
    }
@Override
    public String GetFieldQuotationCharacterR()
    {
            return "`";
    }
    public PFMySqlInsertCollection()
    {
    	super();
    }
    public PFMySqlInsertCollection(Object model,  String... names
        )
    {
    	super(model, names);
    }
}
