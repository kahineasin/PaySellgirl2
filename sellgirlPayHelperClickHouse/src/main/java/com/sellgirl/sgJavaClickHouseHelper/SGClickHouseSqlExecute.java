package com.sellgirl.sgJavaClickHouseHelper;

//import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.sql.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.exception.PFSqlConnOpenException;
//import ru.yandex.clickhouse.ClickHouseDataSource;

/**
 * clickhouse的驱动好像不能动态初始化,所以必要时可能要用PFClickHouseSqlExecute(IPFJdbc jdbc,Connection conn)来构造
 *
 * com.github.housepower.jdbc.ClickHouseDriver驱动不支持的方法有:
 * 1. stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
 * 2. stmt.setQueryTimeout(CommandTimeOut);
 * 3. myResource.GetConn().getMetaData()
 */
public class SGClickHouseSqlExecute extends SGSqlExecuteBase implements ISqlExecute {

	// String url = "jdbc:sqlserver://localhost:1433;DatabaseName=student;";
	// String sql = "select * from GLType";
	public SGClickHouseSqlExecute(String url, String user, String password) {
		// 连接数据库
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
//			PFDataHelper.WriteError(new Throwable(), e);
			SGDataHelper.WriteError( e);
		}
	}

//	public PFClickHouseSqlExecute(IPFJdbc jdbc) {
//		try {
//			_jdbc = jdbc;
//			if (jdbc.GetSqlType() == PFSqlType.ClickHouse) {
////            	if(jdbc.getDriverClassName().equals("ru.yandex.clickhouse.ClickHouseDriver")) {
//				// a连接数据库
//				//conn = getClickHouseConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
//				conn = getClickHouseConnection(jdbc);
//			} else {
//				// a连接数据库
//				conn = getConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
//			}
//		} catch (Exception e) {
//			PFDataHelper.WriteError(new Throwable(),e);
//		}
//	}
	public SGClickHouseSqlExecute(ISGJdbc jdbc) throws Exception {
		super(jdbc);
	}
//	public PFClickHouseSqlExecute(IPFJdbc jdbc,boolean sendConnErrorMessage) throws Exception  {
//		super(jdbc, sendConnErrorMessage);
////		this.needSendConnErrorMessage=sendConnErrorMessage;
////		conn = getConnection(jdbc, sendConnErrorMessage);
////		_jdbc = jdbc;
//	}

	@Override
	public Connection getConnection(ISGJdbc jdbc) throws PFSqlConnOpenException {
//		try {
		// 这里不判断ClickHouse了，因为这样如果项目没引用，也可以正常使用PFSqlExecute
		if (jdbc.GetSqlType() == PFSqlType.ClickHouse) {
			return getClickHouseConnection(jdbc);
		} else {
			return super.getConnection(jdbc);
		}

	}
//	@Override
//	public Connection getConnection(IPFJdbc jdbc,boolean sendConnErrorMessage) throws Exception {
////		try {
//			// 这里不判断ClickHouse了，因为这样如果项目没引用，也可以正常使用PFSqlExecute
//			if (jdbc.GetSqlType() == PFSqlType.ClickHouse) {
//				return getClickHouseConnection(jdbc,sendConnErrorMessage);
//			} else  {
//				return super.getConnection(jdbc, sendConnErrorMessage);
//			}
////		}catch (java.sql.SQLException e) {// SQLSyntaxErrorException e) {
////			SetError(e);
////			PrintError();
////			setErrorMessageToDba(jdbc,sendConnErrorMessage);
////		}  catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		return null;
//	}

	/**
	 *
	 * @param jdbc
	 * @return
	 * @throws PFSqlConnOpenException
	 */
	public  Connection getClickHouseConnection(ISGJdbc jdbc) throws PFSqlConnOpenException {
//		ClickHouseDataSource dataSou = new ClickHouseDataSource(jdbc.getUrl());
//		return dataSou.getConnection(jdbc.getUsername(), jdbc.getPassword());
		try {
			if("com.github.housepower.jdbc.ClickHouseDriver".equals(jdbc.getDriverClassName())) {
//				//非官方
//				MycatMulitJdbcVersionTest.dynamicLoadClickHouseJdbcByVersion(jdbc.getDriverVersion(),
//						jdbc.getDriverClassName());
//				return DriverManager.getConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
				return PFSqlConnHelper.dynamicGetClickHouseConnByVersion(jdbc);
			}else{//官方
				 ru.yandex.clickhouse.ClickHouseDataSource dataSou = new  ru.yandex.clickhouse.ClickHouseDataSource(jdbc.getUrl());
				//ClickHouseDataSource dataSou = new  ClickHouseDataSource(jdbc.getUrl());
				return dataSou.getConnection(jdbc.getUsername(), jdbc.getPassword());

//				//实测这样也能加载出官方的class ru.yandex.clickhouse.ClickHouseConnectionImpl,只是不知道有没有其它问题
//				MycatMulitJdbcVersionTest.dynamicLoadClickHouseJdbcByVersion(jdbc.getDriverVersion(),
//						jdbc.getDriverClassName());
//				Connection connTmp=DriverManager.getConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
//				return connTmp;
			}
		} catch (java.sql.SQLException e) {// SQLSyntaxErrorException e) {
////			SetError(e);
////			PrintError();
//			PFDataHelper.WriteError(new Throwable(), e);
//			setErrorMessageToDba(jdbc);

			SetError(e);
			PrintError();
			setErrorMessageToDba(jdbc);
			//throw e;
		} catch (Exception e) {
//			// e.printStackTrace();
//			PFDataHelper.WriteError(new Throwable(), e);
//			//throw e;
			
			SetError(e);
			PrintError();
		}
		throw new PFSqlConnOpenException( this.GetErrorFullMessage());
	}


