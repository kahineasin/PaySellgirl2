package com.sellgirl.sgJavaHelper.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.PFDataTableFieldValidModel;
import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.PFSqlParameter;
import com.sellgirl.sgJavaHelper.SGRef;

/**
 * 数据库操作工具接口.
 */
public interface ISqlExecute extends AutoCloseable{
	Connection GetConn();
	boolean OpenConn();
	void CloseConn();
    void AutoCloseConn(boolean autoCloseConn);
    Boolean CloseReader(ResultSet reader);
    void SetCommandTimeOut(PFSqlCommandTimeoutSecond second);
    void UpToCommandTimeOut(PFSqlCommandTimeoutSecond second);

    void SetInsertOption(Consumer<BatchInsertOption> insertOptionAction);
     BatchInsertOption GetInsertOption();
    void SetSqlUpdateOption(Consumer<BatchInsertOption> insertOptionAction);
    void SetDeleteOption(Consumer<BatchDeleteOption> deleteOptionAction);
    void SetQueryOption(Consumer<BatchQueryOption> queryOptionAction);
    
    /**
     *  这个方法好像没有必要,像TransferTable那样传入参数好了
     *  先留着此方法吧,有这方法就不需要在HugeUpdate方法加stop参数,其实也好
     * @param stopAction
     */
    //@Deprecated
    void SetStopAction(Predicate<Boolean> stopAction);
    void ClearError();
    void SetErrorNotCatchAction(Function<Throwable,Boolean> errorNotCatchAction);
    Throwable GetErrorLine();
    String GetErrorFullMessage(); 
    //void setNeedSendConnErrorMessage(boolean sendConnErrorMessage);
    //Exception GetError();
    /**
     * 此版本关闭conn有风险，比如在mysql 5.1.46-community中，关闭conn之后再调用md.isReadOnly，会报错
     * @param tableName
     * @param fieldNames
     * @return
     */
    @Deprecated
    ResultSetMetaData GetMetaData(String tableName, List<String> fieldNames);
	ResultSetMetaData GetMetaDataNotClose(String tableName, List<String> fieldNames);

	/**
	 * 事实证明,获得ps时的程序时区,会影响后面插入到数据库的时间的时区(如果程序的时区和数据库不一样的话,也可以在ps.setTimestamp(ts,cal)时用数据库时区的cal参数
	 * @param tableName
	 * @param fieldNames
	 * @param autoCommit
	 * @return
	 */
    PreparedStatement GetPs(String tableName, List<String> fieldNames,boolean autoCommit);
    PreparedStatement updatePs(PreparedStatement crs, ResultSetMetaData md, int mdIdx,
			SqlUpdateItem dstInsertI) throws SQLException;
//    PreparedStatement insertIntoCachedRowSet(PreparedStatement crs, ResultSetMetaData md,
//			PFSqlInsertCollection dstInsert
//	) throws SQLException;
    /**
     * insert update 都可以用
     * @param crs
     * @param md
     * @param dstInsert
     * @return
     * @throws SQLException
     */
    PreparedStatement insertIntoCachedRowSet(PreparedStatement crs, ResultSetMetaData md,
    		BaseSqlUpdateCollection dstInsert
	) throws SQLException;
    
    SGSqlWhereCollection getWhereCollection();
	SGSqlInsertCollection getInsertCollection();
    SGSqlInsertCollection getInsertCollection(ResultSetMetaData dstMd);
    PFSqlUpdateCollection getUpdateCollection(ResultSetMetaData dstMd);
    /**
     * 已插入的行数，便于知道sql有没有超出limit的范围
     * @return
     */
    public int GetInsertedCnt();

    /**
     * 最后插入的id，便于用id>xx的方式代替limit的offset参数
     * @return
     */
    public long GetLastInsertedId() ;
    Boolean IsTableExist(String tableName);
    List<SGSqlFieldInfo> GetTableFields(String tableName);
    /**
     * 可编辑列(多用于insert)
     * @param tableName
     * @return
     */
    List<SGSqlFieldInfo> GetTableEditableFields(String tableName);//,SGRef<ResultSetMetaData> mdRef);
    
//    Boolean CreateTable(SqlCreateTableCollection models);
	ResultSet GetDataReader(String sql);
	ResultSet GetHugeDataReader(String sql);
	ResultSet GetDataReaderUpdatable(String sql);
	
