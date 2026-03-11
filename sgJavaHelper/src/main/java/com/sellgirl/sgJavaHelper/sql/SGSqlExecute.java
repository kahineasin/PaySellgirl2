package com.sellgirl.sgJavaHelper.sql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import com.alibaba.fastjson.JSON;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.PFDataTableFieldValidModel;
import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.PFSqlType;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 此版本作为sqlserver
 *
 * 用法:
 *         try (ISqlExecute myResource = PFSqlExecute.Init(table.getDstJdbc())) {
 *             //...
 *         } catch (Exception e) {
 *             throw e;
 *         }
 * 
 * @author Administrator
 *
 */
public class SGSqlExecute extends SGSqlExecuteBase implements ISqlExecute {

	public SGSqlExecute(String url, String user, String password) {
		// 连接数据库
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			SGDataHelper.WriteError(e);
		}
	}

	/**
	 * 使用PFSqlExecute.Init()来构造吧
	 * 
	 * @param jdbc
	 * @throws Exception
	 */
	protected SGSqlExecute(ISGJdbc jdbc) throws Exception {
//		// 连接数据库
		conn = getConnection(jdbc);
		_jdbc = jdbc;

		//super(jdbc);

		isMicroJdbc="com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(jdbc.getDriverClassName());

		//尝试获得数据数时区
		try{
			SGDataTable dt=this.GetDataTable("SELECT SYSDATETIMEOFFSET()",null,false);
			if(null!=dt&&!dt.IsEmpty()){
				String tz=dt.Rows.get(0).getCol().get(0).getValue().toString();
				//LOGGER.info(tz);
				zoneId= ZoneId.of(tz.substring(tz.length()-6));
				//LOGGER.info(zoneId.toString());
			}
		}
		catch(Exception e){
			//String aa="aa";
			e.printStackTrace();
		}
	}


	public static ISqlExecute Init(ISGJdbc jdbc) throws Exception {
		if (jdbc.GetSqlType() == PFSqlType.ClickHouse) {
			return SGDataHelper.GetConfigMapper().GetClickHouseSqlExecute(jdbc);
			// return new PFClickHouseSqlExecute(jdbc);//如果项目没有使用ClickHouse,可以注释这句
			// return GetClickHouseExecute(jdbc);
		} else if (PFSqlType.MySql == jdbc.GetSqlType()) {
			return new SGMySqlExecute(jdbc);
		} else if (PFSqlType.Tidb == jdbc.GetSqlType()) {
			return new PFTidbSqlExecute(jdbc);
		} else {
			return new SGSqlExecute(jdbc);
		}
		// return Init(jdbc,true);
	}


	public Connection getConnection(String url, String user, String password) throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	@Override
    public void addColumn(String tableName,
    		PFModelConfig model) {

        SGSqlCreateTableCollection create=SGSqlCreateTableCollection.Init(_jdbc);
        ExecuteSql(
                new SGSqlCommandString(
                        SGDataHelper.FormatString(
                                "if not exists(select 1 from syscolumns where id=object_id('{0}') and name='{1}')\n"
                                        + "begin \n"
                                        + "  alter table {0} add {1} {2} \n"
                                        + "end ",
                                tableName, model.FieldName, create.GetFieldTypeString(model ))));
    }

	
	/**
	 * @功能描述 执行一条select语句返回一张数据表，支持多表查询
	 * @可能的错误
	 * @作者 叶小钗
	 * @修改说明
	 * @修改人
	 * @使用方法: String sql = "select * from kpxz where fbh=? order by kpxzsx asc";
	 *        SqlParameter[] p = new SqlParameter[1]; p[0] = new SqlParameter("int",
	 *        pId); return db.getDataTable(sql, p);
	 */
	@Override
	public ResultSet GetDataReader(String sql) {
		// 建立Statement对象
		try {
			OpenConn();
			stmt = conn.createStatement();
			stmt.setQueryTimeout(CommandTimeOut);
			/**
			 * Statement createStatement() 创建一个 Statement 对象来将 SQL 语句发送到数据库。
			 */
			// 执行数据库查询语句
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SetError(e);
//			e.printStackTrace();
			rs = null;
		}
		return rs;

	}

	/**
	 * 2千万行以上如果使用GetDataReader方法会报错java.lang.OutOfMemoryError: GC overhead limit
	 * exceeded https://www.gbase8.cn/1884
	 * 
	 * @param sql
	 * @return
	 */
	@Override
	public ResultSet GetHugeDataReader(String sql) {
		// 建立Statement对象
		try {
			OpenConn();
			// stmt = conn.createStatement();
			// 采用流模式处理JDBC大结果集 https://www.gbase8.cn/1884
			stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			// stmt.setFetchSize(Integer.MIN_VALUE);//mysql支持这句，但sqlserver要求值大于0

			stmt.setQueryTimeout(CommandTimeOut);
			/**
			 * Statement createStatement() 创建一个 Statement 对象来将 SQL 语句发送到数据库。
			 */
			// 执行数据库查询语句
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SetError(e);
//			e.printStackTrace();
			rs = null;
		}
		return rs;

	}

	private static boolean isMicroJdbc=false;
	
	/**
	 * @Deprecated 这打换驱动的类型有风险,尽量用OpenMicroJdbcConn2(bulk时中文字段报了varchar时才有的那个错误-- benjamin20220930)
	 */
	@Deprecated
	public void OpenMicroJdbcConn()// 需要修改ResultSet时要这样打开
	{
		if (this.conn != null) {
			CloseConn();
		}
		try {
			if (this.conn == null || this.conn.isClosed()) {
//				String url = PFDataHelper.FormatString(
//						"jdbc:sqlserver://{0}:{1};DatabaseName={2}" + ";user={3};password={4}", _jdbc.getIp(),
//						_jdbc.getPort(), _jdbc.getDbName(), _jdbc.getUsername(), _jdbc.getPassword());
				String url = SGDataHelper.FormatString("jdbc:sqlserver://{0};DatabaseName={1};user={2};password={3}",
						_jdbc.getIp(),
						// _jdbc.getPort(),
						_jdbc.getDbName(), _jdbc.getUsername(), _jdbc.getPassword());
				conn = DriverManager.getConnection(url);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 应该在初始化SqlExecute时决定用哪种驱动,最好不要两种驱动混合使用
	 * @return
	 * @throws SQLException
	 */
	public Connection OpenMicroJdbcConn2() throws SQLException// 需要修改ResultSet时要这样打开
	{
		String url = SGDataHelper.FormatString("jdbc:sqlserver://{0};DatabaseName={1};user={2};password={3}",
				_jdbc.getIp(),
				// _jdbc.getPort(),
				_jdbc.getDbName(), _jdbc.getUsername(), _jdbc.getPassword());
		return DriverManager.getConnection(url);
	}

//	public boolean OpenConn() {
//		isMicroJdbc=false;
//		return super.OpenConn();
//	}
//	public boolean OpenMicroJdbcConn()// 需要修改ResultSet时要这样打开(这个方法测试到有问题,中文字段报了varchar时才有的那个错误(不关闭conn非常有风险,因为conn有可能是另一种驱动-- benjamin 20220930)
//	{
//		boolean b = false;
//		try {
//			boolean isClosed=true;
//			try {
//				isClosed=this.conn.isClosed();
//			}catch(Exception e2) {
//				
//			}
//			if ((!isMicroJdbc)&&this.conn != null&&(!isClosed)) {
//				CloseConn();
//			}
//			if (this.conn == null || this.conn.isClosed()) {
////				String url = PFDataHelper.FormatString(
////						"jdbc:sqlserver://{0}:{1};DatabaseName={2}" + ";user={3};password={4}", _jdbc.getIp(),
////						_jdbc.getPort(), _jdbc.getDbName(), _jdbc.getUsername(), _jdbc.getPassword());
//				String url = PFDataHelper.FormatString("jdbc:sqlserver://{0};DatabaseName={1};user={2};password={3}",
//						_jdbc.getIp(),
//						// _jdbc.getPort(),
//						_jdbc.getDbName(), _jdbc.getUsername(), _jdbc.getPassword());
//				conn = DriverManager.getConnection(url);
//			}
//			b = conn != null && (!conn.isClosed());
//			if(b&&(!isMicroJdbc)) {isMicroJdbc=true;}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return b;
//	}

	public PFSqlInsertCollection getInsertCollectionByReader(ResultSet rdr, String tableName) {

		// insert = new PFClickHouseSqlInsertCollection();
//        insert = new SqlInsertCollection();
		PFSqlInsertCollection insert = getInsertCollection();
		try {
			ResultSetMetaData md = rdr.getMetaData();

			List<SGSqlFieldInfo> targetFields = GetTableFields(tableName);
			Map<String, SGSqlFieldInfo> targetFieldDict = SGDataHelper
					.<SGSqlFieldInfo, String, SGSqlFieldInfo>ListToMapT(targetFields, a -> a.getFieldName(), a -> a);

			for (int i = 0; i < md.getColumnCount(); i++) {
//                String fieldName = md.getColumnName(i+1);
				String fieldName = md.getColumnLabel(i + 1);

				SqlUpdateItem updateItem = new SqlUpdateItem();
				updateItem.Key = fieldName;
//                updateItem.VType=PFDataHelper.GetTypeByString(md.getColumnTypeName(i+1));
				// 用目标表的type比dataReader的更准确,因为dr中可能有填null的情况
				updateItem.VType = SGDataHelper.GetTypeByString(
						targetFieldDict.containsKey(fieldName) ? targetFieldDict.get(fieldName).getFieldType()
								: md.getColumnTypeName(i + 1));
				insert.Add(updateItem);

			}
		} catch (Exception e) {
			SetError(e);
			return null;
		}
		return insert;
	}

	@Override
	public ResultSet GetDataReaderUpdatable(String sql) {
		// 建立Statement对象
		try {
			// OpenConn();
			Connection conn2=OpenMicroJdbcConn2();

//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_UPDATABLE);
			PreparedStatement pstmt = conn2.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			pstmt.setQueryTimeout(CommandTimeOut);
			/**
			 * Statement createStatement() 创建一个 Statement 对象来将 SQL 语句发送到数据库。
			 */
			// 执行数据库查询语句
			// rs = stmt.executeQuery(sql);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SetError(e);
//			e.printStackTrace();
			rs = null;
		}
		return rs;

	}

//
//	/// <summary>
//	/// 查询返回单个结果
//	/// </summary>
//	/// <param name="sqlval">sql字符串</param>
//	/// <returns>返回结果</returns>
//	public Object QuerySingleValue(String sqlval) {
//		PFDataTable t = GetDataTable(sqlval, null);
//		if (t != null && (!t.IsEmpty())) {
//			return t.getRow().get(0).getCol().get(0).getValue();
//		}
//		return null;
//
//	}
//
//    /*
//     * 执行语句(未测试)
//     */
//    public int ExecuteSqlInt(String sql, PFSqlParameter[] p)//, Boolean autoClose )
//    {
//    	Boolean autoClose=true;
//
//    	PreparedStatement ps = null;
//        PFDataTable t = null;
//    	int rs=-1;
//        try {
//        	if(p!=null) {
//        		ps = conn.prepareStatement(sql);
//                AddSqlParameter(ps, p);
//                rs = ps.executeUpdate();
//        	}else {
//                stmt = conn.createStatement();
//                rs = stmt.executeUpdate(sql);
//        	}
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//        	try {
//        		if(p!=null) {
//            		ps.close();
//        		}
//                if (stmt != null) {
//                    stmt.close();
//                    stmt = null;
//                }
//				conn.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//        return rs;
//    }

///**
//     * @功能描述 增加参数方法
//     * @可能的错误 需要测试全局数据共享问题，以及可能会扩展参数类型
//     * @作者 叶小钗
//     * @修改说明
//     * @修改人
//     */
//    private  void AddSqlParameter(PreparedStatement ps, PFSqlParameter[] p)
//            throws SQLException {
//        for (int j = 0; j < p.length; j++) {
//            // wl(p[j].getValue() + "--" + p[j].getType() + "--" + j);
//            if (p[j].getType().equals("int")) {
//                ps.setInt(j + 1, p[j].getIntValue());
//            }
//            if (p[j].type.equals("String")) {
//                ps.setString(j + 1, p[j].getValue());
//            }
//            if (p[j].type.equals("boolean")) {
//                ps.setBoolean(j + 1, p[j].getBoolValue());
//            }
//            if (p[j].type.equals("Date")) {
//                ps.setDate(j + 1, p[j].getDateValue());
//            }
//            if (p[j].type.equals("Blob")) {
//                ps.setBlob(j + 1, p[j].getBlobValue());
//            }
//        }
//    }

	@Override
	public Boolean HugeDelete(String updateSql) {
		String sql = SGDataHelper.FormatString(
				"SET ROWCOUNT 10000;\r\n" + "declare @rc int\r\n" + "\r\n" + "WHILE 1 = 1\r\n" + "BEGIN\r\n"
						+ "    begin tran t1\r\n" + "    {0} \r\n"
						+ "    set @rc=@@ROWCOUNT  --commic后,@@rowcount为0\r\n" + "    commit tran t1\r\n"
						+ "    IF @rc = 0\r\n" + "        BREAK;\r\n" + "END\r\n" + "\r\n" + "SET ROWCOUNT 0;",
				updateSql);

		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
		// return ExecuteTransactSql(sql);
		return ExecuteSql(new SGSqlCommandString(sql));
	}

	@Override
	public Boolean Delete(String tableName, Consumer<SGSqlWhereCollection> whereAction) {
		SGSqlWhereCollection where = getWhereCollection();
		if (whereAction != null) {
			whereAction.accept(where);
		}
		return ExecuteSql(new SGSqlCommandString(
				SGDataHelper.FormatString("delete from {0} {1}", tableName, where == null ? "" : where.ToSql())));
	}

	@Override
	public Boolean TruncateTable(String tableName) {
		return ExecuteSql(new SGSqlCommandString(SGDataHelper.FormatString("truncate table {0}", tableName)));
	}

	@Override
	public Boolean HugeDelete(String tableName, Consumer<SGSqlWhereCollection> whereAction) {
		SGSqlWhereCollection where = getWhereCollection();
		if (whereAction != null) {
			whereAction.accept(where);
		}
		return HugeDelete(
				SGDataHelper.FormatString("delete from {0} {1}", tableName, where == null ? "" : where.ToSql()));
	}
//
//	@Override
//	public Boolean CloseReader(ResultSet reader) {
//		try {
//			// if(reader.isClosed()) {return true;}
//			stmt.cancel();
//			stmt = null;
//			// sqlCmd.Cancel();//如果没有这句,数据很多时 dr.Close 会很慢
//			// https://www.cnblogs.com/xyz0835/p/3379676.html
//
//			// reader.close();
//			// 当使用GetHugeDataReader时，如果数据很大，中途close会非常慢，原因未明--benjamintodo20210223
//			new Thread() {// 线程操作
//				public void run() {
//					try {
//						reader.close();
//					} catch (SQLException e) {
//						// e.printStackTrace();
//					}
//				}
//			}.start();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//		}
//		return true;
//	}

//@Override
//public void CloseConn() {
////    SqlConnCounter.Subtract(_sqlconnection.ConnectionString);
//    try {
//    if (stmt != null) {
//			stmt.close();
//        stmt = null;
//    }
//	conn.close();
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	// TODO Auto-generated method stub
//	
//}
//@Override
//public Boolean CreateTable(SqlCreateTableCollection models) {
//	// TODO Auto-generated method stub
//	return null;
//}
//	@Override
//	public Boolean ExecuteSql(PFSqlCommandString sqlstr) {
//		// return ExecuteSqlInt(sqlstr,null)>0;
//		return ExecuteSqlInt(sqlstr, null) > -1;
//	}

/// <summary>
/// 根据PFSqlTransferItem类型对象来迁移数据
/// </summary>
/// <param name="transferItem"></param>
/// <param name="alreadyAction"></param>
/// <param name="stopAction"></param>

//	@Override
//	public Boolean HugeUpdate(String updateSql, String... resetHasUpdatedFieldSqls) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Boolean HugeUpdateReader(PFSqlUpdateCollection update, ResultSet rdr, String tableName,
			Consumer<BatchInsertOption> insertOptionAction,
			SGFunc<BaseSqlUpdateCollection, ResultSet, ?, Boolean> rowAction, Consumer<Integer> sqlRowsUpdatedAction) {
		// TODO Auto-generated method stub
		return null;
	}
//移到基类
//	/**
//	 * 测试通过: 从sqlserver迁移到tidb
//	 */
//	@Override
//	public Boolean HugeInsertReader(SqlInsertCollection dstInsert, ResultSet rdr, String tableName,
//			// Consumer<BatchInsertOption> insertOptionAction,
//			Consumer<BaseSqlUpdateCollection> rowAction, Consumer<Integer> sqlRowsCopiedAction,
//			Predicate<Boolean> stopAction) {
//		// TODO Auto-generated method stub
//
//		// TODO Auto-generated method stub
//		if (rdr == null) {
//			SetError(new Exception("DataReader不能为空"));
//			return false;
//		}
//
////	    BatchInsertOption insertOption = new BatchInsertOption();
////	    if (insertOptionAction != null) { insertOptionAction.accept(insertOption); }
//		BatchInsertOption insertOption = GetInsertOption();
//
//		// OpenConn();
//		if (!OpenConn()) {
//			return false;
//		}
//
//		// StringBuilder sb = new StringBuilder();
//
//		int idx = 0;
//		insertedCnt = 0;// 已插入的行数
//
//		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
//		int batchCnt = 0;
//
//		Boolean hasUnDo = false;
//
//		//// SetHugeCommandTimeOut();
//		// SetNormalTransferCommandTimeOutIfNotHuge();
//		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//
//		StringBuilder sb = new StringBuilder();
//		PFSqlCommandString scs = new PFSqlCommandString();
//		int oneThousandCount = 0;
//		try {
////	    	//保存来源的类型，便于转换
//			ResultSetMetaData srcMd = rdr.getMetaData();
//
//			boolean hasSysLimitId = false;
//			String sysLimitIdFieldName = "sys_limit_id";
//			long currentSysLimitId = -1;
//			lastInsertedId = -1;
//
////	    	//Map<String,Integer> columnIdx=new HashMap<String,Integer>();
////	    	Map<String,Integer> srcColumnType=new HashMap<String,Integer>();   	
//			for (int i = 0; i < srcMd.getColumnCount(); i++) {
//				String fieldName = srcMd.getColumnLabel(i + 1);
////	    		  srcColumnType.put(srcMd.getColumnLabel(i+1),srcMd.getColumnType(i+1));
//				if (sysLimitIdFieldName.equals(fieldName)) {// 这个内部值用来代替limit的offset
//					hasSysLimitId = true;
//					continue;
//				}
//			}
//
//			// CachedRowSetImpl crs = GetCrs(tableName);
//			// ResultSet crs = GetRs(tableName);
////				CachedRowSet crs = GetCrs(tableName);
////				ResultSetMetaData dstMd = crs.getMetaData();
//			ResultSetMetaData dstMd = GetMetaData(tableName);
//
//			// 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//			if (dstInsert == null) {
////	        	dstInsert=getInsertCollection();        	 	
////	            for (int i = 0; i < dstMd.getColumnCount(); i++)
////	            {
////	                String fieldName = dstMd.getColumnLabel(i+1);        
////	                SqlUpdateItem updateItem = new SqlUpdateItem();
////	                updateItem.Key=fieldName;
////	                updateItem.VPFType=PFDataHelper.GetPFTypeBySqlType(dstMd.getColumnType(i+1));
////	                dstInsert.Add(updateItem);
////	            }
//				dstInsert = getInsertCollection(dstMd);
//			}
//
//			while (true) {
//				boolean hasNext = rdr.next();
//				if (hasNext) {
//
//					dstInsert.UpdateByDataReaderAutoConvert(rdr);
//					if (rowAction != null) {
//						rowAction.accept(dstInsert);
//					}
//
//					// 更新行
//					// crs = insertIntoCachedRowSet(crs, dstInsert);// ,dstColumnType);
//
//					if (oneThousandCount > 999)// sqlserver里values最多1000行,但tidb没有这个限制(但这句留着备用)(注释这句的话,可以从19秒减少到14秒完成)
//					{
//						oneThousandCount = 0;
////						sb.append(PFDataHelper.FormatString("; insert into {0}({1}) values({2})", tableName,
////								insert.ToKeysSql(), insert.ToValuesSql()));
//						scs.add(sb.toString());
//						sb = new StringBuilder();
//						sb.append(PFDataHelper.FormatString("insert into {0}({1}) values({2})", tableName,
//								dstInsert.ToKeysSql(), dstInsert.ToValuesSql()));
//					} else if (oneThousandCount == 0) {
//						sb.append(PFDataHelper.FormatString(" insert into {0}({1}) values({2})", tableName,
//								dstInsert.ToKeysSql(), dstInsert.ToValuesSql()));
//					} else {
//						sb.append(PFDataHelper.FormatString(",({0})", dstInsert.ToValuesSql()));
//					}
//
//					batchCnt++;
//					oneThousandCount++;
//					hasUnDo = true;
//					if (hasSysLimitId) {
//						currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
//					}
//
//				}
//				if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//
//					if (sb.length() > 0) {
//						scs.add(sb.toString());
//						sb = new StringBuilder();
//					}
//					// Boolean b = ExecuteSql(sb.toString());
//					Boolean b = ExecuteSql(scs);
//
//					if (!b) {
//						// CloseReader(rdr);
//						CloseConn();
//						return false;
//					} else {
//						insertedCnt = idx + 1;
//						if (hasSysLimitId) {
//							lastInsertedId = currentSysLimitId;
//						}
//					}
//					if (sqlRowsCopiedAction != null) {
//						sqlRowsCopiedAction.accept(idx);
//					}
//					if (stopAction != null) {
//						if (stopAction.test(true)) {// 允许中途终止--benjamin20200812
//													// CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//							CloseConn();
//							return true;
//						}
//					}
////            sb.Clear();
//					sb = new StringBuilder();
//					scs.clear();
//					// crs = GetCrs(tableName);
//					// crs = GetRs(tableName);
//					// deleteSb.Clear();
//
//					hasUnDo = false;
//					batchCnt = 0;
//					oneThousandCount = 0;
//				} else {
//					// batchCnt++;
//					// oneThousandCount++;
//				}
//				if (!hasNext) {
//					break;
//				}
//				idx++;
//
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			SetError(e);
//			return false;
//		}
//
//		return true;
//	}

//	/**
//	 * 这种方法的src和dst要求是同一类数据库，不好用
//	 */
//	@Deprecated
//	public Boolean HugeBulkReaderSame(PFSqlInsertCollection insert, ResultSet rdr, String tableName,
//			// Consumer<BatchInsertOption> insertOptionAction,
//			Consumer<BaseSqlUpdateCollection> rowAction, Consumer<Integer> sqlRowsCopiedAction,
//			Predicate<Boolean> stopAction) {
//		// TODO Auto-generated method stub
//
//		if (insert == null) {
//			insert = getInsertCollectionByReader(rdr, tableName);
//		}
//		// SGRef<SqlInsertCollection> insertRef=new SGRef<SqlInsertCollection>(insert);
////    day:
////        driverClassName: net.sourceforge.jtds.jdbc.Driver
////        password: perfect
////        url: jdbc:jtds:sqlserver://192.168.0.29:1433/balance
////        username: sa
//		try {
//			// 使用GetDataReaderUpdatable 修改后报错：链接服务器 '(null)' 的 OLE DB 访问接口 'STREAM' 返回了对列
//			// '[!BulkInsert].Hyxm' 无效的数据。 暂时没解决办法
////		if(rdr.next()) {
////			rdr.updateString("Hyxm", "aabb");
////			//rdr.updateRow();
////
////			rdr.moveToCurrentRow();
////			rdr.first();
////		}
//			// PFDataHelper.ReTry((a,b,c)->{
//			// OpenConn();
//			OpenMicroJdbcConn();
////			String url =PFDataHelper.FormatString( 
////					"jdbc:sqlserver://{0}:{1};DatabaseName={2}"
////	      +";user={3};password={4}",_jdbc.getAddr(),_jdbc.getPort(),_jdbc.getDbName(),_jdbc.getUsername(),_jdbc.getPassword());
////			String url = "jdbc:sqlserver://localhost:1433;DatabaseName=datebaseName"
////		      +";user=userName;password=userPassword";
//			SQLServerBulkCopyOptions copyOptions = new SQLServerBulkCopyOptions();
//			copyOptions.setKeepIdentity(true);
//			copyOptions.setBatchSize(8000);
//			copyOptions.setKeepNulls(true);
//			// copyOptions.setUseInternalTransaction(true);
//			// copyOptions.setUseInternalTransaction(false);
//			SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(conn);
//			// new SQLServerBulkCopy(url);
//			bulkCopy.setBulkCopyOptions(copyOptions);
//			bulkCopy.setDestinationTableName(tableName);
//
////	if(insertRef.GetValue()!=null) {
////
////		String[] fields=new String[] {"Orderno",
////				"Hybh","Hyxm",
////				"Cmonth","backrate","Totalmoney","Backmoney","Giftmoney",
////				//"Discount","Pv","orderdate","isnew","fgsno","agentno","paymoney","dispatchmoney","coupon","shhybh","orderhy","checkdate","amount_detail","dispatch_type","dispatch_method",
////				//"delivery_address"//,"warehouse_code","order_status","is_group_buy","order_source"
////				};
////		List<String> fieldList=Arrays.asList(fields);
////		   Iterator<Entry<String ,SqlUpdateItem>> iter = insertRef.GetValue().entrySet().iterator();
////		   while(iter.hasNext()){
////			   Entry<String ,SqlUpdateItem> key=iter.next();
////			   if(fieldList.contains(key.getKey())) {
////					bulkCopy.addColumnMapping(key.getKey(), key.getKey());
////			   }
////		   }
////	}
//
////	bulkCopy.addColumnMapping("Orderno", "Orderno");
////	bulkCopy.addColumnMapping("Cmonth","Cmonth" );
////	bulkCopy.addColumnMapping("Hyxm","Hyxm" );
//
//			bulkCopy.writeToServer(rdr);
//
//			rdr.close();
//			bulkCopy.close();
//			// });
//		} catch (Exception e) {
//			SetError(e);
//			return false;
//		}
//
//		return true;
//	}

	/**
	 * 向CachedRowSet对象插入一条数据 循环调用这一个方法，将想插入数据库的数据先插入到CachedRowSet对象里
	 * 
	 * @param crs
	 * @param fmt
	 * @return
	 * @throws SQLException
	 */
//private CachedRowSetImpl insertIntoCachedRowSet(CachedRowSetImpl crs,SqlInsertCollection stu) throws SQLException{
//    //移动指针到“插入行”，插入行是一个虚拟行
//    crs.moveToInsertRow();
//    
//    //更新虚拟行的数据
////    crs.updateString("name", stu.getName());
////    crs.updateInt("id", stu.getId());
//
//	   Iterator<Entry<String, SqlUpdateItem>> iter = stu.entrySet().iterator();
//	   
////		String[] fields=new String[] {
////				"Orderno",
//////		"Hybh",
//////		"Hyxm",
//////		"Cmonth","backrate","Totalmoney","Backmoney","Giftmoney",
//////		"Discount",
//////		"Pv",
//////		
//////		"orderdate",
//////		"isnew","fgsno","agentno",
//////		"paymoney",
//////		"dispatchmoney","coupon","shhybh","orderhy","checkdate","amount_detail","dispatch_type","dispatch_method",
//////		"delivery_address"//,"warehouse_code","order_status","is_group_buy","order_source"
////		};
//	   
////	   String[] fields=new String[] {
////			   "Orderno","Hybh","Hyxm","Cmonth","backrate",
////			   "Totalmoney","Backmoney","Giftmoney",
////			   
////			   "Discount","Pv","orderdate","isnew","fgsno",
////			   "agentno","paymoney","dispatchmoney","coupon","shhybh","orderhy","checkdate","amount_detail","dispatch_type","dispatch_method","delivery_address","warehouse_code","order_status","is_group_buy","order_source"
////	   };
////		List<String> fieldList=Arrays.asList(fields);
//	   
//	   while(iter.hasNext()){
//		   Entry<String, SqlUpdateItem> key=iter.next();
////		   if("Pv".equals(key.getKey())) {
////			   String aa="aa";
////			   Object v=PFDataHelper.ConvertObjectByType(key.getValue().Value,key.getValue().Value.getClass(),key.getValue().VType);
////		   }
//		   //if(fieldList.contains(key.getKey())) {
//			   if(PFDataHelper.GetPFTypeByClass(key.getValue().VType).equals(PFSqlFieldType.DateTime)) {
//				   try {
//					   if(key.getValue().Value!=null) {
//						   crs.updateTimestamp(key.getKey(),new Timestamp(PFDataHelper.ObjectToDateTime(key.getValue().Value).getTimeInMillis()));   
//					   }
//				   }catch(Exception e) {
//					   e.printStackTrace();
//				   }
//			   }else {
//					   crs.updateObject(key.getKey(),key.getValue().Value);
//			   }
//		   //}
//	   }
//
//	   //crs.updateObject(key.getKey(),key.getValue().Value);
////	   crs.updateString("Orderno", "aaaaa"); //ok
//	   
//    //插入虚拟行
//    crs.insertRow();
//    //移动指针到当前行
//    crs.moveToCurrentRow();
//    return crs;
//}
//private ResultSet insertIntoCachedRowSet(ResultSet crs,SqlInsertCollection stu) throws SQLException{
//    //移动指针到“插入行”，插入行是一个虚拟行
//    crs.moveToInsertRow();
//    
//    //更新虚拟行的数据
////    crs.updateString("name", stu.getName());
////    crs.updateInt("id", stu.getId());
//
//	   Iterator<Entry<String, SqlUpdateItem>> iter = stu.entrySet().iterator();	   
//
//	   while(iter.hasNext()){
//		   Entry<String, SqlUpdateItem> key=iter.next();
////		   if("Pv".equals(key.getKey())) {
////			   String aa="aa";
////			   Object v=PFDataHelper.ConvertObjectByType(key.getValue().Value,key.getValue().Value.getClass(),key.getValue().VType);
////		   }
//		   //if(fieldList.contains(key.getKey())) {
//			   if(PFDataHelper.GetPFTypeByClass(key.getValue().VType).equals(PFSqlFieldType.DateTime)) {
//				   try {
//					   if(key.getValue().Value!=null) {
//						   crs.updateTimestamp(key.getKey(),new Timestamp(PFDataHelper.ObjectToDateTime(key.getValue().Value).getTimeInMillis()));   
//					   }
//				   }catch(Exception e) {
//					   e.printStackTrace();
//				   }
//			   }else {
//					   crs.updateObject(key.getKey(),key.getValue().Value);
//			   }
//		   //}
//	   }
//
//	   //crs.updateObject(key.getKey(),key.getValue().Value);
////	   crs.updateString("Orderno", "aaaaa"); //ok
//	   
//    //插入虚拟行
//    crs.insertRow();
//    //移动指针到当前行
//    crs.moveToCurrentRow();
//    return crs;
//}
//	@Deprecated
//	private CachedRowSet insertIntoCachedRowSetOld(CachedRowSet crs, PFSqlInsertCollection dstInsert)
//			throws SQLException {
//		// 移动指针到“插入行”，插入行是一个虚拟行
//		crs.moveToInsertRow();
//
//		Iterator<Entry<String, SqlUpdateItem>> iter = dstInsert.entrySet().iterator();
//
//
//		while (iter.hasNext()) {
//			Entry<String, SqlUpdateItem> key = iter.next();
//			if (key.getValue().getSrcDataPFType().equals(PFSqlFieldTypeEnum.DateTime)) {
//				try {
//					// java.sql.Types.
//					// crs.getMetaData().getC.getColumnType("");
//					if (key.getValue().Value != null) {
//						crs.updateTimestamp(key.getKey(),
//								new Timestamp(PFDataHelper.ObjectToDateTime(key.getValue().Value).getTimeInMillis()));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
////	   		   else if(srcColumnType.get(colName)!=d) {
////				   
////			   } 			   
//			else {
//				crs.updateObject(key.getKey(), key.getValue().Value);
//			}
//
//			// }
//		}
//
//		// 插入虚拟行
//		crs.insertRow();
//		// 移动指针到当前行
//		crs.moveToCurrentRow();
//		return crs;
//	}

//	private CachedRowSet insertIntoCachedRowSet(CachedRowSet crs, PFSqlInsertCollection dstInsert) throws SQLException {
//		// 移动指针到“插入行”，插入行是一个虚拟行
//		crs.moveToInsertRow();
//
//		ResultSetMetaData md = crs.getMetaData();
//
//		for (int i = 0; i < md.getColumnCount(); i++) {
//			int mdIdx = i + 1;
//			String colName = md.getColumnLabel(mdIdx);
//			// System.out.println(String.valueOf(i)+"__"+colName);
//			// if(fieldList.contains(colName)) {
//
//			if (dstInsert.containsKey(colName)) {
//				int dataT = md.getColumnType(mdIdx);
//
//				if (java.sql.Types.DECIMAL == dataT) {// 如果直接updateObject,云徒订单的Totalmoney字段会有100倍溢出的问题--benjamin20210112
//					Object v = dstInsert.get(colName).Value;
//					if (v != null) {
//						BigDecimal vD = PFDataHelper.<BigDecimal>ObjectAs(
//								PFDataHelper.ConvertObjectToSqlTypeByPFType(v, dstInsert.get(colName).VPFType, dataT));
//						vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP);
//						crs.updateBigDecimal(colName, vD);
//					}
//				}else if (java.sql.Types.NUMERIC == dataT) {// 如果直接updateObject,numeric的小数位数会影响结果
//					/**
//					 * numeric[1,4]是Short
//numeric[5,9]是Integer
//numeric[10,18]是Long
//numeric[19]及以上是BigDecimal
//					 */
//					Object v = dstInsert.get(colName).Value;
//					if (v != null) {
//						int precision=md.getPrecision(mdIdx);
//						if(precision>=19) {
//							BigDecimal vD = PFDataHelper.ObjectToDecimal(v);
//							vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP);
//							crs.updateBigDecimal(colName, vD);
//						}else if(precision>=10) {
//							Long vD = PFDataHelper.ObjectToLong(v);
//							crs.updateLong(colName, vD);
//						}else if(precision>=5) {
//							Integer vD = PFDataHelper.ObjectToInt(v);
//							crs.updateInt(colName, vD);
//						}else {
//							Integer vD = PFDataHelper.ObjectToInt(v);
//							crs.updateInt(colName, vD);
//						}
//					}
//				}  else if (java.sql.Types.TIMESTAMP == dataT) {
//
//					String dataTName = md.getColumnTypeName(mdIdx);
//					crs.updateTimestamp(colName, PFDataHelper.ObjectAs(PFDataHelper.ConvertObjectToSqlTypeByPFType(
//							dstInsert.get(colName).Value, dstInsert.get(colName).VPFType, dataT, dataTName)));
//				}
//
//				else {
//					crs.updateObject(colName, PFDataHelper.ConvertObjectToSqlTypeByPFType(dstInsert.get(colName).Value,
//							dstInsert.get(colName).VPFType, dataT));
//				}
//
//			}
//			// }
//		}
//
//		// 插入虚拟行
//		crs.insertRow();
//		// 移动指针到当前行
//		crs.moveToCurrentRow();
//		return crs;
//	}

	public CachedRowSet updateCrs(CachedRowSet crs, 
          ResultSetMetaData md,
          int mdIdx,
//          PFSqlInsertCollection dstInsert,
//          Object v
          SqlUpdateItem dstInsertI
          ) throws SQLException {
//		// 移动指针到“插入行”，插入行是一个虚拟行
//		crs.moveToInsertRow();
//
//		//ResultSetMetaData md = crs.getMetaData();
//
//		for (int i = 0; i < md.getColumnCount(); i++) {
//			int mdIdx = i + 1;
			String colName = md.getColumnLabel(mdIdx);
	        BatchInsertOption insertOption = GetInsertOption();
			// System.out.println(String.valueOf(i)+"__"+colName);
			// if(fieldList.contains(colName)) {

			//if (dstInsert.containsKey(colName)) {
				int dataT = md.getColumnType(mdIdx);
				String dataTName = md.getColumnTypeName(mdIdx);
				//SqlUpdateItem dstInsertI=dstInsert.get(colName);
				Object v = dstInsertI.Value;
				Object convertedValue=null;
				if(null!=v) {
					if(null==dstInsertI.convertTo) {
						if(java.sql.Types.NUMERIC == dataT) {
							int precision=md.getPrecision(mdIdx);
//							if(precision>=19) {
//								dstInsertI.convertTo=insertOption.convertNullTo0?PFSqlFieldTypeConverter.DecimalConverter0(v):PFSqlFieldTypeConverter.DecimalConverter(v);
//							}else if(precision>=10) {
//								dstInsertI.convertTo=insertOption.convertNullTo0?PFSqlFieldTypeConverter.LongConverter0(v):PFSqlFieldTypeConverter.LongConverter(v);
//							}else if(precision>=5) {
//								dstInsertI.convertTo=insertOption.convertNullTo0?PFSqlFieldTypeConverter.IntConverter0(v):PFSqlFieldTypeConverter.IntConverter(v);
//							}else {
//								dstInsertI.convertTo=insertOption.convertNullTo0?PFSqlFieldTypeConverter.IntConverter0(v):PFSqlFieldTypeConverter.IntConverter(v);
//							}
							dstInsertI.convertTo=insertOption.convertNullTo0
									?SGDataHelper.GetObjectToSqlTypeByPFTypeConverter0(v, dstInsertI.getSrcDataPFType(), dataT,dataTName,precision//,zoneId
							)
									:SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(v, dstInsertI.getSrcDataPFType(), dataT,dataTName,precision//,zoneId
							);
						}else {
							//dstInsertI.ConvertTo=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(v, dstInsertI.VPFType, dataT,dataTName);
							dstInsertI.convertTo=insertOption.convertNullTo0
									?SGDataHelper.GetObjectToSqlTypeByPFTypeConverter0(v, dstInsertI.getSrcDataPFType(), dataT,dataTName,null//,zoneId
							)
											:SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(v, dstInsertI.getSrcDataPFType(), dataT,dataTName,null//,zoneId
							);
						}
					}
					convertedValue=dstInsertI.convertTo.convert(v);
				}

				if (java.sql.Types.DECIMAL == dataT) {// 如果直接updateObject,云徒订单的Totalmoney字段会有100倍溢出的问题--benjamin20210112
					//Object v = dstInsert.get(colName).Value;
					if (convertedValue != null) {
						BigDecimal vD = SGDataHelper.<BigDecimal>ObjectAs(convertedValue);
						vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP);
						crs.updateBigDecimal(colName, vD);
					}else {
						crs.updateBigDecimal(colName, insertOption.convertNullTo0?new BigDecimal(0):null);//sqlserver不设置空值的话,也会报错-- benjamin 20221108
					}
				}else if (java.sql.Types.NUMERIC == dataT) {// 如果直接updateObject,numeric的小数位数会影响结果
					/**
					 * numeric[1,4]是Short
numeric[5,9]是Integer
numeric[10,18]是Long
numeric[19]及以上是BigDecimal
					 */
					//Object v = dstInsert.get(colName).Value;
					if (convertedValue != null) {
						int precision=md.getPrecision(mdIdx);
						if(precision>=19) {
//							BigDecimal vD = PFDataHelper.ObjectToDecimal(v);
//							vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP);
							crs.updateBigDecimal(colName, (BigDecimal)convertedValue);
						}else if(precision>=10) {
							//Long vD = PFDataHelper.ObjectToLong(v);
							crs.updateLong(colName, (Long)convertedValue);
						}else if(precision>=5) {
							//Integer vD = PFDataHelper.ObjectToInt(v);
							crs.updateInt(colName, (Integer)convertedValue);
						}else {
							//Integer vD = PFDataHelper.ObjectToInt(v);
							crs.updateInt(colName, (Integer)convertedValue);
						}
					}else {
						crs.updateInt(colName,  insertOption.convertNullTo0?0:null);
					}
				}  else if (java.sql.Types.TIMESTAMP == dataT) {
					//日期没有默认值
					crs.updateTimestamp(colName, SGDataHelper.ObjectAs(convertedValue));
				}
				else if (Types.VARCHAR == dataT||Types.NVARCHAR == dataT||SGSqlFieldTypeEnum.String== dstInsertI.getDstDataPFType()) {
	                if (convertedValue != null) {
	                    crs.updateObject(mdIdx, convertedValue);
	                } else {
	                	if(insertOption.convertNullTo0) {
	                        crs.updateObject(mdIdx, "");
	                	}else {
	                        crs.updateObject(mdIdx, null);
	                	}
	                }
	            }

				else {
					//不知道类型,给不了默认值
					crs.updateObject(colName, convertedValue);
				}

			//}
			// }
//		}
//
//		// 插入虚拟行
//		crs.insertRow();
//		// 移动指针到当前行
//		crs.moveToCurrentRow();
		return crs;
	}
	
	/**
	 * 改为Converter
	 * @param crs
	 * @param dstInsert
	 * @return
	 * @throws SQLException
	 */
	private CachedRowSet insertIntoCachedRowSet(CachedRowSet crs, PFSqlInsertCollection dstInsert) throws SQLException {
		// 移动指针到“插入行”，插入行是一个虚拟行
		crs.moveToInsertRow();

		ResultSetMetaData md = crs.getMetaData();

		for (int i = 0; i < md.getColumnCount(); i++) {
			int mdIdx = i + 1;
			String colName = md.getColumnLabel(mdIdx);
			// System.out.println(String.valueOf(i)+"__"+colName);
			// if(fieldList.contains(colName)) {

//			if (dstInsert.containsKey(colName)) {
//				int dataT = md.getColumnType(mdIdx);
//				String dataTName = md.getColumnTypeName(mdIdx);
//				SqlUpdateItem dstInsertI=dstInsert.get(colName);
//				Object v = dstInsertI.Value;
//				Object convertedValue=null;
//				if(null!=v) {
//					if(null==dstInsertI.ConvertTo) {
//						if(java.sql.Types.NUMERIC == dataT) {
//							int precision=md.getPrecision(mdIdx);
//							if(precision>=19) {
//								dstInsertI.ConvertTo=PFSqlFieldTypeConverter.DecimalConverter(v);
//							}else if(precision>=10) {
//								dstInsertI.ConvertTo=PFSqlFieldTypeConverter.LongConverter(v);
//							}else if(precision>=5) {
//								dstInsertI.ConvertTo=PFSqlFieldTypeConverter.IntConverter(v);
//							}else {
//								dstInsertI.ConvertTo=PFSqlFieldTypeConverter.IntConverter(v);
//							}
//						}else {
//							dstInsertI.ConvertTo=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(v, dstInsert.get(colName).VPFType, dataT,dataTName);
//						}
//					}
//					convertedValue=dstInsertI.ConvertTo.convert(v);
//				}
//
//				if (java.sql.Types.DECIMAL == dataT) {// 如果直接updateObject,云徒订单的Totalmoney字段会有100倍溢出的问题--benjamin20210112
//					//Object v = dstInsert.get(colName).Value;
//					if (convertedValue != null) {
//						BigDecimal vD = PFDataHelper.<BigDecimal>ObjectAs(convertedValue);
//						vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP);
//						crs.updateBigDecimal(colName, vD);
//					}
//				}else if (java.sql.Types.NUMERIC == dataT) {// 如果直接updateObject,numeric的小数位数会影响结果
//					/**
//					 * numeric[1,4]是Short
//numeric[5,9]是Integer
//numeric[10,18]是Long
//numeric[19]及以上是BigDecimal
//					 */
//					//Object v = dstInsert.get(colName).Value;
//					if (convertedValue != null) {
//						int precision=md.getPrecision(mdIdx);
//						if(precision>=19) {
////							BigDecimal vD = PFDataHelper.ObjectToDecimal(v);
////							vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP);
//							crs.updateBigDecimal(colName, (BigDecimal)convertedValue);
//						}else if(precision>=10) {
//							//Long vD = PFDataHelper.ObjectToLong(v);
//							crs.updateLong(colName, (Long)convertedValue);
//						}else if(precision>=5) {
//							//Integer vD = PFDataHelper.ObjectToInt(v);
//							crs.updateInt(colName, (Integer)convertedValue);
//						}else {
//							//Integer vD = PFDataHelper.ObjectToInt(v);
//							crs.updateInt(colName, (Integer)convertedValue);
//						}
//					}
//				}  else if (java.sql.Types.TIMESTAMP == dataT) {
//
//					//String dataTName = md.getColumnTypeName(mdIdx);
//					crs.updateTimestamp(colName, PFDataHelper.ObjectAs(convertedValue));
//				}
//
//				else {
//					crs.updateObject(colName, convertedValue);
//				}
//
//			}
//			// }
			

			if (dstInsert.containsKey(colName)) {
				SqlUpdateItem dstInsertI=dstInsert.get(colName);
				//updatePs2(crs,md,mdIdx,dstInsertI,dstInsertI.Value);
				updateCrs(crs,md,mdIdx,dstInsertI);
			}
		}

		// 插入虚拟行
		crs.insertRow();
		// 移动指针到当前行
		crs.moveToCurrentRow();
		return crs;
	}

//	private Boolean DoBulkReader(PFSqlInsertCollection insert,
//			// CachedRowSetImpl crs,
//			// ResultSet crs,
//			CachedRowSet crs, String tableName) {
//		return DoBulkReader(insert, crs, tableName, false);
//	}

	public Boolean DoBulkReader(PFSqlInsertCollection insert,
			// CachedRowSetImpl crs,
			// ResultSet crs,
			CachedRowSet crs, String tableName, Boolean transferOneByOne// 只有当一条一条导入时,bulk的错误才能确定是insert里的值
	) {

		// 报错时,colid后面的序号是根据addColumnMapping来的
		HashMap<Integer, String> mapperIdx = new HashMap<Integer, String>();
		Connection conn2=null;
		try {

			conn2=OpenMicroJdbcConn2();
			SQLServerBulkCopyOptions copyOptions = new SQLServerBulkCopyOptions();
			copyOptions.setKeepIdentity(true);
			copyOptions.setBatchSize(8000);
			copyOptions.setKeepNulls(true);
			// copyOptions.setUseInternalTransaction(true);
			copyOptions.setUseInternalTransaction(false);

			SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(conn2);

			bulkCopy.setBulkCopyOptions(copyOptions);
			bulkCopy.setDestinationTableName(tableName);

			Iterator<Entry<String, SqlUpdateItem>> iter = insert.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				Entry<String, SqlUpdateItem> key = iter.next();
				// if(fieldList.contains(key.getKey())) {
				String colName = key.getKey();
				bulkCopy.addColumnMapping(colName, colName);
				// System.out.println("columnMapper__"+String.valueOf(i)+"__"+colName);
//				   mapperIdx.put(i,colName);
				mapperIdx.put(i + 2, colName);// 这里要+2才能对应上colid的序号,原因不明
				i++;
				// }
			}
//	   bulkCopy.addColumnMapping("Orderno", "Orderno");

			bulkCopy.writeToServer(crs);

			crs.close();
			bulkCopy.close();
		} catch (Exception e) {
			try {
				if (e.getMessage() != null) {
					// String pattern = ".*客户端收到一个对 colid (\\d+) 无效.*";
					// 为了尽量找出异常列
					String[] columnPatterns = new String[] { ".*客户端收到一个对 colid (\\d+) 无效.*",
							".*表示列 (\\d+) 的 Unicode 数据大小的字节数为奇数。应为偶数.*" };
					String msg = e.getMessage();

					Matcher found = null;
					for (int i = 0; i < columnPatterns.length; i++) {
						String pattern = columnPatterns[i];
						Pattern p = Pattern.compile(pattern);
						// boolean isMatch = Pattern.matches(pattern, e.getMessage());
						Matcher m = p.matcher(msg);
						if (m.find()) {
							found = m;
						}
					}
					if (null != found) {
						// m.find();
						String aa = found.group(1);
//						String msg2 = PFDataHelper.FormatString(
//								"DoBulkReader()方法报错\r\n" + "相关列:{0}\r\n" + "insert:{1}\r\n",
//								// crs.getMetaData().getColumnLabel(Integer.valueOf(aa)),
//								mapperIdx.get(Integer.valueOf(aa)), JSON.toJSONString(insert));
						String msg2 = SGDataHelper.FormatString(
								"DoBulkReader()方法报错\r\n" + "相关表:{0} 相关列:{1}\r\n" + "insert:{2}\r\n",
								// crs.getMetaData().getColumnLabel(Integer.valueOf(aa)),
								tableName,
								mapperIdx.get(Integer.valueOf(aa)), JSON.toJSONString(insert));
						// Exception e2=new Exception(msg2,e) ;
						Exception e2 = new Exception(msg2);
						// e2.printStackTrace();
						SetError(e2);
						PrintError();
					}
//		      else {
//					e.printStackTrace();
//					SetError(e);
//		      }
				}
//		else {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			SetError(e);
//		}
			} catch (Exception e1) {// 正常来讲,这里是不会报错的
				// TODO Auto-generated catch block
				e1.printStackTrace();
				SetError(e1);
			}
			if (transferOneByOne) {
//				String msg2 = PFDataHelper.FormatString("DoBulkReader()方法报错\r\n" + "insert:{0}\r\n",
//						JSON.toJSONString(insert));
				String msg2 = SGDataHelper.FormatString("DoBulkReader()方法报错\r\n" + "insert:insert {0}({1}) values({2}) \r\n",
						tableName,insert.ToKeysSql(),insert.ToValuesSql());
				Exception e2 = new Exception(msg2, e);
//		e2.printStackTrace();
				SetError(e2);
				PrintError();
			} else {
				e.printStackTrace();
				SetError(e);
			}
			//CloseConn();
			doCloseConn(conn2);
			return false;
		}
		//CloseConn();
		doCloseConn(conn2);
		return true;
	}

