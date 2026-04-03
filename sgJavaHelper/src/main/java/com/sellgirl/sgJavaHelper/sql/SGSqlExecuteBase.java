package com.sellgirl.sgJavaHelper.sql;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sellgirl.sgJavaHelper.IPFSqlFieldTypeConverter;
import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.PFBatchHelper;
import com.sellgirl.sgJavaHelper.PFDataColumn;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.SGEmailSend;
import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.PFMySqlInsertCollection;
import com.sellgirl.sgJavaHelper.PFMySqlUpdateCollection;
import com.sellgirl.sgJavaHelper.PFMySqlWhereCollection;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.PFSqlParameter;
import com.sellgirl.sgJavaHelper.PFSqlType;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.exception.PFSqlConnOpenException;

public abstract class SGSqlExecuteBase implements ISqlExecute {
    //private static final Logger LOGGER = LoggerFactory.getLogger(PFSqlExecuteBase.class);
	private final static String tag="SGSqlExecuteBase";
    protected Connection conn;
    protected Statement stmt;
    protected ResultSet rs;
    //protected ResultSetMetaData md;
    protected ISGJdbc _jdbc;
    protected int insertedCnt = -1;
    protected static String sys_limit_id = "sys_limit_id";
    protected long lastInsertedId = -1;
    /**
     * tidb有可能要执行 SET tidb_multi_statement_mode='ON'
     */
    protected SGSqlCommandString connConfigSettingSql = null;
    protected ZoneId zoneId = null;


    public SGSqlExecuteBase() {
    }

    public SGSqlExecuteBase(ISGJdbc jdbc) throws Exception {
        // a连接数据库
        conn = getConnection(jdbc);
        // this(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
        _jdbc = jdbc;

        sqlUpdateOpt.setProcessBatch(500000);
    }

    public Connection GetConn() {
        return conn;
    }

    /*
     * 原则上,此方法返回的Error要尽量完整,用于系统日志.可考虑增用GetErrorMessage方法返回简单的错误信息
     */
    public String GetErrorFullMessage() {
        StringBuilder sb = new StringBuilder();
        if (_errors != null) {
            for (Exception i : _errors) {

                sb.append(SGDataHelper.getErrorFullString(i));
                sb.append("\r\n");
            }
        }
        if (_jdbc != null) {
            sb.append(SGDataHelper.FormatString("连接:{0}\r\n", _jdbc.getUrl()));
        }
        if (lastInsertedId != -1) {
            sb.append(SGDataHelper.FormatString("lastInsertedId:{0}\r\n", lastInsertedId));
        }
        return sb.toString();
    }


    /**
     * 这里用Exception类型是有好处的，可以知道每个错误的行位置（不要想着把这里改为String
     */
    private List<Exception> _errors = null;

    // private Throwable _errorLine = null;

    protected Function<Throwable, Boolean> errorNotCatchAction = null;

    public void SetErrorNotCatchAction(Function<Throwable, Boolean> errorNotCatchAction) {
        this.errorNotCatchAction = errorNotCatchAction;
    }

    public Exception GetError() {
        return null != this._errors && (!_errors.isEmpty()) ? _errors.get(0) : null;
    }

    public Throwable GetErrorLine() {
        return null != this._errors && (!_errors.isEmpty()) ? _errors.get(0) : null;
    }

    protected void SetError(Exception e) {
        if (e == null) {
            return;
        }

        if (_errors == null) {
            _errors = new ArrayList<Exception>();
        }
        String m1 = e.toString();
        if (m1 != null && SGDataHelper.ListAny(_errors, a -> a.toString().equals(m1))) { //

        } else {

            _errors.add(e);
        }
    }

    protected void SetError(Exception e, String sql) {
        // sql= sql.substring(0,1000);//太长时,前面不能输出,原因不明
        // int sqlLen=1000;
        // if(sql.length()>sqlLen) {
        // sql= sql.substring(0,sqlLen);//太长时,前面不能输出,原因不明
        // }
        SetError(e);
        SetError(new Exception(SGDataHelper.FormatString("sql:{0}\r\n", sql)));

        // sql= sql.substring(0,1000);//太长时,前面不能输出,原因不明

        // //换行也没什么用
        // int size=100;
        //
        // int totalSize=sql.length();
        // int n=totalSize/size;
        // int remaider=totalSize%size; //(先计算出余数)
        // if(remaider>0) {n+=1;}
        //
        // for(int i=n-1;i>=1;i--) {
        // sql=PFDataHelper.StringInsert(sql, i*size, "\r\n");
        // }
        //
        //
    }

    @Override
    public void ClearError() {
        if (_errors != null) {
            _errors.clear();
            _errors = null;
        }

    }

    protected void PrintError() {

        SGDataHelper.WriteErrors(_errors);
    }

    public int CommandTimeOut = 30;

    public void SetCommandTimeOut(PFSqlCommandTimeoutSecond second) {
        CommandTimeOut = second.ToInt();
    }

    public void UpToCommandTimeOut(PFSqlCommandTimeoutSecond second) {
        CommandTimeOut = PFSqlCommandTimeoutSecond.Max(second.ToInt(), CommandTimeOut);
    }

    // public void setNeedSendConnErrorMessage(boolean needSendConnErrorMessage) {
    // this.needSendConnErrorMessage=needSendConnErrorMessage;
    // }
    /**
     * 插入参数
     */
    protected BatchInsertOption insertOpt = new BatchInsertOption();
    /**
     * sql脚本更新每次处理的最大批数
     */
    protected BatchInsertOption sqlUpdateOpt = new BatchInsertOption();
    protected BatchDeleteOption deleteOpt = new BatchDeleteOption();
    protected BatchQueryOption queryOpt = new BatchQueryOption();

    @Override
    public BatchInsertOption GetInsertOption() {
        return insertOpt;

    }

    /**
     *
     */
    @Override
    public void SetInsertOption(Consumer<BatchInsertOption> insertOptionAction) {
        insertOptionAction.accept(insertOpt);
    }


    protected BatchInsertOption GetSqlUpdateOption() {
        return sqlUpdateOpt;

    }

    public void SetSqlUpdateOption(Consumer<BatchInsertOption> insertOptionAction) {
        // _sqlUpdateOption = insertOptionAction;
        insertOptionAction.accept(sqlUpdateOpt);
    }

//    protected BatchDeleteOption DefaultDeleteOption() {
//        BatchDeleteOption o = new BatchDeleteOption();
//        o.setProcessBatch(50000);
//        return o;
//    }

    protected BatchDeleteOption GetDeleteOption() {
        return deleteOpt;
//        BatchDeleteOption d = DefaultDeleteOption();
//        if (_deleteOption != null) {
//            _deleteOption.accept(d);
//        }
//        return d;
    }

    public void SetDeleteOption(Consumer<BatchDeleteOption> deleteOptionAction) {
        // _deleteOption = deleteOptionAction;
        deleteOptionAction.accept(deleteOpt);
    }

//    protected BatchQueryOption DefaultQueryOption() {
//        BatchQueryOption o = new BatchQueryOption();
//        return o;
//    }

    protected BatchQueryOption GetQueryOption() {
        return queryOpt;
//        BatchQueryOption d = DefaultQueryOption();
//        if (_queryOption != null) {
//            _queryOption.accept(d);
//        }
//        return d;
    }

    public void SetQueryOption(Consumer<BatchQueryOption> queryOptionAction) {
        // _queryOption = queryOptionAction;
        queryOptionAction.accept(queryOpt);
    }

    Predicate<Boolean> _stopAction = null;

    public void SetStopAction(Predicate<Boolean> stopAction) {
        _stopAction = stopAction;
    }

    // public Connection getConnection(IPFJdbc jdbc) throws Exception {
    // return getConnection(jdbc,true);
    // }

    protected static void setErrorMessageToDba(ISGJdbc jdbc) {
        if ( // sendConnErrorMessage&&
                null != SGDataHelper.GetAppConfig() && SGDataHelper.GetAppConfig().getSendSysMsg()) { // 发短信的数据库打开失败时不要进入这里,否则会死循环

            try {
                // url太长,短信好像显示不完整
                // PFDataHelper.SendMobileMessage(new String[]
                // {jdbc.getDbaMobile()},PFDataHelper.FormatString("{0}同志,数据库 {1} {2} {3}
                // 连接异常,请及时处理",jdbc.getDbaName(), jdbc.getUrl(),jdbc.getIp(),jdbc.getDbName()));

                // String msg = PFDataHelper.FormatString("{0}同志,数据库 {1} {2} 异常,需要处理",
                // jdbc.getDbaName(), jdbc.getIp(),
                // jdbc.getDbName());
                String msg = SGDataHelper.FormatString("{0},数据库 {1} 异常,请处理", jdbc.getDbaName(), jdbc.getUrlShortName());
                SGDataHelper.DoIfNoRecent(msg, a -> {
                    boolean sended = false;
                    if ((!jdbc.getIsMobileMessageConn())
                            && !SGDataHelper.StringIsNullOrWhiteSpace(jdbc.getDbaMobile())) {
                        // PFDataHelper.SendSystemErrorMobileMessage(jdbc.getDbaMobile().split(","),msg);
                        // 注意这里有死循环,因为sendMobile方法很有可能又调用了OpenConn,结果又再发信息
                        SGDataHelper.SendMobileMessage(jdbc.getDbaMobile().split(","), msg);
                        sended = true;
                    }
                    if (!SGDataHelper.StringIsNullOrWhiteSpace(jdbc.getDbaEmail())) {
                        SGEmailSend.SendMail(jdbc.getDbaEmail().split(","),
                                SGDataHelper.FormatString("数据库异常通知,to[{0}]", jdbc.getDbaName()), msg);
                        sended = true;
                    }

                    SGEmailSend.SendMail(new String[]{SGEmailSend.EMAIL_OWNER_ADDR}, "数据库异常通知",
                            SGDataHelper.FormatString("数据库 {0} {1} 异常,需要处理<br />{2}", jdbc.getIp(), jdbc.getDbName(),
                                    sended ? "已通知dba" : "未通知dba"));
                }, 30);
            } catch (Exception e2) {

            }
        }
    }
    // protected static void setErrorMessageToDba(IPFJdbc jdbc,boolean
    // sendConnErrorMessage) {
    // if(sendConnErrorMessage
    // &&PFDataHelper.GetAppConfig().getSendSysMsg()
    // ) {//发短信的数据库打开失败时不要进入这里,否则会死循环
    //
    // try {
    // // url太长,短信好像显示不完整
    //// PFDataHelper.SendMobileMessage(new String[]
    // {jdbc.getDbaMobile()},PFDataHelper.FormatString("{0}同志,数据库 {1} {2} {3}
    // 连接异常,请及时处理",jdbc.getDbaName(), jdbc.getUrl(),jdbc.getIp(),jdbc.getDbName()));
    //
    //// String msg = PFDataHelper.FormatString("{0}同志,数据库 {1} {2} 异常,需要处理",
    // jdbc.getDbaName(),
    // jdbc.getIp(),
    //// jdbc.getDbName());
    // String msg = PFDataHelper.FormatString("{0},数据库 {1} 异常,请处理",
    // jdbc.getDbaName(),jdbc.getUrlShortName());
    // PFDataHelper.DoIfNoRecent(msg, a -> {
    // boolean sended = false;
    // if (!PFDataHelper.StringIsNullOrWhiteSpace(jdbc.getDbaMobile())) {
    // //
    // PFDataHelper.SendSystemErrorMobileMessage(jdbc.getDbaMobile().split(","),msg);
    // //注意这里有死循环,因为sendMobile方法很有可能又调用了OpenConn,结果又再发信息
    // PFDataHelper.SendMobileMessage(jdbc.getDbaMobile().split(","), msg);
    // sended = true;
    // }
    // if (!PFDataHelper.StringIsNullOrWhiteSpace(jdbc.getDbaEmail())) {
    // PFEmailSend.SendMail(jdbc.getDbaEmail().split(","),
    // PFDataHelper.FormatString("数据库异常通知,to[{0}]", jdbc.getDbaName()), msg);
    // sended = true;
    // }
    //
    // PFEmailSend.SendMail(new String[] { PFEmailSend.EMAIL_OWNER_ADDR },
    // "数据库异常通知",
    // PFDataHelper.FormatString("数据库 {0} {1} 异常,需要处理<br />{2}", jdbc.getIp(),
    // jdbc.getDbName(),
    // sended ? "已通知dba" : "未通知dba"));
    // }, 30);
    // }catch(Exception e2) {
    //
    // }
    // }
    // }

    public Connection getConnection(ISGJdbc jdbc) throws PFSqlConnOpenException {
        // throw new Exception("Not Declare getConnection()");
        try {
            // 这里不判断ClickHouse了，因为这样如果项目没引用，也可以正常使用PFSqlExecute
            if (PFSqlType.MySql == jdbc.GetSqlType() || PFSqlType.Tidb == jdbc.GetSqlType()) {
//				// if (jdbc.getDriverVersion() != null) {
//				if (!PFDataHelper.StringIsNullOrWhiteSpace(jdbc.getDriverVersion())) {
//					MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion(jdbc.getDriverVersion(),
//							jdbc.getDriverClassName());
//				} else {
//					MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("8.0.15", "com.mysql.cj.jdbc.Driver");
//				}
//				// Class.forName(jdbc.getDriverClassName());
//				return DriverManager.getConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
                return PFSqlConnHelper.dynamicGetConnByVersion(jdbc);
            } else if (PFSqlType.SqlServer == jdbc.GetSqlType()) {
//				MycatMulitJdbcVersionTest.dynamicLoadSqlServerJdbcByVersion(jdbc.getDriverVersion(),
//						jdbc.getDriverClassName());
//				return DriverManager.getConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
                return PFSqlConnHelper.dynamicGetSqlServerConnByVersion(jdbc);
            } else {
                return DriverManager.getConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
            }
        } catch (java.sql.SQLException e) { // SQLSyntaxErrorException e) {
            SetError(e);
            PrintError();

            //LOGGER.error(e.toString());
            //System.err.println(e.toString());
            SGDataHelper.getLog().printException(e,tag+".getConnection");
            setErrorMessageToDba(jdbc);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            SetError(e);
            PrintError();
            // e.printStackTrace();
        }
        // throw this.GetError();
        // // throw new PFUserException(this.GetErrorFullMessage());
        // // throw new PFSysProcessedException(this.GetErrorFullMessage());
        // // return null;
        throw new PFSqlConnOpenException(this.GetErrorFullMessage());
    }

