package com.sellgirl.sgJavaHelper;

import com.sellgirl.sgJavaHelper.sql.SGSqlUpdateCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlWhereCollection;

/*
 * ClickHouse也用这个
 */
public class PFMySqlUpdateCollection extends SGSqlUpdateCollection {
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
protected SGSqlWhereCollection GetWhereCollection() {
    return new PFMySqlWhereCollection ();
}
    public PFMySqlUpdateCollection()
    {
    	super();
    }
    public PFMySqlUpdateCollection(Object model,  String... names
        )
    {
    	super(model, names);
    }
}