	Boolean HugeDelete(String updateSql);
	
	/*便于删除语句不相同的
	 * where:可空
	 */
	Boolean Delete(String tableName,Consumer<SGSqlWhereCollection> whereAction);
	/**
	 * 利用 ROWCOUNT 控制按10000行每批来删除
	 * @param tableName
	 * @param whereAction
	 * @return
	 */
	Boolean HugeDelete(String tableName,Consumer<SGSqlWhereCollection> whereAction);
	Boolean TruncateTable(String tableName);

	/**
	 * 大量更新,本来是给tidb用,但应该也能在mysql家族中应用
	 * 
	 * tidb的事务有限制,要分批更新,所以要如下使用(要求必需有has_updated字段辅助更新)
	 * 
	 * 用法1:(重点是where is_new_hy判断更新完毕) update monthly_hyzl set is_new_hy=(case when
	 * accmon=date_format(create_date, '%Y.%m') then CONVERT(bit,1) else
	 * CONVERT(bit,0) end) where is_new_hy is null
	 * 
	 * 用法2:(重点是has_updated的设置,更新前先设置为0) sqlExec.TidbHugeUpdate(string.Format(@"
	 * update monthly_hyzl a inner join monthly_hyzl as lm on xxx set xxx,
	 * a.has_updated=1 where xxx and a.has_updated=0 "), string.Format(@" update
	 * monthly_hyzl set has_updated=0 where xxx and has_updated =1 ") );
	 */
	Boolean HugeUpdate(String updateSql, String... resetHasUpdatedFieldSqls);
	/**
	 * 注意调用此方法之前要调用OpenConn或者确定数据库已经连接
	 * @param sqlstr
	 * @return
	 */
	Boolean ExecuteSql(SGSqlCommandString sqlstr);
	int ExecuteSqlInt(SGSqlCommandString sql, PFSqlParameter[] p, Boolean autoClose);

	 SGDataTable GetDataTable(String sql, PFSqlParameter[] p);
	SGDataTable GetDataTable(String sql, PFSqlParameter[] p, boolean autoClose);
	SGDataTable GetOneRow (String tableName, Consumer<SGSqlWhereCollection> whereAction);
	 Object QuerySingleValue(String sqlval);
	 /**
	  * 查询某行的某个字段
	  */
	 Object QuerySingleValue(String tableName,String fleld, Consumer<SGSqlWhereCollection> whereAction);
	 
	<T> List<T> QueryList(Class<T> tClass,String sql);
	<T> Boolean HugeBulkList(SGSqlInsertCollection dstInsert, List<T> list,
			//Class<T> tClass, 
			String tableName,
			// Consumer<BatchInsertOption> insertOptionAction,
			//Consumer<BaseSqlUpdateCollection> rowAction, 
			SGAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
			Consumer<Integer> sqlRowsCopiedAction,
			Predicate<Boolean> stopAction);
	
	/**
	 * 此方法是拼接insert语句插入(有些数据库这种方式更快)
	 * @param dstInsert
	 * @param rdr
	 * @param tableName
	 * @param rowAction
	 * @param sqlRowsCopiedAction
	 * @param stopAction
	 * @return
	 */
	Boolean HugeInsertReader(SGSqlInsertCollection dstInsert,
		    ResultSet rdr, String tableName,
		    //Consumer<BatchInsertOption> insertOptionAction,// = null,
		    Consumer<BaseSqlUpdateCollection> rowAction,// = null,
		    Consumer<Integer> sqlRowsCopiedAction,// = null,
		    Predicate<Boolean> stopAction// = null//,
		    //String[] primaryKeys=null//如果不为空,插入时根据主键删除原有数据
		    );
	