    public SGSqlWhereCollection getWhereCollection() {
        // throw new Exception("Not Declare getConnection()");
        try {
            // 这里不判断ClickHouse了，因为这样如果项目没引用，也可以正常使用PFSqlExecute
            if (_jdbc.GetSqlType() == PFSqlType.MySql || _jdbc.GetSqlType() == PFSqlType.ClickHouse
                    || PFSqlType.Tidb == _jdbc.GetSqlType()) {
                return new PFMySqlWhereCollection();
            } else {
                return new SGSqlWhereCollection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public SGSqlInsertCollection getInsertCollection() {
        // throw new Exception("Not Declare getConnection()");
        try {
            SGSqlInsertCollection r = null;
            // 这里不判断ClickHouse了，因为这样如果项目没引用，也可以正常使用PFSqlExecute
            if (_jdbc.GetSqlType() == PFSqlType.MySql || _jdbc.GetSqlType() == PFSqlType.ClickHouse
                    || PFSqlType.Tidb == _jdbc.GetSqlType()) {
                r = new PFMySqlInsertCollection();
            } else {
                r = new SGSqlInsertCollection();
            }
            r.setSqlType(_jdbc.GetSqlType());
            return r;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public SGSqlInsertCollection getInsertCollection(ResultSetMetaData dstMd) {
        SGSqlInsertCollection dstInsert = getInsertCollection();

        // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
        try {
            for (int i = 0; i < dstMd.getColumnCount(); i++) {
                try {
                    int mdIdx = i + 1;
                    if (!dstMd.isAutoIncrement(mdIdx) && !dstMd.isReadOnly(mdIdx)) { // 不是自增列才插入,否则sql会报错-- benjamin
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
                        updateItem.setDstDataPFType(SGDataHelper.GetPFTypeBySqlType2(dstMd.getColumnType(mdIdx), dstMd.getColumnTypeName(mdIdx)));
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

    public SGSqlUpdateCollection getUpdateCollection() {
        // throw new Exception("Not Declare getConnection()");
        try {
            SGSqlUpdateCollection r = null;
            // 这里不判断ClickHouse了，因为这样如果项目没引用，也可以正常使用PFSqlExecute
            if (_jdbc.GetSqlType() == PFSqlType.MySql || _jdbc.GetSqlType() == PFSqlType.ClickHouse
                    || PFSqlType.Tidb == _jdbc.GetSqlType()) {
                r = new PFMySqlUpdateCollection();
            } else {
                r = new SGSqlUpdateCollection();
            }
            r.setSqlType(_jdbc.GetSqlType());
            return r;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public SGSqlUpdateCollection getUpdateCollection(ResultSetMetaData dstMd) {
        SGSqlUpdateCollection dstInsert = getUpdateCollection();

        // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
        try {
            for (int i = 0; i < dstMd.getColumnCount(); i++) {
                try {
                    int mdIdx = i + 1;
                    String fieldName = dstMd.getColumnLabel(mdIdx);
                    SqlUpdateItem updateItem = new SqlUpdateItem();
                    updateItem.Key = fieldName;
//                    updateItem.VPFType =
//                            PFDataHelper.GetPFTypeBySqlType(dstMd.getColumnType(i + 1));
                    // updateItem.setSrcDataPFType(PFDataHelper.GetPFTypeBySqlType2(dstMd.getColumnType(i
                    // + 1)));

                    updateItem.setDstDataType(dstMd.getColumnType(mdIdx));
                    updateItem.setDstDataTypeName(dstMd.getColumnTypeName(mdIdx));
                    updateItem.setDstDataPFType(SGDataHelper.GetPFTypeBySqlType2(dstMd.getColumnType(mdIdx), dstMd.getColumnTypeName(mdIdx)));
                    dstInsert.Add(updateItem);
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

    public boolean OpenConn() {
        // if (this.conn != null) {
        // CloseConn();
        // }
        boolean b = false;
        try {
            if (this.conn == null || this.conn.isClosed()) {
                // this.conn=getClickHouseConnection(_jdbc);
                // conn = DriverManager.getConnection(_jdbc.getUrl(), _jdbc.getUsername(),
                // _jdbc.getPassword());
                conn = this.getConnection(_jdbc);
                if (connConfigSettingSql != null) {
                    this.ExecuteSqlInt(connConfigSettingSql, null, false);
                }
                // conn = this.getConnection(_jdbc,true);
                // SqlConnCounter.Add(_sqlconnection.ConnectionString);
            }
            b = conn != null && (!conn.isClosed());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    public void CloseConn() {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            stmt = null;
        }

        if (this.conn != null) {
            try {
                // conn.close();
                // 以防有ResultSet相关时关闭卡住的问题
                Connection tmpConn = conn;
                conn = null;
                new Thread() { // 线程操作
                    public void run() {
                        try {
                            SGDataHelper.ReTry((a,b,c)->{
                                tmpConn.close();
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }

    @Override
    public void close() throws Exception {
        CloseConn();
    }

    protected void doCloseConn(Connection conn2) {
        if (conn2 != null) {
            try {
                // conn.close();
                // 以防有ResultSet相关时关闭卡住的问题
                Connection tmpConn = conn2;
                conn2 = null;
                new Thread() { // 线程操作
                    public void run() {
                        try {
                            tmpConn.close();
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            conn2 = null;
        }
    }

    protected boolean autoCloseConn = true;

    @Override
    public void AutoCloseConn(boolean autoCloseConn) {
        this.autoCloseConn = autoCloseConn;
    }

    @Override
    public int GetInsertedCnt() {
        return insertedCnt;
    }

    @Override
    public long GetLastInsertedId() {
        return lastInsertedId;
    }

    /**
     * @功能描述 增加参数方法 @可能的错误 需要测试全局数据共享问题，以及可能会扩展参数类型 @作者 叶小钗 @修改说明 @修改人
     */
    protected void AddSqlParameter(PreparedStatement ps, PFSqlParameter[] p) throws SQLException {
        for (int j = 0; j < p.length; j++) {
            // wl(p[j].getValue() + "--" + p[j].getType() + "--" + j);
            if (p[j].getType().equals("int")) {
                ps.setInt(j + 1, p[j].getIntValue());
            }
            if (p[j].getType().equals("String")) {
                ps.setString(j + 1, p[j].getValue());
            }
            if (p[j].getType().equals("boolean")) {
                ps.setBoolean(j + 1, p[j].getBoolValue());
            }
            if (p[j].getType().equals("Date")) {
                ps.setDate(j + 1, p[j].getDateValue());
            }
            if (p[j].getType().equals("Blob")) {
                ps.setBlob(j + 1, p[j].getBlobValue());
            }
        }
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

	@Override
	public Object QuerySingleValue(String tableName,String fleld, Consumer<SGSqlWhereCollection> whereAction) {

        SGSqlWhereCollection where = getWhereCollection();
        if (whereAction != null) {
            whereAction.accept(where);
        }
        return QuerySingleValue(
                SGDataHelper.FormatString("select {0} from {1} {2} limit 1", 
                		fleld,
                		tableName, where == null ? "" : where.ToSql()));
	}
    public <T> List<T> QueryList(Class<T> tClass, String sql) {
        // 建立Statement对象
        try {
            if (!OpenConn()) {
                return null;
            }
            stmt = conn.createStatement();
            /** Statement createStatement() 创建一个 Statement 对象来将 SQL 语句发送到数据库。 */
            // 执行数据库查询语句
            if (isSupportQueryTimeout()) {
                //clickhouse不支持,报错 java.sql.SQLFeatureNotSupportedException -- benjamin 20230215
                stmt.setQueryTimeout(CommandTimeOut);
            }
            rs = stmt.executeQuery(sql);

            List<T> result = new ArrayList<T>();
            ResultSetMetaData md = rs.getMetaData();

            int count = md.getColumnCount();
            // 1.获取字段
            // 1.1 获取所有字段 -- 字段数组
            // 可以获取公用和私有的所有字段，但不能获取父类字段
            Field[] fields = tClass.getDeclaredFields();
            List<Field> orderFields = new ArrayList<Field>();
            Map<String, Integer> mdIdMap = new HashMap<String, Integer>();
            for (int i = 1; i <= count; i++) {
                for (Field field : fields) {
                    // if(field.getName().equals(md.getColumnName(i))){
                    if (field.getName().equals(md.getColumnLabel(i))) {
                        field.setAccessible(true);
                        orderFields.add(field);
                        mdIdMap.put(field.getName(), i);
                    }
                }
            }
            // result.add(String.valueOf(orderFields.size()));
            Map<String, IPFSqlFieldTypeConverter> typeConvertMap = new HashMap<String, IPFSqlFieldTypeConverter>();
            while (rs.next()) {
                // Object item=clazz.newInstance();
                T item = tClass.getDeclaredConstructor().newInstance();
                for (Field field : orderFields) {
                    String columnName = field.getName();
                    // // field.set(item, rs.getObject(columnName));
                    // Object o = rs.getObject(columnName);
                    // if (o != null) {
                    // field.set(item, o);
                    // }

                    // Type typeName=field.getGenericType();
                    // if(typeName.getTypeName()=="int"){
                    // field.set(item, rs.getInt(columnName));
                    // }else{
                    // field.set(item, rs.getString(columnName));
                    // }

                    int mdIdx = mdIdMap.get(columnName);
                    Object v = null;
                    int sqlType = md.getColumnType(mdIdx);
                    String sqlTypeName = md.getColumnTypeName(mdIdx);
                    if (md.getColumnType(mdIdx) == java.sql.Types.TIMESTAMP) { // timestamp(数据库中是DateTime)一定要用getDate,否则会多一天,暂不知道怎么解决(用getObject和getTimestamp得到的值都多一天)--benjamin
                        // todo
                        // v=dr.getDate(fieldName);//这样会丢了分和秒
                        v = rs.getTimestamp(columnName, Calendar.getInstance()); // 如果不传Calendar会多了一天
                    } else if (java.sql.Types.CLOB == sqlType && "ntext".equals(sqlTypeName)) {
                        v = rs.getString(mdIdx);
                    } else {
                        v = rs.getObject(columnName);
                    }
                    if (v == null) {
                        // updateItem.Value=v;
                        field.set(item, v);
                    } else {

                        if (!typeConvertMap.containsKey(columnName)) {

//                            typeConvertMap.put(
//                                    columnName,
//                                    PFDataHelper.GetObjectToPFTypeBySqlTypeConverter(
//                                            v,
//                                            md.getColumnType(mdIdx),
//                                            PFSqlFieldType.InitByClass(field.getType())));
                            typeConvertMap.put(columnName, SGDataHelper.GetObjectToPFTypeBySqlTypeConverter(v,
                                    md.getColumnType(mdIdx), SGSqlFieldTypeEnum.InitByClass(field.getType())));
                        }
                        // field.set(item, PFDataHelper.GetObjectToPFTypeBySqlTypeConverter(v,
                        // md.getColumnType(mdIdx),
                        // PFDataHelper.GetPFTypeBySqlType(md.getColumnType(mdIdx))));
                        field.set(item, typeConvertMap.get(columnName).convert(v));
                    }
                }
                result.add(item);
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
            return result;
        } catch (Exception e) {
            // Error=e;
            SetError(e, sql);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SGDataTable GetOneRow(String tableName, Consumer<SGSqlWhereCollection> whereAction) {
        SGSqlWhereCollection where = getWhereCollection();
        if (whereAction != null) {
            whereAction.accept(where);
        }
        return GetDataTable(
                SGDataHelper.FormatString("select top 1 * from {0} {1}", tableName, where == null ? "" : where.ToSql()));
    }

    public SGDataTable GetDataTable(String sql, PFSqlParameter[] p, boolean autoClose) {
        // Connection conn = DB.createConn();
        PreparedStatement ps = null;
        SGDataTable t = null;
        if (!OpenConn()) {
            return null;
        }
        try {
            ResultSet rs = null;
            if (p != null) {
                ps = conn.prepareStatement(sql);
                AddSqlParameter(ps, p);
                ps.setQueryTimeout(CommandTimeOut);
                rs = ps.executeQuery();
            } else {
                if (PFSqlCommandTimeoutSecond.IsHugeSecond(this.CommandTimeOut)) {
                    // mysql大量数据时,这样虽然不会超时,但后面next的速度挺慢的
                    // rows:9.41E+003 -- speed:2.76E+002条/秒
                    rs = this.GetHugeDataReader(sql);
                } else {
                    // mysql大量数据时,这样可能超时,但如果不超时,也可能速度很快
                    stmt = conn.createStatement();
                    stmt.setQueryTimeout(CommandTimeOut);
                    rs = stmt.executeQuery(sql);
                }
            }
            ResultSetMetaData rsmd = rs.getMetaData();

            List<PFDataRow> row = new ArrayList<PFDataRow>(); // 表所有行集合
            List<PFDataColumn> col = null; // 行所有列集合
            PFDataRow r = null; // 单独一行
            PFDataColumn c = null; // 单独一列
            // 此处开始循环读数据，每次往表格中插入一行记录

            // long[] beginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis()
            // };
            // int total=1;

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
                    c.setSqlType(rsmd.getColumnType(i));
                    // 将列信息加入列集合
                    col.add(c);
                }
                // 初始化单元行
                r = new PFDataRow(col);
                // 将行信息降入行结合
                row.add(r);

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // PFDate now = PFDate.Now();
                // long m = now.ToCalendar().getTimeInMillis();
                // System.out.println(PFDataHelper.FormatString("rows:{0} -- " + "speed:{1}",
                // PFDataHelper.ScientificNotation(total),
                //// "10000条/" + ((PFDate.Now().ToCalendar().getTimeInMillis() -
                // beginTime[0]) * 10 / total) + "秒"
                // PFDataHelper.ScientificNotation(
                // Double.valueOf(total) * 1000 / (m - beginTime[0])) + "条/秒"));
                // total++;
            }
            // 得到数据表
            t = new SGDataTable(row);
            if (autoClose) {
                // rs.close();
                this.CloseReader(rs);
            }
            rs = null;
        } catch (SQLException e) {
            // e.printStackTrace();
            SetError(e, sql);
            PrintError();
        } finally {
            try {
                if (autoClose && autoCloseConn) {
                    if (p != null) {
                        ps.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                        stmt = null;
                    }
                    conn.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // DB.close(ps);
            // DB.close(conn);
        }
        return t;
    }

    public SGDataTable GetDataTable(String sql, PFSqlParameter[] p) {
        return GetDataTable(sql, p, true);
    }

    public SGDataTable GetDataTable(String sql) {
        return GetDataTable(sql, null);
    }

    /**
     * @param tableName
     * @return
     * @deprecated 尽量用 GetPs(String tableName, List<String> fieldNames)
     */
    @Deprecated
    public PreparedStatement GetPs(String tableName) {
        return GetPs(tableName, null, false);
//		try {
//			OpenConn();
//			// Object aa=getTidbVar();
//
//			// PFMySqlExecute dstQueryExec = (PFMySqlExecute) PFSqlExecute.Init(_jdbc);
//			ISqlExecute dstQueryExec = PFSqlExecute.Init(_jdbc);
//			// List<SGSqlFieldInfo> fields=this.GetTableFields(tableName);
//			List<SGSqlFieldInfo> fields = dstQueryExec.GetTableFields(tableName); // GetTableFields不要从this调用，免得应该ps的conn状态
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
    }

//	@Deprecated
//	public PreparedStatement GetPs(String tableName, List<String> fieldNames) {
//		try {
//			OpenConn();
//			// Object aa=getTidbVar();
//
//			if (fieldNames == null) {
//
//				// PFMySqlExecute dstQueryExec = (PFMySqlExecute) PFSqlExecute.Init(_jdbc);
//				// // List<SGSqlFieldInfo> fields=this.GetTableFields(tableName);
//				// List<SGSqlFieldInfo> fields = dstQueryExec.GetTableFields(tableName);//
//				// GetTableFields不要从this调用，免得应该ps的conn状态
//				// String[] commaList = new String[fields.size()];
//				// Arrays.fill(commaList, "?");
//
//				List<SGSqlFieldInfo> fields = GetTableFields(tableName);
//				fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
//			}
//			String[] commaList = new String[fieldNames.size()];
//			Arrays.fill(commaList, "?");
//
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

    /**
     * 是否支持 PreparedStatement.setQueryTimeout方法
     *
     * @return
     */
    protected boolean isSupportQueryTimeout() {
//		if(PFSqlType.ClickHouse==_jdbc.GetSqlType()){
//			//已知com.github.housepower.jdbc.ClickHouseDriver驱动不支持
//			return false;
//		}
        return true;
    }

    @Override
    public PreparedStatement GetPs(String tableName, List<String> fieldNames, boolean autoCommit) {
        try {
            OpenConn();
            // Object aa=getTidbVar();

            if (fieldNames == null) {

                // PFMySqlExecute dstQueryExec = (PFMySqlExecute) PFSqlExecute.Init(_jdbc);
                // // List<SGSqlFieldInfo> fields=this.GetTableFields(tableName);
                // List<SGSqlFieldInfo> fields = dstQueryExec.GetTableFields(tableName);//
                // GetTableFields不要从this调用，免得应该ps的conn状态
                // String[] commaList = new String[fields.size()];
                // Arrays.fill(commaList, "?");

                List<SGSqlFieldInfo> fields = GetTableFields(tableName);
                fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
            }
            String[] commaList = new String[fieldNames.size()];
            Arrays.fill(commaList, "?");

            OpenConn();
            if (conn.getAutoCommit() != autoCommit) {
                conn.setAutoCommit(autoCommit);
            }
            PreparedStatement crs = conn
                    .prepareStatement(SGDataHelper.FormatString("insert into {0}  ({1})  values ({2})", tableName,
                            String.join(",", fieldNames), String.join(",", commaList)));
//			if (!autoCommit) {
//				conn.setAutoCommit(false);
//			}
            if (isSupportQueryTimeout()) {
                crs.setQueryTimeout(CommandTimeOut);// 加上超时时间看看能不能解决跨线程使用时报错已经关闭的问题 -- benjamin 20221024
            }
            return crs;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            SetError(e);
        }
        return null;
    }
    public PreparedStatement GetUpdatePs(String tableName, List<String> fieldNames,String[] idFieldNames, boolean autoCommit) {
        try {
            OpenConn();
            // Object aa=getTidbVar();

            if (fieldNames == null) {

                List<SGSqlFieldInfo> fields = GetTableFields(tableName);
                fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
            }
//            String[] commaList = new String[fieldNames.size()];
//            Arrays.fill(commaList, "?");
//            List<String> updateFieldNames=new ArrayList<>(fieldNames);
////            List<String> idFieldNamesList=new ArrayList<>(idFieldNames);
////            for (String i:fieldNames
////                 ) {
////                if(!idFieldNamesList.contains())
////            }
            List<String> updateFieldNames=SGDataHelper.ListWhere(fieldNames,a->!SGDataHelper.ArrayAny(idFieldNames,b->b.equals(a)));
            //String updateCmd =String.join(",",fieldNames.stream().map(a->a+"=?").collect(Collectors.toList()));
            String updateCmd =String.join(",",updateFieldNames.stream().map(a->a+"=?").collect(Collectors.toList()));
            String whereCmd =String.join(" and ",Arrays.asList(idFieldNames).stream().map(a->a+"=?").collect(Collectors.toList()));

            OpenConn();
            if (conn.getAutoCommit() != autoCommit) {
                conn.setAutoCommit(autoCommit);
            }
            String sql=SGDataHelper.FormatString("update {0} set {1} where {2}", tableName,
                    updateCmd,whereCmd);
            PreparedStatement crs = conn
                    .prepareStatement(sql);
//			if (!autoCommit) {
//				conn.setAutoCommit(false);
//			}
            if (isSupportQueryTimeout()) {
                crs.setQueryTimeout(CommandTimeOut);// 加上超时时间看看能不能解决跨线程使用时报错已经关闭的问题 -- benjamin 20221024
            }
            return crs;
        } catch (Exception e) {
            e.printStackTrace();
            SetError(e);
        }
        return null;
    }
    public PreparedStatement GetDeletePs(String tableName, List<String> fieldNames, boolean autoCommit) {
        try {
            OpenConn();
            // Object aa=getTidbVar();

            if (fieldNames == null) {

                List<SGSqlFieldInfo> fields = GetTableFields(tableName);
                fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
            }
//            String[] commaList = new String[fieldNames.size()];
//            Arrays.fill(commaList, "?");

            String whereCmd =String.join(" and ",fieldNames.stream().map(a->a+"=?").collect(Collectors.toList()));

            OpenConn();
            if (conn.getAutoCommit() != autoCommit) {
                conn.setAutoCommit(autoCommit);
            }
            PreparedStatement crs = conn
                    .prepareStatement(SGDataHelper.FormatString(" delete from {0} where {1}", tableName,
                            whereCmd));
//			if (!autoCommit) {
//				conn.setAutoCommit(false);
//			}
            if (isSupportQueryTimeout()) {
                crs.setQueryTimeout(CommandTimeOut);// 加上超时时间看看能不能解决跨线程使用时报错已经关闭的问题 -- benjamin 20221024
            }
            return crs;
        } catch (Exception e) {
            e.printStackTrace();
            SetError(e);
        }
        return null;
    }


    @Override
    public PreparedStatement updatePs(PreparedStatement crs, ResultSetMetaData md, int mdIdx, SqlUpdateItem dstInsertI)
            throws SQLException {

        String colName = md.getColumnLabel(mdIdx);
        BatchInsertOption insertOption = GetInsertOption();

//        if("deposit_no".equals(colName)) {
//        	String aa="aa";
//        }
        try {
            // 注意,tidb有缺陷,即便列是可空,不执行crs.set()直接addBatch也会报错:java.sql.SQLException: No value
            // specified for parameter 1
            int dataT = md.getColumnType(mdIdx);

            String dataTName = md.getColumnTypeName(mdIdx);
            //注意,sqlsqlserver中,用net.sourceforge.jtds.jdbc.Driver驱动时,date格式,上面是 12 和 nvarchar, 很奇怪, 暂不确定怎么处理好;
            // 后来发现,改用com.microsoft.sqlserver.jdbc.SQLServerDriver驱动,就是91 date,这个驱动也有优点,类型更准确

            Object v = dstInsertI.Value;
            Object convertedValue = null;
            if (null != v) {
                if (null == dstInsertI.convertTo) {
//					dstInsertI.convertTo = insertOption.convertNullTo0
//							? PFDataHelper.GetObjectToSqlTypeByPFTypeConverter0(v, dstInsertI.getSrcDataPFType(), dataT,
//									dataTName//,zoneId
//					)
//							: PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(v, dstInsertI.getSrcDataPFType(), dataT,
//									dataTName//,zoneId
//					);

                    if (java.sql.Types.NUMERIC == dataT) {
                        int precision = md.getPrecision(mdIdx);
//						if(precision>=19) {
//							dstInsertI.convertTo=insertOption.convertNullTo0? PFSqlFieldTypeConverter.DecimalConverter0(v):PFSqlFieldTypeConverter.DecimalConverter(v);
//						}else if(precision>=10) {
//							dstInsertI.convertTo=insertOption.convertNullTo0?PFSqlFieldTypeConverter.LongConverter0(v):PFSqlFieldTypeConverter.LongConverter(v);
//						}else if(precision>=5) {
//							dstInsertI.convertTo=insertOption.convertNullTo0?PFSqlFieldTypeConverter.IntConverter0(v):PFSqlFieldTypeConverter.IntConverter(v);
//						}else {
//							dstInsertI.convertTo=insertOption.convertNullTo0?PFSqlFieldTypeConverter.IntConverter0(v):PFSqlFieldTypeConverter.IntConverter(v);
//						}
                        dstInsertI.convertTo = insertOption.convertNullTo0
                                ? SGDataHelper.GetObjectToSqlTypeByPFTypeConverter0(v, dstInsertI.getSrcDataPFType(), dataT,
                                dataTName, precision//,zoneId
                        )
                                : SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(v, dstInsertI.getSrcDataPFType(), dataT,
                                dataTName, precision//,zoneId
                        );
                    } else {
                        dstInsertI.convertTo = insertOption.convertNullTo0
                                ? SGDataHelper.GetObjectToSqlTypeByPFTypeConverter0(v, dstInsertI.getSrcDataPFType(), dataT,
                                dataTName, null//,zoneId
                        )
                                : SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(v, dstInsertI.getSrcDataPFType(), dataT,
                                dataTName, null//,zoneId
                        );
                    }
                }
                try {
                    convertedValue = dstInsertI.convertTo.convert(v);
                } catch (Exception e) {
                    //LOGGER.error("convertTo转换失败,相关列:" + colName, e);
                    SGDataHelper.getLog().printException(e,tag+".updatePs");
                    //这个容错转换未写完,只写了decimal部分
                    //容错是有必要的,因为如果经过json序列化,就有可能同1列同时出现int和decimal
//					dstInsertI.convertTo = insertOption.convertNullTo0
//							? PFDataHelper.GetObjectToSqlTypeByPFTypeFaultTolerantConverter0(v, dstInsertI.getSrcDataPFType(), dataT,
//									dataTName//,zoneId
//					)
//							: PFDataHelper.GetObjectToSqlTypeByPFTypeFaultTolerantConverter(v, dstInsertI.getSrcDataPFType(), dataT,
//									dataTName//,zoneId
//					);
                    if (java.sql.Types.NUMERIC == dataT) {
                        int precision = md.getPrecision(mdIdx);
//						if(precision>=19) {
//							dstInsertI.convertTo=insertOption.convertNullTo0? a->{return PFDataHelper.ObjectToDecimal0(a);}:a->{return PFDataHelper.ObjectToDecimal(a);};
//						}else if(precision>=10) {
//							dstInsertI.convertTo=insertOption.convertNullTo0?a->{return PFDataHelper.ObjectToLong0(a);}:a->{return PFDataHelper.ObjectToLong(a);};
//						}else if(precision>=5) {
//							dstInsertI.convertTo=insertOption.convertNullTo0?a->{return PFDataHelper.ObjectToInt0(a);}:a->{return PFDataHelper.ObjectToInt(a);};
//						}else {
//							dstInsertI.convertTo=insertOption.convertNullTo0?a->{return PFDataHelper.ObjectToInt0(a);}:a->{return PFDataHelper.ObjectToInt(a);};
//						}
                        dstInsertI.convertTo = insertOption.convertNullTo0
                                ? SGDataHelper.GetObjectToSqlTypeByPFTypeFaultTolerantConverter0(v, dstInsertI.getSrcDataPFType(), dataT,
                                dataTName, precision//,zoneId
                        )
                                : SGDataHelper.GetObjectToSqlTypeByPFTypeFaultTolerantConverter(v, dstInsertI.getSrcDataPFType(), dataT,
                                dataTName, precision//,zoneId
                        );
                    } else {
                        dstInsertI.convertTo = insertOption.convertNullTo0
                                ? SGDataHelper.GetObjectToSqlTypeByPFTypeFaultTolerantConverter0(v, dstInsertI.getSrcDataPFType(), dataT,
                                dataTName, null//,zoneId
                        )
                                : SGDataHelper.GetObjectToSqlTypeByPFTypeFaultTolerantConverter(v, dstInsertI.getSrcDataPFType(), dataT,
                                dataTName, null//,zoneId
                        );
                    }
                    convertedValue = dstInsertI.convertTo.convert(v);
                    //throw e;
                }
                // convertedValue = dstInsertI.ConvertTo2.convert(v);
            }
            if (java.sql.Types.DECIMAL == dataT) { // 如果直接updateObject,云徒订单的Totalmoney字段会有100倍溢出的问题--benjamin20210112
                // Object v = dstInsert.get(colName).Value;
                if (convertedValue != null) {
                    BigDecimal vD = SGDataHelper.ObjectAs(convertedValue);
                    vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP); // 我后来觉得,精度应该是在set到insert的时候设置才对的--

                    crs.setBigDecimal(mdIdx, vD);
                } else {
                    if (insertOption.convertNullTo0) {
                        crs.setBigDecimal(mdIdx, new BigDecimal(0));
                    } else {
                        crs.setObject(mdIdx, null);
                    }
                }
            } else if (java.sql.Types.NUMERIC == dataT) {// 如果直接updateObject,numeric的小数位数会影响结果
                /**
                 * numeric[1,4]是Short
                 numeric[5,9]是Integer
                 numeric[10,18]是Long
                 numeric[19]及以上是BigDecimal
                 */
                //Object v = dstInsert.get(colName).Value;
                if (convertedValue != null) {
                    int precision = md.getPrecision(mdIdx);
                    if (precision >= 19) {
//							BigDecimal vD = PFDataHelper.ObjectToDecimal(v);
//							vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP);
                        crs.setBigDecimal(mdIdx, (BigDecimal) convertedValue);
                    } else if (precision >= 10) {
                        //Long vD = PFDataHelper.ObjectToLong(v);
                        crs.setLong(mdIdx, (Long) convertedValue);
                    } else if (precision >= 5) {
                        //Integer vD = PFDataHelper.ObjectToInt(v);
                        crs.setInt(mdIdx, (Integer) convertedValue);
                    } else {
                        //Integer vD = PFDataHelper.ObjectToInt(v);
                        crs.setInt(mdIdx, (Integer) convertedValue);
                    }
                } else {
                    crs.setInt(mdIdx, insertOption.convertNullTo0 ? 0 : null);
                }
            } else if (SGSqlFieldTypeEnum.Int == dstInsertI.getDstDataPFType()) {
                if (convertedValue != null) {
                    crs.setObject(mdIdx, convertedValue);
                } else {
                    if (insertOption.convertNullTo0) {
                        crs.setObject(mdIdx, 0);
                    } else {
                        crs.setObject(mdIdx, null);
                    }
                }
            } else if (java.sql.Types.TIMESTAMP == dataT) {
                if (convertedValue != null) {
                    if (null != zoneId) {
                        //当程序时区和数据库时区不一致时,这样可以保证插入的时间也是对的
                        crs.setTimestamp(mdIdx, SGDataHelper.ObjectAs(convertedValue), Calendar.getInstance(TimeZone.getTimeZone(zoneId)));
                    } else {
                        crs.setTimestamp(mdIdx, SGDataHelper.ObjectAs(convertedValue));
                    }
                    //crs.setTimestamp(mdIdx, PFDataHelper.ObjectAs(convertedValue));
                } else {
                    crs.setObject(mdIdx, null);
                }
            } else if (Types.DATE == dataT) {
                if (convertedValue != null) {
                    crs.setTimestamp(mdIdx, SGDataHelper.ObjectAs(convertedValue));
                } else {
                    crs.setObject(mdIdx, null);
                }
            } else if (Types.VARCHAR == dataT || Types.NVARCHAR == dataT
                    || SGSqlFieldTypeEnum.String == dstInsertI.getDstDataPFType()) {
                if (convertedValue != null) {
                    crs.setObject(mdIdx, convertedValue);
                } else {
                    if (insertOption.convertNullTo0) {
                        crs.setObject(mdIdx, "");
                    } else {
                        crs.setObject(mdIdx, null);
                    }
                }
            } else {
                crs.setObject(mdIdx, convertedValue);
            }
        } catch (Exception e) {
            //LOGGER.error(PFDataHelper.FormatString("updatePs() [{0}]列转换报错\r\n{1}", colName, e.toString()));
            SGDataHelper.WriteErrors(Arrays.asList(e, new Exception(SGDataHelper.FormatString("updatePs() [{0}]列转换报错", colName))));
            throw e;
        }
        // }
        return crs;
    }

//	@Override
//	public PreparedStatement insertIntoCachedRowSet(PreparedStatement crs, ResultSetMetaData md,
//			PFSqlInsertCollection dstInsert
//	// ,Map<String,Integer> srcColumnType
//	// ,Map<String,Integer> dstColumnType
//	) throws SQLException {
//		// 移动指针到“插入行”，插入行是一个虚拟行
//		// crs.moveToInsertRow();
//
//		//System.out.println("--------------------");
//		for (int i = 0; i < md.getColumnCount(); i++) {
//			int mdIdx = i + 1;
//			String colName = md.getColumnLabel(mdIdx);
//
//			if (dstInsert.containsKey(colName)) {
//				SqlUpdateItem dstInsertI = dstInsert.get(colName);
//				//System.out.println(colName);
//				updatePs(crs, md, mdIdx, dstInsertI);
//			}
//		}
//
//		//System.out.println("--------------------");
//		// // 插入虚拟行
//		// crs.insertRow();
//		// // 移动指针到当前行
//		// crs.moveToCurrentRow();
//		crs.addBatch(); // java.sql.SQLException: Not all parameters binded (placeholder 3 is
//		// undefined)
//		// clickhouse每列都要设置值?
//		return crs;
//	}

    @Override
    public PreparedStatement insertIntoCachedRowSet(PreparedStatement crs, ResultSetMetaData md,
                                                    BaseSqlUpdateCollection dstInsert
                                                    // ,Map<String,Integer> srcColumnType
                                                    // ,Map<String,Integer> dstColumnType
    ) throws SQLException {
        // 移动指针到“插入行”，插入行是一个虚拟行
        // crs.moveToInsertRow();

        //System.out.println("--------------------");
        for (int i = 0; i < md.getColumnCount(); i++) {
            int mdIdx = i + 1;
            String colName = md.getColumnLabel(mdIdx);

            if (dstInsert.containsKey(colName)) {
                SqlUpdateItem dstInsertI = dstInsert.get(colName);
                //System.out.println(colName);
                updatePs(crs, md, mdIdx, dstInsertI);
            }
        }

        //System.out.println("--------------------");
        // // 插入虚拟行
        // crs.insertRow();
        // // 移动指针到当前行
        // crs.moveToCurrentRow();
        crs.addBatch(); // java.sql.SQLException: Not all parameters binded (placeholder 3 is
        // undefined)
        // clickhouse每列都要设置值?
        return crs;
    }

    /**
     * 感觉没必要全字段
     *
     * @deprecated 优先使用GetMetaData(String tableName, SqlInsertCollection insert)
     */
    public ResultSetMetaData GetMetaData(String tableName) {
        try {
            OpenConn();

            List<SGSqlFieldInfo> fields = this.GetTableFields(tableName);
            String[] commaList = new String[fields.size()];
            Arrays.fill(commaList, "?");
            // PreparedStatement crs;
            List<String> fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
            OpenConn();
            //// ResultSet rs =GetDataReader("select * from "+ tableName+" where 1=0");
            //// ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where
            //// 1=0");
            //// ResultSet rs =GetDataReaderUpdatable("select Orderno from "+ tableName+"
            //// where
            // 1=0");//ok
            // ResultSet rs =GetDataReaderUpdatable("select * from "+ tableName+" where
            //// 1=0");
            // //ok 但主键等标识列用来更新会有问题
            // ResultSet rs = GetDataReader(PFDataHelper.FormatString("select {0} from {1}
            // ",String.join(",",fieldNames), tableName));
            ResultSet rs = GetDataReader(SGDataHelper.FormatString("select {0} from {1} where 1=0",
                    String.join(",", fieldNames), tableName));

            // ResultSet rs = GetDataReader(PFDataHelper.FormatString("select * from {0}
            // where
            // 1=0",
            // tableName));
            ResultSetMetaData r = rs.getMetaData();
            this.CloseReader(rs);
            return r;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            SetError(e);
        }
        return null;
    }

    // public ResultSetMetaData GetMetaData(String tableName, SqlInsertCollection
    // insert) {
    // try {
    // OpenConn();
    // List<String> fieldNames = null;
    // if (insert != null) {
    // fieldNames = new ArrayList<>(insert.keySet());
    // } else {
    // List<SGSqlFieldInfo> fields = this.GetTableFields(tableName);
    // String[] commaList = new String[fields.size()];
    // Arrays.fill(commaList, "?");
    // // PreparedStatement crs;
    // fieldNames = fields.stream().map(a ->
    // a.getFieldName()).collect(Collectors.toList());
    // }
    //
    // OpenConn();
    //
    // ResultSet rs = GetDataReader(PFDataHelper.FormatString("select {0} from {1}
    // where 1=0",
    // String.join(",", fieldNames), tableName));
    //
    // return rs.getMetaData();
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // SetError(e);
    // }
    // return null;
    // }
    public ResultSetMetaData GetMetaData(String tableName, List<String> fieldNames) {
        try {
            OpenConn();
            if (fieldNames != null) {
                // fieldNames = new ArrayList<>(insert.keySet());
            } else {
                List<SGSqlFieldInfo> fields = this.GetTableFields(tableName);
                // String[] commaList = new String[fields.size()];
                // Arrays.fill(commaList, "?");
                // // PreparedStatement crs;
                fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
            }

            OpenConn();

            String sql = SGDataHelper.FormatString("select {0} from {1} where 1=0", String.join(",", fieldNames),
                    tableName);
            ResultSet rs = GetHugeDataReader(sql);

            // return rs.getMetaData();
            ResultSetMetaData r = rs.getMetaData();
            this.CloseReader(rs);
            this.CloseConn();
            return r;
        } catch (Exception e) {
            // e.printStackTrace();
            SetError(e);
        }
        return null;
    }

    public ResultSetMetaData GetMetaDataNotClose(String tableName, List<String> fieldNames) {
        try {
            OpenConn();
            if (fieldNames != null) {
                // fieldNames = new ArrayList<>(insert.keySet());
            } else {
                List<SGSqlFieldInfo> fields = this.GetTableFields(tableName);
                // String[] commaList = new String[fields.size()];
                // Arrays.fill(commaList, "?");
                // // PreparedStatement crs;
                fieldNames = fields.stream().map(a -> a.getFieldName()).collect(Collectors.toList());
            }

            OpenConn();

            String sql = SGDataHelper.FormatString("select {0} from {1} where 1=0", String.join(",", fieldNames),
                    tableName);
            ResultSet rs = GetHugeDataReader(sql);

            // return rs.getMetaData();
            ResultSetMetaData r = rs.getMetaData();
            //this.CloseReader(rs);
            //this.CloseConn();
            return r;
        } catch (Exception e) {
            // e.printStackTrace();
            SetError(e);
        }
        return null;
    }

    /**
     * @功能描述 执行一条select语句返回一张数据表，支持多表查询 @可能的错误 @作者 叶小钗 @修改说明 @修改人 @使用方法: String sql
     * = "select * from kpxz where fbh=? order by kpxzsx asc"; SqlParameter[]
     * p = new SqlParameter[1]; p[0] = new SqlParameter("int", pId); return
     * db.getDataTable(sql, p);
     */
    @Override
    public ResultSet GetDataReader(String sql) {
        // 建立Statement对象
        try {
            OpenConn();
            stmt = conn.createStatement();
            stmt.setQueryTimeout(CommandTimeOut);
            /** Statement createStatement() 创建一个 Statement 对象来将 SQL 语句发送到数据库。 */
            // 执行数据库查询语句
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            SetError(e);
            // e.printStackTrace();
            rs = null;
        }
        return rs;
    }

    @Override
    public Boolean CloseReader(ResultSet reader) {
        try {
            // if(reader.isClosed()) {return true;}
            if (null != stmt) {
                stmt.cancel();
                stmt = null;
            }
            // sqlCmd.Cancel();//如果没有这句,数据很多时 dr.Close 会很慢
            // https://www.cnblogs.com/xyz0835/p/3379676.html

            // reader.close();
            // 当使用GetHugeDataReader时，如果数据很大，中途close会非常慢，原因未明--benjamintodo20210223
            new Thread() { // 线程操作
                public void run() {
                    try {
                        reader.close();
                    } catch (SQLException e) {
                        // e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return true;
    }

    /**
     * 测试通过: 从sqlserver迁移到tidb
     * <p>
     * 注意用这个方法插入到SqlServer时非常慢,应该和processBatch和autoCommit有关,用HugeBulkReader吧
     */
    @Override
    public Boolean HugeInsertReader(SGSqlInsertCollection dstInsert, ResultSet rdr, String tableName,
                                    Consumer<BaseSqlUpdateCollection> rowAction, Consumer<Integer> sqlRowsCopiedAction,
                                    Predicate<Boolean> stopAction) {

        if (rdr == null) {
            SetError(new Exception("DataReader不能为空"));
            return false;
        }

        BatchInsertOption insertOption = GetInsertOption();

        if (!OpenConn()) {
            return false;
        }

        int idx = 0;
        insertedCnt = 0; // 已插入的行数

        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
        int batchCnt = 0;

        Boolean hasUnDo = false;

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

        StringBuilder sb = new StringBuilder();
        SGSqlCommandString scs = new SGSqlCommandString();
        int oneThousandCount = 0;
        try {
            // //保存来源的类型，便于转换
            ResultSetMetaData srcMd = rdr.getMetaData();

            boolean hasSysLimitId = false;
            // String sysLimitIdFieldName = "sys_limit_id";
            String sysLimitIdFieldName = sys_limit_id;

            long currentSysLimitId = -1;
            lastInsertedId = -1;

            // //Map<String,Integer> columnIdx=new HashMap<String,Integer>();
            // Map<String,Integer> srcColumnType=new HashMap<String,Integer>();
            List<String> srcFieldNames = new ArrayList<String>();
            for (int i = 0; i < srcMd.getColumnCount(); i++) {
                String fieldName = srcMd.getColumnLabel(i + 1);
                // srcColumnType.put(srcMd.getColumnLabel(i+1),srcMd.getColumnType(i+1));
                if (sysLimitIdFieldName.equals(fieldName)) { // 这个内部值用来代替limit的offset
                    hasSysLimitId = true;
                    continue;
                }
                srcFieldNames.add(fieldName);
            }

            // ResultSetMetaData dstMd = GetMetaData(tableName);
            ResultSetMetaData dstMd = GetMetaData(tableName, srcFieldNames);

            // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
            if (dstInsert == null) {
                dstInsert = getInsertCollection(dstMd);
            }

            while (true) {
                boolean hasNext = rdr.next();
                if (hasNext) {

                    // dstInsert.UpdateByDataReaderAutoConvert(rdr);
                    dstInsert.UpdateByDataReader(rdr);// 其实没必要为了rowAction面AutoConvert,因为反正处理的是Object类型,在rowAction中是要用户自行转换1次的
                    if (rowAction != null) {
                        rowAction.accept(dstInsert);
                    }

                    // 更新行
                    // crs = insertIntoCachedRowSet(crs, dstInsert);// ,dstColumnType);

                    if (oneThousandCount > 999) // sqlserver里values最多1000行,但tidb没有这个限制(但这句留着备用)(注释这句的话,可以从19秒减少到14秒完成)
                    {
                        oneThousandCount = 0;
                        // sb.append(PFDataHelper.FormatString("; insert into {0}({1})
                        // values({2})", tableName,
                        // insert.ToKeysSql(), insert.ToValuesSql()));
                        scs.add(sb.toString());
                        sb = new StringBuilder();
                        sb.append(SGDataHelper.FormatString("insert into {0}({1}) values({2})", tableName,
                                dstInsert.ToKeysSql(), dstInsert.ToValuesSql()));
                    } else if (oneThousandCount == 0) {
                        sb.append(SGDataHelper.FormatString(" insert into {0}({1}) values({2})", tableName,
                                dstInsert.ToKeysSql(), dstInsert.ToValuesSql()));
                    } else {
                        sb.append(SGDataHelper.FormatString(",({0})", dstInsert.ToValuesSql()));
                    }

                    batchCnt++;
                    oneThousandCount++;
                    hasUnDo = true;
                    if (hasSysLimitId) {
                        currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
                    }
                }
                if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {

                    if (sb.length() > 0) {
                        scs.add(sb.toString());
                        sb = new StringBuilder();
                    }
//                    // Boolean b = ExecuteSql(sb.toString());
//                    String sss=JSON.toJSONString(scs);
//                    PFDataHelper.WriteLocalTxt(sss, "test001", LocalDataType.Deletable);
                    // PFDataHelper.writeObject(scs, "test001.txt", LocalDataType.Deletable);
                    Boolean b = ExecuteSql(scs);

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
                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
                            CloseConn();
                            return true;
                        }
                    }
                    // sb.Clear();
                    sb = new StringBuilder();
                    scs.clear();
                    // crs = GetCrs(tableName);
                    // crs = GetRs(tableName);
                    // deleteSb.Clear();

                    hasUnDo = false;
                    batchCnt = 0;
                    oneThousandCount = 0;
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
            SetError(e);
            return false;
        }

        return true;
    }

    /**
     * 注意： 1.因为目标表是Sqlserver里的，如果目标表有中文字段，一定要用nvarhcar,如果用了varchar,bulk不支持，会报错 列长度不够
     * 2.如果提供insert参数，需保证其包含主键
     */
    @Override
    public <T> Boolean HugeBulkList(SGSqlInsertCollection dstInsert, List<T> list,
                                    // Class<T> tClass,
                                    String tableName,
                                    // Consumer<BatchInsertOption> insertOptionAction,
                                    // Consumer<BaseSqlUpdateCollection> rowAction,
                                    SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
                                    Predicate<Boolean> stopAction) {

        // 先参考mysql的方式看行不行
        // TODO Auto-generated method stub
        if (list == null) {
            SetError(new Exception("DataReader不能为空"));
            return false;
        }

        // BatchInsertOption insertOption = GetInsertOption();

        OpenConn();

        // int idx = 0;

        insertedCnt = 0; // 已插入的行数

        // int batchSize = insertOption.getProcessBatch();// 50000;//
        // tidb设置大些试试,测试100万行/25秒
        // int batchCnt = 0;
        //
        // Boolean hasUnDo = false;

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

        // PFSqlInsertCollection srcInsert =null;
        // List<String> srcFieldNames=null;
        // if(list!=null&&(!list.isEmpty())){
        // srcInsert=getInsertCollection();
        // srcInsert.InitItemByModel(list.get(0));
        // srcFieldNames=new ArrayList<String>( srcInsert.keySet());
        // }
        int insertCnt = list.size();
        return this.doInsertList( // null,
                // srcFieldNames,
                null, tableName,
                // list.size(),
                (a, b, c) -> a < insertCnt, (a) -> list.get(a), rowAction, sqlRowsCopiedAction, stopAction);

        // 下面的代码封装到 doInsertList() -- benjamin 20220602
        // // int oneThousandCount = 0;
        // try {
        //// //保存来源的类型，便于转换
        // // ResultSetMetaData srcMd = rdr.getMetaData();
        //
        //// boolean hasSysLimitId = false;
        //// String sysLimitIdFieldName = "sys_limit_id";
        //// long currentSysLimitId = -1;
        // // lastInsertedId = -1;
        //
        //// for (int i = 0; i < srcMd.getColumnCount(); i++) {
        //// String fieldName = srcMd.getColumnLabel(i + 1);
        //// if (sysLimitIdFieldName.equals(fieldName)) {// 这个内部值用来代替limit的offset
        //// hasSysLimitId = true;
        //// continue;
        //// }
        //// }
        //
        // //// CachedRowSet crs = GetCrs(tableName);
        // // ResultSet crs = GetRs(tableName);
        // // ResultSetMetaData dstMd = crs.getMetaData();
        //
        // SqlInsertCollection srcInsert =null;
        // List<String> srcFieldNames=null;
        // if(list!=null&&(!list.isEmpty())){
        // srcInsert=getInsertCollection();
        // srcInsert.InitItemByModel(list.get(0));
        // srcFieldNames=new ArrayList<String>( srcInsert.keySet());
        // }
        //
        // ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
        // :GetMetaData(tableName,srcFieldNames);
        //
        //// List<SGSqlFieldInfo> fields=this.GetTableFields(tableName);
        //// String[] commaList=new String[fields.size()];
        //// Arrays.fill(commaList,"?");
        //// PreparedStatement crs;
        //// List<String>
        // fieldNames=fields.stream().map(a->a.getFieldName()).collect(Collectors.toList());
        //// crs = (PreparedStatement) conn.prepareStatement(
        //// PFDataHelper.FormatString("insert into {0} ({1}) values ({2})",
        // tableName,String.join(",",fieldNames),String.join(",",commaList))
        //// );
        //// conn.setAutoCommit(false);
        //
        // PreparedStatement ps =srcFieldNames==null? GetPs(tableName):
        // GetPs(tableName,srcFieldNames);
        //
        // // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
        // if (dstInsert == null) {
        //
        // dstInsert = getInsertCollection(dstMd);
        // }
        //
        // int cnt = list.size();
        // while (true) {
        // // boolean hasNext = rdr.next();
        // T item = null;
        // boolean hasNext = idx < cnt;
        // if (hasNext) {
        // item = list.get(idx);
        // }
        // if (hasNext) {
        //
        // // dstInsert.UpdateByDataReaderAutoConvert(rdr);
        // //dstInsert.UpdateModelValue(item);
        // dstInsert.UpdateModelValueAutoConvert(item);
        // if (rowAction != null) {
        // rowAction.accept(dstInsert);
        // }
        //
        // ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
        //
        // batchCnt++;
        // hasUnDo = true;
        //// if (hasSysLimitId) {
        //// currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
        //// }
        // }
        //// if (batchCnt > batchSize)
        // if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
        //
        // //// Boolean b = ExecuteSql(sb.toString());
        // // Boolean b = DoBulkReader(dstInsert, ps, tableName, batchSize == 1);
        // Boolean b = false;
        // try {
        // ps.executeBatch();
        // conn.commit();
        // b = true;
        // } catch (Exception e) {
        // SetError(e);
        // }
        //
        // if (!b) {
        // // CloseReader(rdr);
        // CloseConn();
        // return false;
        // } else {
        // insertedCnt = idx + 1;
        //// if (hasSysLimitId) {
        //// lastInsertedId = currentSysLimitId;
        //// }
        // }
        // if (sqlRowsCopiedAction != null) {
        // sqlRowsCopiedAction.accept(idx);
        // }
        // if (stopAction != null) {
        // if (stopAction.test(true)) {// 允许中途终止--benjamin20200812
        // // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
        // CloseConn();
        // return true;
        // }
        // }
        //
        //// // crs = GetCrs(tableName);
        //// ps = GetPs(tableName);
        // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
        //// crs = GetRs(tableName);
        //
        // hasUnDo = false;
        // batchCnt = 0;
        // // oneThousandCount = 0;
        // } else {
        // // batchCnt++;
        // // oneThousandCount++;
        // }
        // if (!hasNext) {
        // break;
        // }
        // idx++;
        //
        // }
        //
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // SetError(e);
        // return false;
        // }
        //
        // CloseConn();
        // return true;
    }

    public <T> boolean HugeUpdateList(SGSqlUpdateCollection update, List<T> list, String tableName,
                                      // Action<BatchInsertOption> insertOptionAction,
                                      // Func<MySqlUpdateCollection, T, bool> rowAction,//考虑这个是否必要
                                      // Action<int> sqlRowsUpdatedAction = null

                                      SGFunc<BaseSqlUpdateCollection, T, Object, Boolean> rowAction, Consumer<Integer> sqlRowsUpdatedAction,
                                      Predicate<Boolean> stopAction) {

        // var insertOption = new BatchInsertOption();
        // if (insertOptionAction != null) { insertOptionAction(insertOption); }
        BatchInsertOption insertOption = GetInsertOption();

        // string sql = "";
        // StringBuilder sb = new StringBuilder();
        int idx = 0;
        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
        int batchCnt = 0;
        boolean hasUnDo = false;
        SGSqlCommandString sql = new SGSqlCommandString();
        for (T i : list) {

            if (insertOption.getAutoUpdateModel()) {
                // update.UpdateModelValue(i);
                update.UpdateModelValueAutoConvert(i);
            }
            if (rowAction != null) {
                if (!rowAction.go(update, i, null)) {
                    continue;
                }
            }

            // sb.append(PFDataHelper.FormatString(" update {0} set {1} {2};", tableName,
            // update.ToSetSql(),
            // update.ToWhereSql()));
            sql.add(SGDataHelper.FormatString(" update {0} set {1} {2};", tableName, update.ToSetSql(),
                    update.ToWhereSql()));

            hasUnDo = true;
            if (batchCnt > batchSize) {
                // if (!ExecuteSql(new PFSqlCommandString(sb.toString()))) {
                if (!ExecuteSql(sql)) {
                    return false;
                }

                if (sqlRowsUpdatedAction != null) {
                    sqlRowsUpdatedAction.accept(idx);
                }
                batchCnt = 0;

                // sb.Clear();
                // sb = new StringBuilder();
                sql.clear();
                hasUnDo = false;
            } else {
                batchCnt++;
            }
            idx++;
        }
        // rdr.Close();
        CloseConn();
        if (hasUnDo) {

            // if (!ExecuteSql(new PFSqlCommandString(sb.toString()))) {
            return ExecuteSql(sql);
        }

        return true; // benjamin
    }

    public int ExecuteSqlInt(SGSqlCommandString sql, PFSqlParameter[] p) {
        return ExecuteSqlInt(sql, p, true);
    }

    @Override
    public Boolean ExecuteSql(SGSqlCommandString sqlstr) {
        // return ExecuteSqlInt(sqlstr,null)>0;
        return ExecuteSqlInt(sqlstr, null) > -1;
    }

    /*
     * 执行语句(未测试)
     */
    @Override
    public int ExecuteSqlInt(SGSqlCommandString sql, PFSqlParameter[] p, Boolean autoClose) {

        PreparedStatement ps = null;
        int rs = -1;
        try {
            if (!OpenConn()) {
                return rs;
            }
            if (p != null) {
                ps = conn.prepareStatement(sql.toString());
                AddSqlParameter(ps, p);
                ps.setQueryTimeout(CommandTimeOut);
                rs = ps.executeUpdate();
            } else {
                int cnt = sql.size();
                if (cnt > 1) {
                    conn.setAutoCommit(false);// 没有这句的话,sqlserver里面非常慢;如果超过10000行这样也很慢 -- benjamin20221110
                    stmt = conn.createStatement();
                    stmt.setQueryTimeout(CommandTimeOut);

//					//这里未考虑size很大的情况
//					for (int i = 0; i < sql.size(); i++) {
//						stmt.addBatch(sql.get(i));
//					}
//					stmt.executeBatch();
//					conn.commit();

//					//这里防止批次太多 -- benjamin 20230222
                    PFBatchHelper batchHelper = new PFBatchHelper();
                    batchHelper.batchSize = this.insertOpt.getProcessBatch();
                    batchHelper.batchCnt = 0;
                    for (int i = 0; i < cnt; i++) {
                        stmt.addBatch(sql.get(i));
                        batchHelper.hasNext = cnt > i + 1;
                        batchHelper.batchCnt++;
                        //batchHelper.hasUnDo=true;
                        if (batchHelper.ifDo()) {
                            stmt.executeBatch();
                            conn.commit();
                            batchHelper.hasDone();
                        }
                    }

                    rs = 1;
                } else {
                    stmt = conn.createStatement();
                    stmt.setQueryTimeout(CommandTimeOut);
                    String oneSql = sql.get(0);
                    if (oneSql.indexOf("exec") > -1) {
                        stmt.execute(oneSql);
                        rs = 1;
                    } else {
//                        rs = stmt.executeUpdate(oneSql);
                    	//为了获得新id
                        rs = stmt.executeUpdate(oneSql,Statement.RETURN_GENERATED_KEYS);
                    }
                    //获得新id--20260310
                    if(0<rs) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                this.lastInsertedId = generatedKeys.getLong(1);  // 获取自增主键
//                                System.out.println("插入成功，新ID = " + newId);
                                // 这里就可以用 newId 作为外键插入子表了
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            if (this.errorNotCatchAction != null && errorNotCatchAction.apply(e)) {
                rs = 0;
            } else {
                SetError(e, sql.toString());
                PrintError();
            }
        } finally {
            try {
                if (autoClose && autoCloseConn) {
                    if (p != null) {
                        ps.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                        stmt = null;
                    }
                    CloseConn();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    @Override
    public Boolean TransferTable(SGSqlTransferItem transferItem, Consumer<Integer> alreadyAction,
                                 Predicate<Boolean> stopAction // ,
                                 // Consumer<BatchInsertOption> insertOptionAction
    ) {

        ISGJdbc srcJdbc = transferItem.SrcJdbc;

        String dstTableName = transferItem.DstTableName;

        Consumer<BaseSqlUpdateCollection> beforeInsertAction = transferItem.BeforeInsertAction;

        try {

            int total = 0;

            SGDataHelper.ReTry((a1, b1, c) -> {
                // if (transferItem.BeforeTransferAction != null) {
                // try {
                // transferItem.BeforeTransferAction.go(transferItem);
                // }catch(Exception e) {
                //// PFDataHelper.WriteError(new Throwable(),new
                // Exception(PFDataHelper.));
                // this.SetError(new
                // Exception(PFDataHelper.FormatString("任务{0}的BeforeTransferAction报错",
                // transferItem.getHashId())));
                // throw e;
                // }
                // }

                if (transferItem.SrcSql != null && transferItem.AutoTransfer) {

                    ResultSet dr = null;

                    ISqlExecute srcExec = null;
                    // srcExec = new PFSqlExecute(srcJdbc);
                    srcExec = SGSqlExecute.Init(srcJdbc);

                    srcExec.SetCommandTimeOut(transferItem.getSqlCommandTimeout());
                    String sql = transferItem.SrcSql;

                    ResultSet[] drArr = new ResultSet[]{dr};
                    ISqlExecute[] srcExecArr = new ISqlExecute[]{srcExec};

                    SGDataHelper.ReTry((a2, b2, c2) -> {
                        // drArr[0] = srcExecArr[0].GetDataReader(sql);
                        drArr[0] = srcExecArr[0].GetHugeDataReader(sql);
                        if (drArr[0] == null) {
                            throw new Exception(srcExecArr[0].GetErrorFullMessage());
                            // throw new Exception("见内部错误");
                        }
                    });
                    dr = drArr[0];

                    if (null == dr) { // 其实上面Retry已经能保证dr不为空了,这里的判断是做保险
                        throw new Exception(srcExecArr[0].GetErrorFullMessage());
                    }

                    // //如果没数据,就不要执行 BeforeTransferAction 里的清空数据操作了
                    // 后来还发现有其它缺点,如 TYPE_FORWARD_ONLY 的ResultSet更快,但不能回读
                    // if(dr.next()) {
                    // dr.beforeFirst();//感觉有的数据库可能不实现此方法,导致有问题,以前再看看
                    // }else {
                    // return;
                    // }

                    // 自动建表 -- benjamin20221107
                    if (transferItem.autoCreateDstTable && (!this.IsTableExist(dstTableName))) {

                        SGRef<String> ms = new SGRef<String>("");
                        CreateTable(transferItem, ms);
                    }

                    if (!this.IsTableExist(dstTableName)) {
                        throw new Exception("表" + dstTableName + "不存在");
                    }

                    // BeforeTransferAction放到这里有个好处,如果dr为空(异常时),就不进行before里面的可能存在的清空数据的操作
                    if (transferItem.BeforeTransferAction != null) {
                        try {
                            transferItem.BeforeTransferAction.go(transferItem);
                        } catch (Exception e) {
                            // PFDataHelper.WriteError(new Throwable(),new
                            // Exception(PFDataHelper.));
//                                    this.SetErrorLineIfNull(e);
                            this.SetError(e);
                            this.SetError(new Exception(SGDataHelper.FormatString("任务{0}的BeforeTransferAction报错",
                                    transferItem.getHashId())));
                            throw e;
                        }
                    }

                    // dr = srcExec.GetDataReader(sql);

                    SGSqlInsertCollection insert = null;

                    SetCommandTimeOut(transferItem.getSqlCommandTimeout());

                    int[] arr = new int[]{total};
//                            if (transferItem.BulkBatch != null) {
//                                SetInsertOption(
//                                        a -> {
//                                            a.setProcessBatch(transferItem.BulkBatch);
//                                        });
//                            }
//                            if (transferItem.TransferOneByOne) {
//                                SetInsertOption(
//                                        a -> {
//                                            a.setProcessBatch(1);
//                                        });
//                            }
//                            if (transferItem.convertNullTo0 != true) {
//                                SetInsertOption(
//                                        a -> {
//                                            a.setConvertNullTo0(transferItem.convertNullTo0);
//                                        });
//                            }
                    SetInsertOption(a -> {
                        if (transferItem.BulkBatch != null) {
                            a.setProcessBatch(transferItem.BulkBatch);
                        }
                        if (transferItem.TransferOneByOne) {
                            a.setProcessBatch(1);
                        }
                        if (!transferItem.convertNullTo0) {
                            a.setConvertNullTo0(transferItem.convertNullTo0);
                        }
                    });

                    ClearError();
                    Boolean b = false;
                    SGAction<BaseSqlUpdateCollection, Integer, Object> beforeInsertAction2 = null;
                    if (null != transferItem.BeforeInsertAction) {
                        beforeInsertAction2 = (a, bb, cc) -> {
                            transferItem.BeforeInsertAction.accept(a);
                        };
                    }
                    if ( // beforeInsertAction==null&&
                            transferItem.DstJdbc.GetSqlType().equals(PFSqlType.SqlServer) // sqlserver要用bulk才会快
                                    || transferItem.DstJdbc.GetSqlType().equals(PFSqlType.MySql) // 阿辉那边的mysql库如cloud.perfect99.com_10129_leGe_prod_merge_slave
                                    // 使用bulk方式也非常慢
                                    || PFSqlType.Tidb == transferItem.DstJdbc.GetSqlType() // tidb好像用insert语句更快,先看看为什么会报错,再注释掉
                                    || PFSqlType.ClickHouse.equals(transferItem.DstJdbc.GetSqlType())) {
                        b = HugeBulkReader(insert, dr, dstTableName, beforeInsertAction2, (already) -> {
                            arr[0] = already;
                            if (alreadyAction != null) {
                                alreadyAction.accept(already);
                            }
                        }, stopAction);
                    } else {
                        b = HugeInsertReader(insert, dr, dstTableName, beforeInsertAction, (already) -> {
                            arr[0] = already;
                            if (alreadyAction != null) {
                                alreadyAction.accept(already);
                            }
                        }, stopAction);
                    }
                    if (!b) {
                        String errStr = SGDataHelper.FormatString(
                                "TransferTable() HugeInsertReader() 报错： " + "dstTableName:{0} ", dstTableName);
                        throw new Exception(errStr);
                    }
                } else {
                    if (transferItem.BeforeTransferAction != null) {
                        try {
                            transferItem.BeforeTransferAction.go(transferItem);
                        } catch (Exception e) {
                            this.SetError(e);
                            this.SetError(new Exception(SGDataHelper.FormatString("任务{0}的BeforeTransferAction报错",
                                    transferItem.getHashId())));
                            throw e;
                        }
                    }
                }
            });

            // 备份后可能需要清洗数据
            if (transferItem.AfterTransferAction != null) {
                SGDataHelper.ReTry((a, b, c) -> {
                    transferItem.AfterTransferAction.go(transferItem);
                });
            }
        } catch (Exception e2) {
            // 这里应考虑捕捉未知的错误
            // this.SetErrorLineIfNull(e2);
            this.SetError(e2);
            SetError(new Exception(SGDataHelper.FormatString("PFSqlExecuteBase.TransferTable()报错:\r\n hashId:{0}",
                    transferItem.getHashId())));
            SetError(e2);
            PrintError();
            // var aa = e2;
            // Error = e2;
            return false;
            /* 可选处理异常 */
        }
        return true;
    }

    public Boolean CreateTable(SGSqlTransferItem transfer, SGRef<String> ms // ,
                               //// string srcConn,
                               // string dstConn,
                               // PFSqlType srcType, PFSqlType dstType,
                               // string dstTableName,
                               // string sql, string[] primaryKeyArray,
                               // string[] tableIndexArray, string
                               // tableComment, Dictionary<string, string>
                               // columnComment, PFModelConfigCollection
                               // modelConfig
    ) {
        // PFModelConfigCollection modelConfig =
        // PFDataHelper.GetMultiModelConfig("tidbSale,yjquery,hyzl,order,DayOrders,DayHyzl,newshop");
        // PFModelConfigCollection modelConfig =
        // PFDataHelper.GetMultiModelConfig(transfer.DstTableName + ",yjquery"
        // + (transfer.ModelConfig == null ? "" : ("," + transfer.ModelConfig)));
        PFModelConfigCollection modelConfig = SGDataHelper.GetMultiModelConfig(
                transfer.ModelConfig == null ? (transfer.DstTableName + ",yjquery") : transfer.ModelConfig);
        String dstTableName = transfer.DstTableName;
        String[] primaryKeyArray = transfer.DstTablePrimaryKeys;
        String[] tableClusteredIndexArray = transfer.DstTableClusteredIndex;
        String[] tableIndexArray = transfer.DstTableIndex;
        String tableComment = transfer.DstTableComment;
        // PFSqlType srcType = transfer.SrcSqlType;
        // var dstType = transfer.DstSqlType;
        ISGJdbc srcJdbc = transfer.SrcJdbc;
        PFSqlCommandTimeoutSecond sqlCommandTimeout = transfer.getSqlCommandTimeout();
        String sql = transfer.SrcSql;
        Map<String, String> columnComment = transfer.DstTableColumnComment;
        // var dstConn = transfer.DstConn;
        ISGJdbc dstJdbc = transfer.DstJdbc;

        return DoCreateTable(srcJdbc, dstJdbc, transfer.DstDbName, dstTableName, sql, primaryKeyArray,
                tableClusteredIndexArray, tableIndexArray, transfer.DstTablePartition, transfer.DstTableOrder,
                tableComment, columnComment, modelConfig, sqlCommandTimeout, ms);
    }

    @Override
    public <T> Boolean CreateTable(SGSqlTransferItem transfer, Class<T> tClass, SGRef<String> ms) {

        // PFModelConfigCollection modelConfig =
        // PFDataHelper.GetMultiModelConfig("tidbSale,yjquery,hyzl,order,DayOrders,DayHyzl,newshop");
        PFModelConfigCollection modelConfig = SGDataHelper.GetMultiModelConfig(transfer.DstTableName + ",yjquery"
                + (transfer.ModelConfig == null ? "" : ("," + transfer.ModelConfig)));
        String dstDbName = transfer.DstDbName;
        String dstTableName = transfer.DstTableName;
        String[] primaryKeyArray = transfer.DstTablePrimaryKeys;
        String[] tableClusteredIndexArray = transfer.DstTableClusteredIndex;
        String[] tableIndexArray = transfer.DstTableIndex;
        String tableComment = transfer.DstTableComment;
        // PFSqlType srcType = transfer.SrcSqlType;
        // var dstType = transfer.DstSqlType;
        // IPFJdbc srcJdbc = transfer.SrcJdbc;
        // PFSqlCommandTimeoutSecond sqlCommandTimeout =
        // transfer.getSqlCommandTimeout();
        // String sql = transfer.SrcSql;
        Map<String, String> columnComment = transfer.DstTableColumnComment;
        // var dstConn = transfer.DstConn;
        ISGJdbc dstJdbc = transfer.DstJdbc;

        ISqlExecute dstExec;
        try {
            dstExec = SGSqlExecute.Init(dstJdbc);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        if (dstExec.IsTableExist(dstTableName)) {
            return true;
        }

        SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(dstJdbc);
        models.DbName = dstDbName;
        models.TableName = dstTableName;
        models.PrimaryKey = primaryKeyArray;
        models.ClusteredIndex = tableClusteredIndexArray;
        models.TableIndex = tableIndexArray;
        models.Partition = transfer.DstTablePartition;
        models.Order = transfer.DstTableOrder;
        models.TableComment = tableComment;
        SGDataHelper.ClassEachPropertyType(tClass, (fieldName, f, c) -> {
            // models.add(new SqlCreateTableItem(m));
            SqlCreateTableItem m = null;
            if (modelConfig.containsKey(fieldName)) {
                // m=new SqlCreateTableItem(modelConfig.get(fieldName));
                m = new SqlCreateTableItem(modelConfig.Get(fieldName));
                m.FieldName = fieldName; // modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
            } else {
                m = new SqlCreateTableItem(c);
                // m.FieldName = fieldName;
            }
            if (m.FieldType == null) {
                // m.FieldType = PFDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
                m.FieldType = f.getType();
            }
            if (m.FieldText == null && columnComment != null && columnComment.containsKey(m.FieldName)) {
                m.FieldText = columnComment.get(m.FieldName);
            }
            models.add(m);
        });
        // models.generateByModel(tClass);
        // return DoCreateTable(models,dstJdbc,dstTableName,ms);

        Boolean b = dstExec.ExecuteSql(models.ToSql());
        if (!b) {
            if (ms != null) {
                ms.SetValue(dstExec.GetErrorFullMessage());
            }
        }
        return b;
    }

    // @Override
    // public PFSqlCommandString GetCreateTableSql(IPFJdbc srcJdbc,String
    // sql,PFSqlCreateTableCollection models,PFModelConfigCollection modelConfig){
    // //PFDataHelper.SetConfigMapper(new PFConfigMapper());
    //
    // //IPFJdbc srcJdbc = JdbcTest.GetLiGeIamJdbc();
    // //IPFJdbc dstJdbc = UncheckTest.GetHiveJdbc();
    //
    //
    //
    //
    // //String sql = " select * from member_login_log limit 10";
    //
    //// PFHiveSqlCreateTableCollection models = new
    // PFHiveSqlCreateTableCollection();
    //// models.DbName = "wm_hive_db";
    //// models.TableName = "member_login_log1";
    //// // models.PrimaryKey = new String[] {"data_date","user_id"};
    //// // models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
    //// //models.Order = new String[] { "data_date", "user_id" };
    //// models.Partition = new String[] { "data_date" };
    //// // models.TableIndex = new String[] {"good_no"};
    //
    // // ResultSet dr = srcExec.GetDataReader(sql);
    //
    // ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
    // ResultSet dr = srcExec.GetHugeDataReader(sql);
    //
    // if (dr == null) {
    // return null;
    // }
    //
    // if(models.size()<1) {
    // //PFModelConfigCollection
    // modelConfig=PFDataHelper.GetModelConfig("yjquery",null);
    // try {
    // ResultSetMetaData md = dr.getMetaData();
    //
    // for (int i = 0; i < md.getColumnCount(); i++) {
    // // String fieldName = md.getColumnName(i+1);
    // String fieldName = md.getColumnLabel(i + 1);
    // SqlCreateTableItem m = null;
    // if (modelConfig.containsKey(fieldName)) {
    //// m=new SqlCreateTableItem(modelConfig.get(fieldName));
    // m = new SqlCreateTableItem(modelConfig.Get(fieldName));
    // m.FieldName = fieldName;// modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
    // } else {
    // m = new SqlCreateTableItem();
    // m.FieldName = fieldName;
    // }
    // if (m.FieldType == null) {
    // m.FieldType = PFDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
    // }
    //// if (columnComment != null && columnComment.containsKey(m.FieldName))
    //// {
    //// m.FieldText = columnComment.get(m.FieldName);
    //// }
    // // var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType =
    // // rdr.GetFieldType(i) };
    // models.add(m);
    // }
    // } catch (Exception e) {
    //
    // }
    // }
    // srcExec.CloseReader(dr);
    // srcExec.CloseConn();
    //
    // PFSqlCommandString sqlStr = models.ToSql();
    // System.out.print(sqlStr);
    //
    // return sqlStr;
    // }

//	@Override
//	public <T extends PFSqlCreateTableCollection> T GetCreateTableSql(IPFJdbc srcJdbc, String sql, T models,
//			PFModelConfigCollection modelConfig) {
//		return DoGetCreateTableSql(srcJdbc, sql, models, modelConfig);
//	}

//	// @Override
//	/**
//	 * @deprecated 直接用 GetCreateTableSql(sql, models, modelConfig) 方法
//	 * @param srcJdbc
//	 * @param sql
//	 * @param models
//	 * @param modelConfig
//	 * @return
//	 */
//	@Deprecated
//	private static <T extends PFSqlCreateTableCollection> T DoGetCreateTableSql(IPFJdbc srcJdbc, String sql, T models,
//			PFModelConfigCollection modelConfig) {
//
//		ISqlExecute srcExec = null;
//		try {
//			srcExec = PFSqlExecute.Init(srcJdbc);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			return null;
//		}
//		return srcExec.GetCreateTableSql(sql, models, modelConfig);
//	}

    @Override
    public <T extends SGSqlCreateTableCollection> T GetCreateTableSql(String sql, T models,
                                                                      PFModelConfigCollection modelConfig) {
        //ISqlExecute srcExec = this;
        ResultSet dr = this.GetHugeDataReader(sql);

        if (dr == null) {
            return null;
        }

        int dstSqlMaxLength = 8000; // sqlserver最大列长度(nvarchar时)
        if (models.size() < 1) {
            try {
                ResultSetMetaData md = dr.getMetaData();

                for (int i = 0; i < md.getColumnCount(); i++) {
                    // String fieldName = md.getColumnName(i+1);
                    int idx = i + 1;
                    String fieldName = md.getColumnLabel(i + 1);
                    if (sys_limit_id.equals(fieldName)) {
                        continue;
                    }
                    SqlCreateTableItem m = null;
                    if (modelConfig != null && modelConfig.containsKey(fieldName)) {
                        // m=new SqlCreateTableItem(modelConfig.get(fieldName));
                        m = new SqlCreateTableItem(modelConfig.Get(fieldName));
                        m.FieldName = fieldName; // modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
                    } else {
                        m = new SqlCreateTableItem();
                        m.FieldName = fieldName;
                    }
                    m.LowerFieldName = fieldName.toLowerCase();
                    int sqlType = md.getColumnType(i + 1);
                    String sqlTypeName = md.getColumnTypeName(idx);
                    if (m.FieldType == null) {
                        // if("money".equals(sqlTypeName)) {
                        // String aa="aa";
                        // }
                        // m.FieldType = PFDataHelper.GetTypeByString(sqlTypeName);
                        m.FieldType = SGDataHelper.GetTypeBySqlType(sqlType, sqlTypeName);
                    }
                    int srcSqlLength = md.getColumnDisplaySize(idx);
                    boolean isSrcMaxLengthSqlType = SGDataHelper.IsMaxLengthSqlType(sqlType, sqlTypeName);
                    if (!m.HasChinese) {
                        m.HasChinese = SGDataHelper.IsChineseSqlType(sqlType, sqlTypeName);
                        if (m.HasChinese && m.FieldSqlLength == null) { // a中文默认200长
                            // m.FieldSqlLength = 200;
                            if (isSrcMaxLengthSqlType || srcSqlLength > dstSqlMaxLength) {
                                m.FieldSqlLength = 200;
                            } else if (srcSqlLength > 0) {
                                m.FieldSqlLength = srcSqlLength * 2; // 如果来源是mysql的varchar,目标是sqlserver的nvarchar,2倍比较保险(未测试验证过)
                            }
                        }
                    }

                    if (isSrcMaxLengthSqlType && (m.FieldSqlLength == null || m.FieldSqlLength < 1000)) {
                        m.FieldSqlLength = 1000;
                    } else if (m.FieldSqlLength == null && srcSqlLength > 0) {
                        m.FieldSqlLength = srcSqlLength > dstSqlMaxLength ? dstSqlMaxLength : srcSqlLength;
                    }

                    // if (columnComment != null &&
                    // columnComment.containsKey(m.FieldName))
                    // {
                    // m.FieldText = columnComment.get(m.FieldName);
                    // }
                    // var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType =
                    // rdr.GetFieldType(i) };
                    models.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.CloseReader(dr);
        this.CloseConn();

        SGSqlCommandString sqlStr = models.ToSql();
        System.out.print(sqlStr);

        return models;
    }

    private static Boolean DoCreateTable(ISGJdbc srcJdbc, ISGJdbc dstJdbc, String dstDbName, String dstTableName,
                                         String sql, String[] primaryKeyArray, String[] tableClusteredIndexArray, String[] tableIndexArray,
                                         String[] dstTablePartition, String[] dstTableOrder, String tableComment, Map<String, String> columnComment,
                                         PFModelConfigCollection modelConfig, PFSqlCommandTimeoutSecond sqlCommandTimeout, SGRef<String> ms) {
        ISqlExecute dstExec = null;
        try {
            dstExec = SGSqlExecute.Init(dstJdbc);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        if (!dstExec.IsTableExist(dstTableName)) {

            SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(dstJdbc);

            models.DbName = dstDbName;
            models.TableName = dstTableName;
            models.PrimaryKey = primaryKeyArray;
            models.ClusteredIndex = tableClusteredIndexArray;
            models.TableIndex = tableIndexArray;
            models.Partition = dstTablePartition;
            models.Order = dstTableOrder;
            models.TableComment = tableComment;

            ISqlExecute srcExec = null;
            try {
                srcExec = SGSqlExecute.Init(srcJdbc);
                srcExec.UpToCommandTimeOut(sqlCommandTimeout);
                models = srcExec.GetCreateTableSql(sql, models, modelConfig);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }
            Boolean b = dstExec.ExecuteSql(models.ToSql());
            if (!b) {
                if (ms != null) {
                    ms.SetValue(dstExec.GetErrorFullMessage());
                }
            }
            return b;
        }
        return true;
        // b = dstExec.AlterTableAddColumn(models, out ms);//等注释这句--benjamin

    }

    @Override
    public void addColumn(String tableName, // String colName,
                          PFModelConfig model) {
        // String sqlType = null;
        SGSqlCreateTableCollection create = SGSqlCreateTableCollection.Init(_jdbc);
        // PFModelConfig model=new PFModelConfig();
        // model.FieldName=colName;
        List<SGSqlFieldInfo> fields = this.GetTableFields(tableName);
        if (!SGDataHelper.ListAny(fields, a -> model.FieldName.equals(a.getFieldName()))) {
            ExecuteSql(new SGSqlCommandString(SGDataHelper.FormatString("alter table {0} add column {1} {2} ; ",
                    tableName, model.FieldName, create.GetFieldTypeString(model))));
        }
    }

    /**
     * 大量更新,本来是给tidb用,但应该也能在mysql家族中应用
     *
     * <p>
     * tidb的事务有限制,要分批更新,所以要如下使用(要求必需有has_updated字段辅助更新)
     *
     * <p>
     * 用法1:(重点是where is_new_hy判断更新完毕) update monthly_hyzl set is_new_hy=(case when
     * accmon=date_format(create_date, '%Y.%m') then CONVERT(bit,1) else
     * CONVERT(bit,0) end) where is_new_hy is null
     *
     * <p>
     * 用法2:(重点是has_updated的设置,更新前先设置为0) sqlExec.TidbHugeUpdate(string.Format(@"
     * update monthly_hyzl a inner join monthly_hyzl as lm on xxx set xxx,
     * a.has_updated=1 where xxx and a.has_updated=0 "), string.Format(@" update
     * monthly_hyzl set has_updated=0 where xxx and has_updated =1 ") );
     */
    @Override
    public Boolean HugeUpdate(String updateSql, String... resetHasUpdatedFieldSqls) {
        // if (updateSql.IndexOf("has_updated") < 0) { return false; }//自行用其它字段控制也行的

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
        // int batch = 500000;//172.16.1.246:10010/sale 这个库用 500000 报错 Transaction is
        // too large, size: 104857600
        int batch = this.GetSqlUpdateOption().getProcessBatch();
        updateSql = SGDataHelper.FormatString("{0} " + "limit {1} ", updateSql, batch);

        int affected = 1;

        if (resetHasUpdatedFieldSqls != null) {
            for (String i : resetHasUpdatedFieldSqls) {
                String resetHasUpdatedFieldSql = SGDataHelper.FormatString("{0} " + "limit {1} ", i, batch);

                affected = 1;
                while (affected > 0) {
                    if (_stopAction != null) {
                        if (_stopAction.test(true)) {
                            return true;
                        }
                    }
                    affected = ExecuteSqlInt(new SGSqlCommandString(resetHasUpdatedFieldSql), null);
                    if (affected == -1) {
                        // PFDataHelper.WriteError(new Throwable(), GetError());
                        // SetError(GetError());
                        SGDataHelper.WriteErrors(_errors);
                        return false;
                    }
                }
            }
        }

        affected = 1;
        while (affected > 0) {
            if (_stopAction != null) {
                if (_stopAction.test(true)) {
                    return true;
                }
            }
            affected = ExecuteSqlInt(new SGSqlCommandString(updateSql), null);
            if (affected == -1) {
                // PFDataHelper.WriteError(new Throwable(), GetError());
                SGDataHelper.WriteErrors(_errors);
                return false;
            }
        }
        return true;
    }

    protected String GetInsertSql(String tableName, SGSqlInsertCollection insert) {
        // return PFDataHelper.FormatString(" ALTER TABLE {0} UPDATE {1} {2};",
        // tableName,
        // update.ToSetSql(),
        // update.ToWhereSql());
        return SGDataHelper.FormatString("insert into {0}  ({1})  values ({2})", tableName, insert.ToKeysSql(),
                insert.ToValuesSql());
    }

    /**
     * 给基类复写
     *
     * @param tableName
     * @param update
     * @return
     */
    protected String GetUpdateSql(String tableName, SGSqlUpdateCollection update) {
//        return PFDataHelper.FormatString(" update {0} set  {1} {2};", tableName, update.ToSetSql(),
//                update.ToWhereSql());//自动加分号其实是不好的,后期使用如果必要时再加
        return SGDataHelper.FormatString(" update {0} set  {1} {2}", tableName, update.ToSetSql(),
                update.ToWhereSql());
    }

    protected String GetDeleteSql(String tableName, SGSqlUpdateCollection update) {
        //return PFDataHelper.FormatString(" delete from {0} {1};", tableName, update.ToWhereSql());
        return SGDataHelper.FormatString(" delete from {0} {1}", tableName, update.ToWhereSql());
    }

    /**
     * 此方法是从PFClickHouseSqlExecute中提取,但未测试此版本
     */
    public Boolean HugeUpdateReader(SGSqlUpdateCollection update, ResultSet rdr, String tableName,
                                    Consumer<BatchInsertOption> insertOptionAction,
                                    // Func<MySqlUpdateCollection, DbDataReader,bool> rowAction,
                                    SGFunc<BaseSqlUpdateCollection, ResultSet, ?, Boolean> rowAction, Consumer<Integer> sqlRowsUpdatedAction) {

        BatchInsertOption insertOption = new BatchInsertOption();
        if (insertOptionAction != null) {
            insertOptionAction.accept(insertOption);
        }

        //StringBuilder sb = new StringBuilder();
        SGSqlCommandString sb = new SGSqlCommandString();
        int idx = 0;
        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
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

                // sb.append(PFDataHelper.FormatString(" ALTER TABLE {0} UPDATE {1} {2};",
                // tableName, update.ToSetSql(),
                // update.ToWhereSql()));
                //sb.append(GetUpdateSql(tableName, update));
                sb.add(GetUpdateSql(tableName, update));

                hasUnDo = true;
                if (batchCnt > batchSize) {
                    // b = insertOption.UserTransition
                    // ? ExecuteTransactSql(sb.ToString())
                    // : ExecuteSql(sb.ToString());
                    // if (!b)
                    //if (!ExecuteSql(new PFSqlCommandString(sb.toString()))) {
                    if (!ExecuteSql(sb)) {
                        CloseReader(rdr);
                        return false;
                    }
                    if (sqlRowsUpdatedAction != null) {
                        sqlRowsUpdatedAction.accept(idx);
                    }
                    batchCnt = 0;

                    //sb = new StringBuilder();
                    sb.clear();
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
        return true; // benjamin
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
    public Boolean HugeDelete(String updateSql) {
        // int batch = 500000;// 1000000还是报错:MySql.Data.MySqlClient.MySqlException
        // (0x80004005): Transaction
        // is too large, size: 104857600
        int batch = this.GetDeleteOption().getProcessBatch();
        Consumer<Integer> deletedAction = this.GetDeleteOption().getDoneNumAction();
        boolean hasDoneAction = null != deletedAction;

        updateSql = SGDataHelper.FormatString("{0}\r\n" + "limit {1}", updateSql, batch);
        int affected = 1;
        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
        int deletedNum = 0;
        while (affected > 0) {
            affected = ExecuteSqlInt(new SGSqlCommandString(updateSql), null);
            if (hasDoneAction && affected > 0) {
                deletedNum += affected;
                deletedAction.accept(deletedNum);
            }
        }
        if (affected == -1) {
            return false;
        } else if (affected < 1) {
            return true;
        }
        return true;
        //// String sql = PFDataHelper.FormatString(
        //// "SET ROWCOUNT 10000;\r\n" + "declare @rc int\r\n" + "\r\n" + "WHILE 1 =
        //// 1\r\n" +
        // "BEGIN\r\n"
        //// + " begin tran t1\r\n" + " {0} \r\n"
        //// + " set @rc=@@ROWCOUNT --commic后,@@rowcount为0\r\n" + " commit tran t1\r\n"
        //// + " IF @rc = 0\r\n" + " BREAK;\r\n" + "END\r\n" + "\r\n" + "SET ROWCOUNT
        // 0;",
        //// updateSql);
        //
        // UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge);
        // // return ExecuteTransactSql(sql);
        // return ExecuteSql( new PFSqlCommandString(updateSql));
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

    @Override
    public Boolean TruncateTable(String tableName) {
        return ExecuteSql(new SGSqlCommandString(SGDataHelper.FormatString("truncate table {0}", tableName)));
    }

    private PFDataRow GetDataRowByInsertCollection(SGSqlInsertCollection srcInsert // ,ResultSetMetaData rsMD
    ) {

        List<PFDataColumn> col = new ArrayList<PFDataColumn>(); // 行所有列集合

        Iterator<String> iter = srcInsert.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            SqlUpdateItem value = srcInsert.get(key);
            PFDataColumn c = new PFDataColumn(key, value.Value);
            // c.setSqlType(rsMD.getColumnType(i));
            // c.setPFDataType(value.VPFType);
            c.setPFDataType(value.getSrcDataPFType());
            // c.setSqlType(rsMD.getColumnType(i));
            // 将列信息加入列集合
            col.add(c);
        }
        return new PFDataRow(col);
    }

    private PFDataRow GetDataRowByResultSet(ResultSet rs // ,ResultSetMetaData rsMD
    ) throws SQLException {

//		List<PFDataColumn> col = new ArrayList<PFDataColumn>(); // 行所有列集合
//
//		Iterator<String> iter = srcInsert.keySet().iterator();
//		while (iter.hasNext()) {
//			String key = iter.next();
//			SqlUpdateItem value = srcInsert.get(key);
//			PFDataColumn c = new PFDataColumn(key, value.Value);
//			// c.setSqlType(rsMD.getColumnType(i));
//			// c.setPFDataType(value.VPFType);
//			c.setPFDataType(value.getSrcDataPFType());
//			// c.setSqlType(rsMD.getColumnType(i));
//			// 将列信息加入列集合
//			col.add(c);
//		}
//		return new PFDataRow(col);

        List<PFDataColumn> col = null; // 行所有列集合

        PFDataRow r = null; // 单独一行
        PFDataColumn c = null; // 单独一列
        // 初始化列集合
        col = new ArrayList<PFDataColumn>();

        ResultSetMetaData rsmd = rs.getMetaData();
        // 此处开始列循环，每次向一行对象插入一列
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            // String columnName = rsmd.getColumnName(i);
            String columnName = rsmd.getColumnLabel(i);
            Object value = rs.getObject(columnName);
            // 初始化单元列
            c = new PFDataColumn(columnName, value);
            c.setSqlType(rsmd.getColumnType(i));
            // 将列信息加入列集合
            col.add(c);
        }
        // 初始化单元行
        r = new PFDataRow(col);
        // 将行信息降入行结合
        //row.add(r);
        return r;
    }

//	/**
//	 * 比对数据 订单差异总结,电子商务和里格的订单差异为: 1.隔月退货单(电子商务多) 2.调差单(报表系统多) 2.非正常退货单(电子商务多)
//	 *
//	 * <p>
//	 * 效率: 总行数 612703.0 条 速度:speed:1.93E+001条/秒
//	 */
//	public PFDataTable FindTableRowDifferenceOld(
//			// IPFJdbc srcJdbc,
//			IPFJdbc dstJdbc, String srcSql, String dstSql, String[] joinColumn, String[] compareColumn,
//			Consumer<Double> accessed, Consumer<PFDataTable> resultUpdated, Predicate<Boolean> stopAction) {
//		try {
//			ISqlExecute srcExec = this; // PFSqlExecute.Init(srcJdbc);
//			ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//			srcExec.UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
//			ResultSet srcRs = srcExec.GetDataReader(srcSql);
//			PFDataTable dstTable = dstExec.GetDataTable(dstSql, null);
//			List<PFDataRow> compareRow = new ArrayList<PFDataRow>(); // 表所有行集合
//
//			ResultSetMetaData rsMD = srcRs.getMetaData();
//			PFSqlInsertCollection srcInsert = srcExec.getInsertCollection(rsMD);
//			double cnt = 0;
//			while (srcRs.next()) {
//				if (stopAction != null) {
//					if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
//						CloseReader(srcRs); // rdr不是本连接产生的,这样关闭不太好
//						CloseConn();
//						return new PFDataTable(compareRow);
//					}
//				}
//				boolean isExist = false;
//				srcInsert.UpdateByDataReader(srcRs);
//				String vL = srcInsert.Get(joinColumn[0]).Value.toString();
//				// for(int j=dstTable.Rows.size()-1;j>=0;j--) {
//				for (int j = 0; j < dstTable.Rows.size(); j++) {
//					PFDataRow dstRow = dstTable.Rows.get(j);
//					String vR = dstRow.getColumn(joinColumn[0]).toString();
//					if (vL.equals(vR)) {
//						isExist = true;
//						Object compareValueL = srcInsert.Get(compareColumn[0]).Value;
//						Object compareValueR = dstRow.getColumn(compareColumn[0]);
//						if (compareValueL.equals(compareValueR)) { // 有L有R,且compare相等.不返回,从dstTable中移除R
//							dstTable.Rows.remove(j);
//						} else { // 有L有R,但compare值不等于时;要返回L和R,从dstTable中移除R
//							// compareTable.Rows.add(dstRow);
//							compareRow.add(dstRow);
//							PFDataColumn cr = new PFDataColumn("LR", "R");
//							cr.setPFDataType(PFSqlFieldTypeEnum.String);
//							dstRow.getCol().add(cr);
//							dstTable.Rows.remove(j);
//
//							PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
//							PFDataColumn cl = new PFDataColumn("LR", "L");
//							cl.setPFDataType(PFSqlFieldTypeEnum.String);
//							r.getCol().add(cl);
//							// compareTable.Rows.add(dstRow);
//							compareRow.add(r);
//
//							if (null != resultUpdated) {
//								resultUpdated.accept(new PFDataTable(compareRow));
//							}
//						}
//						break;
//					}
//				}
//				if (isExist) { // 有L有R时,内部已经处理过了
//
//				} else { // 有L无R时,返回L
//					PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
//					PFDataColumn cl = new PFDataColumn("LR", "L");
//					cl.setPFDataType(PFSqlFieldTypeEnum.String);
//					r.getCol().add(cl);
//					// compareTable.Rows.add(dstRow);
//					compareRow.add(r);
//				}
//				// cnt.add(new Double(1));
//				cnt += 1;
//				if (null != accessed) {
//					accessed.accept(cnt);
//				}
//			}
//			// dstTable剩余的是 无L有R的,返回R
//			// compareRow.addAll(dstTable.Rows);
//			for (PFDataRow r : dstTable.Rows) {
//				PFDataColumn cr = new PFDataColumn("LR", "R");
//				cr.setPFDataType(PFSqlFieldTypeEnum.String);
//				r.getCol().add(cr);
//				compareRow.add(r);
//			}
//
//			// compareTable = new PFDataTable(compareRow);
//
//			return new PFDataTable(compareRow);
//			// this.PrintObject(compareTable.ToDictList());
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

    /**
     * 效率比FindTableRowDifferenceOld快多了
     *
     * <p>
     * 效率: 总行数 612703.0 条 总耗时:0时1分8秒" 速度:speed:8.93E+003条/秒"
     */
    @Override
    public SGDataTable FindTableRowDifference(
            // IPFJdbc srcJdbc,
            ISGJdbc dstJdbc, String srcSql, String dstSql, String[] joinColumn, String[] compareColumn,
            SGAction<Double, Double, Double> accessed, Consumer<SGDataTable> resultUpdated, Predicate<Boolean> stopAction) {

        List<PFDataRow> lRow = new ArrayList<PFDataRow>();
        List<PFDataRow> rRow = new ArrayList<PFDataRow>();
        //List<PFDataRow> lrRow = new ArrayList<PFDataRow>();
        SGRef<List<PFDataRow>> lRowRef = new SGRef<List<PFDataRow>>(lRow);
        SGRef<List<PFDataRow>> rRowRef = new SGRef<List<PFDataRow>>(rRow);
        //SGRef<List<PFDataRow>> lrRowRef = new SGRef<List<PFDataRow>>(lrRow);

        return doFindTableRowDifference(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, accessed, resultUpdated,
                stopAction, lRowRef, rRowRef//, lrRowRef
        );
    }

//	/**
//	 * PFSqlInsertCollection只有可编辑列,不适合用在这
//	 * @param dstJdbc
//	 * @param srcSql
//	 * @param dstSql
//	 * @param joinColumn
//	 * @param compareColumn
//	 * @param accessed
//	 * @param resultUpdated
//	 * @param stopAction
//	 * @param lRowRef
//	 * @param rRowRef
//	 * @param lrRowRef
//	 * @return
//	 */
//	@Deprecated
//	public PFDataTable doFindTableRowDifferenceOld(
//			// IPFJdbc srcJdbc,
//			IPFJdbc dstJdbc, String srcSql, String dstSql, String[] joinColumn, String[] compareColumn,
//			BiConsumer<Double, Double> accessed, Consumer<PFDataTable> resultUpdated, Predicate<Boolean> stopAction,
//			SGRef<List<PFDataRow>> lRowRef, SGRef<List<PFDataRow>> rRowRef, SGRef<List<PFDataRow>> lrRowRef) {
//		try {
//			//ISqlExecute srcExec = this; // PFSqlExecute.Init(srcJdbc);
//			ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//			this.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));
//			dstExec.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));
//
//			ResultSet srcRs = this.GetHugeDataReader(srcSql);
//			PFDataTable dstTable = dstExec.GetDataTable(dstSql, null);
//			Map<String, PFDataRow> dstMap = new HashMap<String, PFDataRow>();
//
//			int dstTotal = dstTable.Rows.size();
//			double dstTotalDouble = Double.valueOf(dstTotal);
//			for (int j = 0; j < dstTotal; j++) {
//				PFDataRow dstRow = dstTable.Rows.get(j);
//				String key = "";
//				for (int k = 0; k < joinColumn.length; k++) {
//					key += dstRow.getColumn(joinColumn[k]).toString();
//				}
//				dstMap.put(key, dstRow);
//			}
//			// String[] joinColumn=new String[] {"orderno"};
//			// String[] compareColumn=new String[] {"Totalmoney"};
//			// PFDataTable compareTable=null;//new PFDataTable();
//			List<PFDataRow> compareRow = new ArrayList<PFDataRow>(); // 表所有行集合
//
//			// List<PFDataColumn> col = null;// 行所有列集合
//			ResultSetMetaData rsMD = srcRs.getMetaData();
//			PFSqlInsertCollection srcInsert = this.getInsertCollection(rsMD);
//			// Double cnt=new Double(0);
//			double cnt = 0;
//
//			List<PFDataRow> lRow = lRowRef.GetValue();
//			List<PFDataRow> rRow = rRowRef.GetValue();
//			List<PFDataRow> lrRow = lrRowRef.GetValue();
//
//			int logCnt = 0;
//			int logBatch = 10000;
//			while (srcRs.next()) {
//				if (stopAction != null) {
//					if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
//						CloseReader(srcRs); // rdr不是本连接产生的,这样关闭不太好
//						CloseConn();
//						return new PFDataTable(compareRow);
//					}
//				}
//				// boolean isExist=false;
//				srcInsert.UpdateByDataReader(srcRs);
//
//				String keyL = "";
//				for (int k = 0; k < joinColumn.length; k++) {
//					//keyL += srcInsert.Get(joinColumn[k]).Value.toString();
//					keyL +=PFDataHelper.ObjectToString0( srcInsert.Get(joinColumn[k]).Value);
//				}
//
//				if (dstMap.containsKey(keyL)) {
//					// isExist=true;
//					PFDataRow dstRow = dstMap.get(keyL);
//					// Object compareValueL=srcInsert.Get(compareColumn[0]).Value;
//					// Object compareValueR=dstRow.getColumn(compareColumn[0]);
//					boolean isValueEqual = true;
//					// for(int k=0;k<joinColumn.length;k++) {
//					for (int k = 0; k < compareColumn.length; k++) {
//						Object compareValueL = srcInsert.Get(compareColumn[k]).Value;
//						Object compareValueR = dstRow.getColumn(compareColumn[k]);
//						if (!PFDataHelper.IsPFValueEquals(compareValueL, compareValueR)) {
//							isValueEqual = false;
//							break;
//						}
//					}
//					// if(compareValueL.equals(compareValueR)) {//有L有R,且compare相等.不返回,从dstTable中移除R
//					if (isValueEqual) { // 有L有R,且compare相等.不返回,从dstTable中移除R
//						dstTable.Rows.remove(dstRow);
//						dstMap.remove(keyL);
//					} else { // 有L有R,但compare不相等.返回L和R
//						compareRow.add(dstRow);
//						PFDataColumn cr = new PFDataColumn("LR", "R");
//						cr.setPFDataType(PFSqlFieldTypeEnum.String);
//						dstRow.getCol().add(cr);
//						// dstTable.Rows.remove(j);
//						dstTable.Rows.remove(dstRow);
//						dstMap.remove(keyL);
//
//						PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
//						PFDataColumn cl = new PFDataColumn("LR", "L");
//						cl.setPFDataType(PFSqlFieldTypeEnum.String);
//						r.getCol().add(cl);
//						// compareTable.Rows.add(dstRow);
//						compareRow.add(r);
//
//						lrRow.add(r);
//
//						if (null != resultUpdated) {
//							resultUpdated.accept(new PFDataTable(compareRow));
//						}
//					}
//				} else { // 有L无R时,返回L
//
//					PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
//					PFDataColumn cl = new PFDataColumn("LR", "L");
//					cl.setPFDataType(PFSqlFieldTypeEnum.String);
//					r.getCol().add(cl);
//					// compareTable.Rows.add(dstRow);
//					compareRow.add(r);
//
//					lRow.add(r);
//					if (null != resultUpdated) {
//						resultUpdated.accept(new PFDataTable(compareRow));
//					}
//				}
//
//				cnt += 1;
//				if (null != accessed) {
//					if (cnt == 1) {
//						accessed.accept(cnt, dstTotalDouble);
//					}
//					if (logCnt > logBatch) {
//						accessed.accept(cnt, dstTotalDouble);
//						logCnt = 0;
//					} else {
//						logCnt++;
//					}
//				}
//			}
//			// dstTable剩余的是 无L有R的,返回R
//			// compareRow.addAll(dstTable.Rows);
//			for (PFDataRow r : dstTable.Rows) {
//				PFDataColumn cr = new PFDataColumn("LR", "R");
//				cr.setPFDataType(PFSqlFieldTypeEnum.String);
//				r.getCol().add(cr);
//				compareRow.add(r);
//
//				rRow.add(r);
//			}
//			if (null != resultUpdated) {
//				resultUpdated.accept(new PFDataTable(compareRow));
//			}
//
//			// compareTable = new PFDataTable(compareRow);
//
//			return new PFDataTable(compareRow);
//			// this.PrintObject(compareTable.ToDictList());
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

    public SGDataTable doFindTableRowDifference(
            // IPFJdbc srcJdbc,
            ISGJdbc dstJdbc, String srcSql, String dstSql, String[] joinColumn, String[] compareColumn,
            SGAction<Double, Double, Double> accessed, Consumer<SGDataTable> resultUpdated, Predicate<Boolean> stopAction,
            SGRef<List<PFDataRow>> lRowRef, SGRef<List<PFDataRow>> rRowRef
            //, SGRef<List<PFDataRow>> lrRowRef
    ) {
        try {
            //ISqlExecute srcExec = this; // PFSqlExecute.Init(srcJdbc);
            ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
            this.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));
            dstExec.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));

            ResultSet srcRs = this.GetHugeDataReader(srcSql);
            SGDataTable dstTable = dstExec.GetDataTable(dstSql, null);
            Map<String, PFDataRow> dstMap = new HashMap<String, PFDataRow>();

            int dstTotal = dstTable.Rows.size();
            double dstTotalDouble = Double.valueOf(dstTotal);
            for (int j = 0; j < dstTotal; j++) {
                PFDataRow dstRow = dstTable.Rows.get(j);
                String key = "";
                for (int k = 0; k < joinColumn.length; k++) {
                    key += dstRow.getColumn(joinColumn[k]).toString();
                }
                dstMap.put(key, dstRow);
            }
            // String[] joinColumn=new String[] {"orderno"};
            // String[] compareColumn=new String[] {"Totalmoney"};
            // PFDataTable compareTable=null;//new PFDataTable();
            List<PFDataRow> compareRow = new ArrayList<PFDataRow>(); // 表所有行集合

            // List<PFDataColumn> col = null;// 行所有列集合
            ResultSetMetaData rsMD = srcRs.getMetaData();
            //PFSqlInsertCollection srcInsert = this.getInsertCollection(rsMD);

            SGSqlInsertCollection srcInsert = this.getInsertCollection();
            for (int i = 0; i < rsMD.getColumnCount(); i++) {
                try {
                    int mdIdx = i + 1;
                    //if (!dstMd.isAutoIncrement(mdIdx) && !dstMd.isReadOnly(mdIdx)) { // 不是自增列才插入,否则sql会报错-- benjamin
                    // 20220902
                    String fieldName = rsMD.getColumnLabel(i + 1);
                    SqlUpdateItem updateItem = new SqlUpdateItem();
                    updateItem.Key = fieldName;
                    updateItem.setDstDataType(rsMD.getColumnType(mdIdx));
                    updateItem.setDstDataTypeName(rsMD.getColumnTypeName(mdIdx));
                    updateItem.setDstDataPFType(SGDataHelper.GetPFTypeBySqlType2(rsMD.getColumnType(mdIdx), rsMD.getColumnTypeName(mdIdx)));
                    srcInsert.Add(updateItem);
                    //}
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Double cnt=new Double(0);
            double cnt = 0;

            List<PFDataRow> lRow = lRowRef.GetValue();
            List<PFDataRow> rRow = rRowRef.GetValue();
            //List<PFDataRow> lrRow = lrRowRef.GetValue();

            int logCnt = 0;
            int logBatch = 10000;

            double compareCnt = 0;
            while (srcRs.next()) {
                if (stopAction != null) {
                    if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                        CloseReader(srcRs); // rdr不是本连接产生的,这样关闭不太好
                        CloseConn();
                        return new SGDataTable(compareRow);
                    }
                }
                // boolean isExist=false;
                srcInsert.UpdateByDataReader(srcRs);

                String keyL = "";
                for (int k = 0; k < joinColumn.length; k++) {
                    //keyL += srcInsert.Get(joinColumn[k]).Value.toString();
                    keyL += SGDataHelper.ObjectToString0(srcInsert.Get(joinColumn[k]).Value);
                    //keyL+=BaseSqlUpdateCollection.getValueByDataReaderI2(srcRs, rsMD, k);
                }

                if (dstMap.containsKey(keyL)) {
                    // isExist=true;
                    PFDataRow dstRow = dstMap.get(keyL);
                    // Object compareValueL=srcInsert.Get(compareColumn[0]).Value;
                    // Object compareValueR=dstRow.getColumn(compareColumn[0]);
                    boolean isValueEqual = true;
                    // for(int k=0;k<joinColumn.length;k++) {
                    for (int k = 0; k < compareColumn.length; k++) {
                        Object compareValueL = srcInsert.Get(compareColumn[k]).Value;
                        //Object compareValueL = BaseSqlUpdateCollection.getValueByDataReaderI2(srcRs, rsMD, k);
                        Object compareValueR = dstRow.getColumn(compareColumn[k]);
                        if (!SGDataHelper.IsPFValueEquals(compareValueL, compareValueR)) {
                            isValueEqual = false;
                            break;
                        }
                    }
                    // if(compareValueL.equals(compareValueR)) {//有L有R,且compare相等.不返回,从dstTable中移除R
                    if (isValueEqual) { // 有L有R,且compare相等.不返回,从dstTable中移除R
                        dstTable.Rows.remove(dstRow);
                        dstMap.remove(keyL);
                    } else { // 有L有R,但compare不相等.返回L和R
                        compareRow.add(dstRow);
                        compareCnt++;
                        PFDataColumn cr = new PFDataColumn("LR", "R");
                        cr.setPFDataType(SGSqlFieldTypeEnum.String);
                        dstRow.getCol().add(cr);
                        // dstTable.Rows.remove(j);
                        dstTable.Rows.remove(dstRow);
                        dstMap.remove(keyL);

                        //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                        PFDataRow r = this.GetDataRowByResultSet(srcRs);

                        PFDataColumn cl = new PFDataColumn("LR", "L");
                        cl.setPFDataType(SGSqlFieldTypeEnum.String);
                        r.getCol().add(cl);
                        // compareTable.Rows.add(dstRow);
                        compareRow.add(r);
                        compareCnt++;

                        //lrRow.add(r);

                        if (null != resultUpdated) {
                            resultUpdated.accept(new SGDataTable(compareRow));
                        }
                    }
                } else { // 有L无R时,返回L

                    //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                    PFDataRow r = this.GetDataRowByResultSet(srcRs);
                    PFDataColumn cl = new PFDataColumn("LR", "L");
                    cl.setPFDataType(SGSqlFieldTypeEnum.String);
                    r.getCol().add(cl);
                    // compareTable.Rows.add(dstRow);
                    compareRow.add(r);
                    compareCnt++;

                    lRow.add(r);
                    if (null != resultUpdated) {
                        resultUpdated.accept(new SGDataTable(compareRow));
                    }
                }

                cnt += 1;
                if (null != accessed) {
                    if (cnt == 1) {
                        accessed.go(cnt, dstTotalDouble, compareCnt);
                    }
                    if (logCnt > logBatch) {
                        accessed.go(cnt, dstTotalDouble, compareCnt);
                        logCnt = 0;
                    } else {
                        logCnt++;
                    }
                }
                Thread.sleep(1);
            }
            // dstTable剩余的是 无L有R的,返回R
            // compareRow.addAll(dstTable.Rows);
            for (PFDataRow r : dstTable.Rows) {
                PFDataColumn cr = new PFDataColumn("LR", "R");
                cr.setPFDataType(SGSqlFieldTypeEnum.String);
                r.getCol().add(cr);
                compareRow.add(r);
                compareCnt++;

                rRow.add(r);
            }
            if (null != resultUpdated) {
                resultUpdated.accept(new SGDataTable(compareRow));
            }

            // compareTable = new PFDataTable(compareRow);

            return new SGDataTable(compareRow);
            // this.PrintObject(compareTable.ToDictList());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 为解决旧版本doFindTableRowDifference 方法要把1个表载入内存的问题
     * <p>
     * 此方法有2个不足:
     * 1.要提供目标表名,不能用sql(其实后来想想是可以用sql,如 select * from xx where col1={col1} 这样,见doFindTableRowDifference3)
     * 2.不能得到目标表中多出的数据
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
    public SGDataTable doFindTableRowDifference2(
            // IPFJdbc srcJdbc,
            ISGJdbc dstJdbc, String srcSql, String dstSql,
            String dstTableName,
            String[] joinColumn, String[] compareColumn,
            SGAction<Double, Double, Double> accessed, Consumer<SGDataTable> resultUpdated, Predicate<Boolean> stopAction,
            SGRef<List<PFDataRow>> lRowRef, SGRef<List<PFDataRow>> rRowRef
            //, SGRef<List<PFDataRow>> lrRowRef
    ) {
        try {
            //ISqlExecute srcExec = this; // PFSqlExecute.Init(srcJdbc);
            ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
            this.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));
            dstExec.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));

            ResultSet srcRs = this.GetHugeDataReader(srcSql);
            //PFDataTable dstTable = dstExec.GetDataTable(dstSql, null);
            //Map<String, PFDataRow> dstMap = new HashMap<String, PFDataRow>();

            //int dstTotal = dstTable.Rows.size();
//			double dstTotalDouble = Double.valueOf(dstTotal);
//			for (int j = 0; j < dstTotal; j++) {
//				PFDataRow dstRow = dstTable.Rows.get(j);
//				String key = "";
//				for (int k = 0; k < joinColumn.length; k++) {
//					key += dstRow.getColumn(joinColumn[k]).toString();
//				}
//				dstMap.put(key, dstRow);
//			}


            List<PFDataRow> compareRow = new ArrayList<PFDataRow>(); // 表所有行集合

            // List<PFDataColumn> col = null;// 行所有列集合
            ResultSetMetaData rsMD = srcRs.getMetaData();
            //PFSqlInsertCollection srcInsert = this.getInsertCollection(rsMD);

            SGSqlInsertCollection srcInsert = this.getInsertCollection();
            for (int i = 0; i < rsMD.getColumnCount(); i++) {
                try {
                    int mdIdx = i + 1;
                    //if (!dstMd.isAutoIncrement(mdIdx) && !dstMd.isReadOnly(mdIdx)) { // 不是自增列才插入,否则sql会报错-- benjamin
                    // 20220902
                    String fieldName = rsMD.getColumnLabel(i + 1);
                    SqlUpdateItem updateItem = new SqlUpdateItem();
                    updateItem.Key = fieldName;
                    updateItem.setDstDataType(rsMD.getColumnType(mdIdx));
                    updateItem.setDstDataTypeName(rsMD.getColumnTypeName(mdIdx));
                    updateItem.setDstDataPFType(SGDataHelper.GetPFTypeBySqlType2(rsMD.getColumnType(mdIdx), rsMD.getColumnTypeName(mdIdx)));
                    srcInsert.Add(updateItem);
                    //}
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Double cnt=new Double(0);
            double cnt = 0;

            List<PFDataRow> lRow = lRowRef.GetValue();
            List<PFDataRow> rRow = rRowRef.GetValue();
            //List<PFDataRow> lrRow = lrRowRef.GetValue();

            int logCnt = 0;
            int logBatch = 10000;

            double compareCnt = 0;
            while (srcRs.next()) {
                if (stopAction != null) {
                    if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                        CloseReader(srcRs); // rdr不是本连接产生的,这样关闭不太好
                        CloseConn();
                        return new SGDataTable(compareRow);
                    }
                }
                // boolean isExist=false;
                srcInsert.UpdateByDataReader(srcRs);

                String keyL = "";
                //PFSqlWhereCollection where =this.getWhereCollection();
                for (int k = 0; k < joinColumn.length; k++) {
                    //keyL += srcInsert.Get(joinColumn[k]).Value.toString();
                    keyL += SGDataHelper.ObjectToString0(srcInsert.Get(joinColumn[k]).Value);
                    //keyL+=BaseSqlUpdateCollection.getValueByDataReaderI2(srcRs, rsMD, k);

                    //where.Add(joinColumn[k], srcInsert.Get(joinColumn[k]).Value);
                }

                //PFDataTable dstTable =this.GetDataTable(PFDataHelper.FormatString("select top 1 * from {0} {1}",dstTableName,where.ToSql()));
                SGDataTable dstTable = GetOneRow(dstTableName, a -> {
                    for (int k = 0; k < joinColumn.length; k++) {
                        a.Add(joinColumn[k], srcInsert.Get(joinColumn[k]).Value);
                    }
                });
                if (null != dstTable && !dstTable.IsEmpty()) {
                    //PFDataRow dstRow = dstMap.get(keyL);
                    PFDataRow dstRow = dstTable.Rows.get(0);
                    // Object compareValueL=srcInsert.Get(compareColumn[0]).Value;
                    // Object compareValueR=dstRow.getColumn(compareColumn[0]);
                    boolean isValueEqual = true;
                    // for(int k=0;k<joinColumn.length;k++) {
                    for (int k = 0; k < compareColumn.length; k++) {
                        Object compareValueL = srcInsert.Get(compareColumn[k]).Value;
                        //Object compareValueL = BaseSqlUpdateCollection.getValueByDataReaderI2(srcRs, rsMD, k);
                        Object compareValueR = dstRow.getColumn(compareColumn[k]);
                        if (!SGDataHelper.IsPFValueEquals(compareValueL, compareValueR)) {
                            isValueEqual = false;
                            break;
                        }
                    }
                    // if(compareValueL.equals(compareValueR)) {//有L有R,且compare相等.不返回,从dstTable中移除R
                    if (isValueEqual) { // 有L有R,且compare相等.不返回,从dstTable中移除R
                        dstTable.Rows.remove(dstRow);
                        //dstMap.remove(keyL);
                    } else { // 有L有R,但compare不相等.返回L和R
                        compareRow.add(dstRow);
                        compareCnt++;
                        PFDataColumn cr = new PFDataColumn("LR", "R");
                        cr.setPFDataType(SGSqlFieldTypeEnum.String);
                        dstRow.getCol().add(cr);
                        // dstTable.Rows.remove(j);
                        dstTable.Rows.remove(dstRow);
                        //dstMap.remove(keyL);

                        //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                        PFDataRow r = this.GetDataRowByResultSet(srcRs);

                        PFDataColumn cl = new PFDataColumn("LR", "L");
                        cl.setPFDataType(SGSqlFieldTypeEnum.String);
                        r.getCol().add(cl);
                        // compareTable.Rows.add(dstRow);
                        compareRow.add(r);
                        compareCnt++;

                        //lrRow.add(r);

                        if (null != resultUpdated) {
                            resultUpdated.accept(new SGDataTable(compareRow));
                        }
                    }
                } else { // 有L无R时,返回L

                    //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                    PFDataRow r = this.GetDataRowByResultSet(srcRs);
                    PFDataColumn cl = new PFDataColumn("LR", "L");
                    cl.setPFDataType(SGSqlFieldTypeEnum.String);
                    r.getCol().add(cl);
                    // compareTable.Rows.add(dstRow);
                    compareRow.add(r);
                    compareCnt++;

                    lRow.add(r);
                    if (null != resultUpdated) {
                        resultUpdated.accept(new SGDataTable(compareRow));
                    }
                }

                cnt += 1;
                if (null != accessed) {
                    if (cnt == 1) {
                        //accessed.go(cnt, dstTotalDouble,compareCnt);
                    }
                    if (logCnt > logBatch) {
                        //accessed.go(cnt, dstTotalDouble,compareCnt);
                        logCnt = 0;
                    } else {
                        logCnt++;
                    }
                }
                Thread.sleep(1);
            }
//			// dstTable剩余的是 无L有R的,返回R
//			// compareRow.addAll(dstTable.Rows);
//			for (PFDataRow r : dstTable.Rows) {
//				PFDataColumn cr = new PFDataColumn("LR", "R");
//				cr.setPFDataType(PFSqlFieldTypeEnum.String);
//				r.getCol().add(cr);
//				compareRow.add(r);
//				compareCnt++;
//
//				rRow.add(r);
//			}
            if (null != resultUpdated) {
                resultUpdated.accept(new SGDataTable(compareRow));
            }

            // compareTable = new PFDataTable(compareRow);

            return new SGDataTable(compareRow);
            // this.PrintObject(compareTable.ToDictList());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 为解决旧版本doFindTableRowDifference 方法要把1个表载入内存的问题
     * <p>
     * 此方法有2个不足:
     * 1.要提供目标表名,不能用sql(其实后来想想是可以用sql,如 "select top 1 * from xx {where}" 这样)
     * 2.不能得到目标表中多出的数据
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
    public SGDataTable doFindTableRowDifference3(
            // IPFJdbc srcJdbc,
            ISGJdbc dstJdbc, String srcSql, String dstSql,
            //String dstTableName,
            String[] joinColumn, String[] compareColumn,
            SGAction<Double, Double, Double> accessed, Consumer<SGDataTable> resultUpdated, Predicate<Boolean> stopAction,
            SGRef<List<PFDataRow>> lRowRef, SGRef<List<PFDataRow>> rRowRef
            //, SGRef<List<PFDataRow>> lrRowRef
    ) {
        try {
            //ISqlExecute srcExec = this; // PFSqlExecute.Init(srcJdbc);
            ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
            this.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));
            dstExec.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));

            ResultSet srcRs = this.GetHugeDataReader(srcSql);
            //PFDataTable dstTable = dstExec.GetDataTable(dstSql, null);
            //Map<String, PFDataRow> dstMap = new HashMap<String, PFDataRow>();

            //int dstTotal = dstTable.Rows.size();
//			double dstTotalDouble = Double.valueOf(dstTotal);
//			for (int j = 0; j < dstTotal; j++) {
//				PFDataRow dstRow = dstTable.Rows.get(j);
//				String key = "";
//				for (int k = 0; k < joinColumn.length; k++) {
//					key += dstRow.getColumn(joinColumn[k]).toString();
//				}
//				dstMap.put(key, dstRow);
//			}


            List<PFDataRow> compareRow = new ArrayList<PFDataRow>(); // 表所有行集合

            // List<PFDataColumn> col = null;// 行所有列集合
            ResultSetMetaData rsMD = srcRs.getMetaData();
            //PFSqlInsertCollection srcInsert = this.getInsertCollection(rsMD);

            SGSqlInsertCollection srcInsert = this.getInsertCollection();
            for (int i = 0; i < rsMD.getColumnCount(); i++) {
                try {
                    int mdIdx = i + 1;
                    //if (!dstMd.isAutoIncrement(mdIdx) && !dstMd.isReadOnly(mdIdx)) { // 不是自增列才插入,否则sql会报错-- benjamin
                    // 20220902
                    String fieldName = rsMD.getColumnLabel(i + 1);
                    SqlUpdateItem updateItem = new SqlUpdateItem();
                    updateItem.Key = fieldName;
                    updateItem.setDstDataType(rsMD.getColumnType(mdIdx));
                    updateItem.setDstDataTypeName(rsMD.getColumnTypeName(mdIdx));
                    updateItem.setDstDataPFType(SGDataHelper.GetPFTypeBySqlType2(rsMD.getColumnType(mdIdx), rsMD.getColumnTypeName(mdIdx)));
                    srcInsert.Add(updateItem);
                    //}
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Double cnt=new Double(0);
            double cnt = 0;

            List<PFDataRow> lRow = lRowRef.GetValue();
            List<PFDataRow> rRow = rRowRef.GetValue();
            //List<PFDataRow> lrRow = lrRowRef.GetValue();

            int logCnt = 0;
            int logBatch = 10000;

            double compareCnt = 0;
            while (srcRs.next()) {
                if (stopAction != null) {
                    if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                        CloseReader(srcRs); // rdr不是本连接产生的,这样关闭不太好
                        CloseConn();
                        return new SGDataTable(compareRow);
                    }
                }
                // boolean isExist=false;
                srcInsert.UpdateByDataReader(srcRs);

                String keyL = "";
                //PFSqlWhereCollection where =this.getWhereCollection();
                for (int k = 0; k < joinColumn.length; k++) {
                    //keyL += srcInsert.Get(joinColumn[k]).Value.toString();
                    keyL += SGDataHelper.ObjectToString0(srcInsert.Get(joinColumn[k]).Value);
                    //keyL+=BaseSqlUpdateCollection.getValueByDataReaderI2(srcRs, rsMD, k);

                    //where.Add(joinColumn[k], srcInsert.Get(joinColumn[k]).Value);
                }

                //PFDataTable dstTable =this.GetDataTable(PFDataHelper.FormatString("select top 1 * from {0} {1}",dstTableName,where.ToSql()));
//				PFDataTable dstTable =GetOneRow(dstTableName,a->{
//					for (int k = 0; k < joinColumn.length; k++) {
//						a.Add(joinColumn[k], srcInsert.Get(joinColumn[k]).Value);
//					}
//				});

                //如 select top 1 * from xx where col1={col1}
//				String sql=dstSql;
//				for (int k = 0; k < joinColumn.length; k++) {
//					sql=sql.replace("{"+joinColumn[k]+"}",srcInsert.Get(joinColumn[k]).Value.toString());
//				}

                //如 select top 1 * from xx {where}
                SGSqlWhereCollection where = this.getWhereCollection();
                for (int k = 0; k < joinColumn.length; k++) {
                    where.Add(joinColumn[k], srcInsert.Get(joinColumn[k]).Value);
                }
                String sql = dstSql.replace("{where}", where.ToSql());

                //PFDataTable dstTable =this.GetDataTable(sql,null,false);
                //PFDataTable dstTable =dstExec.GetDataTable(sql,null);
                SGDataTable dstTable = dstExec.GetDataTable(sql, null, false);
                if (null != dstTable && !dstTable.IsEmpty()) {
                    //PFDataRow dstRow = dstMap.get(keyL);
                    PFDataRow dstRow = dstTable.Rows.get(0);
                    // Object compareValueL=srcInsert.Get(compareColumn[0]).Value;
                    // Object compareValueR=dstRow.getColumn(compareColumn[0]);
                    boolean isValueEqual = true;
                    // for(int k=0;k<joinColumn.length;k++) {
                    for (int k = 0; k < compareColumn.length; k++) {
                        Object compareValueL = srcInsert.Get(compareColumn[k]).Value;
                        //Object compareValueL = BaseSqlUpdateCollection.getValueByDataReaderI2(srcRs, rsMD, k);
                        Object compareValueR = dstRow.getColumn(compareColumn[k]);
                        if (!SGDataHelper.IsPFValueEquals(compareValueL, compareValueR)) {
                            isValueEqual = false;
                            break;
                        }
                    }
                    // if(compareValueL.equals(compareValueR)) {//有L有R,且compare相等.不返回,从dstTable中移除R
                    if (isValueEqual) { // 有L有R,且compare相等.不返回,从dstTable中移除R
                        dstTable.Rows.remove(dstRow);
                        //dstMap.remove(keyL);
                    } else { // 有L有R,但compare不相等.返回L和R
                        compareRow.add(dstRow);
                        compareCnt++;
                        PFDataColumn cr = new PFDataColumn("LR", "R");
                        cr.setPFDataType(SGSqlFieldTypeEnum.String);
                        dstRow.getCol().add(cr);
                        // dstTable.Rows.remove(j);
                        dstTable.Rows.remove(dstRow);
                        //dstMap.remove(keyL);

                        //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                        PFDataRow r = this.GetDataRowByResultSet(srcRs);

                        PFDataColumn cl = new PFDataColumn("LR", "L");
                        cl.setPFDataType(SGSqlFieldTypeEnum.String);
                        r.getCol().add(cl);
                        // compareTable.Rows.add(dstRow);
                        compareRow.add(r);
                        compareCnt++;

                        //lrRow.add(r);

                        if (null != resultUpdated) {
                            resultUpdated.accept(new SGDataTable(compareRow));
                        }
                    }
                } else { // 有L无R时,返回L

                    //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                    PFDataRow r = this.GetDataRowByResultSet(srcRs);
                    PFDataColumn cl = new PFDataColumn("LR", "L");
                    cl.setPFDataType(SGSqlFieldTypeEnum.String);
                    r.getCol().add(cl);
                    // compareTable.Rows.add(dstRow);
                    compareRow.add(r);
                    compareCnt++;

                    lRow.add(r);
                    if (null != resultUpdated) {
                        resultUpdated.accept(new SGDataTable(compareRow));
                    }
                }

                cnt += 1;
                if (null != accessed) {
                    if (cnt == 1) {
                        //accessed.go(cnt, dstTotalDouble,compareCnt);
                        accessed.go(cnt, new Double(1), compareCnt);
                    }
                    if (logCnt > logBatch) {
                        //accessed.go(cnt, dstTotalDouble,compareCnt);
                        accessed.go(cnt, new Double(1), compareCnt);
                        logCnt = 0;
                    } else {
                        logCnt++;
                    }
                }
                Thread.sleep(1);
            }
//			// dstTable剩余的是 无L有R的,返回R
//			// compareRow.addAll(dstTable.Rows);
//			for (PFDataRow r : dstTable.Rows) {
//				PFDataColumn cr = new PFDataColumn("LR", "R");
//				cr.setPFDataType(PFSqlFieldTypeEnum.String);
//				r.getCol().add(cr);
//				compareRow.add(r);
//				compareCnt++;
//
//				rRow.add(r);
//			}
            if (null != resultUpdated) {
                resultUpdated.accept(new SGDataTable(compareRow));
            }

            // compareTable = new PFDataTable(compareRow);

            return new SGDataTable(compareRow);
            // this.PrintObject(compareTable.ToDictList());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public SGDataTable doFindTableRowDifference4(
            // IPFJdbc srcJdbc,
            ISGJdbc dstJdbc, String srcSql, String dstSql,
            //String dstTableName,
            String[] joinColumn, String[] compareColumn,
            SGAction<Double, Double, Double> accessed, Consumer<SGDataTable> resultUpdated, Predicate<Boolean> stopAction,
            SGRef<List<PFDataRow>> lRowRef, SGRef<List<PFDataRow>> rRowRef
            //, SGRef<List<PFDataRow>> lrRowRef
    ) {
        try {
            //ISqlExecute srcExec = this; // PFSqlExecute.Init(srcJdbc);
            ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
            this.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));
            dstExec.UpToCommandTimeOut(new PFSqlCommandTimeoutSecond(this.CommandTimeOut));

            ResultSet srcRs = this.GetHugeDataReader(srcSql);
            ResultSet dstRs = dstExec.GetHugeDataReader(dstSql);
            String keyL = "";
            String keyR = "";
            //PFDataTable dstTable = dstExec.GetDataTable(dstSql, null);
            //Map<String, PFDataRow> dstMap = new HashMap<String, PFDataRow>();

            //int dstTotal = dstTable.Rows.size();
//			double dstTotalDouble = Double.valueOf(dstTotal);
//			for (int j = 0; j < dstTotal; j++) {
//				PFDataRow dstRow = dstTable.Rows.get(j);
//				String key = "";
//				for (int k = 0; k < joinColumn.length; k++) {
//					key += dstRow.getColumn(joinColumn[k]).toString();
//				}
//				dstMap.put(key, dstRow);
//			}


            List<PFDataRow> compareRow = new ArrayList<PFDataRow>(); // 表所有行集合

            List<PFDataRow> cacheL = new ArrayList<PFDataRow>(); //
            List<PFDataRow> cacheR = new ArrayList<PFDataRow>(); //
            List<String> cacheKeyL = new ArrayList<String>(); //
            List<String> cacheKeyR = new ArrayList<String>(); //

            // List<PFDataColumn> col = null;// 行所有列集合
            ResultSetMetaData rsMD = srcRs.getMetaData();
            ResultSetMetaData rsMDR = dstRs.getMetaData();
            //PFSqlInsertCollection srcInsert = this.getInsertCollection(rsMD);

            SGSqlInsertCollection srcInsert = this.getInsertCollection();
            //PFSqlInsertCollection srcInsert =this.getInsertCollection(srcRs.getMetaData());
            SGSqlInsertCollection dstInsert = dstExec.getInsertCollection();
            //PFSqlInsertCollection dstInsert =dstExec.getInsertCollection(dstRs.getMetaData());
            for (int i = 0; i < rsMD.getColumnCount(); i++) {
                try {
                    int mdIdx = i + 1;
                    //if (!dstMd.isAutoIncrement(mdIdx) && !dstMd.isReadOnly(mdIdx)) { // 不是自增列才插入,否则sql会报错-- benjamin
                    // 20220902
                    String fieldName = rsMD.getColumnLabel(i + 1);
                    SqlUpdateItem updateItem = new SqlUpdateItem();
                    updateItem.Key = fieldName;
                    updateItem.setDstDataType(rsMD.getColumnType(mdIdx));
                    updateItem.setDstDataTypeName(rsMD.getColumnTypeName(mdIdx));
                    updateItem.setDstDataPFType(SGDataHelper.GetPFTypeBySqlType2(rsMD.getColumnType(mdIdx), rsMD.getColumnTypeName(mdIdx)));
                    srcInsert.Add(updateItem);
                    //}
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < rsMDR.getColumnCount(); i++) {
                try {
                    int mdIdx = i + 1;

                    String fieldName2 = rsMDR.getColumnLabel(i + 1);
                    SqlUpdateItem updateItem2 = new SqlUpdateItem();
                    updateItem2.Key = fieldName2;
                    updateItem2.setDstDataType(rsMDR.getColumnType(mdIdx));
                    updateItem2.setDstDataTypeName(rsMDR.getColumnTypeName(mdIdx));
                    updateItem2.setDstDataPFType(SGDataHelper.GetPFTypeBySqlType2(rsMDR.getColumnType(mdIdx), rsMDR.getColumnTypeName(mdIdx)));
                    dstInsert.Add(updateItem2);
                    //}
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Double cnt=new Double(0);
            double cnt = 0;

            List<PFDataRow> lRow = lRowRef.GetValue();
            List<PFDataRow> rRow = rRowRef.GetValue();
            //List<PFDataRow> lrRow = lrRowRef.GetValue();

            int logCnt = 0;
            int logBatch = 10000;

            double compareCnt = 0;
            boolean endL = false;
            boolean endR = false;
            while ((!(endL && endR && cacheL.isEmpty() && cacheR.isEmpty()))) {
                if ((!cacheL.isEmpty()) && (!cacheR.isEmpty())) {
                    for (int i = 0; i < cacheKeyL.size(); i++) {
                        for (int j = 0; j < cacheKeyR.size(); j++) {
                            if (cacheKeyL.get(i).equals(cacheKeyR.get(j))) {
                                for (int k = i; k >= 0; k--) {
                                    //有L无R时,返回L

                                    if (k == i) {
                                        cacheKeyL.remove(k);
                                        cacheL.remove(k);
                                    } else {
                                        PFDataRow r = cacheL.get(k);
                                        //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                                        PFDataColumn cl = new PFDataColumn("LR", "L");
                                        cl.setPFDataType(SGSqlFieldTypeEnum.String);
                                        r.getCol().add(cl);
                                        // compareTable.Rows.add(dstRow);
                                        compareRow.add(r);
                                        compareCnt++;

                                        lRow.add(r);
                                        cacheKeyL.remove(k);
                                        cacheL.remove(k);
                                        if (null != resultUpdated) {
                                            resultUpdated.accept(new SGDataTable(compareRow));
                                        }
                                    }
                                }
                                for (int k = j; k >= 0; k--) {
                                    //有L无R时,返回L

                                    if (k == j) {
                                        cacheKeyR.remove(k);
                                        cacheR.remove(k);
                                    } else {
                                        PFDataRow r = cacheR.get(k);
                                        //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                                        PFDataColumn cl = new PFDataColumn("LR", "R");
                                        cl.setPFDataType(SGSqlFieldTypeEnum.String);
                                        r.getCol().add(cl);
                                        // compareTable.Rows.add(dstRow);
                                        compareRow.add(r);
                                        compareCnt++;

                                        lRow.add(r);
                                        cacheKeyR.remove(k);
                                        cacheR.remove(k);
                                        if (null != resultUpdated) {
                                            resultUpdated.accept(new SGDataTable(compareRow));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (endL && endR && (!cacheL.isEmpty() || !cacheR.isEmpty())) {
                    for (int k = 0; k < cacheL.size(); k++) {
                        //有L无R时,返回L

                        PFDataRow r = cacheL.get(k);
                        //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                        PFDataColumn cl = new PFDataColumn("LR", "L");
                        cl.setPFDataType(SGSqlFieldTypeEnum.String);
                        r.getCol().add(cl);
                        // compareTable.Rows.add(dstRow);
                        compareRow.add(r);
                        compareCnt++;

                        lRow.add(r);
                        if (null != resultUpdated) {
                            resultUpdated.accept(new SGDataTable(compareRow));
                        }
                    }
                    for (int k = 0; k < cacheR.size(); k++) {
                        //有L无R时,返回L

                        PFDataRow r = cacheR.get(k);
                        //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                        PFDataColumn cl = new PFDataColumn("LR", "R");
                        cl.setPFDataType(SGSqlFieldTypeEnum.String);
                        r.getCol().add(cl);
                        // compareTable.Rows.add(dstRow);
                        compareRow.add(r);
                        compareCnt++;

                        lRow.add(r);
                        if (null != resultUpdated) {
                            resultUpdated.accept(new SGDataTable(compareRow));
                        }
                    }
                    cacheKeyL.clear();
                    cacheL.clear();
                    cacheKeyR.clear();
                    cacheR.clear();
                }

                if (0 == cnt) {
                    endL = !srcRs.next();
                    endR = !dstRs.next();
                    srcInsert.UpdateByDataReader(srcRs);
                    dstInsert.UpdateByDataReader(dstRs);
                }

                keyL = "";
                for (int k = 0; k < joinColumn.length; k++) {
                    keyL += SGDataHelper.ObjectToString0(srcInsert.Get(joinColumn[k]).Value);
                }
                keyR = "";
                for (int k = 0; k < joinColumn.length; k++) {
                    keyR += SGDataHelper.ObjectToString0(dstInsert.Get(joinColumn[k]).Value);
                }
                //---------------

                PFDataRow srcRow = endL ? null : this.GetDataRowByResultSet(srcRs);
                PFDataRow dstRow = endR ? null : this.GetDataRowByResultSet(dstRs);
                //----------------
                if (keyL.equals(keyR)) {

                    //有L有R时,比较值
                    boolean isValueEqual = true;
                    // for(int k=0;k<joinColumn.length;k++) {
                    for (int k = 0; k < compareColumn.length; k++) {
                        Object compareValueL = srcInsert.Get(compareColumn[k]).Value;
                        //Object compareValueL = BaseSqlUpdateCollection.getValueByDataReaderI2(srcRs, rsMD, k);
                        //Object compareValueR = dstRow.getColumn(compareColumn[k]);
                        Object compareValueR = dstInsert.Get(compareColumn[k]).Value;
                        if (!SGDataHelper.IsPFValueEquals(compareValueL, compareValueR)) {
                            isValueEqual = false;
                            break;
                        }
                    }
                    // if(compareValueL.equals(compareValueR)) {//有L有R,且compare相等.不返回,从dstTable中移除R
                    if (isValueEqual) { // 有L有R,且compare相等.不返回,从dstTable中移除R
                    } else { // 有L有R,但compare不相等.返回L和R

                        PFDataRow rowR = this.GetDataRowByResultSet(dstRs);
                        compareRow.add(rowR);
                        compareCnt++;

                        PFDataColumn cr = new PFDataColumn("LR", "R");
                        cr.setPFDataType(SGSqlFieldTypeEnum.String);
                        rowR.getCol().add(cr);

                        //PFDataRow r = this.GetDataRowByInsertCollection(srcInsert);
                        PFDataRow r = this.GetDataRowByResultSet(srcRs);

                        PFDataColumn cl = new PFDataColumn("LR", "L");
                        cl.setPFDataType(SGSqlFieldTypeEnum.String);
                        r.getCol().add(cl);
                        // compareTable.Rows.add(dstRow);
                        compareRow.add(r);
                        compareCnt++;

                        //lrRow.add(r);

                        if (null != resultUpdated) {
                            resultUpdated.accept(new SGDataTable(compareRow));
                        }
                    }

                    endL = !srcRs.next();
                    endR = !dstRs.next();
                    if (!endL) {
                        srcInsert.UpdateByDataReader(srcRs);
                    }
                    if (!endR) {
                        dstInsert.UpdateByDataReader(dstRs);
                    }
                } else {
                    //哪边cache少就先读那个
                    //if(keyL.compareTo(keyR)>0){
                    if (cacheKeyL.size() > cacheKeyR.size()) {
                        if (!endR) {
                            cacheR.add(dstRow);
                            cacheKeyR.add(keyR);
                            endR = !dstRs.next();
                        } else {
                            if (!endL) {
                                cacheL.add(srcRow);
                                cacheKeyL.add(keyL);
                                endL = !srcRs.next();
                            }
                        }
                        if (!endL) {
                            srcInsert.UpdateByDataReader(srcRs);
                        }
                        if (!endR) {
                            dstInsert.UpdateByDataReader(dstRs);
                        }

                    }
                    //else if(keyL.compareTo(keyR)<0)
                    else if (cacheKeyL.size() < cacheKeyR.size()) {
                        if (!endL) {
                            cacheL.add(srcRow);
                            cacheKeyL.add(keyL);
                            endL = !srcRs.next();
                        } else {
                            if (!endR) {
                                cacheR.add(dstRow);
                                cacheKeyR.add(keyR);
                                endR = !dstRs.next();
                            }
                        }
                        if (!endL) {
                            srcInsert.UpdateByDataReader(srcRs);
                        }
                        if (!endR) {
                            dstInsert.UpdateByDataReader(dstRs);
                        }

                    } else {
                        if (!endL) {
                            cacheL.add(srcRow);
                            cacheKeyL.add(keyL);
                            endL = !srcRs.next();
                            if (!endL) {
                                srcInsert.UpdateByDataReader(srcRs);
                            }
                        }
                        if (!endR) {
                            cacheR.add(dstRow);
                            cacheKeyR.add(keyR);
                            endR = !dstRs.next();
                            if (!endR) {
                                dstInsert.UpdateByDataReader(dstRs);
                            }
                        }
                    }
                }

                if (stopAction != null) {
                    if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                        CloseReader(srcRs); // rdr不是本连接产生的,这样关闭不太好
                        CloseConn();
                        return new SGDataTable(compareRow);
                    }
                }
                cnt += 1;
                if (null != accessed) {
                    if (cnt == 1) {
                        //accessed.go(cnt, dstTotalDouble,compareCnt);
                        accessed.go(cnt, new Double(1), compareCnt);
                    }
                    if (logCnt > logBatch) {
                        //accessed.go(cnt, dstTotalDouble,compareCnt);
                        accessed.go(cnt, new Double(1), compareCnt);
                        logCnt = 0;
                    } else {
                        logCnt++;
                    }
                }
                Thread.sleep(1);
            }
//			// dstTable剩余的是 无L有R的,返回R
//			// compareRow.addAll(dstTable.Rows);
//			for (PFDataRow r : dstTable.Rows) {
//				PFDataColumn cr = new PFDataColumn("LR", "R");
//				cr.setPFDataType(PFSqlFieldTypeEnum.String);
//				r.getCol().add(cr);
//				compareRow.add(r);
//				compareCnt++;
//
//				rRow.add(r);
//			}
            if (null != resultUpdated) {
                resultUpdated.accept(new SGDataTable(compareRow));
            }

            // compareTable = new PFDataTable(compareRow);

            return new SGDataTable(compareRow);
            // this.PrintObject(compareTable.ToDictList());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    // public <T> boolean doInsertList(
    // List<String> srcFieldNames//SqlInsertCollection dstInsert
    // ,String tableName,
    // //int cnt,
    // Function<Integer,Boolean> isEnd ,
    // Function<Integer,T> getItemAction ,
    // PFAction<BaseSqlUpdateCollection,Integer,Object> rowAction,
    // Consumer<Integer> sqlRowsCopiedAction,
    // Predicate<Boolean> stopAction
    // ) {
    // PFSqlInsertCollection dstInsert=null;
    //
    // BatchInsertOption insertOption = GetInsertOption();
    //
    // OpenConn();
    //
    // int idx = 0;
    //
    // insertedCnt = 0;// 已插入的行数
    //
    // int batchSize = insertOption.getProcessBatch();// 50000;//
    // tidb设置大些试试,测试100万行/25秒
    // int batchCnt = 0;
    //
    // Boolean hasUnDo = false;
    //
    // UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
    // try {
    //
    // boolean hasSysLimitId = srcFieldNames.contains(sys_limit_id);
    // String sysLimitIdFieldName = sys_limit_id;
    // long currentSysLimitId = -1;
    // lastInsertedId = -1;
    //
    // ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
    // :GetMetaData(tableName,srcFieldNames);
    //
    // PreparedStatement ps =srcFieldNames==null? GetPs(tableName):
    // GetPs(tableName,srcFieldNames);
    //
    // // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
    // if (dstInsert == null) {
    //
    // dstInsert = getInsertCollection(dstMd);
    // }
    //
    // while (true) {
    // //T item = null;
    //// boolean hasNext = idx < cnt;
    // boolean hasNext =isEnd.apply(idx);
    //// if (hasNext) {
    //// item=getItemAction.apply(idx);
    //// }
    // if (hasNext) {
    // if(insertOption.getAutoUpdateModel()&&null!=getItemAction) {
    // T item=getItemAction.apply(idx);
    // dstInsert.UpdateModelValueAutoConvert(item);
    // }
    // if (rowAction != null) {
    // //rowAction.accept(dstInsert);
    // rowAction.go(dstInsert,idx,null);
    // }
    //
    // ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
    //
    // batchCnt++;
    // hasUnDo = true;
    //
    // if (hasSysLimitId) {
    // //currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
    // currentSysLimitId
    // =PFDataHelper.ObjectToLong0(dstInsert.Get(sysLimitIdFieldName).Value);
    // }
    // }
    // if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
    //
    // Boolean b = false;
    // try {
    // ps.executeBatch();
    // conn.commit();
    // b = true;
    // if (hasSysLimitId) {
    // lastInsertedId = currentSysLimitId;
    // }
    // } catch (Exception e) {
    // SetError(e);
    // }
    //
    // if (!b) {
    // // CloseReader(rdr);
    // CloseConn();
    // return false;
    // } else {
    // insertedCnt = idx + 1;
    //// if (hasSysLimitId) {
    //// lastInsertedId = currentSysLimitId;
    //// }
    // }
    // if (sqlRowsCopiedAction != null) {
    // sqlRowsCopiedAction.accept(idx);
    // }
    // if (stopAction != null) {
    // if (stopAction.test(true)) {// 允许中途终止--benjamin20200812
    // // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
    // CloseConn();
    // return true;
    // }
    // }
    //
    //
    // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
    //
    // hasUnDo = false;
    // batchCnt = 0;
    // } else {
    // // batchCnt++;
    // // oneThousandCount++;
    // }
    // if (!hasNext) {
    // break;
    // }
    // idx++;
    //
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // SetError(e);
    // return false;
    // }
    //
    // CloseConn();
    //
    // return true;
    // }

    // protected List<String> getFieldsByRowObject(
    // Class<?> cls) {
    //
    // List<String> srcFieldNames=null;
    // if(ResultSet.class.isAssignableFrom(cls)){
    //
    // } else if ( Map.class.isAssignableFrom(cls) ) {
    // srcFieldNames=new
    // ArrayList<String>(PFDataHelper.GetProperties(cls).keySet());
    // }else if(PFDataRow.class.isAssignableFrom(cls)) {
    // } else {
    // srcFieldNames=new
    // ArrayList<String>(PFDataHelper.GetProperties(cls).keySet());
    // }
    // return srcFieldNames;
    // }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected List<String> getFieldsByRowObject(Object row) {

        List<String> srcFieldNames = null;
        if (row instanceof ResultSet) {

        } else if (row instanceof Map) {
            srcFieldNames = new ArrayList<String>(((Map) row).keySet());
        } else if (row instanceof PFDataRow) {
        } else {
            srcFieldNames = new ArrayList<String>(SGDataHelper.GetProperties(row.getClass()).keySet());
        }
        return srcFieldNames;
    }

//    @SuppressWarnings("resource")
//    @Override
//    public <T> boolean doInsertList(
//            List<String> dstFieldNames // SqlInsertCollection dstInsert
//            , String tableName,
//            PFFunc3<Integer, Object, Object, Boolean> hasNextAction, Function<Integer, T> getItemAction,
//            // PFFunc3<Integer,Object,Object,T> getItemAction ,
//            SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
//            Predicate<Boolean> stopAction) {
//        PFSqlInsertCollection dstInsert = null;
//
//        BatchInsertOption insertOption = GetInsertOption();
//
//        // OpenConn();
//
//        int idx = 0;
//
//        insertedCnt = 0; // 已插入的行数
//
//        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
//
//        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//
//        try {
//            boolean hasSysLimitId = false;
//            String sysLimitIdFieldName = sys_limit_id;
//            long currentSysLimitId = -1;
//            lastInsertedId = -1;
//
//            List<String> srcFieldNames = null;
//
//            ResultSetMetaData dstMd = null;
//
//            OpenConn();
//            conn.setAutoCommit(false);
//            PreparedStatement ps = null;
//
//            PFBatchHelper batchHelper = new PFBatchHelper();
//            batchHelper.batchSize = batchSize;
//            batchHelper.batchCnt = 0;
//            while (true) {
//                batchHelper.hasNext = hasNextAction.go(idx, null, null);
//                if (batchHelper.hasNext) {
//                    T item = null;
//                    if (null != getItemAction) {
//                        item = getItemAction.apply(idx);
//                    }
//
//                    if (0 == idx) {
//                        if (null == srcFieldNames && null != item) {
//                            srcFieldNames = getFieldsByRowObject(item);
//                        }
//                        hasSysLimitId = null != srcFieldNames && srcFieldNames.contains(sys_limit_id);
//
//                        if (null == dstFieldNames) {
//
//                            dstFieldNames = SGDataHelper.ListSelect(this.GetTableEditableFields(tableName),
//                                    a -> a.getFieldName());
//
//                            if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
//                                SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
//                                // dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
//                                dstFieldNames = SGDataHelper.ListWhere(dstFieldNames,
//                                        a -> SGDataHelper.ListAny(srcFieldNamesRef.GetValue(),
//                                                b -> b.equalsIgnoreCase(a))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
//                            }
//                        }
//                        if (null == dstMd) {
//                            dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
//                                    .filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
//                        }
//                        if (null == ps) {
//                            ps = dstFieldNames == null ? GetPs(tableName) : GetPs(tableName, dstFieldNames, false);
//
//                        }
//                        if (dstInsert == null) {
//                            dstInsert = getInsertCollection(dstMd);
//                            if (null != srcFieldNames) {
//                                dstInsert.UpdateLowerKeyMapBySrcField(srcFieldNames);
//                            }
//                        }
//                    }
//
//                    if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
//                        dstInsert.UpdateModelValue(item);
//                        if (hasSysLimitId) {
//                            if (item instanceof ResultSet) {
//                                currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
//                            }
//                        }
//                    }
//
//                    if (rowAction != null) {
//                        rowAction.go(dstInsert, idx, null);
//                    }
//
//                    ps = insertIntoCachedRowSet(ps, dstMd, dstInsert); // ,dstColumnType);
//
//                    batchHelper.batchCnt++;
//                }
//                if (batchHelper.ifDo()) {
//
//                    Boolean b = false;
//                    try {
//                        ps.executeBatch();
//                        conn.commit();
//                        b = true;
//                        if (hasSysLimitId) {
//                            lastInsertedId = currentSysLimitId;
//                        }
//                    } catch (Exception e) {
//                        SetError(e);
//                    }
//
//                    if (!b) {
//                        // CloseReader(rdr);
//                        CloseConn();
//                        return false;
//                    } else {
//                        insertedCnt = idx + 1;
//                    }
//                    if (sqlRowsCopiedAction != null) {
//                        sqlRowsCopiedAction.accept(idx);
//                    }
//                    if (stopAction != null) {
//                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
//                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//                            CloseConn();
//                            return true;
//                        }
//                    }
//
//                    ps.clearBatch();
//
//                    batchHelper.hasDone();
//                } else {
//                    // batchCnt++;
//                    // oneThousandCount++;
//                }
//                if (!batchHelper.hasNext) {
//                    break;
//                }
//                idx++;
//
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            SetError(e);
//            return false;
//        }
//
//        CloseConn();
//
//        return true;
//    }

    @SuppressWarnings("resource")
    @Override
    public <T> boolean doInsertList(
            List<String> dstFieldNames // SqlInsertCollection dstInsert
            , String tableName,
            PFFunc3<Integer, Object, Object, Boolean> hasNextAction, Function<Integer, T> getItemAction,
            // PFFunc3<Integer,Object,Object,T> getItemAction ,
            SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
            Predicate<Boolean> stopAction) {
        SGSqlInsertCollection dstInsert = null;

        BatchInsertOption insertOption = GetInsertOption();

        // OpenConn();

        int idx = 0;

        insertedCnt = 0; // 已插入的行数

        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

        //在mysql 5.1.46-community中,md的conn close之后调用md.readOnly报错，因此用另一个conn来查md应该比较保险--benjamin 20250208
		ISqlExecute mdExec = null;//PFSqlExecute.Init(this._jdbc);
        try {
            boolean hasSysLimitId = false;
            String sysLimitIdFieldName = sys_limit_id;
            long currentSysLimitId = -1;
            lastInsertedId = -1;

            List<String> srcFieldNames = null;

            ResultSetMetaData dstMd = null;

            OpenConn();
            conn.setAutoCommit(false);
            PreparedStatement ps = null;

            PFBatchHelper batchHelper = new PFBatchHelper();
            batchHelper.batchSize = batchSize;
            batchHelper.batchCnt = 0;
            while (true) {
                batchHelper.hasNext = hasNextAction.go(idx, null, null);
                if (batchHelper.hasNext) {
                    T item = null;
                    if (null != getItemAction) {
                        item = getItemAction.apply(idx);
                    }

                    if (0 == idx) {
                        if (null == srcFieldNames && null != item) {
                            srcFieldNames = getFieldsByRowObject(item);
                        }
                        hasSysLimitId = null != srcFieldNames && srcFieldNames.contains(sys_limit_id);

                        if (null == dstFieldNames) {

                            dstFieldNames = SGDataHelper.ListSelect(this.GetTableEditableFields(tableName),
                                    a -> a.getFieldName());

                            if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
                                SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
                                // dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
                                dstFieldNames = SGDataHelper.ListWhere(dstFieldNames,
                                        a -> SGDataHelper.ListAny(srcFieldNamesRef.GetValue(),
                                                b -> b.equalsIgnoreCase(a))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
                            }
                        }
                        if (null == dstMd) {
//                            dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
//                                    .filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
                            if(null==mdExec) {mdExec= SGSqlExecute.Init(this._jdbc);}
                            dstMd = mdExec.GetMetaDataNotClose(tableName, Arrays.asList(dstFieldNames.stream()
                                    .filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
                        }
                        if (null == ps) {
                            ps = dstFieldNames == null ? GetPs(tableName) : GetPs(tableName, dstFieldNames, false);

                        }
                        if (dstInsert == null) {
                            dstInsert = getInsertCollection(dstMd);
                            if (null != srcFieldNames) {
                                dstInsert.UpdateLowerKeyMapBySrcField(srcFieldNames);
                            }
                        }
                    }

                    if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
                        dstInsert.UpdateModelValue(item);
                        if (hasSysLimitId) {
                            if (item instanceof ResultSet) {
                                currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
                            }
                        }
                    }

                    if (rowAction != null) {
                        rowAction.go(dstInsert, idx, null);
                    }

                    ps = insertIntoCachedRowSet(ps, dstMd, dstInsert); // ,dstColumnType);

                    batchHelper.batchCnt++;
                }
                if (batchHelper.ifDo()) {

                    Boolean b = false;
                    try {
                        ps.executeBatch();
                        conn.commit();
                        b = true;
                        if (hasSysLimitId) {
                            lastInsertedId = currentSysLimitId;
                        }
                    } catch (Exception e) {
                        SetError(e);
                    }

                    if (!b) {
                        // CloseReader(rdr);
                        CloseConn();
                        if(null!=mdExec) {mdExec.CloseConn();}
                        return false;
                    } else {
                        insertedCnt = idx + 1;
                    }
                    if (sqlRowsCopiedAction != null) {
                        sqlRowsCopiedAction.accept(idx);
                    }
                    if (stopAction != null) {
                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
                            CloseConn();
                            if(null!=mdExec) {mdExec.CloseConn();}
                            return true;
                        }
                    }

                    ps.clearBatch();

                    batchHelper.hasDone();
                } else {
                    // batchCnt++;
                    // oneThousandCount++;
                }
                if (!batchHelper.hasNext) {
                    break;
                }
                idx++;


            }

        } catch (Exception e) {
            e.printStackTrace();
            SetError(e);
            CloseConn();
            if(null!=mdExec) {mdExec.CloseConn();}
            return false;
        }

        CloseConn();
        if(null!=mdExec) {mdExec.CloseConn();}

        return true;
    }
//	/**
//	 * 为了分析为什么用了PFBatchHelper之后性能变慢了1/10, 后来发现是错觉
//	 * @param dstFieldNames
//	 * @param tableName
//	 * @param hasNextAction
//	 * @param getItemAction
//	 * @param rowAction
//	 * @param sqlRowsCopiedAction
//	 * @param stopAction
//	 * @return
//	 * @param <T>
//	 */
//	public <T> boolean doInsertListOld(
//			// Class<T> cls,
//			// List<String> srcFieldNames//SqlInsertCollection dstInsert
//			List<String> dstFieldNames // SqlInsertCollection dstInsert
//			, String tableName,
//			// int cnt,
//			// Function<Integer,Boolean> hasNextAction ,
//			PFFunc3<Integer, Object, Object, Boolean> hasNextAction, Function<Integer, T> getItemAction,
//			// PFFunc3<Integer,Object,Object,T> getItemAction ,
//			PFAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
//			Predicate<Boolean> stopAction) {
//		PFSqlInsertCollection dstInsert = null;
//
//		BatchInsertOption insertOption = GetInsertOption();
//
//		// OpenConn();
//
//		int idx = 0;
//
//		insertedCnt = 0; // 已插入的行数
//
//		int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
//		int batchCnt = 0;
//
//		Boolean hasUnDo = false;
//
//		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//
//		// long[] beginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis()
//		// };
//
//		try {
//			// if(null==srcFieldNames&&null!=cls) {
//			// srcFieldNames=getFieldsByRowObject(cls);
//			// }
//
//			// boolean hasSysLimitId =null!=srcFieldNames&&
//			// srcFieldNames.contains(sys_limit_id);
//			boolean hasSysLimitId = false;
//			String sysLimitIdFieldName = sys_limit_id;
//			long currentSysLimitId = -1;
//			lastInsertedId = -1;
//
//			List<String> srcFieldNames = null;
//
//			ResultSetMetaData dstMd = null;
//
//			OpenConn();
//			conn.setAutoCommit(false);
//			PreparedStatement ps = null;
//
//			while (true) {
//				// T item = null;
//				// boolean hasNext = idx < cnt;
//				// boolean hasNext =hasNextAction.apply(idx);
//				boolean hasNext = hasNextAction.go(idx, null, null);
//				// if (hasNext) {
//				// item=getItemAction.apply(idx);
//				// }
//				if (hasNext) {
//					T item = null;
//					if (null != getItemAction) {
//						item = getItemAction.apply(idx);
//						// dstInsert.UpdateModelValueAutoConvert(item);
//						// //dstInsert.UpdateByDataReaderAutoConvert(item);
//						// if (hasSysLimitId) {
//						// if(item instanceof ResultSet){
//						// currentSysLimitId =
//						// ((ResultSet)item).getLong(sysLimitIdFieldName);
//						// }
//						// }
//					}
//
//					if (0 == idx) {
//						if (null == srcFieldNames && null != item) {
//							srcFieldNames = getFieldsByRowObject(item);
//						}
//						hasSysLimitId = null != srcFieldNames && srcFieldNames.contains(sys_limit_id);
//
//						if (null == dstFieldNames) {
////                            dstFieldNames =
////                                    PFDataHelper.ListSelect(
////                                            this.GetTableFields(tableName), a -> a.getFieldName());
//
//							dstFieldNames = PFDataHelper.ListSelect(this.GetTableEditableFields(tableName),
//									a -> a.getFieldName());
////							SGRef<ResultSetMetaData> mdRef=new SGRef<>();
////							dstFieldNames = PFDataHelper.ListSelect(this.GetTableEditableFields(tableName,mdRef),
////									a -> a.getFieldName());
////							if (null == dstMd) {
////								dstMd = mdRef.GetValue();
////							}
//
//							if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
//								SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
//								// dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
//								dstFieldNames = PFDataHelper.ListWhere(dstFieldNames,
//										a -> PFDataHelper.ListAny(srcFieldNamesRef.GetValue(),
//												b -> b.toLowerCase().equals(a.toLowerCase()))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
//							}
//						}
//						if (null == dstMd) {
//							dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
//									.filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
//						}
////						if (null == md) {
////							md = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
////									.filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
////						}
//						if (null == ps) {
//							ps = dstFieldNames == null ? GetPs(tableName) : GetPs(tableName, dstFieldNames,false);
//
////							if (null == dstMd) {
////								dstMd = ps.getMetaData();
////							}
//						}
//						if (dstInsert == null) {
//							dstInsert = getInsertCollection(dstMd);
//							//dstInsert = getInsertCollection(md);
//							if (null != srcFieldNames) {
//								dstInsert.UpdateLowerKeyMapBySrcField(srcFieldNames);
//							}
//						}
//					}
//
//					if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
//						// dstInsert.UpdateModelValueAutoConvert(item);
//						dstInsert.UpdateModelValue(item);
//						if (hasSysLimitId) {
//							if (item instanceof ResultSet) {
//								currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
//							}
//						}
//					}
//
//					if (rowAction != null) {
//						// rowAction.accept(dstInsert);
//						rowAction.go(dstInsert, idx, null);
//					}
//
//					ps = insertIntoCachedRowSet(ps, dstMd, dstInsert); // ,dstColumnType);
//					//ps = insertIntoCachedRowSet(ps, md, dstInsert); // ,dstColumnType);
//					// sb.add(GetInsertSql(tableName, dstInsert));
//
//					batchCnt++;
//					hasUnDo = true;
//
//					// if (hasSysLimitId) {
//					// //currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
//					// currentSysLimitId
//					// =PFDataHelper.ObjectToLong0(dstInsert.Get(sysLimitIdFieldName).Value);
//					// }
//				}
//				if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//
//					Boolean b = false;
//					try {
//						ps.executeBatch();
//						conn.commit();
//						b = true;
//						// b=ExecuteSql(sb);
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
//						// if (hasSysLimitId) {
//						// lastInsertedId = currentSysLimitId;
//						// }
//					}
//					if (sqlRowsCopiedAction != null) {
//						sqlRowsCopiedAction.accept(idx);
//					}
//					if (stopAction != null) {
//						if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
//							// CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//							CloseConn();
//							return true;
//						}
//					}
//
//					// ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
//					ps.clearBatch();
//					// sb.clear();
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
//				// PFDate now = PFDate.Now();
//				// long m = now.ToCalendar().getTimeInMillis();
//				// System.out.println(PFDataHelper.FormatString("rows:{0} -- " + "speed:{1}",
//				// PFDataHelper.ScientificNotation(idx),
//				//// "10000条/" + ((PFDate.Now().ToCalendar().getTimeInMillis() -
//				// beginTime[0]) * 10 / total) + "秒"
//				// PFDataHelper.ScientificNotation(
//				// Double.valueOf(idx) * 1000 / (m - beginTime[0])) + "条/秒"));
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
//		return true;
//	}
//
//
//	public <T> boolean doInsertListNew2(
//			// Class<T> cls,
//			// List<String> srcFieldNames//SqlInsertCollection dstInsert
//			List<String> dstFieldNames // SqlInsertCollection dstInsert
//			, String tableName,
//			// int cnt,
//			// Function<Integer,Boolean> hasNextAction ,
//			PFFunc3<Integer, Object, Object, Boolean> hasNextAction, Function<Integer, T> getItemAction,
//			// PFFunc3<Integer,Object,Object,T> getItemAction ,
//			PFAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
//			Predicate<Boolean> stopAction) {
//		PFSqlInsertCollection dstInsert = null;
//
//		BatchInsertOption insertOption = GetInsertOption();
//
//		// OpenConn();
//
//		int idx = 0;
//
//		insertedCnt = 0; // 已插入的行数
//
//		int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
//		//int batchCnt = 0;
//
//		//Boolean hasUnDo = false;
//
//		UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//
//		// long[] beginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis()
//		// };
//
//		try {
//			// if(null==srcFieldNames&&null!=cls) {
//			// srcFieldNames=getFieldsByRowObject(cls);
//			// }
//
//			// boolean hasSysLimitId =null!=srcFieldNames&&
//			// srcFieldNames.contains(sys_limit_id);
//			boolean hasSysLimitId = false;
//			String sysLimitIdFieldName = sys_limit_id;
//			long currentSysLimitId = -1;
//			lastInsertedId = -1;
//
//			List<String> srcFieldNames = null;
//
//			ResultSetMetaData dstMd = null;
//
//			OpenConn();
//			conn.setAutoCommit(false);
//			PreparedStatement ps = null;
//
//			com.sellgirl.pfHelperNotSpring.PFBatchHelper2 batchHelper=new com.sellgirl.pfHelperNotSpring.PFBatchHelper2();
//			batchHelper.batchSize = batchSize;
//			batchHelper.batchCnt = 0;
//			while (true) {
//				// T item = null;
//				// boolean hasNext = idx < cnt;
//				// boolean hasNext =hasNextAction.apply(idx);
//				batchHelper.hasNext = hasNextAction.go(idx, null, null);
//				// if (hasNext) {
//				// item=getItemAction.apply(idx);
//				// }
//				if (batchHelper.hasNext) {
//					T item = null;
//					if (null != getItemAction) {
//						item = getItemAction.apply(idx);
//						// dstInsert.UpdateModelValueAutoConvert(item);
//						// //dstInsert.UpdateByDataReaderAutoConvert(item);
//						// if (hasSysLimitId) {
//						// if(item instanceof ResultSet){
//						// currentSysLimitId =
//						// ((ResultSet)item).getLong(sysLimitIdFieldName);
//						// }
//						// }
//					}
//
//					if (0 == idx) {
//						if (null == srcFieldNames && null != item) {
//							srcFieldNames = getFieldsByRowObject(item);
//						}
//						hasSysLimitId = null != srcFieldNames && srcFieldNames.contains(sys_limit_id);
//
//						if (null == dstFieldNames) {
////                            dstFieldNames =
////                                    PFDataHelper.ListSelect(
////                                            this.GetTableFields(tableName), a -> a.getFieldName());
//
//							dstFieldNames = PFDataHelper.ListSelect(this.GetTableEditableFields(tableName),
//									a -> a.getFieldName());
////							SGRef<ResultSetMetaData> mdRef=new SGRef<>();
////							dstFieldNames = PFDataHelper.ListSelect(this.GetTableEditableFields(tableName,mdRef),
////									a -> a.getFieldName());
////							if (null == dstMd) {
////								dstMd = mdRef.GetValue();
////							}
//
//							if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
//								SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
//								// dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
//								dstFieldNames = PFDataHelper.ListWhere(dstFieldNames,
//										a -> PFDataHelper.ListAny(srcFieldNamesRef.GetValue(),
//												b -> b.toLowerCase().equals(a.toLowerCase()))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
//							}
//						}
//						if (null == dstMd) {
//							dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
//									.filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
//						}
////						if (null == md) {
////							md = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
////									.filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
////						}
//						if (null == ps) {
//							ps = dstFieldNames == null ? GetPs(tableName) : GetPs(tableName, dstFieldNames,false);
//
////							if (null == dstMd) {
////								dstMd = ps.getMetaData();
////							}
//						}
//						if (dstInsert == null) {
//							dstInsert = getInsertCollection(dstMd);
//							//dstInsert = getInsertCollection(md);
//							if (null != srcFieldNames) {
//								dstInsert.UpdateLowerKeyMapBySrcField(srcFieldNames);
//							}
//						}
//					}
//
//					if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
//						// dstInsert.UpdateModelValueAutoConvert(item);
//						dstInsert.UpdateModelValue(item);
//						if (hasSysLimitId) {
//							if (item instanceof ResultSet) {
//								currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
//							}
//						}
//					}
//
//					if (rowAction != null) {
//						// rowAction.accept(dstInsert);
//						rowAction.go(dstInsert, idx, null);
//					}
//
//					ps = insertIntoCachedRowSet(ps, dstMd, dstInsert); // ,dstColumnType);
//					//ps = insertIntoCachedRowSet(ps, md, dstInsert); // ,dstColumnType);
//					// sb.add(GetInsertSql(tableName, dstInsert));
//
//					batchHelper.batchCnt++;
//					batchHelper.hasUnDo = true;
//
//					// if (hasSysLimitId) {
//					// //currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
//					// currentSysLimitId
//					// =PFDataHelper.ObjectToLong0(dstInsert.Get(sysLimitIdFieldName).Value);
//					// }
//				}
//				//if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//				if (batchHelper.ifDo()) {
//
//					Boolean b = false;
//					try {
//						ps.executeBatch();
//						conn.commit();
//						b = true;
//						// b=ExecuteSql(sb);
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
//						// if (hasSysLimitId) {
//						// lastInsertedId = currentSysLimitId;
//						// }
//					}
//					if (sqlRowsCopiedAction != null) {
//						sqlRowsCopiedAction.accept(idx);
//					}
//					if (stopAction != null) {
//						if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
//							// CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//							CloseConn();
//							return true;
//						}
//					}
//
//					// ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
//					ps.clearBatch();
//					// sb.clear();
//
////					hasUnDo = false;
////					batchCnt = 0;
//					batchHelper.hasDone();
//				} else {
//					// batchCnt++;
//					// oneThousandCount++;
//				}
//				if (!batchHelper.hasNext) {
//					break;
//				}
//				idx++;
//
//				// PFDate now = PFDate.Now();
//				// long m = now.ToCalendar().getTimeInMillis();
//				// System.out.println(PFDataHelper.FormatString("rows:{0} -- " + "speed:{1}",
//				// PFDataHelper.ScientificNotation(idx),
//				//// "10000条/" + ((PFDate.Now().ToCalendar().getTimeInMillis() -
//				// beginTime[0]) * 10 / total) + "秒"
//				// PFDataHelper.ScientificNotation(
//				// Double.valueOf(idx) * 1000 / (m - beginTime[0])) + "条/秒"));
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
//		return true;
//	}

    /**
     * 测试多线程插入.
     * (性能提升不大,暂不使用)
     * <p>
     * 实测:
     * 1.在sqlServer里面,60万订单,不使用多线程耗时0时1分8秒112毫秒; 使用3线程耗时0时0分57秒217毫秒.
     * 对sqlServer性能提升不大
     *
     * @param <T>
     * @param dstFieldNames
     * @param tableName
     * @param hasNextAction
     * @param getItemAction
     * @param rowAction
     * @param sqlRowsCopiedAction
     * @param stopAction
     * @return
     */
    @SuppressWarnings("resource")
    public <T> boolean doInsertListMultiThread(
            // Class<T> cls,
            // List<String> srcFieldNames//SqlInsertCollection dstInsert
            List<String> dstFieldNames // SqlInsertCollection dstInsert
            , String tableName,
            // int cnt,
            // Function<Integer,Boolean> hasNextAction ,
            PFFunc3<Integer, Object, Object, Boolean> hasNextAction, Function<Integer, T> getItemAction,
            // PFFunc3<Integer,Object,Object,T> getItemAction ,
            SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
            Predicate<Boolean> stopAction,
            int totalThread) {
        SGSqlInsertCollection dstInsert = null;

        BatchInsertOption insertOption = GetInsertOption();

        // OpenConn();

        int idx = 0;

        insertedCnt = 0; // 已插入的行数

        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
        int batchCnt = 0;

        Boolean hasUnDo = false;

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

        //int totalThread=5;
        Integer[] threadCnt = new Integer[]{totalThread};// 3线程

        // long[] beginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis()
        // };

        try {
            // if(null==srcFieldNames&&null!=cls) {
            // srcFieldNames=getFieldsByRowObject(cls);
            // }

            // boolean hasSysLimitId =null!=srcFieldNames&&
            // srcFieldNames.contains(sys_limit_id);
            boolean hasSysLimitId = false;
            String sysLimitIdFieldName = sys_limit_id;
            long currentSysLimitId = -1;
            lastInsertedId = -1;

            List<String> srcFieldNames = null;

            ResultSetMetaData dstMd = null;

            OpenConn();
            conn.setAutoCommit(false);
            PreparedStatement ps = null;

            while (true) {
                // T item = null;
                // boolean hasNext = idx < cnt;
                // boolean hasNext =hasNextAction.apply(idx);
                boolean hasNext = hasNextAction.go(idx, null, null);
                // if (hasNext) {
                // item=getItemAction.apply(idx);
                // }
                if (hasNext) {
                    T item = null;
                    if (null != getItemAction) {
                        item = getItemAction.apply(idx);
                        // dstInsert.UpdateModelValueAutoConvert(item);
                        // //dstInsert.UpdateByDataReaderAutoConvert(item);
                        // if (hasSysLimitId) {
                        // if(item instanceof ResultSet){
                        // currentSysLimitId =
                        // ((ResultSet)item).getLong(sysLimitIdFieldName);
                        // }
                        // }
                    }

                    if (0 == idx) {
                        if (null == srcFieldNames && null != item) {
                            srcFieldNames = getFieldsByRowObject(item);
                        }
                        hasSysLimitId = null != srcFieldNames && srcFieldNames.contains(sys_limit_id);

                        if (null == dstFieldNames) {
//                            dstFieldNames =
//                                    PFDataHelper.ListSelect(
//                                            this.GetTableFields(tableName), a -> a.getFieldName());
                            dstFieldNames = SGDataHelper.ListSelect(this.GetTableEditableFields(tableName),
                                    a -> a.getFieldName());

                            if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
                                SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
                                // dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
                                dstFieldNames = SGDataHelper.ListWhere(dstFieldNames,
                                        a -> SGDataHelper.ListAny(srcFieldNamesRef.GetValue(),
                                                b -> b.equalsIgnoreCase(a))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
                            }
                        }
                        if (null == dstMd) {
                            dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
                                    .filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
                        }
                        if (null == ps) {
                            ps = dstFieldNames == null ? GetPs(tableName) : GetPs(tableName, dstFieldNames, false);
                        }
                        if (dstInsert == null) {
                            dstInsert = getInsertCollection(dstMd);
                            if (null != srcFieldNames) {
                                dstInsert.UpdateLowerKeyMapBySrcField(srcFieldNames);
                            }
                        }
                    }

                    if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
                        // dstInsert.UpdateModelValueAutoConvert(item);
                        dstInsert.UpdateModelValue(item);
                        if (hasSysLimitId) {
                            if (item instanceof ResultSet) {
                                currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
                            }
                        }
                    }

                    if (rowAction != null) {
                        // rowAction.accept(dstInsert);
                        rowAction.go(dstInsert, idx, null);
                    }

                    ps = insertIntoCachedRowSet(ps, dstMd, dstInsert); // ,dstColumnType);
                    // sb.add(GetInsertSql(tableName, dstInsert));

                    batchCnt++;
                    hasUnDo = true;

                    // if (hasSysLimitId) {
                    // //currentSysLimitId = rdr.getLong(sysLimitIdFieldName);
                    // currentSysLimitId
                    // =PFDataHelper.ObjectToLong0(dstInsert.Get(sysLimitIdFieldName).Value);
                    // }
                }
                if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {

                    if (0 < threadCnt[0]) {
                        threadCnt[0]--;
                        PreparedStatement psTmp = ps;
                        Connection connTmp = conn;
                        new Thread() {// 线程操作
                            public void run() {
                                try {
                                    psTmp.executeBatch();
                                    connTmp.commit();
                                    psTmp.close();
                                    connTmp.close();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                threadCnt[0]++;
                            }
                        }.start();
                        conn = null;
                        OpenConn();
                        //ps.clearBatch();
                        ps = dstFieldNames == null ? GetPs(tableName) : GetPs(tableName, dstFieldNames, false);
                        // sb.clear();

                        hasUnDo = false;
                        batchCnt = 0;
                    } else {
                        Boolean b = false;
                        try {
                            ps.executeBatch();
                            conn.commit();
                            b = true;
                            // b=ExecuteSql(sb);
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
                            // if (hasSysLimitId) {
                            // lastInsertedId = currentSysLimitId;
                            // }
                        }
                        if (sqlRowsCopiedAction != null) {
                            sqlRowsCopiedAction.accept(idx);
                        }
                        if (stopAction != null) {
                            if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                                // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
                                CloseConn();
                                return true;
                            }
                        }

                        // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
                        ps.clearBatch();
                        // sb.clear();

                        hasUnDo = false;
                        batchCnt = 0;
                    }
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
        while (totalThread != threadCnt[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (sqlRowsCopiedAction != null) {
            sqlRowsCopiedAction.accept(idx);
        }
        return true;
    }

//    public <T> boolean doUpdateListOld(List<String> srcFieldNames // SqlInsertCollection dstInsert
//            , String tableName, String[] primaryKey, int cnt, Function<Integer, T> getItemAction,
//                                       // Consumer<BaseSqlUpdateCollection> rowAction,
//                                       PFAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
//                                       Predicate<Boolean> stopAction) {
//        // SqlInsertCollection dstInsert=null;
//        PFSqlUpdateCollection dstUpdate = null;
//
//        BatchInsertOption insertOption = GetInsertOption();
//        // BatchInsertOption insertOption = GetSqlUpdateOption();
//
//        OpenConn();
//
//        int idx = 0;
//
//        insertedCnt = 0; // 已插入的行数
//
//        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
//        int batchCnt = 0;
//
//        Boolean hasUnDo = false;
//
//        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//        try {
//
//            // ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
//            // :GetMetaData(tableName,srcFieldNames);
//
//            // PreparedStatement ps =srcFieldNames==null? GetPs(tableName):
//            // GetPs(tableName,srcFieldNames);
//
//            // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//            if (dstUpdate == null) {
//
//                ResultSetMetaData dstMd = srcFieldNames == null ? GetMetaData(tableName)
//                        : GetMetaData(tableName, srcFieldNames);
//                // dstInsert = getInsertCollection(dstMd);
//                dstUpdate = this.getUpdateCollection(dstMd);
//                dstUpdate.PrimaryKeyFields(false, primaryKey);
//            }
//
//            // StringBuilder sb = new StringBuilder();
//            PFSqlCommandString sb = new PFSqlCommandString();
//            while (true) {
//                T item = null;
//                boolean hasNext = idx < cnt;
//                if (hasNext) {
//                    item = getItemAction.apply(idx);
//                }
//                if (hasNext) {
//
//                    // dstInsert.UpdateModelValueAutoConvert(item);
//                    if (insertOption.getAutoUpdateModel()) {
//                        // dstUpdate.UpdateByDataReader(rdr);
//                        dstUpdate.UpdateModelValueAutoConvert(item);
//                    }
//                    if (rowAction != null) {
//                        // rowAction.accept(dstUpdate);
//                        rowAction.go(dstUpdate, idx, null);
//                    }
//
//                    // ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
//                    // sb.append(GetUpdateSql(tableName, dstUpdate));
//                    sb.add(GetUpdateSql(tableName, dstUpdate));
//
//                    batchCnt++;
//                    hasUnDo = true;
//                }
//                if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//
//                    Boolean b = false;
//                    try {
//                        // ps.executeBatch();
//                        // conn.commit();
//                        // b = true;
//                        b = ExecuteSql(sb);
//                    } catch (Exception e) {
//                        SetError(e);
//                    }
//
//                    if (!b) {
//                        // CloseReader(rdr);
//                        CloseConn();
//                        return false;
//                    } else {
//                        insertedCnt = idx + 1;
//                        // if (hasSysLimitId) {
//                        // lastInsertedId = currentSysLimitId;
//                        // }
//                    }
//                    if (sqlRowsCopiedAction != null) {
//                        sqlRowsCopiedAction.accept(idx);
//                    }
//                    if (stopAction != null) {
//                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
//                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//                            CloseConn();
//                            return true;
//                        }
//                    }
//
//                    // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
//                    // sb = new StringBuilder();
//                    sb.clear();
//
//                    hasUnDo = false;
//                    batchCnt = 0;
//                } else {
//                    // batchCnt++;
//                    // oneThousandCount++;
//                }
//                if (!hasNext) {
//                    break;
//                }
//                idx++;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            SetError(e);
//            return false;
//        }
//
//        CloseConn();
//
//        return true;
//    }

    //@Override
    public <T> boolean doUpdateListOld2(
            //List<String> srcFieldNames // SqlInsertCollection dstInsert
            List<String> dstFieldNames
            , String tableName, String[] primaryKey,
            //int cnt,
            PFFunc3<Integer, Object, Object, Boolean> hasNextAction,
            Function<Integer, T> getItemAction,
            // Consumer<BaseSqlUpdateCollection> rowAction,
            SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
            Predicate<Boolean> stopAction) {
        // SqlInsertCollection dstInsert=null;
        SGSqlUpdateCollection dstUpdate = null;

        BatchInsertOption insertOption = GetInsertOption();
        // BatchInsertOption insertOption = GetSqlUpdateOption();

        //OpenConn();

        int idx = 0;

        insertedCnt = 0; // 已插入的行数

        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
        //int batchCnt = 0;

        //Boolean hasUnDo = false;

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
        try {

            List<String> srcFieldNames = null;
            ResultSetMetaData dstMd = null;

            OpenConn();
            // ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
            // :GetMetaData(tableName,srcFieldNames);

            // PreparedStatement ps =srcFieldNames==null? GetPs(tableName):
            // GetPs(tableName,srcFieldNames);

//            // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//            if (dstUpdate == null) {
//
//                ResultSetMetaData dstMd = srcFieldNames == null ? GetMetaData(tableName)
//                        : GetMetaData(tableName, srcFieldNames);
//                // dstInsert = getInsertCollection(dstMd);
//                dstUpdate = this.getUpdateCollection(dstMd);
//                dstUpdate.PrimaryKeyFields(false, primaryKey);
//            }

            // StringBuilder sb = new StringBuilder();
            SGSqlCommandString sb = new SGSqlCommandString();

            PFBatchHelper batchHelper = new PFBatchHelper();
            batchHelper.batchSize = batchSize;
            batchHelper.batchCnt = 0;
            while (true) {
                //boolean hasNext = idx < cnt;
                batchHelper.hasNext = hasNextAction.go(idx, null, null);
                if (batchHelper.hasNext) {
                    T item = null;
                    if (null != getItemAction) {
                        item = getItemAction.apply(idx);
                    }

                    if (0 == idx) {
                        if (null == srcFieldNames && null != item) {
                            srcFieldNames = getFieldsByRowObject(item);
                        }
                        if (null == dstFieldNames) {

                            dstFieldNames = SGDataHelper.ListSelect(this.GetTableEditableFields(tableName),
                                    a -> a.getFieldName());

                            if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
                                SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
                                // dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
                                dstFieldNames = SGDataHelper.ListWhere(dstFieldNames,
                                        a -> SGDataHelper.ListAny(srcFieldNamesRef.GetValue(),
                                                b -> b.equalsIgnoreCase(a))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
                            }
                        }
                        if (null == dstMd) {
                            dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
                                    .filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
                        }
                        if (null == dstUpdate) {

                            dstUpdate = this.getUpdateCollection(dstMd);
                            dstUpdate.PrimaryKeyFields(false, primaryKey);
                            if (null != srcFieldNames) {
                                dstUpdate.UpdateLowerKeyMapBySrcField(srcFieldNames);
                            }
                        }
                    }
//                    if (insertOption.getAutoUpdateModel()) {
//                        dstUpdate.UpdateModelValueAutoConvert(item);
//                    }
                    if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
                        dstUpdate.UpdateModelValue(item);
//						if (hasSysLimitId) {
//							if (item instanceof ResultSet) {
//								currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
//							}
//						}
                    }
                    if (rowAction != null) {
                        // rowAction.accept(dstUpdate);
                        rowAction.go(dstUpdate, idx, null);
                    }

                    // ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
                    // sb.append(GetUpdateSql(tableName, dstUpdate));
                    sb.add(GetUpdateSql(tableName, dstUpdate));

//                    batchCnt++;
//                    hasUnDo = true;
                    batchHelper.batchCnt++;
                }
                //if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
                if (batchHelper.ifDo()) {

                    Boolean b = false;
                    try {
                        // ps.executeBatch();
                        // conn.commit();
                        // b = true;
                        b = ExecuteSql(sb);
                    } catch (Exception e) {
                        SetError(e);
                    }

                    if (!b) {
                        // CloseReader(rdr);
                        CloseConn();
                        return false;
                    } else {
                        insertedCnt = idx + 1;
                        // if (hasSysLimitId) {
                        // lastInsertedId = currentSysLimitId;
                        // }
                    }
                    if (sqlRowsCopiedAction != null) {
                        sqlRowsCopiedAction.accept(idx);
                    }
                    if (stopAction != null) {
                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
                            CloseConn();
                            return true;
                        }
                    }

                    // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
                    // sb = new StringBuilder();
                    sb.clear();

//                    hasUnDo = false;
//                    batchCnt = 0;
                    batchHelper.hasDone();
                } else {
                    // batchCnt++;
                    // oneThousandCount++;
                }
                if (!batchHelper.hasNext) {
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

    @Override
    public <T> boolean doUpdateList(
            //List<String> srcFieldNames // SqlInsertCollection dstInsert
            List<String> dstFieldNames
            , String tableName, String[] primaryKey,
            //int cnt,
            PFFunc3<Integer, Object, Object, Boolean> hasNextAction,
            Function<Integer, T> getItemAction,
            // Consumer<BaseSqlUpdateCollection> rowAction,
            SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
            Predicate<Boolean> stopAction) {
        // SqlInsertCollection dstInsert=null;
        SGSqlUpdateCollection dstUpdate = null;

        BatchInsertOption insertOption = GetInsertOption();
        // BatchInsertOption insertOption = GetSqlUpdateOption();

        //OpenConn();

        int idx = 0;

        insertedCnt = 0; // 已插入的行数

        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
        //int batchCnt = 0;

        //Boolean hasUnDo = false;

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
        try {

            List<String> srcFieldNames = null;
            ResultSetMetaData dstMd = null;

            OpenConn();
            conn.setAutoCommit(false);
            PreparedStatement ps = null;
            // ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
            // :GetMetaData(tableName,srcFieldNames);

            // PreparedStatement ps =srcFieldNames==null? GetPs(tableName):
            // GetPs(tableName,srcFieldNames);

//            // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//            if (dstUpdate == null) {
//
//                ResultSetMetaData dstMd = srcFieldNames == null ? GetMetaData(tableName)
//                        : GetMetaData(tableName, srcFieldNames);
//                // dstInsert = getInsertCollection(dstMd);
//                dstUpdate = this.getUpdateCollection(dstMd);
//                dstUpdate.PrimaryKeyFields(false, primaryKey);
//            }

//            // StringBuilder sb = new StringBuilder();
//            PFSqlCommandString sb = new PFSqlCommandString();

            PFBatchHelper batchHelper = new PFBatchHelper();
            batchHelper.batchSize = batchSize;
            batchHelper.batchCnt = 0;
            while (true) {
                //boolean hasNext = idx < cnt;
                batchHelper.hasNext = hasNextAction.go(idx, null, null);
                if (batchHelper.hasNext) {
                    T item = null;
                    if (null != getItemAction) {
                        item = getItemAction.apply(idx);
                    }

                    if (0 == idx) {
                        if (null == srcFieldNames && null != item) {
                            srcFieldNames = getFieldsByRowObject(item);
                        }
                        if (null == dstFieldNames) {

                            dstFieldNames = SGDataHelper.ListSelect(this.GetTableEditableFields(tableName),
                                    a -> a.getFieldName());

                            if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
                                SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
                                // dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
                                dstFieldNames = SGDataHelper.ListWhere(dstFieldNames,
                                        a -> SGDataHelper.ListAny(srcFieldNamesRef.GetValue(),
                                                b -> b.equalsIgnoreCase(a))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
                            }
                        }
                        if (null == dstMd) {
//                            dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
//                                    .filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
                            //注意,因为是update,所以列顺序最好和下面的ps的参数顺序一致,才容易处理
                            List<String> updateFieldNames=SGDataHelper.ListWhere(dstFieldNames,a->!SGDataHelper.ArrayAny(primaryKey,b->b.equals(a)));
                            updateFieldNames.addAll(Arrays.asList(primaryKey));
                            dstMd = GetMetaData(tableName,updateFieldNames.stream()
                                    .filter(a -> !a.equals(sys_limit_id)).collect(Collectors.toList()));
                        }
                        if (null == ps) {
                            //ps = dstFieldNames == null ? GetPs(tableName) : GetPs(tableName, dstFieldNames, false);
                            ps = GetUpdatePs(tableName,dstFieldNames,primaryKey, false);
                        }
                        if (null == dstUpdate) {

                            dstUpdate = this.getUpdateCollection(dstMd);
                            dstUpdate.PrimaryKeyFields(false, primaryKey);
                            if (null != srcFieldNames) {
                                dstUpdate.UpdateLowerKeyMapBySrcField(srcFieldNames);
                            }
                        }
                    }
//                    if (insertOption.getAutoUpdateModel()) {
//                        dstUpdate.UpdateModelValueAutoConvert(item);
//                    }
                    if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
                        dstUpdate.UpdateModelValue(item);
//						if (hasSysLimitId) {
//							if (item instanceof ResultSet) {
//								currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
//							}
//						}
                    }
                    if (rowAction != null) {
                        // rowAction.accept(dstUpdate);
                        rowAction.go(dstUpdate, idx, null);
                    }

//                    // ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
//                    // sb.append(GetUpdateSql(tableName, dstUpdate));
//                    sb.add(GetUpdateSql(tableName, dstUpdate));
                    ps = insertIntoCachedRowSet(ps, dstMd, dstUpdate); // ,dstColumnType);

//                    batchCnt++;
//                    hasUnDo = true;
                    batchHelper.batchCnt++;
                }
                //if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
                if (batchHelper.ifDo()) {

                    Boolean b = false;
                    try {
                         ps.executeBatch();
                         conn.commit();
                         b = true;
                        //b = ExecuteSql(sb);
                    } catch (Exception e) {
                        SetError(e);
                    }

                    if (!b) {
                        // CloseReader(rdr);
                        CloseConn();
                        return false;
                    } else {
                        insertedCnt = idx + 1;
                        // if (hasSysLimitId) {
                        // lastInsertedId = currentSysLimitId;
                        // }
                    }
                    if (sqlRowsCopiedAction != null) {
                        sqlRowsCopiedAction.accept(idx);
                    }
                    if (stopAction != null) {
                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
                            CloseConn();
                            return true;
                        }
                    }

//                    // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
//                    // sb = new StringBuilder();
//                    sb.clear();
                    ps.clearBatch();

//                    hasUnDo = false;
//                    batchCnt = 0;
                    batchHelper.hasDone();
                } else {
                    // batchCnt++;
                    // oneThousandCount++;
                }
                if (!batchHelper.hasNext) {
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
//    public <T> boolean doDeleteListOld(List<String> srcFieldNames // SqlInsertCollection dstInsert
//            , String tableName, String[] primaryKey, int cnt, Function<Integer, T> getItemAction,
//                                       // Consumer<BaseSqlUpdateCollection> rowAction,
//                                       PFAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
//                                       Predicate<Boolean> stopAction) {
//        // SqlInsertCollection dstInsert=null;
//        PFSqlUpdateCollection dstUpdate = null;
//
//        BatchInsertOption insertOption = GetInsertOption();
//        // BatchInsertOption insertOption = GetSqlDeleteOption();
//
//        OpenConn();
//
//        int idx = 0;
//
//        insertedCnt = 0; // 已插入的行数
//
//        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
//        int batchCnt = 0;
//
//        Boolean hasUnDo = false;
//
//        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//        try {
//
//            // ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
//            // :GetMetaData(tableName,srcFieldNames);
//
//            // PreparedStatement ps =srcFieldNames==null? GetPs(tableName):
//            // GetPs(tableName,srcFieldNames);
//
//            // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//            if (dstUpdate == null) {
//
//                ResultSetMetaData dstMd = srcFieldNames == null ? GetMetaData(tableName)
//                        : GetMetaData(tableName, srcFieldNames);
//                // dstInsert = getInsertCollection(dstMd);
//                dstUpdate = this.getUpdateCollection(dstMd);
//                dstUpdate.PrimaryKeyFields(false, primaryKey);
//            }
//
//            // StringBuilder sb = new StringBuilder();
//            PFSqlCommandString sb = new PFSqlCommandString();
//            while (true) {
//                T item = null;
//                boolean hasNext = idx < cnt;
//                if (hasNext) {
//                    item = getItemAction.apply(idx);
//                }
//                if (hasNext) {
//
//                    // dstInsert.UpdateModelValueAutoConvert(item);
//                    if (insertOption.getAutoUpdateModel()) {
//                        // dstUpdate.UpdateByDataReader(rdr);
//                        dstUpdate.UpdateModelValueAutoConvert(item);
//                    }
//                    if (rowAction != null) {
//                        // rowAction.accept(dstUpdate);
//                        rowAction.go(dstUpdate, idx, null);
//                    }
//
//                    // ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
//                    // sb.append(GetDeleteSql(tableName, dstUpdate));
//                    sb.add(GetDeleteSql(tableName, dstUpdate));
//
//                    batchCnt++;
//                    hasUnDo = true;
//                }
//                if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//
//                    Boolean b = false;
//                    try {
//                        // ps.executeBatch();
//                        // conn.commit();
//                        // b = true;
//                        // b=ExecuteSql(new PFSqlCommandString(sb.toString()));
//                        b = ExecuteSql(sb);
//                    } catch (Exception e) {
//                        SetError(e);
//                    }
//
//                    if (!b) {
//                        // CloseReader(rdr);
//                        CloseConn();
//                        return false;
//                    } else {
//                        insertedCnt = idx + 1;
//                        // if (hasSysLimitId) {
//                        // lastInsertedId = currentSysLimitId;
//                        // }
//                    }
//                    if (sqlRowsCopiedAction != null) {
//                        sqlRowsCopiedAction.accept(idx);
//                    }
//                    if (stopAction != null) {
//                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
//                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
//                            CloseConn();
//                            return true;
//                        }
//                    }
//
//                    // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
//                    // sb = new StringBuilder();
//                    sb.clear();
//
//                    hasUnDo = false;
//                    batchCnt = 0;
//                } else {
//                    // batchCnt++;
//                    // oneThousandCount++;
//                }
//                if (!hasNext) {
//                    break;
//                }
//                idx++;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            SetError(e);
//            return false;
//        }
//
//        CloseConn();
//
//        return true;
//    }

    //@Override
    public <T> boolean doDeleteListOld2(
            //List<String> srcFieldNames
            List<String> dstFieldNames// SqlInsertCollection dstInsert
            , String tableName, String[] primaryKey,
            //int cnt,
            PFFunc3<Integer, Object, Object, Boolean> hasNextAction,
            Function<Integer, T> getItemAction,
            // Consumer<BaseSqlUpdateCollection> rowAction,
            SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
            Predicate<Boolean> stopAction) {
        // SqlInsertCollection dstInsert=null;
        SGSqlUpdateCollection dstUpdate = null;

        BatchInsertOption insertOption = GetInsertOption();
        // BatchInsertOption insertOption = GetSqlDeleteOption();

        OpenConn();

        int idx = 0;

        insertedCnt = 0; // 已插入的行数

        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
//        int batchCnt = 0;
//
//        Boolean hasUnDo = false;

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
        try {

            List<String> srcFieldNames = null;
            ResultSetMetaData dstMd = null;

            OpenConn();
            // ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
            // :GetMetaData(tableName,srcFieldNames);

            // PreparedStatement ps =srcFieldNames==null? GetPs(tableName):
            // GetPs(tableName,srcFieldNames);

//            // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//            if (dstUpdate == null) {
//
//                ResultSetMetaData dstMd = srcFieldNames == null ? GetMetaData(tableName)
//                        : GetMetaData(tableName, srcFieldNames);
//                // dstInsert = getInsertCollection(dstMd);
//                dstUpdate = this.getUpdateCollection(dstMd);
//                dstUpdate.PrimaryKeyFields(false, primaryKey);
//            }

            // StringBuilder sb = new StringBuilder();
            SGSqlCommandString sb = new SGSqlCommandString();

            PFBatchHelper batchHelper = new PFBatchHelper();
            batchHelper.batchSize = batchSize;
            batchHelper.batchCnt = 0;
            while (true) {
                //T item = null;
                //boolean hasNext = idx < cnt;
                batchHelper.hasNext = hasNextAction.go(idx, null, null);
//                if (hasNext) {
//                    item = getItemAction.apply(idx);
//                }
                if (batchHelper.hasNext) {
                    T item = null;
                    if (null != getItemAction) {
                        item = getItemAction.apply(idx);
                    }

                    if (0 == idx) {
                        if (null == srcFieldNames && null != item) {
                            srcFieldNames = getFieldsByRowObject(item);
                        }
                        if (null == dstFieldNames) {

                            dstFieldNames = SGDataHelper.ListSelect(this.GetTableEditableFields(tableName),
                                    a -> a.getFieldName());

                            if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
                                SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
                                // dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
                                dstFieldNames = SGDataHelper.ListWhere(dstFieldNames,
                                        a -> SGDataHelper.ListAny(srcFieldNamesRef.GetValue(),
                                                b -> b.equalsIgnoreCase(a))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
                            }
                        }
                        if (null == dstMd) {
                            dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
                                    .filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
                        }
                        if (null == dstUpdate) {

                            dstUpdate = this.getUpdateCollection(dstMd);
                            dstUpdate.PrimaryKeyFields(false, primaryKey);
                            if (null != srcFieldNames) {
                                dstUpdate.UpdateLowerKeyMapBySrcField(srcFieldNames);
                            }
                        }
                    }

//                    // dstInsert.UpdateModelValueAutoConvert(item);
//                    if (insertOption.getAutoUpdateModel()) {
//                        // dstUpdate.UpdateByDataReader(rdr);
//                        dstUpdate.UpdateModelValueAutoConvert(item);
//                    }
                    if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
                        dstUpdate.UpdateModelValue(item);
//						if (hasSysLimitId) {
//							if (item instanceof ResultSet) {
//								currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
//							}
//						}
                    }

                    if (rowAction != null) {
                        // rowAction.accept(dstUpdate);
                        rowAction.go(dstUpdate, idx, null);
                    }

                    // ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
                    // sb.append(GetDeleteSql(tableName, dstUpdate));
                    sb.add(GetDeleteSql(tableName, dstUpdate));

//                    batchCnt++;
//                    hasUnDo = true;
                    batchHelper.batchCnt++;
                }
                //if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
                if (batchHelper.ifDo()) {

                    Boolean b = false;
                    try {
                        // ps.executeBatch();
                        // conn.commit();
                        // b = true;
                        // b=ExecuteSql(new PFSqlCommandString(sb.toString()));
                        b = ExecuteSql(sb);
                    } catch (Exception e) {
                        SetError(e);
                    }

                    if (!b) {
                        // CloseReader(rdr);
                        CloseConn();
                        return false;
                    } else {
                        insertedCnt = idx + 1;
                        // if (hasSysLimitId) {
                        // lastInsertedId = currentSysLimitId;
                        // }
                    }
                    if (sqlRowsCopiedAction != null) {
                        sqlRowsCopiedAction.accept(idx);
                    }
                    if (stopAction != null) {
                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
                            CloseConn();
                            return true;
                        }
                    }

                    // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
                    // sb = new StringBuilder();
                    sb.clear();

//                    hasUnDo = false;
//                    batchCnt = 0;
                    batchHelper.hasDone();
                } else {
                    // batchCnt++;
                    // oneThousandCount++;
                }
                if (!batchHelper.hasNext) {
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

    @Override
    public <T> boolean doDeleteList(
            //List<String> srcFieldNames
            //List<String> dstFieldNames// SqlInsertCollection dstInsert
            //,
            String tableName, String[] primaryKey,
            //int cnt,
            PFFunc3<Integer, Object, Object, Boolean> hasNextAction,
            Function<Integer, T> getItemAction,
            // Consumer<BaseSqlUpdateCollection> rowAction,
            SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction, Consumer<Integer> sqlRowsCopiedAction,
            Predicate<Boolean> stopAction) {
        // SqlInsertCollection dstInsert=null;
        SGSqlUpdateCollection dstUpdate = null;

        BatchInsertOption insertOption = GetInsertOption();
        // BatchInsertOption insertOption = GetSqlDeleteOption();

        OpenConn();

        int idx = 0;

        insertedCnt = 0; // 已插入的行数

        int batchSize = insertOption.getProcessBatch(); // 50000;// tidb设置大些试试,测试100万行/25秒
//        int batchCnt = 0;
//
//        Boolean hasUnDo = false;

        UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
        try {

            List<String> srcFieldNames = null;
            ResultSetMetaData dstMd = null;

            OpenConn();
            conn.setAutoCommit(false);
            PreparedStatement ps = null;
            // ResultSetMetaData dstMd =srcFieldNames==null? GetMetaData(tableName)
            // :GetMetaData(tableName,srcFieldNames);

            // PreparedStatement ps =srcFieldNames==null? GetPs(tableName):
            // GetPs(tableName,srcFieldNames);

//            // 注意这里的insert的valueType应该是目标表的类型(但转换为PFType的
//            if (dstUpdate == null) {
//
//                ResultSetMetaData dstMd = srcFieldNames == null ? GetMetaData(tableName)
//                        : GetMetaData(tableName, srcFieldNames);
//                // dstInsert = getInsertCollection(dstMd);
//                dstUpdate = this.getUpdateCollection(dstMd);
//                dstUpdate.PrimaryKeyFields(false, primaryKey);
//            }

//            // StringBuilder sb = new StringBuilder();
//            PFSqlCommandString sb = new PFSqlCommandString();

            PFBatchHelper batchHelper = new PFBatchHelper();
            batchHelper.batchSize = batchSize;
            batchHelper.batchCnt = 0;
            while (true) {
                //T item = null;
                //boolean hasNext = idx < cnt;
                batchHelper.hasNext = hasNextAction.go(idx, null, null);
//                if (hasNext) {
//                    item = getItemAction.apply(idx);
//                }
                if (batchHelper.hasNext) {
                    T item = null;
                    if (null != getItemAction) {
                        item = getItemAction.apply(idx);
                    }

                    if (0 == idx) {
                        if (null == srcFieldNames && null != item) {
                            srcFieldNames = getFieldsByRowObject(item);
                        }
                        //因为是删除,所以不需要dstFieldNames,只需要主键就够了
//                        if (null == dstFieldNames) {
//
//                            dstFieldNames = PFDataHelper.ListSelect(this.GetTableEditableFields(tableName),
//                                    a -> a.getFieldName());
//
//                            if (null != srcFieldNames) { // 来源中没有的字段,就不插入目标表,这样可以使用sql表列的默认值
//                                SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);
//                                // dstFieldNames=dstFieldNames.stream().filter(a->srcFieldNamesRef.GetValue().contains(a)).collect(Collectors.toList());
//                                dstFieldNames = PFDataHelper.ListWhere(dstFieldNames,
//                                        a -> PFDataHelper.ListAny(srcFieldNamesRef.GetValue(),
//                                                b -> b.equalsIgnoreCase(a))); // 大小写不匹配时,还要改UpdateModelValueAutoConvert才行
//                            }
//                        }
                        if (null == primaryKey) {

                            primaryKey = this.GetTableEditableFields(tableName).stream().filter(a->a.getIsPrimaryKey()).map(a->a.getFieldName()).toArray(String[]::new);

                        }
                        if (null == dstMd) {
//                            dstMd = GetMetaData(tableName, Arrays.asList(dstFieldNames.stream()
//                                    .filter(a -> !a.equals(sys_limit_id)).toArray(String[]::new)));
                            dstMd = GetMetaData(tableName, Arrays.asList(primaryKey));
                        }
                        if (null == ps) {
                            //ps = dstFieldNames == null ? GetPs(tableName) : GetPs(tableName, dstFieldNames, false);
                            ps = GetDeletePs(tableName,Arrays.asList( primaryKey), false);
                        }
                        if (null == dstUpdate) {

                            dstUpdate = this.getUpdateCollection(dstMd);
                            dstUpdate.PrimaryKeyFields(false, primaryKey);
                            if (null != srcFieldNames) {
                                dstUpdate.UpdateLowerKeyMapBySrcField(srcFieldNames);
                            }
                        }
                    }

//                    // dstInsert.UpdateModelValueAutoConvert(item);
//                    if (insertOption.getAutoUpdateModel()) {
//                        // dstUpdate.UpdateByDataReader(rdr);
//                        dstUpdate.UpdateModelValueAutoConvert(item);
//                    }
                    if (insertOption.getAutoUpdateModel() && null != getItemAction && null != item) {
                        dstUpdate.UpdateModelValue(item);
//						if (hasSysLimitId) {
//							if (item instanceof ResultSet) {
//								currentSysLimitId = ((ResultSet) item).getLong(sysLimitIdFieldName);
//							}
//						}
                    }

                    if (rowAction != null) {
                        // rowAction.accept(dstUpdate);
                        rowAction.go(dstUpdate, idx, null);
                    }

//                    // ps = insertIntoCachedRowSet(ps, dstMd, dstInsert);// ,dstColumnType);
//                    // sb.append(GetDeleteSql(tableName, dstUpdate));
//                    sb.add(GetDeleteSql(tableName, dstUpdate));
                    ps = insertIntoCachedRowSet(ps, dstMd, dstUpdate); // ,dstColumnType);

//                    batchCnt++;
//                    hasUnDo = true;
                    batchHelper.batchCnt++;
                }
                //if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
                if (batchHelper.ifDo()) {

                    Boolean b = false;
                    try {
                        ps.executeBatch();
                        conn.commit();
                        b = true;
                        //b = ExecuteSql(sb);
                    } catch (Exception e) {
                        SetError(e);
                    }

                    if (!b) {
                        // CloseReader(rdr);
                        CloseConn();
                        return false;
                    } else {
                        insertedCnt = idx + 1;
                        // if (hasSysLimitId) {
                        // lastInsertedId = currentSysLimitId;
                        // }
                    }
                    if (sqlRowsCopiedAction != null) {
                        sqlRowsCopiedAction.accept(idx);
                    }
                    if (stopAction != null) {
                        if (stopAction.test(true)) { // 允许中途终止--benjamin20200812
                            // CloseReader(rdr);//rdr不是本连接产生的,这样关闭不太好
                            CloseConn();
                            return true;
                        }
                    }

//                    // ps =srcFieldNames==null? GetPs(tableName): GetPs(tableName,srcFieldNames);
//                    // sb = new StringBuilder();
//                    sb.clear();
                    ps.clearBatch();

//                    hasUnDo = false;
//                    batchCnt = 0;
                    batchHelper.hasDone();
                } else {
                    // batchCnt++;
                    // oneThousandCount++;
                }
                if (!batchHelper.hasNext) {
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
    @Override
    public Boolean HugeUpdateByComparedTable(ISGJdbc dstJdbc, String srcSql, String dstSql, String[] joinColumn,
                                             String[] compareColumn, SGAction<Double, Double, Double> compareAccessed, BiConsumer<Double, Double> accessed,

                                             Predicate<Boolean> stopAction,

                                             String tableName // ,

    ) {
        try {

            SGAction<BaseSqlUpdateCollection, Integer, Object> rowAction = null;
            // Consumer<Integer> sqlRowsCopiedAction = null;

            List<PFDataRow> lRow = new ArrayList<PFDataRow>();
            List<PFDataRow> rRow = new ArrayList<PFDataRow>();
            List<PFDataRow> lrRow = new ArrayList<PFDataRow>();
            SGRef<List<PFDataRow>> lRowRef = new SGRef<List<PFDataRow>>(lRow);
            SGRef<List<PFDataRow>> rRowRef = new SGRef<List<PFDataRow>>(rRow);
            //SGRef<List<PFDataRow>> lrRowRef = new SGRef<List<PFDataRow>>(lrRow);

            doFindTableRowDifference(dstJdbc, srcSql, dstSql, joinColumn, compareColumn,
                    // accessed,
                    compareAccessed,
                    // resultUpdated,
                    null, stopAction, lRowRef, rRowRef//, lrRowRef
            );

            // BatchInsertOption insertOption = GetInsertOption();

            // OpenConn();

            // int idx = 0;

            insertedCnt = 0; // 已插入的行数

            // int batchSize = insertOption.getProcessBatch();// 50000;//
            // tidb设置大些试试,测试100万行/25秒
            // int batchCnt = 0;
            //
            // Boolean hasUnDo = false;

            UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());

            SGSqlInsertCollection srcInsert = null;
            List<String> srcFieldNames = null;
            if (lRow != null && (!lRow.isEmpty())) {
                srcInsert = getInsertCollection();
                // srcInsert.InitItemByModel(columns);
                srcInsert.InitItemByDataColumn(lRow.get(0).getCol());
                srcFieldNames = new ArrayList<String>(srcInsert.keySet());
            }
            // List<String>[] srcFieldNamesArr=new ArrayList<String>[1]();
            // Object[] srcFieldNamesArr= {srcFieldNames};
            // String[] srcFieldNamesArr= new String[1];
            // Object[] srcFieldNamesArr = {srcFieldNames};
            SGRef<List<String>> srcFieldNamesRef = new SGRef<List<String>>(srcFieldNames);

            int insertCnt = lRow.size();
            int updateCnt = lrRow.size();
            int deleteCnt = rRow.size();


            boolean[] b1 = {false};
            boolean[] b2 = {false};
            boolean[] b3 = {false};
            // AtomicInteger already = new AtomicInteger(0);
            int[] inserted = {0};
            int[] updated = {0};
            int[] deleted = {0};
            double total = insertCnt + updateCnt + deleteCnt;
            Thread insertThread = new Thread() { // 线程操作
                public void run() {
                    ISqlExecute dstExec = null;
                    try {
                        dstExec = SGSqlExecute.Init(dstJdbc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    b1[0] = dstExec.doInsertList(
                            // null,
                            // (List<String>) srcFieldNamesArr[0],
                            srcFieldNamesRef.GetValue(), tableName,
                            // lRow.size(),
                            (a, b, c) -> a < insertCnt,
                            // a -> lRow.get(a),
                            (a) -> {
                                PFDataRow dr = lRow.get(0);
                                lRow.remove(dr); // 为了清理GC
                                return dr;
                            }, rowAction, a -> {
                                SGDataHelper.GCCollectAsync(true);
                                if (accessed != null) {
                                    inserted[0] = a;
                                    // accessed.accept((inserted[0] + updated[0] +
                                    // deleted[0]) * 100 / total);
                                    accessed.accept(Double.valueOf(inserted[0] + updated[0] + deleted[0]), total);
                                }
                            }, stopAction);
                }
            };
            insertThread.start();
            Thread updateThread = new Thread() { // 线程操作
                public void run() {
                    ISqlExecute dstExec = null;
                    try {
                        dstExec = SGSqlExecute.Init(dstJdbc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
//                    b2[0] = dstExec.doUpdateList(
//                            // (List<String>) srcFieldNamesArr[0],
//                            srcFieldNamesRef.GetValue(), tableName, joinColumn, updateCnt,
//                            // a -> lrRow.get(a),
//                            a -> {
//                                PFDataRow dr = lrRow.get(0);
//                                lrRow.remove(dr); // 为了清理GC
//                                return dr;
//                            }, rowAction, a -> {
//                                if (accessed != null) {
//                                    PFDataHelper.GCCollectAsync(true);
//                                    updated[0] = a;
//                                    // accessed.accept((inserted[0] + updated[0] +
//                                    // deleted[0]) * 100 / total);
//                                    accessed.accept(Double.valueOf(inserted[0] + updated[0] + deleted[0]), total);
//                                }
//                            }, stopAction);

                    b2[0] = dstExec.doUpdateList(
                            // (List<String>) srcFieldNamesArr[0],
                            srcFieldNamesRef.GetValue(), tableName, joinColumn,
                            //updateCnt,
                            (a, b, c) -> a < updateCnt,
                            // a -> lrRow.get(a),
                            a -> {
                                PFDataRow dr = lrRow.get(0);
                                lrRow.remove(dr); // 为了清理GC
                                return dr;
                            }, rowAction, a -> {
                                if (accessed != null) {
                                    SGDataHelper.GCCollectAsync(true);
                                    updated[0] = a;
                                    // accessed.accept((inserted[0] + updated[0] +
                                    // deleted[0]) * 100 / total);
                                    accessed.accept(Double.valueOf(inserted[0] + updated[0] + deleted[0]), total);
                                }
                            }, stopAction);
                }
            };
            updateThread.start();
            Thread deleteThread = new Thread() { // 线程操作
                public void run() {
                    ISqlExecute dstExec = null;
                    try {
                        dstExec = SGSqlExecute.Init(dstJdbc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
//                    b3[0] = dstExec.doDeleteList(
//                            // (List<String>) srcFieldNamesArr[0],
//                            srcFieldNamesRef.GetValue(), tableName, joinColumn, deleteCnt,
//                            // a -> rRow.get(a),
//                            a -> {
//                                PFDataRow dr = rRow.get(0);
//                                rRow.remove(dr); // 为了清理GC
//                                return dr;
//                            }, rowAction, a -> {
//                                if (accessed != null) {
//                                    PFDataHelper.GCCollectAsync(true);
//                                    deleted[0] = a;
//                                    // accessed.accept((inserted[0] + updated[0] +
//                                    // deleted[0]) * 100 / total);
//                                    accessed.accept(Double.valueOf(inserted[0] + updated[0] + deleted[0]), total);
//                                }
//                            }, stopAction);
                    b3[0] = dstExec.doDeleteList(
                            // (List<String>) srcFieldNamesArr[0],
                            //srcFieldNamesRef.GetValue(),
                            tableName, joinColumn,
                            //deleteCnt,
                            (a, b, c) -> a < deleteCnt,
                            // a -> rRow.get(a),
                            a -> {
                                PFDataRow dr = rRow.get(0);
                                rRow.remove(dr); // 为了清理GC
                                return dr;
                            }, rowAction, a -> {
                                if (accessed != null) {
                                    SGDataHelper.GCCollectAsync(true);
                                    deleted[0] = a;
                                    // accessed.accept((inserted[0] + updated[0] +
                                    // deleted[0]) * 100 / total);
                                    accessed.accept(Double.valueOf(inserted[0] + updated[0] + deleted[0]), total);
                                }
                            }, stopAction);
                }
            };
            deleteThread.start();

            insertThread.join();
            updateThread.join();
            deleteThread.join();

            lRow.clear();
            lrRow.clear();
            rRow.clear();
            SGDataHelper.GCCollectAsync(true);
            return b1[0] && b2[0] && b3[0];
        } catch (Exception e) {
//            PFDataHelper.WriteError(new Throwable(), e);
            SetError(e);
        }
        return false;
    }

    @Override
    public List<SGSqlFieldInfo> GetTableEditableFields(String tableName) {//,SGRef<ResultSetMetaData> mdRef) {
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
                if (!md.isAutoIncrement(mdIdx) && !md.isReadOnly(mdIdx)) { // 不是自增列才插入,否则sql会报错-- benjamin
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
}