//public CachedRowSetImpl GetCrs(String tableName) {
//    try {
//    	//OpenConn();
//    //ResultSet rs =GetDataReader("select * from "+ tableName+" where 1=0");
//    //ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where 1=0");
////    ResultSet rs =GetDataReaderUpdatable("select Orderno from "+ tableName+" where 1=0");//ok
//    ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where 1=0");
//    CachedRowSetImpl crs = new CachedRowSetImpl();
//		crs.populate(rs);
//    CloseConn();
//    return crs;
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		SetError(e);
//	}
//    return null;
//}
//public ResultSet GetRs(String tableName) {
//    try {
//    	//OpenConn();
//    
//    //ResultSet rs =GetDataReader("select * from "+ tableName+" where 1=0");
//    //ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where 1=0");
////    ResultSet rs =GetDataReaderUpdatable("select Orderno from "+ tableName+" where 1=0");//ok
//    ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where 1=0");    
//    return rs;
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		SetError(e);
//	}
//    return null;
//}
	public CachedRowSet GetCrs(String tableName) {
		try {
			// OpenConn();
			if (!OpenConn()) {
				return null;
			}

//    //ResultSet rs =GetDataReader("select * from "+ tableName+" where 1=0");
//    //ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where 1=0");
////    ResultSet rs =GetDataReaderUpdatable("select Orderno from "+ tableName+" where 1=0");//ok
//    ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where 1=0");    //ok 但主键等标识列用来更新会有问题
			String sql=SGDataHelper.FormatString("	declare @tableName varchar(50)\r\n"
					+ "	set @tableName='{0}'	\r\n" + "	\r\n"
					+ "  	declare @sql nvarchar(max),@colNames varchar(max),@insertColNames varchar(max),@selectColNames varchar(max),@changeTableName varchar(50)\r\n"
					+ "\r\n" + "	set @selectColNames=''\r\n" + "\r\n" + "--colstat 1自增列 4计算列\r\n"
					+ "select @selectColNames+=('a.'+a.name+',')\r\n" + "from  syscolumns a\r\n"
					+ "where a.id=object_id(@tableName) and a.colstat<>1\r\n" + "\r\n"
					+ "set @selectColNames=substring(@selectColNames,0,Len(@selectColNames))\r\n" + "\r\n"
					+ "	set @sql=N'	\r\n" + "select '+@selectColNames+'\r\n" + "from '+@tableName+' a\r\n"
					+ "where 1=0\r\n" + "	'\r\n" + "-- select @sql\r\n" + "exec(@sql)", tableName);
			ResultSet rs = GetDataReaderUpdatable(sql);

			RowSetFactory factory = RowSetProvider.newFactory();
			CachedRowSet crs = factory.createCachedRowSet();
			crs.populate(rs);
			CloseConn();
			return crs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SetError(e);
		}
		return null;
	}
	public CachedRowSet GetCrs(String tableName, List<String> fieldNames) {
		try {
			// OpenConn();
			if (!OpenConn()) {
				return null;
			}

			ResultSet rs = GetDataReaderUpdatable(SGDataHelper.FormatString("select {0} from {1} where 1=0",String.join(",", fieldNames),tableName));

			RowSetFactory factory = RowSetProvider.newFactory();
			CachedRowSet crs = factory.createCachedRowSet();
			crs.populate(rs);
			CloseConn();
			return crs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SetError(e);
		}
		return null;
	}

