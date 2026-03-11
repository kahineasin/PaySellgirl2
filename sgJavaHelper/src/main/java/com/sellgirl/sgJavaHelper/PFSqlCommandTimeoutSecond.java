package com.sellgirl.sgJavaHelper;

public class PFSqlCommandTimeoutSecond {

    /// <summary>
    /// 数据库默认30秒
    /// </summary>
    private static int _commandTimeout = 30;
    private static final int _hugeCommandTimeout = 7200;
    private static final int _normalTransferCommandTimeout = 1800;
    private static final int _updateOneRowCommandTimeout = 10;
    public PFSqlCommandTimeoutSecond(int second)
    {
        _commandTimeout = second;
    }
    /// <summary>
    /// 超大表2小时
    /// </summary>
//    public static PFSqlCommandTimeoutSecond Huge { get { return new PFSqlCommandTimeoutSecond(_hugeCommandTimeout); } }
//    public static PFSqlCommandTimeoutSecond Huge=new PFSqlCommandTimeoutSecond(_hugeCommandTimeout);//这样有被赋值改变的风险
    public static PFSqlCommandTimeoutSecond Huge() {
    	return new PFSqlCommandTimeoutSecond(_hugeCommandTimeout);
    	} 

    /// <summary>
    /// 正常传输30分钟
    /// </summary>
//    public static PFSqlCommandTimeoutSecond NormalTransfer { get { return new PFSqlCommandTimeoutSecond(1800); } }
   // public static PFSqlCommandTimeoutSecond NormalTransfer=new PFSqlCommandTimeoutSecond(_normalTransferCommandTimeout);
    public static PFSqlCommandTimeoutSecond NormalTransfer() {
    	return new PFSqlCommandTimeoutSecond(_normalTransferCommandTimeout);
    	} 
    /**
     * 更新一行时,超时设置短一些,试试这样可不可以减少死锁的问题
     * @return
     */
    public static PFSqlCommandTimeoutSecond UpdateOneRow() {
    	return new PFSqlCommandTimeoutSecond(_updateOneRowCommandTimeout);
    	} 
    public static int Max(int... second)
    {
        int max = -1;
        for( int i:second)
        {
            if (i == 0) { return 0; }
            if(i> max) { max = i; }
        }
        return max;
    }
    public int ToInt() {
        return _commandTimeout;
    }
    public Boolean IsHuge()
    {
        return _commandTimeout == 0 || _commandTimeout >= _hugeCommandTimeout;
    }
    public static Boolean IsHugeSecond(int second)
    {
        return second == 0 || second >= _hugeCommandTimeout;
    }
}
