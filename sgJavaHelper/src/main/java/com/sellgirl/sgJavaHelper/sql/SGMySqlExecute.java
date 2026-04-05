package com.sellgirl.sgJavaHelper.sql;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class SGMySqlExecute extends SGSqlExecuteBase {// PFSqlExecute {
    private static final Logger LOGGER = LoggerFactory.getLogger(SGMySqlExecute.class);

    public SGMySqlExecute(ISGJdbc jdbc) throws Exception {
        super(jdbc);
        insertOpt.setProcessBatch(5000);
        queryOpt.setFetchSize(100);
        //尝试获得数据数时区
        try {
            //PFDataTable dt=this.GetDataTable("show VARIABLES like 'time_zone';");
            SGDataTable dt = this.GetDataTable("show VARIABLES like 'time_zone';", null, false);
            if (null != dt && !dt.IsEmpty()) {
                String tz = dt.Rows.get(0).getCol().get(1).getValue().toString();
                //LOGGER.info(tz);
                if (null != tz) {
                    if (tz.equals("SYSTEM")) {
                        //tidb的sale库的时区
                        //system_time_zone	UTC
                        //time_zone	SYSTEM
                    } else {
                        //mysql的时区通常是这样
                        // system_time_zone	Asia
                        //time_zone	+08:00
                        zoneId = ZoneId.of(tz);//"Europe/Rome");
                    }
                }
                //LOGGER.info(zoneId.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //	public PFMySqlExecute(IPFJdbc jdbc,boolean sendConnErrorMessage) throws Exception  {
//		super(jdbc, sendConnErrorMessage);
//	}
//	/**
//	 * 用于子类复写
//	 *
//	 * @return
//	 */
//	@Override
//	protected BatchInsertOption DefaultInsertOption() {
//		BatchInsertOption o = new BatchInsertOption();
//		//速度
//		//batch为1000时  1000条/5秒
//		//batch为50000时 50000条/3615秒->1000条/72.3 变慢了
//		o.setProcessBatch(5000);//1000);  50000
//		return o;
//	}
//    protected BatchDeleteOption DefaultDeleteOption() {
//        BatchDeleteOption o = new BatchDeleteOption();
//        o.setProcessBatch(5000);
//        return o;
//    }
//	@Override
//	protected BatchQueryOption DefaultQueryOption() {
//		BatchQueryOption o = new BatchQueryOption();
//		o.setFetchSize(100);
//		return o;
//	}

    @Override
    public Boolean IsTableExist(String tableName) {
        SGDataTable dt = GetDataTable("SHOW TABLES like \"" + tableName + "\"", null);
        return !dt.IsEmpty();
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

//			//mysql大量数据时,这样虽然不会超时,但后面next的速度挺慢的
//			//rows:9.41E+003  --  speed:2.76E+002条/秒
            //stmt.setFetchSize(Integer.MIN_VALUE);//在调度器运行时报错Can not read response from server. Expected to read 1,011 bytes, read 681 bytes before connection was unexpectedly lost.

//			//rows:5.61E+005  --  speed:2.89E+004条/秒
//			stmt.setFetchSize(10000);

            ////rows:5.61E+005  --  speed:2.88E+004条/秒
            //stmt.setFetchSize(1000);

//			//rows:5.61E+005  --  speed:2.80E+004条/秒
//			stmt.setFetchSize(100);

            //stmt.setFetchSize(this.GetQueryOption().getFetchSize());
            stmt.setFetchSize(this.GetQueryOption().getHugeFetchSize());

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
//
//	@Override
//	public Boolean HugeDelete(String updateSql) {
//		//int batch = 500000;// 1000000还是报错:MySql.Data.MySqlClient.MySqlException (0x80004005): Transaction
//							// is too large, size: 104857600
//		int batch =this.GetDeleteOption().getProcessBatch();
//		Consumer<Integer> deletedAction=this.GetDeleteOption().getDoneNumAction();
//		boolean hasDoneAction=null!=deletedAction;
//
//		updateSql = PFDataHelper.FormatString("{0}\r\n" + "limit {1}", updateSql, batch);
//		int affected = 1;
//		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
//		int deletedNum=0;
//		while (affected > 0) {
//			affected = ExecuteSqlInt(new PFSqlCommandString(updateSql), null);
//			if(hasDoneAction&&affected>0) {
//				deletedNum+=affected;
//				deletedAction.accept(deletedNum);
//			}
//		}
//		if (affected == -1) {
//			return false;
//		} else if (affected < 1) {
//			return true;
//		}
//		return true;
//////		String sql = PFDataHelper.FormatString(
//////				"SET ROWCOUNT 10000;\r\n" + "declare @rc int\r\n" + "\r\n" + "WHILE 1 = 1\r\n" + "BEGIN\r\n"
//////						+ "    begin tran t1\r\n" + "    {0} \r\n"
//////						+ "    set @rc=@@ROWCOUNT  --commic后,@@rowcount为0\r\n" + "    commit tran t1\r\n"
//////						+ "    IF @rc = 0\r\n" + "        BREAK;\r\n" + "END\r\n" + "\r\n" + "SET ROWCOUNT 0;",
//////				updateSql);
////
////		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge);
////		// return ExecuteTransactSql(sql);
////		return ExecuteSql( new PFSqlCommandString(updateSql));
//	}


    @Override
    public List<SGSqlFieldInfo> GetTableFields(String tableName//,String dbName
    ) {
        // String sql=PFDataHelper.FormatString("DESC {0}",tableName);
        SGSqlWhereCollection where = getWhereCollection();
        where.Add("TABLE_NAME", tableName);
        where.Add("TABLE_SCHEMA", _jdbc.getDbName());
        //where.Add("TABLE_SCHEMA",dbName);

        String sql = SGDataHelper.FormatString("    SELECT  \r\n" + "     ORDINAL_POSITION fieldIdx,\r\n" + "      COLUMN_NAME fieldName, -- 列名,  \r\n" + "      if(COLUMN_KEY='PRI',b'1',b'0')  isPrimaryKey,\r\n" + "       COLUMN_TYPE 数据类型,  \r\n" + "        DATA_TYPE fieldType, -- 字段类型,  \r\n"
                //+ "      CHARACTER_MAXIMUM_LENGTH fieldSqlLength, -- 长度,  \r\n" + "      -- IS_NULLABLE 是否为空,  \r\n"
                + "     (case   \r\n" + "		  when DATA_TYPE='decimal' or DATA_TYPE='bigint' or DATA_TYPE='smallint' then NUMERIC_PRECISION\r\n" 
                +(null!=this._jdbc.getDriverVersion()&&!"".equals(this._jdbc.getDriverVersion())
                &&SGDataHelper.compareVersion("5.1.47", this._jdbc.getDriverVersion())>0? "":"		  when DATA_TYPE='datetime' then DATETIME_PRECISION\r\n") //旧版本没有DATETIME_PRECISION字段 --benjamin 20250208 
                + "		  else CHARACTER_MAXIMUM_LENGTH\r\n" + "		end)  fieldSqlLength, -- 长度,  \r\n" + "     (case   \r\n" + "		  when DATA_TYPE='decimal' or DATA_TYPE='bigint' or DATA_TYPE='smallint' then NUMERIC_SCALE\r\n" + "		  when DATA_TYPE='datetime' then 9\r\n" + "		  else 0\r\n" + "		end)  `precision`, -- 长度,   \r\n" + "      if(IS_NULLABLE='YES',b'0',b'1')  isRequired,\r\n" + "      COLUMN_DEFAULT defaultValue, -- 默认值,  \r\n" + "      COLUMN_COMMENT columnDescription -- 备注   \r\n" + "    FROM  \r\n" + "     INFORMATION_SCHEMA.COLUMNS  \r\n" + "    -- developerclub为数据库名称，到时候只需要修改成你要导出表结构的数据库即可  \r\n" + "    -- table_schema ='cbbk'  \r\n" + "    -- AND  \r\n" + "    -- article为表名，到时候换成你要导出的表的名称  \r\n" + "    -- 如果不写的话，默认会查询出所有表中的数据，这样可能就分不清到底哪些字段是哪张表中的了，所以还是建议写上要导出的名名称  \r\n" + "    {0}  ", where.ToSql());
        SGDataTable dt = this.GetDataTable(sql, null);
        if (dt != null && !dt.IsEmpty()) {
            return dt.ToList(SGSqlFieldInfo.class, (o, r, o1) -> {
//			o.setFieldName(r.getStringColumn("Field"));
//			o.setFieldType(r.getStringColumn("Type"));
//			o.setIsRequired("NO".equals(r.getStringColumn("Null")));
//			o.setIsPrimaryKey("PRI".equals(r.getStringColumn("Key")));
//			o.setDefaultValue(r.getStringColumn("Default"));
//			
                // Key=UNI时应该是唯一列 ;Extra=auto_increment 时应该是标识列
            });
        }
        return null;

    }

//	public PreparedStatement GetPs(String tableName) {
//		try {
//			OpenConn();
//			// Object aa=getTidbVar();
//
//			PFMySqlExecute dstQueryExec = (PFMySqlExecute) PFSqlExecute.Init(_jdbc);
//			// List<SGSqlFieldInfo> fields=this.GetTableFields(tableName);
//			List<SGSqlFieldInfo> fields = dstQueryExec.GetTableFields(tableName);// GetTableFields不要从this调用，免得应该ps的conn状态
//			String[] commaList = new String[fields.size()];
//			Arrays.fill(commaList, "?");
//			// PreparedStatement crs;
//			List<String> fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
//			OpenConn();
//			PreparedStatement crs = (PreparedStatement) conn
//					.prepareStatement(PFDataHelper.FormatString("insert into {0}  ({1})  values ({2})", tableName,
//							String.join(",", fieldNames), String.join(",", commaList)));
//			conn.setAutoCommit(false);
//			return crs;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			SetError(e);
//		}
//		return null;
//	}

//	/**
//	 * 之前用这方法好像只是为了拿metadata?
//	 *
//	 * @param tableName
//	 * @return
//	 */
//	@Deprecated
//	public ResultSet GetRs(String tableName) {
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
//			ResultSet rs = GetDataReader(
//					PFDataHelper.FormatString("select {0} from {1} ", String.join(",", fieldNames), tableName));
//
//			return rs;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			SetError(e);
//		}
//		return null;
//	}

//	/**
//   * 已移到基类 PFSqlExecuteBase
//	 * 感觉没必要全字段
//	 * 
//	 * @deprecated 优先使用GetMetaData(String tableName,SqlInsertCollection insert)
//	 */
//	@Deprecated
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
//			return rs.getMetaData();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			SetError(e);
//		}
//		return null;
//	}
//public ResultSetMetaData GetMetaData(String tableName,SqlInsertCollection insert) {
//	try {
//		 OpenConn();
//		 List<String> fieldNames= null;
//		 if(insert!=null) {
//			 fieldNames=new ArrayList<>(insert.keySet());
//		 }else {
//				List<SGSqlFieldInfo> fields=this.GetTableFields(tableName);
//				String[] commaList=new String[fields.size()];
//				Arrays.fill(commaList,"?");
//			     //PreparedStatement crs;
//			    fieldNames=fields.stream().map(a->a.getFieldName()).collect(Collectors.toList());
//		 }
//
//		 OpenConn();
//
//		ResultSet rs = GetDataReader(PFDataHelper.FormatString("select {0} from {1} where 1=0",String.join(",",fieldNames), tableName));
//
//		return rs.getMetaData();
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		SetError(e);
//	}
//	return null;
//}

//	/**
//	 * @deprecated 注意，mysql用executeBatch非常慢,插入50000行要10分钟左右，改用拼接insert语句的效率高很多
//	 * @param insert
//	 * @param crs
//	 * @param tableName
//	 * @param transferOneByOne
//	 * @return
//	 */
//	@Deprecated
//	private Boolean DoBulkReader(PFSqlInsertCollection insert,
//			// CachedRowSetImpl crs,
//			// ResultSet crs,
//			PreparedStatement crs, String tableName, Boolean transferOneByOne// 只有当一条一条导入时,bulk的错误才能确定是insert里的值
//	) {
//
//		try {
//			crs.executeBatch();
//			conn.commit();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return true;
//	}

//    @Override
//    public int ExecuteSqlInt(PFSqlCommandString sql, PFSqlParameter[] p, Boolean autoClose) {
//
//        PreparedStatement ps = null;
//        int rs = -1;
//        try {
//            if (!OpenConn()) {
//                return rs;
//            }
//            if (p != null) {
//                ps = conn.prepareStatement(sql.toString());
//                AddSqlParameter(ps, p);
//                ps.setQueryTimeout(CommandTimeOut);
//                rs = ps.executeUpdate();
//            } else {
//
//                int cnt = sql.size();
//                if (cnt > 1) {
//                    conn.setAutoCommit(false);
//                    stmt = conn.createStatement();
//                    stmt.setQueryTimeout(CommandTimeOut);
//
////					//这里防止批次太多 -- benjamin 20230222
//                    PFBatchHelper batchHelper = new PFBatchHelper();
//                    batchHelper.batchSize = this.insertOpt.getProcessBatch();
//                    batchHelper.batchCnt = 0;
//                    for (int i = 0; i < cnt; i++) {
//                        stmt.addBatch(sql.get(i));
//                        batchHelper.hasNext = cnt > i + 1;
//                        batchHelper.batchCnt++;
//                        //batchHelper.hasUnDo=true;
//                        if (batchHelper.ifDo()) {
//                            stmt.executeBatch();
//                            conn.commit();
//                            batchHelper.hasDone();
//                        }
//                    }
//
//                    rs = 1;
//                } else {
//                    stmt = conn.createStatement();
//                    stmt.setQueryTimeout(CommandTimeOut);
//
//                    String oneSql = sql.get(0);
//                    if (oneSql.indexOf("exec") > -1) {
//                        stmt.execute(oneSql);
//                        rs = 1;
//                    } else {
//                        rs = stmt.executeUpdate(oneSql);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            if (this.errorNotCatchAction != null && errorNotCatchAction.apply(e)) {
//                rs = 0;
//            } else {
//                SetError(e, sql.toString());
//                PrintError();
//            }
//        } finally {
//            try {
//                if (autoClose && autoCloseConn) {
//                    if (p != null) {
//                        ps.close();
//                    }
//                    if (stmt != null) {
//                        stmt.close();
//                        stmt = null;
//                    }
//                    CloseConn();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return rs;
//    }

//	/**
//	 * 注意，mysql用executeBatch非常慢,插入50000行要10分钟左右，改用拼接insert语句的效率高很多 -- benjamin 20220728
//	 */
//	@Override
//	public  <T> boolean doInsertList(
//			List<String> srcFieldNames//SqlInsertCollection dstInsert
//			,String tableName,
//			//int cnt,
//			Function<Integer,Boolean> isEnd ,
//			Function<Integer,T> getItemAction ,
//			PFAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
//			Consumer<Integer> sqlRowsCopiedAction,
//			Predicate<Boolean> stopAction
//			) {
//		PFSqlInsertCollection dstInsert=null;
//
//		BatchInsertOption insertOption = GetInsertOption();
//
//		OpenConn();
//
//		int idx = 0;
//
//		insertedCnt = 0;// 已插入的行数
//
//		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
//		int batchCnt = 0;
//
//		Boolean hasUnDo = false;
//
//		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//
//			long[] beginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis() };
//
//		try {
//
//			boolean hasSysLimitId = srcFieldNames.contains(sys_limit_id);
//			String sysLimitIdFieldName = sys_limit_id;
//			long currentSysLimitId = -1;
//			lastInsertedId = -1;
//
//			ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
//					:GetMetaData(tableName,srcFieldNames);
//
//			conn.setAutoCommit(false);
//			PreparedStatement ps =srcFieldNames==null?  GetPs(tableName): GetPs(tableName,srcFieldNames);
//			//PFSqlCommandString sb = new PFSqlCommandString();
//
//			// 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//			if (dstInsert == null) {
//
//				dstInsert = getInsertCollection(dstMd);
//			}
//
//			while (true) {
//				//T item = null;
////				boolean hasNext = idx < cnt;
//				boolean hasNext =isEnd.apply(idx);
////				if (hasNext) {
////					item=getItemAction.apply(idx);
////				}
//				if (hasNext) {
//					if(insertOption.getAutoUpdateModel()&&null!=getItemAction) {
//						T item=getItemAction.apply(idx);
//						dstInsert.UpdateModelValueAutoConvert(item);
//					}
//					if (rowAction != null) {
//						//rowAction.accept(dstInsert);
//						rowAction.go(dstInsert,idx,null);
//					}
//
//					ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
//					//sb.add(GetInsertSql(tableName, dstInsert));
//
//					batchCnt++;
//					hasUnDo = true;
//
//					if (hasSysLimitId) {
//						//currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
//						currentSysLimitId =PFDataHelper.ObjectToLong0(dstInsert.Get(sysLimitIdFieldName).Value);
//					}
//				}
//				if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//
//					Boolean b = false;
//					try {
//						ps.executeBatch();
//						conn.commit();
//						b = true;
//						//b=ExecuteSql(sb);
//						if (hasSysLimitId) {
//							lastInsertedId = currentSysLimitId;
//						}
//					} catch (Exception e) {
//						SetError(e);
//					}
//
//					if (!b) {
//						// CloseReader(rdr);
//						CloseConn();
//						return false;
//					} else {
//						insertedCnt = idx + 1;
////						if (hasSysLimitId) {
////							lastInsertedId = currentSysLimitId;
////						}
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
//
//
//					//ps =srcFieldNames==null?  GetPs(tableName): GetPs(tableName,srcFieldNames);
//					ps.clearBatch();
//					//sb.clear();
//
//					hasUnDo = false;
//					batchCnt = 0;
//				} else {
//					// batchCnt++;
//					// oneThousandCount++;
//				}
//				if (!hasNext) {
//					break;
//				}
//				idx++;
//
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				System.out.println(PFDataHelper.FormatString("rows:{0}  --  " + "speed:{1}",
//						PFDataHelper.ScientificNotation(idx),
////                    	"10000条/" + ((PFDate.Now().ToCalendar().getTimeInMillis() - beginTime[0]) * 10 / total) + "秒"
//								PFDataHelper.ScientificNotation(
//										Double.valueOf(idx) * 1000 / (m - beginTime[0])) + "条/秒"));
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
//
//	 return true;
//	}

    @Override
    public ResultSet GetDataReaderUpdatable(String sql) {
        // 建立Statement对象
        try {
            OpenConn();
            // OpenMicroJdbcConn();

//		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//                ResultSet.CONCUR_UPDATABLE);
            PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
//		e.printStackTrace();
            rs = null;
        }
        return rs;

    }
//
//	@Override
//	public PreparedStatement insertIntoCachedRowSet(PreparedStatement crs, ResultSetMetaData md,
//			PFSqlInsertCollection dstInsert
//// ,Map<String,Integer> srcColumnType
//// ,Map<String,Integer> dstColumnType
//	) throws SQLException {
//		// 移动指针到“插入行”，插入行是一个虚拟行
//		// crs.moveToInsertRow();
//
//		// 更新虚拟行的数据
////crs.updateString("name", stu.getName());
////crs.updateInt("id", stu.getId());
//
//		// Iterator<Entry<String, SqlUpdateItem>> iter =
//		// dstInsert.entrySet().iterator();
//
////String[] fields=new String[] {
////	   "Hybh","Hyxm",
////	   "sfzh","Qx",
////	   "Pexm",//是这列的问题 colid 20?
////	   "Pesfzh","Tel1"
////	   //,"Tel2","Bankzh",
////	   //"Bankname","cmonth","bjhybh","agentno","hyxb","qxmonth","province","city","county","address",
////	   //"fbirth","fjoindate","BankSf","BankCity","BankPS","BankNo"
////};
////List<String> fieldList=Arrays.asList(fields);
//
//		// ResultSetMetaData md = crs.getMetaData();
//
//		List<String> nullCols = new ArrayList<String>();
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
//						// crs.updateBigDecimal(colName, vD);
//						crs.setBigDecimal(mdIdx, vD);
//						if (vD == null) {
//							// String aa="aa";
//							nullCols.add(colName);
//						}
//					} else {
//						// String aa="aa";
//						nullCols.add(colName);
//					}
//				} else if (java.sql.Types.TIMESTAMP == dataT) {
//
//					String dataTName = md.getColumnTypeName(mdIdx);
////				crs.updateTimestamp(colName, PFDataHelper.ObjectAs(PFDataHelper.ConvertObjectToSqlTypeByPFType(
////						dstInsert.get(colName).Value, dstInsert.get(colName).VPFType, dataT, dataTName)));
//					Timestamp t = PFDataHelper.ObjectAs(PFDataHelper.ConvertObjectToSqlTypeByPFType(
//							dstInsert.get(colName).Value, dstInsert.get(colName).VPFType, dataT, dataTName));
//					crs.setTimestamp(mdIdx, t);
//					if (t == null) {
//						// String aa="aa";
//						nullCols.add(colName);
//					}
//				}
//
//				else {
////				crs.updateObject(colName, PFDataHelper.ConvertObjectToSqlTypeByPFType(dstInsert.get(colName).Value,
////						dstInsert.get(colName).VPFType, dataT));
//					Object v = PFDataHelper.ConvertObjectToSqlTypeByPFType(dstInsert.get(colName).Value,
//							dstInsert.get(colName).VPFType, dataT);
//					crs.setObject(mdIdx, v);
//					if (v == null) {
//						// String aa="aa";
//						nullCols.add(colName);
//					}
//				}
//
//			} else {
//				nullCols.add(colName);
//			}
//			// }
//		}
//
////	// 插入虚拟行
////	crs.insertRow();
////	// 移动指针到当前行
////	crs.moveToCurrentRow();
//		try {
//			crs.addBatch();
//		} catch (SQLException e) {
//			String err = e.toString();
//			if (err != null && err.indexOf("No value specified for parameter") > -1) {
//				PFDataHelper.WriteError(new Throwable(),
//						new Exception(PFDataHelper.FormatString(
//								"tidb的addBatch不支持忽略没执行set的值,当前dstInsert为:{0},\r\n转换后的值为null的列有:{1}",
//								JSON.toJSONString(nullCols), JSON.toJSONString(nullCols))));
//			}
//			throw e;
//		}
//		return crs;
//	}

    //private boolean isTidb() {
//	return PFSqlType.Tidb==_jdbc.GetSqlType();
//}
///**
// * 测试tidb的tidb_batch_insert参数有没有设置成功
// * @return
// */
//private Object getTidbVar() {
//	if(isTidb()) {
////		//ISqlExecute dstJdbc = PFSqlExecute.Init(_jdbc);
////		PFMySqlExecute dstJdbc = this;
////		String sql2="show variables LIKE 'tidb_batch_insert' ";
////		PFDataTable variablesDt=dstJdbc.GetDataTable(sql2,null,false);
////		return variablesDt.Rows.get(0).col.get(1).value;
//	}
//	return null;
//}
    @Override
    public Boolean HugeBulkReader(SGSqlInsertCollection dstInsert, ResultSet rdr, String tableName,
                                  // Consumer<BatchInsertOption> insertOptionAction,
                                  //Consumer<BaseSqlUpdateCollection> rowAction,
                                  SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction, Predicate<Boolean> stopAction) {
        // TODO Auto-generated method stub
        if (rdr == null) {
            SetError(new Exception("DataReader不能为空"));
            return false;
        }

        //BatchInsertOption insertOption = GetInsertOption();

        OpenConn();

        //int idx = 0;
        insertedCnt = 0;// 已插入的行数

//		int batchSize = insertOption.getProcessBatch();// 50000;// tidb设置大些试试,测试100万行/25秒
//		int batchCnt = 0;
//
//		Boolean hasUnDo = false;

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

        if (SGSqlType.Tidb == _jdbc.GetSqlType() && null == connConfigSettingSql) {
            connConfigSettingSql = new SGSqlCommandString("set tidb_batch_insert = 1", "set tidb_batch_delete = 1",
                    // "update mysql.tidb set variable_value='24h' where
                    // variable_name='tikv_gc_life_time'",
                    "update mysql.tidb set variable_value='30m' where variable_name='tikv_gc_life_time'");
        }
        try {
            return this.doInsertList(//null,
//					srcFieldNames, 
                    null, tableName,
                    //list.size(),
                    (a, b, c) -> rdr.next(), (a) -> rdr, rowAction, sqlRowsCopiedAction, stopAction);

////	//保存来源的类型，便于转换
//			ResultSetMetaData srcMd = rdr.getMetaData();
//
//			boolean hasSysLimitId = false;
//			String sysLimitIdFieldName = "sys_limit_id";
//			long currentSysLimitId = -1;
//			lastInsertedId = -1;
//
//			List<String> srcFieldNames = new ArrayList<String>();
//			for (int i = 0; i < srcMd.getColumnCount(); i++) {
//				String fieldName = srcMd.getColumnLabel(i + 1);
//				if (sysLimitIdFieldName.equals(fieldName)) {// 这个内部值用来代替limit的offset
//					hasSysLimitId = true;
//					continue;
//				} else {
//					srcFieldNames.add(fieldName);
//				}
//			}
//
//			ResultSetMetaData dstMd = GetMetaData(tableName, srcFieldNames);
//
//			if (PFSqlType.Tidb == _jdbc.GetSqlType()) {
//				ExecuteSqlInt(new PFSqlCommandString("set tidb_batch_insert = 1", "set tidb_batch_delete = 1",
//						// "update mysql.tidb set variable_value='24h' where
//						// variable_name='tikv_gc_life_time'",
//						"update mysql.tidb set variable_value='30m' where variable_name='tikv_gc_life_time'"), null,
//						false);
//			}
//
//			PreparedStatement ps = GetPs(tableName, srcFieldNames);
//
//			// 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//			if (dstInsert == null) {
//
//				dstInsert = getInsertCollection(dstMd);
//			}
//
//			while (true) {
//				boolean hasNext = rdr.next();
//				if (hasNext) {
//
//					dstInsert.UpdateByDataReaderAutoConvert(rdr);
//					if (rowAction != null) {
//						//rowAction.accept(dstInsert);
//						rowAction.go(dstInsert,idx,null);
//					}
//
//					ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
//
//					batchCnt++;
//					hasUnDo = true;
//					if (hasSysLimitId) {
//						currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
//					}
//				}
//				
//				if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//					Boolean b = DoBulkReader(dstInsert, ps, tableName, batchSize == 1);
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
//
//					ps = GetPs(tableName, srcFieldNames);
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

        } catch (Exception e) {
            e.printStackTrace();
            SetError(e);
            return false;
        }

//		CloseConn();
//		return true;
    }


//	@Override
//	public <T> boolean HugeUpdateList(PFSqlUpdateCollection update, List<T> list, String tableName,
//	// Action<BatchInsertOption> insertOptionAction,
////		        Func<MySqlUpdateCollection, T, bool> rowAction,//考虑这个是否必要
////		        Action<int> sqlRowsUpdatedAction = null
//
//			PFFunc<BaseSqlUpdateCollection, T, Object, Boolean> rowAction, Consumer<Integer> sqlRowsUpdatedAction,
//			Predicate<Boolean> stopAction) {
////		if(PFSqlType.Tidb==_jdbc.GetSqlType()) {
////			CloseConn();
////			this.connConfigSettingSql=new PFSqlCommandString(
////					//"SET tidb_multi_statement_mode='ON'",
////					"set tidb_batch_insert = 1",
////					"update mysql.tidb set variable_value='24h' where variable_name='tikv_gc_life_time'",
////					"update mysql.tidb set variable_value='30m' where variable_name='tikv_gc_life_time'");
////			//this.SetInsertOption(a->a.setProcessBatch(500));
////			//this.SetInsertOption(a->a.setProcessBatch(1));
////		}
//		return super.HugeUpdateList(update, list, tableName, rowAction, sqlRowsUpdatedAction, stopAction);
//	}

    /**
     * 更新同比环比字段(暂规定统一以create_date为时间字段,同比前缀ly_,环比前缀lm_)
     * tidb_monthly_province_statistics表用到,以后看看能不能不用此方法
     *
     * @param tableName
     * @param cmonth
     * @param primaryKeys
     * @param valuefields
     */
    public boolean UpdateYearOnYearField(String tableName, String cmonth, String[] primaryKeys, String[] valuefields) {
        // var cmonth = transfer.ViewData["cmonth"].ToString();
        String last_cmonth = SGDataHelper.CMonthAddMonths(cmonth, -1);
        String last_year = SGDataHelper.CMonthAddYears(cmonth, -1);

        PFCmonth pfCmonth = new PFCmonth();
        pfCmonth.setCmonth(cmonth);
        // var sqlExec = new MySqlExecute(transfer.DstConn);
        SGDataTable thisMonthDt = GetDataTable(SGDataHelper.FormatString("select * from {0} where create_date= STR_TO_DATE('{1}.01','%Y.%m.%d')", tableName, cmonth));
        // var thisMonthList = thisMonthDt == null ? new List<Dictionary<string,
        // object>>() : thisMonthDt.ToDictList(false);
        ArrayList<LinkedHashMap<String, Object>> thisMonthList = thisMonthDt == null ? new ArrayList<LinkedHashMap<String, Object>>() : thisMonthDt.ToDictList();

        SGDataTable lastMonthDt = GetDataTable(SGDataHelper.FormatString("select * from {0} where create_date= STR_TO_DATE('{1}.01','%Y.%m.%d')", tableName, last_cmonth));
        ArrayList<LinkedHashMap<String, Object>> lastMonthList = lastMonthDt == null ? new ArrayList<LinkedHashMap<String, Object>>() : lastMonthDt.ToDictList();

        SGDataTable lastYearDt = GetDataTable(SGDataHelper.FormatString("select * from {0} where create_date= STR_TO_DATE('{1}.01','%Y.%m.%d')", tableName, last_year));
        ArrayList<LinkedHashMap<String, Object>> lastYearList = lastYearDt == null ? new ArrayList<LinkedHashMap<String, Object>>() : lastYearDt.ToDictList();

        ArrayList<LinkedHashMap<String, Object>> updateList = new ArrayList<LinkedHashMap<String, Object>>();
        ArrayList<LinkedHashMap<String, Object>> insertList = new ArrayList<LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> valueFieldZero = new LinkedHashMap<String, Object>();// 值列最好是不要有null值,否则BI里相减会得null,很不方便,其实所有decimal列都要

        SGAction<ArrayList<LinkedHashMap<String, Object>>, Object, Object> findZero = (list, c, d) -> {
            if (!list.isEmpty()) {
//            foreach (var i in list[0])
//            {
//                if ((!primaryKeys.Contains(i.Key)) && (!valueFieldZero.ContainsKey(i.Key)) && i.Value != null)
//                {
//                    var typeString = PFDataHelper.GetStringByType(i.Value.GetType());
//                    if (typeString == "decimal")
//                    {
//                        valueFieldZero.Add(i.Key, new decimal(0));
//                    }
//                    else if (new string[] { "int", "long" }.Contains(typeString))
//                    {
//                        valueFieldZero.Add(i.Key, 0);
//                    }
//                }
//            }

                LinkedHashMap<String, Object> r0 = list.get(0);
                Iterator<Entry<String, Object>> iter = r0.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, Object> key = iter.next();

                    if ((!Arrays.asList(primaryKeys).contains(key.getKey())) && (!valueFieldZero.containsKey(key.getKey())) && key.getValue() != null) {
                        String typeString = SGDataHelper.GetStringByType(key.getValue().getClass());
                        if ("decimal".equals(typeString)) {
                            valueFieldZero.put(key.getKey(), new BigDecimal(0));
                        } else if (Arrays.asList(new String[]{"int", "long"}).contains(typeString)) {
                            valueFieldZero.put(key.getKey(), 0);
                        }
                    }
                }
            }
        };
        findZero.go(thisMonthList, null, null);
        findZero.go(lastMonthList, null, null);
        findZero.go(lastYearList, null, null);

        SGFunc<LinkedHashMap<String, Object>, String, Object, LinkedHashMap<String, Object>> newRow = (srcRow, cmonth1, d) -> {
            LinkedHashMap<String, Object> r = new LinkedHashMap<String, Object>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                {
                    // put("create_date",PFDataHelper.CMonthToMySqlDate(cmonth));
                    put("create_date", pfCmonth.ToDateTime());
                }
            };
            for (String k : primaryKeys) {
                if (!k.equals("create_date")) {
                    r.put(k, srcRow.get(k));
                }
            }
//        foreach (var k in valueFieldZero)
//        {
//            r.Add(k.Key, k.Value);
//        }
            Iterator<Entry<String, Object>> iter = valueFieldZero.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Object> key = iter.next();
                r.put(key.getKey(), key.getValue());
            }
            return r;
        };

        for (LinkedHashMap<String, Object> j : lastMonthList) {
            LinkedHashMap<String, Object> item = null;
            if (!thisMonthList.isEmpty()) {
                for (LinkedHashMap<String, Object> i : thisMonthList) {
                    boolean isMatch = true;
                    for (String k : primaryKeys) {
                        if (!k.equals("create_date")) {

                            if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
                                isMatch = false;
                                break;
                            }
                        }
                    }
                    if (isMatch) {
                        item = i;
                        break;
                    }
                }
            }
            // var item = thisMonthList.Any()? thisMonthList.FirstOrDefault(a =>
            // a["province_name"] == j["province_name"] && a["hpos"] == j["hpos"] &&
            // a["qpos"] == j["qpos"] && a["is_new_hy"] == j["is_new_hy"])
            // :null;
            // bool isAdd = false;
            if (item == null) {
                // isAdd = true;
                item = newRow.go(j, cmonth, null);

                insertList.add(item);
            } else {
                updateList.add(item);
            }
            for (String k : valuefields) {
//            item["lm_" + k] = j[k];
                item.put("lm_" + k, j.get(k));
            }

        }
        for (LinkedHashMap<String, Object> j : lastYearList) {
            LinkedHashMap<String, Object> item = null;
            if (!thisMonthList.isEmpty()) {
                for (LinkedHashMap<String, Object> i : thisMonthList) {
                    boolean isMatch = true;
                    for (String k : primaryKeys) {
                        if (!k.equals("create_date")) {
                            if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
                                isMatch = false;
                                break;
                            }
                        }
                    }
                    if (isMatch) {
                        item = i;
                        break;
                    }
                }
            }
            // var item = thisMonthList.FirstOrDefault(a => a.province_name ==
            // j.province_name && a.hpos == j.hpos && a.qpos == j.qpos && a.is_new_hy ==
            // j.is_new_hy);
            // bool isAdd = false;
            if (item == null) {
                // isAdd = true;
                if (!insertList.isEmpty()) {
                    for (LinkedHashMap<String, Object> i : insertList) {
                        boolean isMatch = true;
                        for (String k : primaryKeys) {
                            if (!k.equals("create_date")) {
                                if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
                                    isMatch = false;
                                    break;
                                }
                            }
                        }
                        if (isMatch) {
                            item = i;
                            break;
                        }
                    }
                }
                // item = insertList.FirstOrDefault(a => a.province_name == j.province_name &&
                // a.hpos == j.hpos && a.qpos == j.qpos && a.is_new_hy == j.is_new_hy);
                if (item == null) {
                    item = newRow.go(j, cmonth, null);

                    insertList.add(item);
                }
            } else {
                LinkedHashMap<String, Object> updateItem = null;
                if (!updateList.isEmpty()) {
                    for (LinkedHashMap<String, Object> i : updateList) {
                        boolean isMatch = true;
                        for (String k : primaryKeys) {
                            if (k != "create_date") {
                                if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
                                    isMatch = false;
                                    break;
                                }
                            }
                        }
                        if (isMatch) {
                            updateItem = i;
                            break;
                        }
                    }
                }

                // var updateItem = updateList.FirstOrDefault(a => a.province_name ==
                // j.province_name && a.hpos == j.hpos && a.qpos == j.qpos && a.is_new_hy ==
                // j.is_new_hy);
                if (updateItem == null) {
                    updateList.add(item);
                } else {
                    item = updateItem;
                }
            }
            for (String k : valuefields) {
//            item["ly_" + k] = j[k];
                item.put("ly_" + k, j.get(k));
            }
        }

        boolean b = true;
        // 当单独执行AfterTransferAction时,还是先把同比环比清空比较稳,否则会有上次残留结果
        // var valuefields = new string[] { "new_hy_qty", "valid_hy_qty",
        // "valid_hy_hpos_above_5_qty", "valid_hy_hpos_0_qty" };
        b = ExecuteSql(new SGSqlCommandString(SGDataHelper.FormatString("update {0} set {1},{2} where create_date= STR_TO_DATE('{3}.01','%Y.%m.%d')", tableName, String.join(",", SGDataHelper.ListSelect(Arrays.asList(valuefields), a -> SGDataHelper.FormatString(" ly_{0}=0 ", a))),
//string.Join(",", valuefields.Select(a => string.Format(" lm_{0}=0 ", a))),
                String.join(",", SGDataHelper.ListSelect(Arrays.asList(valuefields), a -> SGDataHelper.FormatString(" lm_{0}=0 ", a))), cmonth)));
        if (!updateList.isEmpty()) {
            // MySqlUpdateCollection update = new MySqlUpdateCollection(updateList.get(0),
            // null);

            List<String> srcFieldNames = new ArrayList<String>(updateList.get(0).keySet());
            ResultSetMetaData dstMd = GetMetaData(tableName, srcFieldNames);
            SGSqlUpdateCollection update = getUpdateCollection(dstMd);

            //// update.UpdateFields("lm_new_hy_qty", "lm_valid_hy_qty",
            //// "lm_valid_hy_hpos_above_5_qty", "lm_valid_hy_hpos_0_qty");
            // ArrayList<String> updateFields = new ArrayList<String>();

//			List<String> mList = PFDataHelper.MergeList(
//					// valuefields.Select(a => "lm_" + a).ToList(),
//					PFDataHelper.ListSelect(Arrays.asList(valuefields), a -> "lm_" + a),
//					// valuefields.Select(a => "ly_" + a).ToList()
//					PFDataHelper.ListSelect(Arrays.asList(valuefields), a -> "ly_" + a));

            List<String> list1 = SGDataHelper.ListSelect(Arrays.asList(valuefields), a -> "lm_" + a);
            List<String> list2 = SGDataHelper.ListSelect(Arrays.asList(valuefields), a -> "ly_" + a);
            List<String> mList = SGDataHelper.MergeList(new ArrayList<List<String>>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                {
                    add(list1);
                    add(list2);
                }
            });
//			List<String> list1=PFDataHelper.ListSelect(Arrays.asList(valuefields), a -> "lm_" + a);
//			List<String> list2=PFDataHelper.ListSelect(Arrays.asList(valuefields), a -> "ly_" + a);
//			String[] aa=new String[] {""};
//			List<String> mList = PFDataHelper.MergeList(
//					new List<String>[] {
//						list1
//					});

            String[] mArray = mList.toArray(new String[mList.size()]);
            update.UpdateFields(mArray);
            update.PrimaryKeyFields(false, primaryKeys);
            b = HugeUpdateList(update, updateList, tableName, null, null, null);
        }
        if (!insertList.isEmpty()) {
            // var insert = new MySqlInsertCollection(insertList.First());
            b = HugeBulkList(null, insertList, tableName, null, null, null);
        }
        return b;
    }

    @Override
    public Boolean PreValidTransferReader(ResultSet rdr, String tableName, Function<BaseSqlUpdateCollection, List<SGDataTableFieldValidModel>> PreValidAction, SGRef<List<SGDataTableFieldValidModel>> validRef, Consumer<Integer> alreadyAction, Predicate<Boolean> stopAction) {
        return null;
    }

    @Override
    public Boolean PreValidTransferTable(SGSqlTransferItem transferItem, SGRef<List<SGDataTableFieldValidModel>> validRef, Consumer<Integer> alreadyAction, Predicate<Boolean> stopAction) {
        return null;
    }
    

    @Override
    public SGDataTable GetOneRow(String tableName, Consumer<SGSqlWhereCollection> whereAction) {
        SGSqlWhereCollection where = getWhereCollection();
        if (whereAction != null) {
            whereAction.accept(where);
        }
        return GetDataTable(
                SGDataHelper.FormatString("select * from {0} {1} limit 1", tableName, where == null ? "" : where.ToSql()));
    }

}