     Boolean HugeUpdateReader(PFSqlUpdateCollection update, ResultSet rdr, String tableName,
    			Consumer<BatchInsertOption> insertOptionAction,
    	    //Func<MySqlUpdateCollection, DbDataReader,bool> rowAction,
    	    SGFunc<BaseSqlUpdateCollection, ResultSet,?, Boolean> rowAction,
    	    Consumer<Integer> sqlRowsUpdatedAction);
	/**
	 * 此方法是用bulk方式插入
	 * @param dstInsert
	 * @param rdr
	 * @param tableName
	 * @param rowAction
	 * @param sqlRowsCopiedAction
	 * @param stopAction
	 * @return
	 */
     Boolean HugeBulkReader(SGSqlInsertCollection dstInsert, ResultSet rdr, String tableName,
    			//Consumer<BatchInsertOption> insertOptionAction,
    			//Consumer<BaseSqlUpdateCollection> rowAction,
    			SGAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
    			Consumer<Integer> sqlRowsCopiedAction, Predicate<Boolean> stopAction);
     
     Boolean TransferTable(
    		    SGSqlTransferItem transferItem, Consumer<Integer> alreadyAction, Predicate<Boolean> stopAction//,
    		    //Consumer<BatchInsertOption> insertOptionAction
    		    );
     Boolean CreateTable(SGSqlTransferItem transfer,
    		 SGRef<String> ms 
        );
     <T> Boolean CreateTable(SGSqlTransferItem transfer, Class<T> tClass, SGRef<String> ms 
    			) ;
     
     /**
      * 加列.
      * 
      * 用法
      * 
		PFModelConfig model=new PFModelConfig();
		model.FieldName="pf_row_cnt";
		model.FieldType=long.class;
		dstExec.addColumn("test_tb_10",model);
      * @param tableName
      * @param model
      */
     void addColumn(String tableName, //String colName, 
    		 PFModelConfig model);
//     /**
//      * 改用 GetCreateTableSql(String sql,T models,PFModelConfigCollection modelConfig) 重载
//      * 此方法的缺点:
//      * 1.不便于指定sql的执行超时时间
//      * @param srcJdbc
//      * @param sql
//      * @param models
//      * @param modelConfig
//      * @return
//      */
//     @Deprecated
//     <T extends PFSqlCreateTableCollection> T GetCreateTableSql(IPFJdbc srcJdbc,String sql,T models,PFModelConfigCollection modelConfig);
     <T extends SGSqlCreateTableCollection> T GetCreateTableSql(String sql,T models,PFModelConfigCollection modelConfig);
/**
 * 改用PreValidTransferTable吧
 * 预验证reader(主要验证字符串长度)(注意此方法是面向sqlserver的,待移到派生类)
 * @param insert
 * @param rdr
 * @param tableName
 * @param rowAction
 * @param sqlRowsCopiedAction
 * @param stopAction
 * @return
 */
     @Deprecated
 Boolean PreValidTransferReader(//SqlInsertCollection insert, 
		ResultSet rdr, String tableName,
		Function<BaseSqlUpdateCollection,List<PFDataTableFieldValidModel>> PreValidAction,
		SGRef<List<PFDataTableFieldValidModel>> validRef,
		 Consumer<Integer> alreadyAction, Predicate<Boolean> stopAction
//		//Consumer<BatchInsertOption> insertOptionAction,
//		Consumer<BaseSqlUpdateCollection> rowAction,
//		Consumer<Integer> sqlRowsCopiedAction, Predicate<Boolean> stopAction
		);
 Boolean PreValidTransferTable(
 SGSqlTransferItem transferItem,
			SGRef<List<PFDataTableFieldValidModel>> validRef,
			 Consumer<Integer> alreadyAction, Predicate<Boolean> stopAction
//			//Consumer<BatchInsertOption> insertOptionAction,
//			Consumer<BaseSqlUpdateCollection> rowAction,
//			Consumer<Integer> sqlRowsCopiedAction, Predicate<Boolean> stopAction
			);
 /**
  * 比较表数据差异.注意,尽量使src和dst的sql按joinColumn来反向排序,性能应该会高些(后来改为一样的排序
  * @param dstJdbc
  * @param srcSql
  * @param dstSql
  * @param joinColumn
  * @param compareColumn
  * @param accessed  参数:cnt,dstTotalDouble
  * @param resultUpdated
  * @return
  */
 SGDataTable FindTableRowDifference(
			//IPFJdbc srcJdbc,
			ISGJdbc dstJdbc,
			String srcSql,String dstSql,
			String[] joinColumn,
			String[] compareColumn,
			SGAction<Double,Double,Double> accessed ,
			Consumer<SGDataTable> resultUpdated , Predicate<Boolean> stopAction
			) ;

