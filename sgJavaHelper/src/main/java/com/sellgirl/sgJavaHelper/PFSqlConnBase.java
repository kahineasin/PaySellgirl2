package com.sellgirl.sgJavaHelper;

public abstract class PFSqlConnBase {
	public int CommandTimeOut=30;
    public void SetCommandTimeOut(PFSqlCommandTimeoutSecond second)
    {
        CommandTimeOut = second.ToInt();
    }
    public void UpToCommandTimeOut(PFSqlCommandTimeoutSecond second)
    {
        CommandTimeOut = PFSqlCommandTimeoutSecond.Max(second.ToInt(), CommandTimeOut);
    }
}