//	/**
//	 * 注意： 1.因为目标表是Sqlserver里的，如果目标表有中文字段，一定要用nvarhcar,如果用了varchar,bulk不支持，会报错 列长度不够
//	 * 2.如果提供insert参数，需保证其包含主键
//	 */
//	@Deprecated
//	public Boolean HugeBulkReaderOldWhile(PFSqlInsertCollection insert, ResultSet rdr, String tableName,
//			// Consumer<BatchInsertOption> insertOptionAction,
//			Consumer<BaseSqlUpdateCollection> rowAction, Consumer<Integer> sqlRowsCopiedAction,
//			Predicate<Boolean> stopAction) {
//		// TODO Auto-generated method stub
//		if (rdr == null) {
//			SetError(new Exception("DataReader不能为空"));
//			return false;
//		}
//		if (insert == null) {
//			// insert = new PFClickHouseSqlInsertCollection();
////        insert = new SqlInsertCollection();
//			insert = getInsertCollection();
//			try {
//				ResultSetMetaData md = rdr.getMetaData();
//
//				List<SGSqlFieldInfo> targetFields = GetTableFields(tableName);
//				Map<String, SGSqlFieldInfo> targetFieldDict = PFDataHelper
//						.<SGSqlFieldInfo, String, SGSqlFieldInfo>ListToMapT(targetFields, a -> a.getFieldName(),
//								a -> a);
//
//				for (int i = 0; i < md.getColumnCount(); i++) {
////                String fieldName = md.getColumnName(i+1);
//					String fieldName = md.getColumnLabel(i + 1);
//
//					SqlUpdateItem updateItem = new SqlUpdateItem();
//					updateItem.Key = fieldName;
////                updateItem.VType=PFDataHelper.GetTypeByString(md.getColumnTypeName(i+1));
//					// 用目标表的type比dataReader的更准确,因为dr中可能有填null的情况
//					updateItem.VType = PFDataHelper.GetTypeByString(
//							targetFieldDict.containsKey(fieldName) ? targetFieldDict.get(fieldName).getFieldType()
//									: md.getColumnTypeName(i + 1));
//					insert.Add(updateItem);
//
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				SetError(e);
//				return false;
//			}
//
//		}
//
////    BatchInsertOption insertOption = new BatchInsertOption();
////    if (insertOptionAction != null) { insertOptionAction.accept(insertOption); }
//		BatchInsertOption insertOption = GetInsertOption();
//
//		OpenConn();
//
//		// StringBuilder sb = new StringBuilder();
//
//		int idx = 0;
//
//		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
//		int batchCnt = 0;
//
//		Boolean hasUnDo = false;
//
//		//// SetHugeCommandTimeOut();
//		// SetNormalTransferCommandTimeOutIfNotHuge();
//		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//
//		// int oneThousandCount = 0;
//		try {
//			// CachedRowSetImpl crs = GetCrs(tableName);
//			// ResultSet crs = GetRs(tableName);
//			CachedRowSet crs = GetCrs(tableName);
//			// PFSqlExecute crsExec=new PFSqlExecute(_jdbc);
////        ResultSet rs =dstExec.GetDataReader("select * from "+ tableName+" where 1=0");
////        CachedRowSetImpl crs = new CachedRowSetImpl();
////        crs.populate(rs);
////        dstExec.CloseConn();
//
//			while (rdr.next()) {
//				insert.UpdateByDataReader(rdr);
//				if (rowAction != null) {
//					rowAction.accept(insert);
//				}
//
////            if (oneThousandCount > 999)//sqlserver里values最多1000行,但tidb没有这个限制(但这句留着备用)(注释这句的话,可以从19秒减少到14秒完成)
////            {
////                oneThousandCount = 0;
////                sb.append(PFDataHelper.FormatString("; insert into {0}({1}) values({2})", tableName, insert.ToKeysSql(), insert.ToValuesSql()));
////            }
////            else if (oneThousandCount == 0)
////		    {
////		        sb.append(PFDataHelper.FormatString(" insert into {0}({1}) values({2})", tableName, insert.ToKeysSql(), insert.ToValuesSql()));
////		    }
////		    else
////		    {
////		    	sb.append(PFDataHelper.FormatString(",({0})", insert.ToValuesSql()));
////		    }
//
//				crs = insertIntoCachedRowSetOld(crs, insert);
//
//				batchCnt++;
//				hasUnDo = true;
////		    if (batchCnt > batchSize)
//				if (batchCnt >= batchSize) {
//
//					// Boolean b = ExecuteSql(sb.toString());
//					Boolean b = DoBulkReader(insert, crs, tableName, batchSize == 1);
//
//					if (!b) {
//						// CloseReader(rdr);
//						CloseConn();
//						return false;
//					}
//					if (sqlRowsCopiedAction != null) {
//						sqlRowsCopiedAction.accept(idx);
//					}
//					if (stopAction != null) {
//						if (stopAction.test(true)) {// 允许中途终止--benjamin20200812
//													// CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//							CloseConn();
//							return true;
//						}
//					}
////            sb.Clear();
//					// sb=new StringBuilder();
//					crs = GetCrs(tableName);
//					// crs = GetRs(tableName);
//					// deleteSb.Clear();
//
//					hasUnDo = false;
//					batchCnt = 0;
//					// oneThousandCount = 0;
//				} else {
//					// batchCnt++;
//					// oneThousandCount++;
//				}
//				idx++;
//
//			}
//			if (hasUnDo) {
//				// if (deleteBeforeInsert)
//				// {
//				// ExecuteSql(deleteSb.ToString(), false);
//				// }
//				// Boolean b = ExecuteSql(sb.toString());
//				Boolean b = DoBulkReader(insert, crs, tableName);
//
//				if (!b) {
//					// CloseReader(rdr);
//					CloseConn();
//					return false;
//				}
//			} else {
//				// CloseReader(rdr);
//				CloseConn();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			SetError(e);
//			return false;
//		}
//
//		return true;
//	}

	/**
	 * DoBulkReader时用的是sqlserver官网的bulk方式
	 * (后来实测,这种方式和基类的doInsertList速度上好像是一样的-- benjamin 20220926)
	 */
	@Override
	public  <T> boolean doInsertList(
			//Class<T> cls,
//			List<String> srcFieldNames//SqlInsertCollection dstInsert
			List<String> dstFieldNames//SqlInsertCollection dstInsert
			,String tableName,
			//int cnt,
			//Function<Integer,Boolean> isEnd ,
			PFFunc3<Integer,Object,Object,Boolean> hasNextAction ,
			Function<Integer,T> getItemAction ,
			SGAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
			Consumer<Integer> sqlRowsCopiedAction,
			Predicate<Boolean> stopAction
			) {
		if(!isMicroJdbc) {
			return super.doInsertList(dstFieldNames, tableName, hasNextAction, getItemAction, rowAction, sqlRowsCopiedAction, stopAction);
		}
		PFSqlInsertCollection dstInsert=null;
		
		BatchInsertOption insertOption = GetInsertOption();

		OpenConn();

		int idx = 0;

		insertedCnt = 0;// 已插入的行数

		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
		int batchCnt = 0;

		Boolean hasUnDo = false;

		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
		try {
//			if(null==srcFieldNames&&null!=cls) {
//				srcFieldNames=getFieldsByRowObject(cls);
//			}
			//boolean hasSysLimitId =null!=srcFieldNames&& srcFieldNames.contains(sys_limit_id);
			boolean hasSysLimitId =false;
			String sysLimitIdFieldName = sys_limit_id;
			long currentSysLimitId = -1;
			lastInsertedId = -1;

			List<String> srcFieldNames =null;
			//List<String> dstFieldNames =null;
//			dstFieldNames=PFDataHelper.ListSelect(this.GetTableFields(tableName), a->a.getFieldName());
//			//List<String> dstFieldLowerNames =PFDataHelper.ListSelect(this.GetTableFields(tableName), a->a.getFieldName().toLowerCase());
//			if(null!=srcFieldNames) {//来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值				
////				dstFieldNames=PFDataHelper.ListWhere(dstFieldNames, a->srcFieldNames.contains(a));
//				//SGRef<List<String>> srcFieldNamesRef=new SGRef<List<String>>(srcFieldNames);
//				dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNames.contains(a)).collect(Collectors.toList());
//			}
			
//			@SuppressWarnings("deprecation")
//			ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
//					:GetMetaData(tableName, Arrays.asList(srcFieldNames.stream().filter(a->!a.equals(sys_limit_id)).toArray(String[]::new)));

			ResultSetMetaData dstMd=null;
//			if(null==dstFieldNames) {
//				dstMd=GetMetaData(tableName);
//			}else{
//				//dstMd=GetMetaData(tableName,Arrays.asList(srcFieldNames.stream().filter(a->!a.equals(sys_limit_id)).toArray(String[]::new)));
//				dstMd=GetMetaData(tableName, Arrays.asList(dstFieldNames.stream().filter(a->!a.equals(sys_limit_id)).toArray(String[]::new)));					
//			}
			
			//PreparedStatement ps =srcFieldNames==null?  GetPs(tableName): GetPs(tableName,srcFieldNames);
			CachedRowSet crs = GetCrs(tableName);

//			// 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//			if (dstInsert == null) {
//
//				dstInsert = getInsertCollection(dstMd);
//			}

			while (true) {
				//T item = null;
//				boolean hasNext = idx < cnt;
				//boolean hasNext =isEnd.apply(idx);
				boolean hasNext =hasNextAction.go(idx,null,null);
//				if (hasNext) {
//					item=getItemAction.apply(idx);
//				}
				if (hasNext) {
					T item=null;
					if(null!=getItemAction) {
						item=getItemAction.apply(idx);
					}
					if(0==idx) {
						if(null==srcFieldNames&&null!=item) {
							srcFieldNames=getFieldsByRowObject(item);
						}
						hasSysLimitId =null!=srcFieldNames&& srcFieldNames.contains(sys_limit_id);

						if(null==dstFieldNames) {
							dstFieldNames=SGDataHelper.ListSelect(this.GetTableFields(tableName), a->a.getFieldName());
							if(null!=srcFieldNames) {//来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值	
								SGRef<List<String>> srcFieldNamesRef=new SGRef<List<String>>(srcFieldNames);
								//dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
								dstFieldNames=SGDataHelper.ListWhere(dstFieldNames, a->SGDataHelper.ListAny(srcFieldNamesRef.GetValue(), b->b.toLowerCase().equals(a.toLowerCase())));//大小写不匹配时,还要改UpdateModelValueAutoConvert才行
								
							}
						}
						
						if(null==dstMd) {
							dstMd=GetMetaData(tableName, Arrays.asList(dstFieldNames.stream().filter(a->!a.equals(sys_limit_id)).toArray(String[]::new)));
						}
						if (dstInsert == null) {
							dstInsert = getInsertCollection(dstMd);

							if(null!=srcFieldNames) {
								dstInsert.UpdateLowerKeyMapBySrcField(srcFieldNames);
							}
						}
					}

					if(insertOption.getAutoUpdateModel()&&null!=getItemAction&&null!=item) {
						dstInsert.UpdateModelValueAutoConvert(item);
						if (hasSysLimitId) {
							if(item instanceof ResultSet){
								currentSysLimitId = ((ResultSet)item).getLong(sysLimitIdFieldName);
							}
						}
					}
					
					if (rowAction != null) {
						//rowAction.accept(dstInsert);
						rowAction.go(dstInsert,idx,null);
					}

					//ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
					crs = insertIntoCachedRowSet(crs, dstInsert);// ,dstColumnType);

					batchCnt++;
					hasUnDo = true;

//					if (hasSysLimitId) {
//						//currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
//						currentSysLimitId =PFDataHelper.ObjectToLong0(dstInsert.Get(sysLimitIdFieldName).Value);
//					}
				}
				if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {

					Boolean b = false;
					try {
//						ps.executeBatch();
//						conn.commit();
//						b = true;
						b = DoBulkReader(dstInsert, crs, tableName, batchSize == 1);
						if (hasSysLimitId) {
							lastInsertedId = currentSysLimitId;
						}
					} catch (Exception e) {
						SetError(e);
					}

					if (!b) {
						// CloseReader(rdr);
						CloseConn();
						return false;
					} else {
						insertedCnt = idx + 1;
//						if (hasSysLimitId) {
//							lastInsertedId = currentSysLimitId;
//						}
					}
					if (sqlRowsCopiedAction != null) {
						sqlRowsCopiedAction.accept(idx);
					}
					if (stopAction != null) {
						if (stopAction.test(true)) {// 允许中途终止--benjamin20200812
													// CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
							CloseConn();
							return true;
						}
					}


					//ps =srcFieldNames==null?  GetPs(tableName): GetPs(tableName,srcFieldNames);
					crs = GetCrs(tableName);

					hasUnDo = false;
					batchCnt = 0;
				} else {
					// batchCnt++;
					// oneThousandCount++;
				}
				if (!hasNext) {
					break;
				}
				idx++;

			}

		} catch (Exception e) {
			e.printStackTrace();
			SetError(e);
			return false;
		}

		CloseConn();
		
	 return true;
	}
	
	/**
	 * 注意： 1.因为目标表是Sqlserver里的，如果目标表有中文字段，一定要用nvarhcar,如果用了varchar,bulk不支持，会报错 列长度不够
	 * 2.如果提供insert参数，需保证其包含主键
	 */
	@Override
	public Boolean HugeBulkReader(PFSqlInsertCollection dstInsert, ResultSet rdr, String tableName,
			//Consumer<BaseSqlUpdateCollection> rowAction, 
			SGAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
			Consumer<Integer> sqlRowsCopiedAction,
			Predicate<Boolean> stopAction
) {

		if (rdr == null) {
			SetError(new Exception("DataReader不能为空"));
			return false;
		}

		//BatchInsertOption insertOption = GetInsertOption();

//		OpenConn();
		if (!OpenConn()) {
			return false;
		}

		//int idx = 0;
		insertedCnt = 0;// 已插入的行数

//		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
//		int batchCnt = 0;
//
//		Boolean hasUnDo = false;

		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

		//PFSqlInsertCollection srcInsert =null;
		//List<String> srcFieldNames=null;
//		if(list!=null&&(!list.isEmpty())){
//			srcInsert=getInsertCollection();
//			srcInsert.InitItemByModel(list.get(0));
//			srcFieldNames=new ArrayList<String>( srcInsert.keySet());
//		}
//		List<String> srcFieldNames=new ArrayList<String>();
//		try {
//			ResultSetMetaData srcMd = rdr.getMetaData();
//			for (int i = 0; i < srcMd.getColumnCount(); i++) {
//				String fieldName = srcMd.getColumnLabel(i + 1);
//////		  srcColumnType.put(srcMd.getColumnLabel(i+1),srcMd.getColumnType(i+1));
////			if (sysLimitIdFieldName.equals(fieldName)) {// 这个内部值用来代替limit的offset
////				hasSysLimitId = true;
////				continue;
////			}
//				srcFieldNames.add(fieldName);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//int insertCnt=list.size();
		return this.<ResultSet>doInsertList(//null,
//				srcFieldNames,
				null,
				tableName,
				//list.size(), 
				(a,b,c)->{
					try {
						return rdr.next();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				} , 
				a->rdr, 
				rowAction, sqlRowsCopiedAction, stopAction);
		
//		try {
////    	//保存来源的类型，便于转换
//			ResultSetMetaData srcMd = rdr.getMetaData();
//
//			boolean hasSysLimitId = false;
//			String sysLimitIdFieldName = "sys_limit_id";
//			long currentSysLimitId = -1;
//			lastInsertedId = -1;
//
//			for (int i = 0; i < srcMd.getColumnCount(); i++) {
//				String fieldName = srcMd.getColumnLabel(i + 1);
////    		  srcColumnType.put(srcMd.getColumnLabel(i+1),srcMd.getColumnType(i+1));
//				if (sysLimitIdFieldName.equals(fieldName)) {// 这个内部值用来代替limit的offset
//					hasSysLimitId = true;
//					continue;
//				}
//			}
//
//			CachedRowSet crs = GetCrs(tableName);
//			ResultSetMetaData dstMd = crs.getMetaData();
//
//			// 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//			if (dstInsert == null) {
//				dstInsert = getInsertCollection(dstMd);
//			}
//
//			while (true) {
//				boolean hasNext = rdr.next();
//				if (hasNext) {
//					dstInsert.UpdateByDataReaderAutoConvert(rdr);
//					if (rowAction != null) {
//						rowAction.accept(dstInsert);
//					}
//
//					crs = insertIntoCachedRowSet(crs, dstInsert);// ,dstColumnType);
//
//					batchCnt++;
//					hasUnDo = true;
//					if (hasSysLimitId) {
//						currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
//					}
//				}
//				if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//
//					Boolean b = DoBulkReader(dstInsert, crs, tableName, batchSize == 1);
//
//					if (!b) {
//						// CloseReader(rdr);
//						CloseConn();
//						return false;
//					} else {
//						insertedCnt = idx + 1;
//						if (hasSysLimitId) {
//							lastInsertedId = currentSysLimitId;
//						}
//					}
//					if (sqlRowsCopiedAction != null) {
//						sqlRowsCopiedAction.accept(idx);
//					}
//					if (stopAction != null) {
//						if (stopAction.test(true)) {// 允许中途终止--benjamin20200812
//													// CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//							CloseConn();
//							return true;
//						}
//					}
//					crs = GetCrs(tableName);
//
//					hasUnDo = false;
//					batchCnt = 0;
//				} else {
//				}
//				if (!hasNext) {
//					break;
//				}
//				idx++;
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			SetError(e);
//			return false;
//		}
//
//		CloseConn();
//		return true;
	}

	@Override
	public Boolean IsTableExist(String tableName) {
		SGDataTable dt = GetDataTable("select OBJECT_ID(N'" + tableName + "',N'U')", null);
		return (!dt.IsEmpty()) && dt.Rows.get(0).getCol().get(0).getValue() != null;
	}

	@Override
	public List<SGSqlFieldInfo> GetTableFields(String tableName) {
		return QueryList(SGSqlFieldInfo.class, SGDataHelper.FormatString("\r\n"
				+ "SELECT (case when a.colorder=1 then d.name else null end) tableName,-- 表名,  \r\n"
				+ "(case when a.colorder=1 then h.value else null end) tableDescription, -- 表说明,\r\n"
				+ "  a.colorder fieldIdx, -- 字段序号 ,\r\n" + "  a.name as fieldName,-- 字段名,\r\n"
				+ "  (case when COLUMNPROPERTY( a.id,a.name,'IsIdentity')=1 then '√'else '' end) 标识, \r\n"
				+ "  (case when (SELECT count(*) FROM sysobjects  \r\n"
				+ "  WHERE (name in (SELECT name FROM sysindexes  \r\n" + "  WHERE (id = a.id) AND (indid in  \r\n"
				+ "  (SELECT indid FROM sysindexkeys  \r\n" + "  WHERE (id = a.id) AND (colid in  \r\n"
				+ "  (SELECT colid FROM syscolumns WHERE (id = a.id) AND (name = a.name)))))))  \r\n"
				+ " -- AND (xtype = 'PK'))>0 then '√' else '' end) 主键,\r\n"
				+ " AND (xtype = 'PK'))>0 then CONVERT(bit,1) else CONVERT(bit,0) end) isPrimaryKey, -- 主键,\r\n"
				+ " b.name fieldType, -- 类型,\r\n" + " a.length bitLength,-- 占用字节数,  \r\n"
				+ " COLUMNPROPERTY(a.id,a.name,'PRECISION') as fieldSqlLength,-- 长度,  \r\n"
				+ " isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0) as precision,-- 小数位数,\r\n"
				+ " -- (case when a.isnullable=1 then '√'else '' end) 允许空,  \r\n"
				+ " (case when a.isnullable=1 then CONVERT(bit,0) else CONVERT(bit,1) end) isRequired,  \r\n"
				+ " isnull(e.text,'') defaultValue,-- 默认值,\r\n"
				+ " isnull(g.[value], ' ') AS columnDescription --[说明]\r\n" + " FROM  syscolumns a \r\n"
				+ " left join systypes b on a.xtype=b.xusertype  \r\n"
				+ " inner join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties' \r\n"
				+ " left join syscomments e on a.cdefault=e.id  \r\n"
				+ " left join sys.extended_properties g on a.id=g.major_id AND a.colid=g.minor_id\r\n"
				+ " left join sys.extended_properties f on d.id=f.class and f.minor_id=0\r\n"
				+ " left join sys.extended_properties h on a.id=h.major_id AND 0=h.minor_id\r\n"
				+ " where b.name is not null\r\n" + " and d.name='{0}' --如果只查询指定表,加上此条件\r\n"
				+ " order by a.id,a.colorder", tableName));

	}

	/**
	 * 预验证reader(主要验证字符串长度)(注意此方法是面向sqlserver的,待移到派生类)
	 * 
	 * @param insert
	 * @param rdr
	 * @param tableName
	 * @param rowAction
	 * @param sqlRowsCopiedAction
	 * @param stopAction
	 * @return
	 */
	@Override
	public Boolean PreValidTransferReader(// SqlInsertCollection insert,
			ResultSet rdr, String tableName,
			Function<BaseSqlUpdateCollection, List<PFDataTableFieldValidModel>> PreValidAction,
			SGRef<List<PFDataTableFieldValidModel>> validRef, Consumer<Integer> alreadyAction,
			Predicate<Boolean> stopAction
//		//Consumer<BatchInsertOption> insertOptionAction,
//		Consumer<BaseSqlUpdateCollection> rowAction,
//		Consumer<Integer> sqlRowsCopiedAction, Predicate<Boolean> stopAction
	) {
		// TODO Auto-generated method stub

		List<SGSqlFieldInfo> targetFields = GetTableFields(tableName);

		// Map<String,SGSqlFieldInfo>
		// targetFieldDict=PFDataHelper.<SGSqlFieldInfo,String,SGSqlFieldInfo>ListToMapT(targetFields,
		// a->a.getFieldName(), a->a);
//	for(PFDataColumn i : targetDt.Columns) {
//		String typeStr=PFDataHelper.GetStringByType(i.getDataType());
//		if("string".equals(typeStr)) {
//			
//		}		
//	}
//	
//    if (insert == null)
//    {
		// insert = new PFClickHouseSqlInsertCollection();
//        insert = new SqlInsertCollection();

//	SqlInsertCollection insert=getInsertCollection();
//        try {
//            ResultSetMetaData md=rdr.getMetaData();
//            
//            for (int i = 0; i < md.getColumnCount(); i++)
//            {
////                String fieldName = md.getColumnName(i+1);
//                String fieldName = md.getColumnLabel(i+1);                
//
//                SqlUpdateItem updateItem = new SqlUpdateItem();
//                updateItem.Key=fieldName;
//                updateItem.VType=PFDataHelper.GetTypeByString(md.getColumnTypeName(i+1));
//                insert.Add(updateItem);
//                
//            }
//        }catch(Exception e) {
//       	 
//        }

		CachedRowSet crs = GetCrs(tableName);
		ResultSetMetaData dstMd = null;
		try {
			dstMd = crs.getMetaData();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PFSqlInsertCollection dstInsert = getInsertCollection(dstMd);

//    BatchInsertOption insertOption = new BatchInsertOption();
//    if (insertOptionAction != null) { insertOptionAction.accept(insertOption); }
		// BatchInsertOption insertOption=GetInsertOption();

		// OpenConn();

		// StringBuilder sb = new StringBuilder();

//    int idx = 0;
//
//    int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
//    int batchCnt = 0;
//
//    Boolean hasUnDo = false;

		//// SetHugeCommandTimeOut();
		// SetNormalTransferCommandTimeOutIfNotHuge();
		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

		// int oneThousandCount = 0;

//    SqlInsertCollection targetInsert =getInsertCollection();
//    ResultSet targetDr= GetDataReader(PFDataHelper.FormatString( "select * from {0} limit 0,1",tableName));
//
//    try {
//        ResultSetMetaData md=targetDr.getMetaData();
//        
//        for (int i = 0; i < md.getColumnCount(); i++)
//        {
////            String fieldName = md.getColumnName(i+1);
//            String fieldName = md.getColumnLabel(i+1);                
//
//            SqlUpdateItem updateItem = new SqlUpdateItem();
//            updateItem.Key=fieldName;
//            updateItem.VType=PFDataHelper.GetTypeByString(md.getColumnTypeName(i+1));
//            targetInsert.Add(updateItem);
//            
//        }
//    }catch(Exception e) {
//   	 
//    }
//
//    
		int idx = 0;// 当前行号
		int batchSize = 1000;// 用于更新进度
		int batchCnt = 0;
		List<String> pKeys = new ArrayList<String>();

		List<String> keyFields = new ArrayList<String>();
		boolean hasPKey = false;
		for (SGSqlFieldInfo i : targetFields) {
			if (i.getIsPrimaryKey()) {
				keyFields.add(i.getFieldName());
			}
		}
		if (keyFields.size() > 0) {
			hasPKey = true;
		}
		try {
			while (rdr.next()) {
				//dstInsert.UpdateByDataReaderAutoConvert(rdr);
				dstInsert.UpdateByDataReader(rdr);

				if (PreValidAction != null) {
					List<PFDataTableFieldValidModel> validModel = PreValidAction.apply(dstInsert);
					if (validModel != null) {
						for (PFDataTableFieldValidModel i : validModel) {
							if (SGDataHelper.ListAny(validRef.GetValue(),
									a -> a.getFieldName().equals(i.getFieldName()))) {

							} else {
								validRef.GetValue().add(i);
							}
						}
					}
				}

				// StringBuilder tmpPKey=new StringBuilder();
				List<String> tmpPKey = new ArrayList<String>();
				for (SGSqlFieldInfo i : targetFields) {
					String fieldName = i.getFieldName();
					if (dstInsert.containsKey(fieldName)) {

						// Object ov=rdr.getObject(i.getFieldName());
						Object ov = dstInsert.get(fieldName).Value;
						if (ov != null) {

							if (i.getIsPrimaryKey()) {
								// tmpPKey.append(ov.toString());
								tmpPKey.add(ov.toString());
							}
//						PFSqlFieldType pfType=PFDataHelper.GetPFTypeByString(i.getFieldType());
							SGSqlFieldTypeEnum pfType = SGSqlFieldTypeEnum.InitByString(i.getFieldType());
							if (SGSqlFieldTypeEnum.String.equals(pfType)) {
								String val = rdr.getString(i.getFieldName());

								if (SGDataHelper.ListAny(validRef.GetValue(),
										a -> a.getFieldName().equals(i.getFieldName()))) {

								} else {
									if (val != null) {
										if (SGDataHelper.GetWordsCharLength(val) > i.getFieldSqlLength()) {
//											if("postcode".equals(i.getFieldName())
//													//&&"12315".equals(val)
//													) {
//												String aa="aa";
//												PFDataHelper.GetWordsCharLength(val);
//												
//											}
											validRef.GetValue()
													.add(new PFDataTableFieldValidModel(i.getFieldName(), false,
															SGDataHelper.FormatString("字段[{0}]超长,值为[{1}].最大长度[{2}]",
																	i.getFieldName(), val, i.getFieldSqlLength())));
										}
										if ("varchar".equals(i.getFieldType())
												&& SGDataHelper.StringHasChineseChar(val)) {
											validRef.GetValue()
													.add(new PFDataTableFieldValidModel(i.getFieldName(), false,
															SGDataHelper.FormatString("字段[{0}]有中文,值为[{1}].只允许英文",
																	i.getFieldName(), val, i.getFieldSqlLength())));
										}
									}
								}
							}
						}
					}
				}
				if (hasPKey) {
					String tmpPKeyStr = String.join("_", tmpPKey);
					if (pKeys.contains(tmpPKeyStr)) {
						String keys = String.join(",", keyFields);
						validRef.GetValue().add(new PFDataTableFieldValidModel(keys, false,
								SGDataHelper.FormatString("主键[{0}]复,值为[{1}].", keys, tmpPKeyStr)));
					} else {
						pKeys.add(tmpPKeyStr);
					}
				}

				idx++;
				batchCnt++;
				if (batchCnt >= batchSize) {
					if (alreadyAction != null) {
						alreadyAction.accept(idx);
					}
					if (stopAction != null) {
						if (stopAction.test(true)) {// 允许中途终止--benjamin20200812
													// CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
							CloseConn();
							return true;
						}
					}
					batchCnt = 0;
				}

//		    if (rowAction != null) { rowAction.accept(insert); }
//
//            if (oneThousandCount > 999)//sqlserver里values最多1000行,但tidb没有这个限制(但这句留着备用)(注释这句的话,可以从19秒减少到14秒完成)
//            {
//                oneThousandCount = 0;
//                sb.append(PFDataHelper.FormatString("; insert into {0}({1}) values({2})", tableName, insert.ToKeysSql(), insert.ToValuesSql()));
//            }
//            else if (oneThousandCount == 0)
//		    {
//		        sb.append(PFDataHelper.FormatString(" insert into {0}({1}) values({2})", tableName, insert.ToKeysSql(), insert.ToValuesSql()));
//		    }
//		    else
//		    {
//		    	sb.append(PFDataHelper.FormatString(",({0})", insert.ToValuesSql()));
//		    }
//		    
//
//	        batchCnt++;
//		    hasUnDo = true;
////		    if (batchCnt > batchSize)
//			if (batchCnt >= batchSize)
//		    {
//		        //if (deleteBeforeInsert)
//		        //{
//		        //    ExecuteSql(deleteSb.ToString(), false);
//		        //}
//		        Boolean b = ExecuteSql(sb.toString());
//		        if (!b)
//		        {
//		            //CloseReader(rdr);
//		            CloseConn();
//		            return false;
//		        }
//		        if (sqlRowsCopiedAction != null)
//		        {
//		            sqlRowsCopiedAction.accept(idx);
//		        }
//		        if (stopAction != null)
//		        {
//		            if (stopAction.test(true))
//		            {//允许中途终止--benjamin20200812
//		                //CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//		                CloseConn();
//		                return true;
//		            }
//		        }
////            sb.Clear();
//		        sb=new StringBuilder();
//		        //deleteSb.Clear();
//
//		        hasUnDo = false;
//		        batchCnt = 0;
//		        oneThousandCount = 0;
//		    }
//		    else
//		    {
//		        //batchCnt++;
//		        oneThousandCount++;
//		    }
//		    idx++;
//		    
//		    
//		    
//		    

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//    if (hasUnDo)
//    {
//        //if (deleteBeforeInsert)
//        //{
//        //    ExecuteSql(deleteSb.ToString(), false);
//        //}
//        Boolean b = ExecuteSql(sb.toString());
//        if (!b)
//        {
//            //CloseReader(rdr);
//            CloseConn();
//            return false;
//        }
//    }
//    else
//    {
//        //CloseReader(rdr);
//        CloseConn();
//    }
		return validRef.GetValue().size() < 1;
//    return true;
	}

	/**
	 * 预验证reader(主要验证字符串长度)(注意此方法是面向sqlserver的,待移到派生类)
	 * 
	 * @param insert
	 * @param rdr
	 * @param tableName
	 * @param rowAction
	 * @param sqlRowsCopiedAction
	 * @param stopAction
	 * @return
	 */
	@Override
	public Boolean PreValidTransferTable(SGSqlTransferItem transferItem,
			SGRef<List<PFDataTableFieldValidModel>> validRef, Consumer<Integer> alreadyAction,
			Predicate<Boolean> stopAction
//		//Consumer<BatchInsertOption> insertOptionAction,
//		Consumer<BaseSqlUpdateCollection> rowAction,
//		Consumer<Integer> sqlRowsCopiedAction, Predicate<Boolean> stopAction
	) {
		// TODO Auto-generated method stub

		ISGJdbc srcJdbc = transferItem.SrcJdbc;
		// IPFJdbc dstJdbc = task.DstJdbc;
		ISqlExecute srcExec = null;
		// srcExec = new PFSqlExecute(srcJdbc);
		if (srcJdbc != null) {
			try {
				srcExec = SGSqlExecute.Init(srcJdbc);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				return false;
			}
		}
		String sql = transferItem.SrcSql;
		// ResultSet dr = null;
		srcExec.SetCommandTimeOut(transferItem.getSqlCommandTimeout());

		ResultSet rdr = srcExec.GetHugeDataReader(sql);
		String tableName = transferItem.DstTableName;
		Function<BaseSqlUpdateCollection, List<PFDataTableFieldValidModel>> PreValidAction = transferItem.PreValidAction;

		List<SGSqlFieldInfo> targetFields = GetTableFields(tableName);

		// Map<String,SGSqlFieldInfo>
		// targetFieldDict=PFDataHelper.<SGSqlFieldInfo,String,SGSqlFieldInfo>ListToMapT(targetFields,
		// a->a.getFieldName(), a->a);
//	for(PFDataColumn i : targetDt.Columns) {
//		String typeStr=PFDataHelper.GetStringByType(i.getDataType());
//		if("string".equals(typeStr)) {
//			
//		}		
//	}
//	
//    if (insert == null)
//    {
		// insert = new PFClickHouseSqlInsertCollection();
//        insert = new SqlInsertCollection();

//	SqlInsertCollection insert=getInsertCollection();
//        try {
//            ResultSetMetaData md=rdr.getMetaData();
//            
//            for (int i = 0; i < md.getColumnCount(); i++)
//            {
////                String fieldName = md.getColumnName(i+1);
//                String fieldName = md.getColumnLabel(i+1);                
//
//                SqlUpdateItem updateItem = new SqlUpdateItem();
//                updateItem.Key=fieldName;
//                updateItem.VType=PFDataHelper.GetTypeByString(md.getColumnTypeName(i+1));
//                insert.Add(updateItem);
//                
//            }
//        }catch(Exception e) {
//       	 
//        }

		CachedRowSet crs = GetCrs(tableName);
		ResultSetMetaData dstMd = null;
		try {
			dstMd = crs.getMetaData();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PFSqlInsertCollection dstInsert = getInsertCollection(dstMd);

//    BatchInsertOption insertOption = new BatchInsertOption();
//    if (insertOptionAction != null) { insertOptionAction.accept(insertOption); }
		// BatchInsertOption insertOption=GetInsertOption();

		// OpenConn();

		// StringBuilder sb = new StringBuilder();

//    int idx = 0;
//
//    int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
//    int batchCnt = 0;
//
//    Boolean hasUnDo = false;

		//// SetHugeCommandTimeOut();
		// SetNormalTransferCommandTimeOutIfNotHuge();
		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

		// int oneThousandCount = 0;

//    SqlInsertCollection targetInsert =getInsertCollection();
//    ResultSet targetDr= GetDataReader(PFDataHelper.FormatString( "select * from {0} limit 0,1",tableName));
//
//    try {
//        ResultSetMetaData md=targetDr.getMetaData();
//        
//        for (int i = 0; i < md.getColumnCount(); i++)
//        {
////            String fieldName = md.getColumnName(i+1);
//            String fieldName = md.getColumnLabel(i+1);                
//
//            SqlUpdateItem updateItem = new SqlUpdateItem();
//            updateItem.Key=fieldName;
//            updateItem.VType=PFDataHelper.GetTypeByString(md.getColumnTypeName(i+1));
//            targetInsert.Add(updateItem);
//            
//        }
//    }catch(Exception e) {
//   	 
//    }
//
//    
		int idx = 0;// 当前行号
		int batchSize = 1000;// 用于更新进度
		int batchCnt = 0;
		List<String> pKeys = new ArrayList<String>();

		List<String> keyFields = new ArrayList<String>();
		boolean hasPKey = false;
		for (SGSqlFieldInfo i : targetFields) {
			if (i.getIsPrimaryKey()) {
				keyFields.add(i.getFieldName());
			}
		}
		if (keyFields.size() > 0) {
			hasPKey = true;
		}
		try {
			while (rdr.next()) {
				//dstInsert.UpdateByDataReaderAutoConvert(rdr);
				dstInsert.UpdateByDataReader(rdr);

				if (PreValidAction != null) {
					List<PFDataTableFieldValidModel> validModel = PreValidAction.apply(dstInsert);
					if (validModel != null) {
						for (PFDataTableFieldValidModel i : validModel) {
							if (SGDataHelper.ListAny(validRef.GetValue(),
									a -> a.getFieldName().equals(i.getFieldName()))) {

							} else {
								validRef.GetValue().add(i);
							}
						}
					}
				}

				// StringBuilder tmpPKey=new StringBuilder();
				List<String> tmpPKey = new ArrayList<String>();
				for (SGSqlFieldInfo i : targetFields) {
					String fieldName = i.getFieldName();
					if (dstInsert.containsKey(fieldName)) {

						// Object ov=rdr.getObject(i.getFieldName());
						Object ov = dstInsert.get(fieldName).Value;
						if (ov != null) {

							if (i.getIsPrimaryKey()) {
								// tmpPKey.append(ov.toString());
								tmpPKey.add(ov.toString());
							}
//						PFSqlFieldType pfType=PFDataHelper.GetPFTypeByString(i.getFieldType());
							SGSqlFieldTypeEnum pfType = SGSqlFieldTypeEnum.InitByString(i.getFieldType());
							if (SGSqlFieldTypeEnum.String.equals(pfType)) {
								String val = rdr.getString(i.getFieldName());

								if (SGDataHelper.ListAny(validRef.GetValue(),
										a -> a.getFieldName().equals(i.getFieldName()))) {

								} else {
									if (val != null) {
										if (SGDataHelper.GetWordsCharLength(val) > i.getFieldSqlLength()) {
//											if("postcode".equals(i.getFieldName())
//													//&&"12315".equals(val)
//													) {
//												String aa="aa";
//												PFDataHelper.GetWordsCharLength(val);
//												
//											}
//											PFDataTableFieldValidModel valid=new PFDataTableFieldValidModel();
//											valid.setFieldName(i.getFieldName());
//											valid.setIsValid(false);
//											valid.setErrMsg(errMsg);
											validRef.GetValue()
													.add(new PFDataTableFieldValidModel(i.getFieldName(), false,
															SGDataHelper.FormatString("字段[{0}]超长,值为[{1}].最大长度[{2}]",
																	i.getFieldName(), val, i.getFieldSqlLength())));
										}
										if ("varchar".equals(i.getFieldType())
												&& SGDataHelper.StringHasChineseChar(val)) {
											validRef.GetValue()
													.add(new PFDataTableFieldValidModel(i.getFieldName(), false,
															SGDataHelper.FormatString("字段[{0}]有中文,值为[{1}].只允许英文",
																	i.getFieldName(), val, i.getFieldSqlLength())));
										}
									}
								}
								// if(validRef.GetValue().contains(o))
							}
						}
					}
				}
				if (hasPKey) {
					String tmpPKeyStr = String.join("_", tmpPKey);
					if (pKeys.contains(tmpPKeyStr)) {
						String keys = String.join(",", keyFields);
						validRef.GetValue().add(new PFDataTableFieldValidModel(keys, false,
								SGDataHelper.FormatString("主键[{0}]复,值为[{1}].", keys, tmpPKeyStr)));
					} else {
						pKeys.add(tmpPKeyStr);
					}
				}

				idx++;
				batchCnt++;
				if (batchCnt >= batchSize) {
					if (alreadyAction != null) {
						alreadyAction.accept(idx);
					}
					if (stopAction != null) {
						if (stopAction.test(true)) {// 允许中途终止--benjamin20200812
													// CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
							CloseConn();
							return true;
						}
					}
					batchCnt = 0;
				}

//		    if (rowAction != null) { rowAction.accept(insert); }
//
//            if (oneThousandCount > 999)//sqlserver里values最多1000行,但tidb没有这个限制(但这句留着备用)(注释这句的话,可以从19秒减少到14秒完成)
//            {
//                oneThousandCount = 0;
//                sb.append(PFDataHelper.FormatString("; insert into {0}({1}) values({2})", tableName, insert.ToKeysSql(), insert.ToValuesSql()));
//            }
//            else if (oneThousandCount == 0)
//		    {
//		        sb.append(PFDataHelper.FormatString(" insert into {0}({1}) values({2})", tableName, insert.ToKeysSql(), insert.ToValuesSql()));
//		    }
//		    else
//		    {
//		    	sb.append(PFDataHelper.FormatString(",({0})", insert.ToValuesSql()));
//		    }
//		    
//
//	        batchCnt++;
//		    hasUnDo = true;
////		    if (batchCnt > batchSize)
//			if (batchCnt >= batchSize)
//		    {
//		        //if (deleteBeforeInsert)
//		        //{
//		        //    ExecuteSql(deleteSb.ToString(), false);
//		        //}
//		        Boolean b = ExecuteSql(sb.toString());
//		        if (!b)
//		        {
//		            //CloseReader(rdr);
//		            CloseConn();
//		            return false;
//		        }
//		        if (sqlRowsCopiedAction != null)
//		        {
//		            sqlRowsCopiedAction.accept(idx);
//		        }
//		        if (stopAction != null)
//		        {
//		            if (stopAction.test(true))
//		            {//允许中途终止--benjamin20200812
//		                //CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//		                CloseConn();
//		                return true;
//		            }
//		        }
////            sb.Clear();
//		        sb=new StringBuilder();
//		        //deleteSb.Clear();
//
//		        hasUnDo = false;
//		        batchCnt = 0;
//		        oneThousandCount = 0;
//		    }
//		    else
//		    {
//		        //batchCnt++;
//		        oneThousandCount++;
//		    }
//		    idx++;
//		    
//		    
//		    
//		    

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//    if (hasUnDo)
//    {
//        //if (deleteBeforeInsert)
//        //{
//        //    ExecuteSql(deleteSb.ToString(), false);
//        //}
//        Boolean b = ExecuteSql(sb.toString());
//        if (!b)
//        {
//            //CloseReader(rdr);
//            CloseConn();
//            return false;
//        }
//    }
//    else
//    {
//        //CloseReader(rdr);
//        CloseConn();
//    }
		return validRef.GetValue().size() < 1;
//    return true;
	}
}