	/**
	 * 为解决旧版本doFindTableRowDifference 方法要把1个表载入内存的问题(测试通过)
	 * 还有更好的方法是根据joinColumn排序后用srcRs和dstRs来逐行比较,key小的先next
	 *
	 * 此方法有2个不足:
	 * 1.要提供目标表名,不能用sql(其实后来想想是可以用sql,如 "select top 1 * from xx {where} and col2=3" 这样)
	 * 2.不能得到目标表中多出的数据
	 * 3.速度太慢,speed:8.49E+001条/秒,100万数据大约要3.5小时
	 * @param dstJdbc
	 * @param srcSql
	 * @param dstSql
	 * @param joinColumn
	 * @param compareColumn
	 * @param accessed
	 * @param resultUpdated
	 * @param stopAction
	 * @param lRowRef
	 * @param rRowRef
	 * @return
	 */
	SGDataTable doFindTableRowDifference3(
			// IPFJdbc srcJdbc,
			ISGJdbc dstJdbc, String srcSql, String dstSql,
			//String dstTableName,
			String[] joinColumn, String[] compareColumn,
			SGAction<Double, Double,Double> accessed, Consumer<SGDataTable> resultUpdated, Predicate<Boolean> stopAction,
			SGRef<List<PFDataRow>> lRowRef, SGRef<List<PFDataRow>> rRowRef
			//, SGRef<List<PFDataRow>> lrRowRef
	);

	/**
	 * 根据排序结果来比较
	 * (但这样有个问题,如果数据库的排序和程序的排序不一致,会有问题...这个问题很致命,会导致判断不了哪个resultSet先读下一条,不过有个解决办法,只要不等,就两边1起读,找到相同之前都放到内存中,不过这样更复杂了)
	 * 简单测试通过
	 *
	 * speed:5.49E+002条/秒 100万数据大约半小时
	 *
	 * @param dstJdbc
	 * @param srcSql
	 * @param dstSql
	 * @param joinColumn
	 * @param compareColumn
	 * @param accessed
	 * @param resultUpdated
	 * @param stopAction
	 * @param lRowRef
	 * @param rRowRef
	 * @return
	 */
	SGDataTable doFindTableRowDifference4(
			// IPFJdbc srcJdbc,
			ISGJdbc dstJdbc, String srcSql, String dstSql,
			//String dstTableName,
			String[] joinColumn, String[] compareColumn,
			SGAction<Double, Double,Double> accessed, Consumer<SGDataTable> resultUpdated, Predicate<Boolean> stopAction,
			SGRef<List<PFDataRow>> lRowRef, SGRef<List<PFDataRow>> rRowRef
			//, SGRef<List<PFDataRow>> lrRowRef
	);