/*	*//**
	 * 
	 * @deprecated 使用 getClickHouseConnection(IPFJdbc jdbc,boolean
	 *             sendConnErrorMessage)
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 * @throws SQLException
	 *//*
	@Deprecated
	public static Connection getClickHouseConnection(String url, String user, String password) throws SQLException {
		ClickHouseDataSource dataSou = new ClickHouseDataSource(url);
		return dataSou.getConnection(user, password);
	}*/

//	public static Connection getConnection(String url, String user, String password) throws SQLException {
//		return DriverManager.getConnection(url, user, password);
//	}
	@Override
	protected boolean isSupportQueryTimeout(){
		//已知com.github.housepower.jdbc.ClickHouseDriver驱动不支持
		return false;
//		if(PFSqlType.ClickHouse==_jdbc.GetSqlType()){
//			//已知com.github.housepower.jdbc.ClickHouseDriver驱动不支持
//			return false;
//		}
//		return true;
	}

	public Boolean IsTableExist(String tableName) {
		return SGDataHelper.ObjectToBool0(QuerySingleValue("EXISTS " + tableName));
	}

//	public <T> List<T> QueryList(Class<T> tClass, String sql) {
//		// 建立Statement对象
//		try {
//			OpenConn();
//			stmt = conn.createStatement();
//			/**
//			 * Statement createStatement() 创建一个 Statement 对象来将 SQL 语句发送到数据库。
//			 */
//			// 执行数据库查询语句
//			rs = stmt.executeQuery(sql);
//
//			List<T> result = new ArrayList<T>();
//			ResultSetMetaData md = rs.getMetaData();
//
//			int count = md.getColumnCount();
//			// 1.获取字段
//			// 1.1 获取所有字段 -- 字段数组
//			// 可以获取公用和私有的所有字段，但不能获取父类字段
//			Field[] fields = tClass.getDeclaredFields();
//			List<Field> orderFields = new ArrayList<Field>();
//			for (int i = 1; i <= count; i++) {
//				for (Field field : fields) {
//					// if(field.getName().equals(md.getColumnName(i))){
//					if (field.getName().equals(md.getColumnLabel(i))) {
//						field.setAccessible(true);
//						orderFields.add(field);
//					}
//				}
//			}
//			// result.add(String.valueOf(orderFields.size()));
//			while (rs.next()) {
//				// Object item=clazz.newInstance();
//				T item = tClass.getDeclaredConstructor().newInstance();
//				for (Field field : orderFields) {
//					String columnName = field.getName();
//					// field.set(item, rs.getObject(columnName));
//					Object o = rs.getObject(columnName);
//					if (o != null) {
//						field.set(item, o);
//					}
//
//					// Type typeName=field.getGenericType();
//					// if(typeName.getTypeName()=="int"){
//					// field.set(item, rs.getInt(columnName));
//					// }else{
//					// field.set(item, rs.getString(columnName));
//					// }
//				}
//				result.add(item);
//
//			}
//			if (rs != null) {
//				rs.close();
//				rs = null;
//			}
//			if (stmt != null) {
//				stmt.close();
//				stmt = null;
//			}
//			if (conn != null) {
//				conn.close();
//				conn = null;
//			}
//			return result;
//		} catch (Exception e) {
//			// Error=e;
//			SetError(e, sql);
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

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
	public SGDataTable GetDataTable(String sql, PFSqlParameter[] p) {
		// Connection conn = DB.createConn();
		// PreparedStatement ps = DB.prepare(conn, sql);
		PreparedStatement ps = null;
		OpenConn();
		if (p != null) {
			try {
				ps = conn.prepareStatement(sql);
			} catch (SQLException e1) {
				return null;
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}
		}
		SGDataTable t = null;
		try {
			ResultSet rs = null;
			if (p != null) {
				AddSqlParameter(ps, p);
				rs = ps.executeQuery();
			} else {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
			}
//            ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			List<PFDataRow> row = new ArrayList<PFDataRow>();// 表所有行集合
			List<PFDataColumn> col = null;// 行所有列集合
			PFDataRow r = null; // 单独一行
			PFDataColumn c = null;// 单独一列
			// 此处开始循环读数据，每次往表格中插入一行记录
			while (rs.next()) {
				// 初始化行集合，

				// 初始化列集合
				col = new ArrayList<PFDataColumn>();
				// 此处开始列循环，每次向一行对象插入一列
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// String columnName = rsmd.getColumnName(i);
					String columnName = rsmd.getColumnLabel(i);
					Object value = rs.getObject(columnName);
					// 初始化单元列
					c = new PFDataColumn(columnName, value);
					// 将列信息加入列集合
					col.add(c);
				}
				// 初始化单元行
				r = new PFDataRow(col);
				// 将行信息降入行结合
				row.add(r);
			}
			// 得到数据表
			t = new SGDataTable(row);
			rs.close();
			rs = null;
		} catch (SQLException e) {
			// Error=e;
			SetError(e, sql);
			e.printStackTrace();
		} finally {
			try {
				if (p != null) {
					ps.close();
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// DB.close(ps);
			// DB.close(conn);
		}
		return t;
	}

	// 为了和旧方法名相同
	public SGDataTable GetDataTable(String sql) {
		return GetDataTable(sql, null);
	}

	/// <summary>
	/// 查询返回单个结果
	/// </summary>
	/// <param name="sqlval">sql字符串</param>
	/// <returns>返回结果</returns>
	public Object QuerySingleValue(String sqlval) {
		SGDataTable t = GetDataTable(sqlval, null);
		if (t != null && (!t.IsEmpty())) {
			return t.getRow().get(0).getCol().get(0).getValue();
		}
		return null;

	}

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
		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
		int affected = ExecuteSqlInt(new SGSqlCommandString(updateSql), null);
		if (affected == -1) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean Delete(String tableName, Consumer<SGSqlWhereCollection> whereAction) {
		SGSqlWhereCollection where = getWhereCollection();
		if (whereAction != null) {
			whereAction.accept(where);
		}
//		return ExecuteSql(new PFSqlCommandString(PFDataHelper.FormatString("ALTER TABLE  {0} delete {1}", tableName,
//				where == null ? "" : where.ToSql())));
		return ExecuteSql(new SGSqlCommandString(
				SGDataHelper.FormatString("ALTER TABLE  {0}  ON CLUSTER default_cluster delete {1}", tableName,
						where == null ? "" : (where.size() < 1 ? "where 1=1" : where.ToSql()) // 注意clickhouse删除语句一定要有where条件
				)));
	}

	@Override
	public Boolean HugeDelete(String tableName, Consumer<SGSqlWhereCollection> whereAction) {
		// TODO Auto-generated method stub
		return Delete(tableName, whereAction);
	}

/// tidb的事务有限制,要分批更新,所以要如下使用(要求必需有has_updated字段辅助更新)
/// 
/// 用法1:(重点是where is_new_hy判断更新完毕)
/// update monthly_hyzl set is_new_hy=(case when accmon=date_format(create_date, '%Y.%m') then CONVERT(bit,1) else CONVERT(bit,0) end)
/// where is_new_hy is null
/// 
/// 用法2:(重点是has_updated的设置,更新前先设置为0)
///sqlExec.TidbHugeUpdate(string.Format(@"
///update monthly_hyzl a
///inner join monthly_hyzl as lm on xxx
///set xxx,
///    a.has_updated=1
///where xxx
///and a.has_updated=0
///"),
///string.Format(@"
///update monthly_hyzl set has_updated=0
///where xxx
///and has_updated =1
///")
///);
	/**
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
	@Override
	public Boolean HugeUpdate(String updateSql, String... resetHasUpdatedFieldSqls) {
		// if (updateSql.IndexOf("has_updated") < 0) { return false; }//自行用其它字段控制也行的

		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
		int batch = 500000;
		updateSql = SGDataHelper.FormatString("{0} " + "limit {1} ", updateSql, batch);

		int affected = 1;

		if (resetHasUpdatedFieldSqls != null) {
			for (String i : resetHasUpdatedFieldSqls) {
				String resetHasUpdatedFieldSql = SGDataHelper.FormatString("{0} " + "limit {1} ", i, batch);

				affected = 1;
				while (affected > 0) {
					affected = ExecuteSqlInt(new SGSqlCommandString(resetHasUpdatedFieldSql), null);
					if (affected == -1) {
						//PFDataHelper.WriteError(new Throwable(), GetError());
						SGDataHelper.WriteError( GetError());
						return false;
					}
				}
			}
		}

		affected = 1;
		while (affected > 0) {
			affected = ExecuteSqlInt(new SGSqlCommandString(updateSql), null);
			if (affected == -1) {
				//PFDataHelper.WriteError(new Throwable(), GetError());
				SGDataHelper.WriteError( GetError());
				return false;
			}
		}
		return true;
	}

	@Override
	public ResultSet GetDataReader(String sql) {
		// 建立Statement对象
		try {
			OpenConn();
			stmt = conn.createStatement();
			/**
			 * Statement createStatement() 创建一个 Statement 对象来将 SQL 语句发送到数据库。
			 */
			// 执行数据库查询语句
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	/*
	 * 执行语句(未测试)
	 */
	public int ExecuteSqlInt(SGSqlCommandString sql, PFSqlParameter[] p)// , Boolean autoClose )
	{
		// Boolean autoClose=true;

		PreparedStatement ps = null;
		// PFDataTable t = null;
		int rs = -1;
		try {
			OpenConn();
			if (p != null) {
				ps = conn.prepareStatement(sql.toString());
				AddSqlParameter(ps, p);
				ps.setQueryTimeout(CommandTimeOut);
				rs = ps.executeUpdate();
			} else {
				stmt = conn.createStatement();
				stmt.setQueryTimeout(CommandTimeOut);
				// if(sql.indexOf("exec")>-1) {
				if (sql.size() > 1) {

//					for (int i = 0; i < sql.size(); i++) {
//						stmt.addBatch(sql.get(i));
//					}
//					stmt.executeBatch();//clickhouse这种方式不生效

					for (int i = 0; i < sql.size(); i++) {
						stmt.execute(sql.get(i));
					}
					rs = 1;
				} else {
					String oneSql = sql.get(0);
					if (oneSql.indexOf("exec") > -1) {
						stmt.execute(oneSql);
						rs = 1;
					} else {
						rs = stmt.executeUpdate(oneSql);
					}
				}
//                	if(b) {rs=1;}
				// rs=1;
//                }else {
//                	//这种方式可以执行insert upudate等;但如果执行exec存储过程返回select结果就会报错--benjamin20201224
////                	for(String i :sql) {
////                        rs = stmt.executeUpdate(i);
////                	}
//                	for(int i=0;i<sql.size();i++) {
//            			rs=stmt.executeUpdate(sql.get(i));
//                	}
//                }
			}
		} catch (SQLException e) {
			// Error = e;
			// SetError(e);
			SetError(e, sql.toString());
			PrintError();
			// PrintError();
			// e.printStackTrace();
			// GetError().printStackTrace();
		} finally {
			try {
				if (p != null) {
					ps.close();
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				CloseConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rs;
	}

//	@Override
//	public boolean OpenConn() {
//		boolean b=false;
//		try {
//			if (this.conn == null || this.conn.isClosed()) {
//				this.conn = getClickHouseConnection(_jdbc,this.needSendConnErrorMessage);
//				b=conn!=null&&(!conn.isClosed());
//				// SqlConnCounter.Add(_sqlconnection.ConnectionString);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return b;
//	}

//@Override
//public void CloseConn() {
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
//    ms = null;
//    var b = this.ExecuteSql(models.ToSql());
//    if (!b)
//    {
//        ms = ErrorFullMessage;
//    }
//    return b;
//	return null;
//}
	@Override
	public Boolean ExecuteSql(SGSqlCommandString sqlstr) {
		return ExecuteSqlInt(sqlstr, null) > 0;
	}

	public Boolean HugeInsertReader(SGSqlInsertCollection insert, ResultSet rdr, String tableName,
			// Consumer<BatchInsertOption> insertOptionAction,// = null,
			Consumer<BaseSqlUpdateCollection> rowAction, // = null,
			Consumer<Integer> sqlRowsCopiedAction, // = null,
			Predicate<Boolean> stopAction// = null//,
	// String[] primaryKeys=null//如果不为空,插入时根据主键删除原有数据
	) {
		if (insert == null) {
			// insert = new SqlInsertCollection
			// {
			// FieldQuotationCharacterL = "`",
			// FieldQuotationCharacterR = "`"
			// };
			insert = new PFMySqlInsertCollection();

			try {
				ResultSetMetaData md = rdr.getMetaData();

				for (int i = 0; i < md.getColumnCount(); i++) {
					// String fieldName = md.getColumnName(i+1);
					String fieldName = md.getColumnLabel(i + 1);

					SqlUpdateItem updateItem = new SqlUpdateItem();
					updateItem.Key = fieldName;
					updateItem.VType = SGDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
					insert.Add(updateItem);

				}
			} catch (Exception e) {

			}

		}

//    BatchInsertOption insertOption = new BatchInsertOption();
//    if (insertOptionAction != null) { insertOptionAction.accept(insertOption); }
		BatchInsertOption insertOption = GetInsertOption();

		OpenConn();

		StringBuilder sb = new StringBuilder();

		int idx = 0;

		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
		int batchCnt = 0;

		Boolean hasUnDo = false;

		//// SetHugeCommandTimeOut();
		// SetNormalTransferCommandTimeOutIfNotHuge();
		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

		int oneThousandCount = 0;
		try {
			while (rdr.next()) {
				insert.UpdateByDataReader(rdr);
				if (rowAction != null) {
					rowAction.accept(insert);
				}

				if (oneThousandCount == 0) {
					sb.append(SGDataHelper.FormatString(" insert into {0}({1}) values({2})", tableName,
							insert.ToKeysSql(), insert.ToValuesSql()));
				} else {
					sb.append(SGDataHelper.FormatString(",({0})", insert.ToValuesSql()));
				}

				hasUnDo = true;
				if (batchCnt > batchSize) {
					// if (deleteBeforeInsert)
					// {
					// ExecuteSql(deleteSb.ToString(), false);
					// }
					Boolean b = ExecuteSql(new SGSqlCommandString(sb.toString()));
					if (!b) {
						// CloseReader(rdr);
						CloseConn();
						return false;
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
//            sb.Clear();
					sb = new StringBuilder();
					// deleteSb.Clear();

					hasUnDo = false;
					batchCnt = 0;
					oneThousandCount = 0;
				} else {
					batchCnt++;
					oneThousandCount++;
				}
				idx++;

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (hasUnDo) {
			// if (deleteBeforeInsert)
			// {
			// ExecuteSql(deleteSb.ToString(), false);
			// }
			Boolean b = ExecuteSql(new SGSqlCommandString(sb.toString()));
			if (!b) {
				// CloseReader(rdr);
				CloseConn();
				return false;
			}
		} else {
			// CloseReader(rdr);
			CloseConn();
		}

		return true;
	}

	public Boolean HugeUpdateReader(PFSqlUpdateCollection update, ResultSet rdr, String tableName,
			Consumer<BatchInsertOption> insertOptionAction,
			// Func<MySqlUpdateCollection, DbDataReader,bool> rowAction,
			SGFunc<BaseSqlUpdateCollection, ResultSet, ?, Boolean> rowAction, Consumer<Integer> sqlRowsUpdatedAction) {

		BatchInsertOption insertOption = new BatchInsertOption();
		if (insertOptionAction != null) {
			insertOptionAction.accept(insertOption);
		}

		StringBuilder sb = new StringBuilder();
		int idx = 0;
		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
		int batchCnt = 0;
		Boolean hasUnDo = false;
		// var b = false;
		try {
			while (rdr.next()) {
				if (insertOption.getAutoUpdateModel()) {
					update.UpdateByDataReader(rdr);
				}
				if (rowAction != null) {
					if (!rowAction.go(update, rdr, null)) {
						continue;
					}
				}

				sb.append(SGDataHelper.FormatString(" ALTER TABLE {0} UPDATE  {1} {2};", tableName, update.ToSetSql(),
						update.ToWhereSql()));

				hasUnDo = true;
				if (batchCnt > batchSize) {
					// b = insertOption.UserTransition
					// ? ExecuteTransactSql(sb.ToString())
					// : ExecuteSql(sb.ToString());
					// if (!b)
					if (!ExecuteSql(new SGSqlCommandString(sb.toString()))) {
						CloseReader(rdr);
						return false;
					}
					if (sqlRowsUpdatedAction != null) {
						sqlRowsUpdatedAction.accept(idx);
					}
					batchCnt = 0;

//            sb.Clear();
					sb = new StringBuilder();
					hasUnDo = false;
				} else {
					batchCnt++;
				}
				idx++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// rdr.Close();
		CloseReader(rdr);
		if (hasUnDo) {
			// b = insertOption.UserTransition
			// ? ExecuteTransactSql(sb.ToString())
			// : ExecuteSql(sb.ToString());
			// if (!b)
			if (!ExecuteSql(new SGSqlCommandString(sb.toString()))) {
				return false;
			}
		}
		CloseConn();
		return true;// benjamin
	}

//@Override
//public SqlWhereCollection getWhereCollection() {
//    return new PFClickHouseWhereCollection();
//}

	@Override
	public Boolean CloseReader(ResultSet reader) {
		try {
			// if(reader.isClosed()) {return true;}
			stmt.cancel();
			// sqlCmd.Cancel();//如果没有这句,数据很多时 dr.Close 会很慢
			// https://www.cnblogs.com/xyz0835/p/3379676.html
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return true;
	}

	@Override
	public Boolean PreValidTransferReader(ResultSet rdr, String tableName,
			Function<BaseSqlUpdateCollection, List<SGDataTableFieldValidModel>> PreValidAction,
			SGRef<List<SGDataTableFieldValidModel>> validRef, Consumer<Integer> alreadyAction,
			Predicate<Boolean> stopAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean PreValidTransferTable(SGSqlTransferItem transferItem,
			SGRef<List<SGDataTableFieldValidModel>> validRef, Consumer<Integer> alreadyAction,
			Predicate<Boolean> stopAction) {
		return null;
	}

	@Override
	public PreparedStatement GetPs(String tableName) {
		try {
			OpenConn();

			List<SGSqlFieldInfo> fields = this.GetTableFields(tableName);
			String[] commaList = new String[fields.size()];
			Arrays.fill(commaList, "?");
			// PreparedStatement crs;
			List<String> fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
			OpenConn();
			PreparedStatement crs = (PreparedStatement) conn
					.prepareStatement(SGDataHelper.FormatString("insert into {0}  ({1})  values ({2})", tableName,
							String.join(",", fieldNames), String.join(",", commaList)));
			conn.setAutoCommit(false);
			return crs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SetError(e);
		}
		return null;
	}

	// 移到PFSqlExecuteBase
//	public ResultSetMetaData GetMetaData(String tableName) {
//		try {
//			OpenConn();
//
//			List<SGSqlFieldInfo> fields = this.GetTableFields(tableName);
//			String[] commaList = new String[fields.size()];
//			Arrays.fill(commaList, "?");
//			// PreparedStatement crs;
//			List<String> fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
//			OpenConn();
//////ResultSet rs =GetDataReader("select * from "+ tableName+" where 1=0");
//////ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where 1=0");
//////ResultSet rs =GetDataReaderUpdatable("select Orderno from "+ tableName+" where 1=0");//ok
////ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where 1=0");    //ok 但主键等标识列用来更新会有问题
////		ResultSet rs = GetDataReader(PFDataHelper.FormatString("select {0} from {1} ",String.join(",",fieldNames), tableName));
//			ResultSet rs = GetDataReader(PFDataHelper.FormatString("select {0} from {1} where 1=0",
//					String.join(",", fieldNames), tableName));
//
////			ResultSet rs = GetDataReader(PFDataHelper.FormatString("select * from {0} where 1=0",
////					tableName));
//			return rs.getMetaData();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			SetError(e);
//		}
//		return null;
//	}

	public PreparedStatement insertIntoCachedRowSet(PreparedStatement crs, ResultSetMetaData md,
			SGSqlInsertCollection dstInsert
	// ,Map<String,Integer> srcColumnType
	// ,Map<String,Integer> dstColumnType
	) throws SQLException {
		// 移动指针到“插入行”，插入行是一个虚拟行
		// crs.moveToInsertRow();

		// 更新虚拟行的数据
		// crs.updateString("name", stu.getName());
		// crs.updateInt("id", stu.getId());

		// Iterator<Entry<String, SqlUpdateItem>> iter =
		// dstInsert.entrySet().iterator();

		// String[] fields=new String[] {
//			   "Hybh","Hyxm",
//			   "sfzh","Qx",
//			   "Pexm",//是这列的问题 colid 20?
//			   "Pesfzh","Tel1"
//			   //,"Tel2","Bankzh",
//			   //"Bankname","cmonth","bjhybh","agentno","hyxb","qxmonth","province","city","county","address",
//			   //"fbirth","fjoindate","BankSf","BankCity","BankPS","BankNo"
		// };
		// List<String> fieldList=Arrays.asList(fields);

		// ResultSetMetaData md = crs.getMetaData();

		for (int i = 0; i < md.getColumnCount(); i++) {
			int mdIdx = i + 1;
			String colName = md.getColumnLabel(mdIdx);
			// System.out.println(String.valueOf(i)+"__"+colName);
			// if(fieldList.contains(colName)) {

			if (dstInsert.containsKey(colName)) {
				int dataT = md.getColumnType(mdIdx);

				if (java.sql.Types.DECIMAL == dataT) {// 如果直接updateObject,云徒订单的Totalmoney字段会有100倍溢出的问题--benjamin20210112
					Object v = dstInsert.get(colName).Value;
					if (v != null) {
						BigDecimal vD = SGDataHelper.<BigDecimal>ObjectAs(
								SGDataHelper.ConvertObjectToSqlTypeByPFType(v, dstInsert.get(colName).getDstDataPFType(), dataT));
						vD = vD.setScale(md.getScale(mdIdx), BigDecimal.ROUND_HALF_UP);
						// crs.updateBigDecimal(colName, vD);
						crs.setBigDecimal(mdIdx, vD);
					} else {
						crs.setObject(mdIdx, null);
					}
				} else if (java.sql.Types.TIMESTAMP == dataT) {

					String dataTName = md.getColumnTypeName(mdIdx);
////						crs.updateTimestamp(colName, PFDataHelper.ObjectAs(PFDataHelper.ConvertObjectToSqlTypeByPFType(
////								dstInsert.get(colName).Value, dstInsert.get(colName).VPFType, dataT, dataTName)));
//					crs.setTimestamp(mdIdx, PFDataHelper.ObjectAs(PFDataHelper.ConvertObjectToSqlTypeByPFType(
//							dstInsert.get(colName).Value, dstInsert.get(colName).VPFType, dataT, dataTName)));
					Object v = SGDataHelper.ConvertObjectToSqlTypeByPFType(dstInsert.get(colName).Value,
							dstInsert.get(colName).getDstDataPFType(), dataT, dataTName);
					if (v != null) {
						crs.setTimestamp(mdIdx, SGDataHelper.ObjectAs(v));
					} else {
						crs.setObject(mdIdx, null);
					}
				}

				else {
//						crs.updateObject(colName, PFDataHelper.ConvertObjectToSqlTypeByPFType(dstInsert.get(colName).Value,
//								dstInsert.get(colName).VPFType, dataT));
					crs.setObject(mdIdx, SGDataHelper.ConvertObjectToSqlTypeByPFType(dstInsert.get(colName).Value,
							dstInsert.get(colName).getDstDataPFType(), dataT));
				}

			}
			// }
		}

//			// 插入虚拟行
//			crs.insertRow();
//			// 移动指针到当前行
//			crs.moveToCurrentRow();
		crs.addBatch();// java.sql.SQLException: Not all parameters binded (placeholder 3 is undefined)
						// clickhouse每列都要设置值?
		return crs;
	}

	private Boolean DoBulkReader(SGSqlInsertCollection insert,
			// CachedRowSetImpl crs,
			// ResultSet crs,
			PreparedStatement crs, String tableName, Boolean transferOneByOne// 只有当一条一条导入时,bulk的错误才能确定是insert里的值
	) {

		try {
			crs.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Boolean HugeBulkReader(SGSqlInsertCollection dstInsert, ResultSet rdr, String tableName,
			//Consumer<BaseSqlUpdateCollection> rowAction,
			SGAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
			Consumer<Integer> sqlRowsCopiedAction,
			Predicate<Boolean> stopAction) {

		// 先参考mysql的方式看行不行
		// TODO Auto-generated method stub
		if (rdr == null) {
			SetError(new Exception("DataReader不能为空"));
			return false;
		}

		BatchInsertOption insertOption = GetInsertOption();

		OpenConn();

		int idx = 0;
		insertedCnt = 0;// 已插入的行数

		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
		int batchCnt = 0;

		Boolean hasUnDo = false;

		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

		// int oneThousandCount = 0;
		try {
//		//保存来源的类型，便于转换
			ResultSetMetaData srcMd = rdr.getMetaData();

			boolean hasSysLimitId = false;
			String sysLimitIdFieldName = "sys_limit_id";
			long currentSysLimitId = -1;
			lastInsertedId = -1;

			for (int i = 0; i < srcMd.getColumnCount(); i++) {
				String fieldName = srcMd.getColumnLabel(i + 1);
				if (sysLimitIdFieldName.equals(fieldName)) {// 这个内部值用来代替limit的offset
					hasSysLimitId = true;
					continue;
				}
			}

			//// CachedRowSet crs = GetCrs(tableName);
			// ResultSet crs = GetRs(tableName);
			// ResultSetMetaData dstMd = crs.getMetaData();
			@SuppressWarnings("deprecation")
			ResultSetMetaData dstMd = GetMetaData(tableName);

//			List<SGSqlFieldInfo> fields=this.GetTableFields(tableName);
//			String[] commaList=new String[fields.size()];
//			Arrays.fill(commaList,"?");
//		     PreparedStatement crs;
//		     List<String> fieldNames=fields.stream().map(a->a.getFieldName()).collect(Collectors.toList());
//		     crs = (PreparedStatement) conn.prepareStatement(
//		    		 PFDataHelper.FormatString("insert into {0}  ({1})  values ({2})", tableName,String.join(",",fieldNames),String.join(",",commaList))
//		    		 );
//		     conn.setAutoCommit(false);

			PreparedStatement ps = GetPs(tableName);

			// 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
			if (dstInsert == null) {

				dstInsert = getInsertCollection(dstMd);
			}

			while (true) {
				boolean hasNext = rdr.next();
				if (hasNext) {

					dstInsert.UpdateByDataReaderAutoConvert(rdr);
					if (rowAction != null) {
						//rowAction.accept(dstInsert);
						rowAction.go(dstInsert,idx,null);
					}

					ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);

					batchCnt++;
					hasUnDo = true;
					if (hasSysLimitId) {
						currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
					}
				}
//		    if (batchCnt > batchSize)
				if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {

					// Boolean b = ExecuteSql(sb.toString());
					Boolean b = DoBulkReader(dstInsert, ps, tableName, batchSize == 1);

					if (!b) {
						// CloseReader(rdr);
						CloseConn();
						return false;
					} else {
						insertedCnt = idx + 1;
						if (hasSysLimitId) {
							lastInsertedId = currentSysLimitId;
						}
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

					// crs = GetCrs(tableName);
					ps = GetPs(tableName);
//					crs = GetRs(tableName);

					hasUnDo = false;
					batchCnt = 0;
					// oneThousandCount = 0;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			SetError(e);
			return false;
		}

		CloseConn();
		return true;
	}

	@Override
	public ResultSet GetDataReaderUpdatable(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SGSqlFieldInfo> GetTableFields(String tableName) {
		return QueryList(SGSqlFieldInfo.class, SGDataHelper.FormatString("SELECT \r\n" + "     `position` fieldIdx,\r\n"
				+ "      name as fieldName , -- 列名,  \r\n" + "      is_in_primary_key as isPrimaryKey,\r\n"
				+ "       -- COLUMN_TYPE 数据类型,  \r\n" + "        `type` as fieldType, -- 字段类型,  \r\n"
				+ "      -- CHARACTER_MAXIMUM_LENGTH fieldSqlLength, -- 长度,  \r\n" + "      -- IS_NULLABLE 是否为空,  \r\n"
				+ "      -- if(IS_NULLABLE='YES',b'0',b'1')  isRequired,\r\n"
				+ "      -- COLUMN_DEFAULT defaultValue, -- 默认值,  \r\n"
				+ "      comment as columnDescription -- 备注   \r\n" + "FROM `system`.columns\r\n"
				+ "WHERE `table` = '{0}'", tableName));
//		// mysql是通过INFORMATION_SCHEMA.COLUMNS 系统表获得列信息的,clickhouse不用这个方法了
//		//以后还是改为mysql这种方式吧,因为meta里面没有主键信息的
//		ResultSetMetaData md = GetMetaData(tableName);
//		List<SGSqlFieldInfo> r = new ArrayList<SGSqlFieldInfo>();
//
//		try {
//			for (int i = 0; i < md.getColumnCount(); i++) {
//				try {
//					// String fieldName = md.getColumnName(i+1);
//					String fieldName = md.getColumnLabel(i + 1);
//					SGSqlFieldInfo updateItem = new SGSqlFieldInfo();
//					updateItem.setFieldName(fieldName);
//					updateItem.setFieldType(md.getColumnTypeName(i + 1));
//					// PFDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
//					r.add(updateItem);
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return r;
	}

	@Override
	public ResultSet GetHugeDataReader(String sql) {
		// 建立Statement对象
		try {
			OpenConn();
//			// stmt = conn.createStatement();
//			// 采用流模式处理JDBC大结果集 https://www.gbase8.cn/1884
			//stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);//clickhouse不支持
			stmt = conn.createStatement();
//			// stmt.setFetchSize(Integer.MIN_VALUE);//mysql支持这句，但sqlserver要求值大于0

			//stmt.setQueryTimeout(CommandTimeOut);//clickhouse不支持
			/**
			 * Statement createStatement() 创建一个 Statement 对象来将 SQL 语句发送到数据库。
			 */
			// 执行数据库查询语句
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			SetError(e);
//			e.printStackTrace();
			rs = null;
		}
		return rs;
	}

	@Override
	public Boolean TruncateTable(String tableName) {
		// https://clickhouse.tech/docs/en/sql-reference/statements/truncate/
		return null;
	}
	@Override
	public List<SGSqlFieldInfo> GetTableEditableFields(String tableName){//,SGRef<ResultSetMetaData> mdRef) {
		List<SGSqlFieldInfo> r = GetTableFields(tableName);
		ResultSetMetaData md = this.GetMetaData(tableName, SGDataHelper.ListSelect(r, a -> a.getFieldName()));
//		if(null==md) {//这个md和r的列不对应,用类属性也是没用的
//			md = this.GetMetaData(tableName, PFDataHelper.ListSelect(r, a -> a.getFieldName()));
//		}
		if (null == md) {
			return null;
		}
		//mdRef.SetValue(md);
		try {
			for (int i = 0; i < md.getColumnCount(); i++) {
				int mdIdx = i + 1;
				String colName = md.getColumnLabel(mdIdx);
				if (!md.isAutoIncrement(mdIdx)
					//&& !md.isReadOnly(mdIdx)  //注意,clickhouse所有列都是只读的,所以暂去掉这个判断-- benjamin 20230215
				) { // 不是自增列才插入,否则sql会报错-- benjamin
				} else {
					r.removeIf(a -> a.getFieldName().equals(colName));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	@Override
	public SGSqlInsertCollection getInsertCollection(ResultSetMetaData dstMd) {
		SGSqlInsertCollection dstInsert = getInsertCollection();

		// 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
		try {
			for (int i = 0; i < dstMd.getColumnCount(); i++) {
				try {
					int mdIdx = i + 1;
					if (!dstMd.isAutoIncrement(mdIdx)
						//&& !dstMd.isReadOnly(mdIdx)//clickhouse全部列都是只读的,原因不明
					 ) { // 不是自增列才插入,否则sql会报错-- benjamin
						// 20220902
						String fieldName = dstMd.getColumnLabel(i + 1);
						SqlUpdateItem updateItem = new SqlUpdateItem();
						updateItem.Key = fieldName;
////                        updateItem.VPFType =
////                                PFDataHelper.GetPFTypeBySqlType(dstMd.getColumnType(i + 1));
//                        updateItem.setSrcDataPFType(
//                                PFDataHelper.GetPFTypeBySqlType2(dstMd.getColumnType(mdIdx)));
						updateItem.setDstDataType(dstMd.getColumnType(mdIdx));
						updateItem.setDstDataTypeName(dstMd.getColumnTypeName(mdIdx));
						updateItem.setDstDataPFType(SGDataHelper.GetPFTypeBySqlType2(dstMd.getColumnType(mdIdx),dstMd.getColumnTypeName(mdIdx)));
						dstInsert.Add(updateItem);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dstInsert;
	}
}
