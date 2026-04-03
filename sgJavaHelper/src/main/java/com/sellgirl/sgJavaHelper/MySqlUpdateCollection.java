package com.sellgirl.sgJavaHelper;


import com.sellgirl.sgJavaHelper.sql.SGSqlUpdateCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlWhereCollection;

public class MySqlUpdateCollection extends SGSqlUpdateCollection//SqlUpdateCollection<MySqlWhereCollection>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -988385104886032631L;
	@Override
    public  String GetFieldQuotationCharacterL()
    {
            return "`";
    }
	@Override
    public  String GetFieldQuotationCharacterR()
    {
            return "`";
    }
	@Override
    protected SGSqlWhereCollection GetWhereCollection()
    {
        return new PFMySqlWhereCollection();
    }
    public MySqlUpdateCollection() {
    	super();
    }
    public MySqlUpdateCollection(Object model, String... names
        )
    {
    	super(model, names);
    }
    //public override SqlUpdateCollection PrimaryKeyFields(bool checkWhereNotNull, params string[] names)
    //{
    //    _where = new MySqlWhereCollection();
    //    return base.PrimaryKeyFields(checkWhereNotNull, names);
    //}
}