 /**
  * 按表范围比对l和r表,然后按变更做 增删改操作,适用场境如下:
  * 1.按业绩月份增量更新订单表
  * @param dstJdbc r库
  * @param srcSql l表sql
  * @param dstSql r表sql
  * @param joinColumn 关联键(一般用唯一主键order_no或复合唯一主键均可)
  * @param compareColumn 比较列
  * @param compareAccessed 进度事件
  * @param accessed 进度事件
  * @param stopAction 停止事件
  * @param tableName 表名
  * @return
  */
 public Boolean HugeUpdateByComparedTable(

			ISGJdbc dstJdbc,
			String srcSql,String dstSql,
			String[] joinColumn,
			String[] compareColumn,
			SGAction<Double, Double, Double> compareAccessed ,
			BiConsumer<Double,Double> accessed ,
			//Consumer<Integer> sqlRowsCopiedAction,
			//Consumer<PFDataTable> resultUpdated ,
			Predicate<Boolean> stopAction,
//			SGRef<List<PFDataRow>> lRowRef,
//			SGRef<List<PFDataRow>> rRowRef,
//			SGRef<List<PFDataRow>> lrRowRef,
			
			//SqlInsertCollection dstInsert, 
//			//PFDataTable list,
//			//Class<T> tClass, 
			String tableName//,
//			// Consumer<BatchInsertOption> insertOptionAction,
//			Consumer<BaseSqlUpdateCollection> rowAction, Consumer<Integer> sqlRowsCopiedAction//,
////			Predicate<Boolean> stopAction
			);
 /**
  * 插入列.
  * 最后更新 20220903
  * @param <T>
  * @param srcFieldNames 可以这么计算 srcFieldNames =new ArrayList<String>(PFDataHelper.GetProperties(cls).keySet())
  * @param tableName
  * @param hasNextAction
  * @param getItemAction
  * @param rowAction
  * @param sqlRowsCopiedAction
  * @param stopAction
  * @return
  */
 public  <T> boolean doInsertList(
		 //Class<T> cls,//没必要,除了普通类模型,通常要第一行数据才能计算srcFieldNames
//			List<String> srcFieldNames//SqlInsertCollection dstInsert
			List<String> dstFieldNames//SqlInsertCollection dstInsert
			,String tableName,
			//int cnt,
			//Function<Integer,Boolean> hasNextAction ,
			PFFunc3<Integer,Object,Object,Boolean> hasNextAction ,
			Function<Integer,T> getItemAction ,
			//PFFunc3<Integer,Object,Object,T> getItemAction ,
			//Consumer<BaseSqlUpdateCollection> rowAction, 
			SGAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
			Consumer<Integer> sqlRowsCopiedAction,
			Predicate<Boolean> stopAction
			);
// public  <T> boolean doUpdateList(
//			List<String> srcFieldNames//SqlInsertCollection dstInsert
//			,String tableName,
//			String[] primaryKey,int cnt,
//			Function<Integer,T> getItemAction ,
//			//Consumer<BaseSqlUpdateCollection> rowAction,
//			PFAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
//			Consumer<Integer> sqlRowsCopiedAction,
//			Predicate<Boolean> stopAction
//			);
	public  <T> boolean doUpdateList(
			//List<String> srcFieldNames
			List<String> dstFieldNames//SqlInsertCollection dstInsert
			,String tableName,
			String[] primaryKey,
			//int cnt,
			PFFunc3<Integer,Object,Object,Boolean> hasNextAction ,
			Function<Integer,T> getItemAction ,
			//Consumer<BaseSqlUpdateCollection> rowAction,
			SGAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
			Consumer<Integer> sqlRowsCopiedAction,
			Predicate<Boolean> stopAction
	);
// public  <T> boolean doDeleteList(
//			List<String> srcFieldNames//SqlInsertCollection dstInsert
//			,String tableName,
//			String[] primaryKey,int cnt,
//			Function<Integer,T> getItemAction ,
//			//Consumer<BaseSqlUpdateCollection> rowAction,
//			PFAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
//			Consumer<Integer> sqlRowsCopiedAction,
//			Predicate<Boolean> stopAction
//			);
	public  <T> boolean doDeleteList(
			//List<String> srcFieldNames
			//List<String> dstFieldNames//SqlInsertCollection dstInsert
			//,
			String tableName,
			String[] primaryKey,
			//int cnt,
			PFFunc3<Integer,Object,Object,Boolean> hasNextAction ,
			Function<Integer,T> getItemAction ,
			//Consumer<BaseSqlUpdateCollection> rowAction,
			SGAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
			Consumer<Integer> sqlRowsCopiedAction,
			Predicate<Boolean> stopAction
	);
}
