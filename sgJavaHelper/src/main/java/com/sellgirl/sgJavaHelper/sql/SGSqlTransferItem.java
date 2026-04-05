package com.sellgirl.sgJavaHelper.sql;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.ISGSqlTransferItem;
import com.sellgirl.sgJavaHelper.SGAction1;
import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTableFieldValidModel;
import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGDate;

public class SGSqlTransferItem extends DirectNode implements ISGSqlTransferItem, Cloneable {

	// public Boolean _hasWhereMonth=false;
	// private Boolean _useDataReader = false;
	// private Boolean _removeOldData = true;
//    private Function<DataTable, DataTable> _dataRender = null;

	public ISGJdbc SrcJdbc;
	public ISGJdbc DstJdbc;
	public SGSqlType SrcSqlType = SGSqlType.SqlServer;
	public SGSqlType DstSqlType = SGSqlType.SqlServer;

	public String SrcSql = null;


	public String DstDbName = null;
	public String DstTableName = null;
	public String ModelConfig = null;


	// 这样不好,因为时间循环的方法写起来不方便
//    public enum UpdateFrequencyEnum{
//    	ByMonth,//按月更新
//    	ByYear//按年更新
//    }
//    public UpdateFrequencyEnum UpdateFrequency=UpdateFrequencyEnum.ByMonth;

	/**
	 * 通常是按月复盖数据时设置此属性(其实就是任务的执行数据间隔)
	 */
	public Consumer<SGSqlTransferItem> TransferByMonth = null;
	/**
	 * 按年复盖数据
	 */
	public Consumer<SGSqlTransferItem> TransferByYear = null;

//    public String getHashId() {
//		return _hashId==null?DstTableName:_hashId;
//	}
	@Override
	public String getHashId() {
		return hashId == null ? DstTableName : hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}


	/***
	 * 目标表主键(用于目标表不存在时生成表)
	 */
	public String[] DstTablePrimaryKeys = null;

	/**
	 * 目标表聚集索引
	 */
	public String[] DstTableClusteredIndex = null;
	/**
	 * 目标表非聚集索引
	 */
	public String[] DstTableIndex = null;
	/**
	 * 大数据表中的分区属性,如clickhouse的PARTITION
	 */
	public String[] DstTablePartition = null;
	/**
	 * 大数据表(如clickhouse)中的分区属性
	 */
	public String[] DstTableOrder = null;

	public String DstTableComment = null;
	/// <summary>
	/// 目标表备注(用于目标表不存在时生成表)
	/// </summary>
	public Map<String, String> DstTableColumnComment = null;
	public String ProcedureName = null;
	// public Action<SqlInsertCollection> BeforeInsertAction=null;
	/**
	 * 插入行之前对数据进行修改
	 */
	public Consumer<BaseSqlUpdateCollection> BeforeInsertAction = null;
	public Consumer<PFDataRow> BeforeBulkAction = null;

	/**
	 * 转移数据之前执行(常用于按月更新时先清除某月的数据)
	 */
	public SGAction1<SGSqlTransferItem> BeforeTransferAction = null;
	/**
	 * 自动迁移 true:ISqlExecute.TransferTable方法会自动把dr通过jdbc bulk入
	 * false:不会自动迁移（如当想使用spark时），可以把spark的方法写到 BeforeTransferAction里执行
	 */
	public boolean AutoTransfer = true;
	/**
	 * 自动创建目标表(需要有srcSql).
	 */
	public boolean autoCreateDstTable = false;
	/**
	 * 把null转为默认值.
	 */
	public boolean convertNullTo0=true;
	public Function<BaseSqlUpdateCollection, List<SGDataTableFieldValidModel>> PreValidAction = null;
	/**
	 * 迁移后的完整性验证
	 */
	public SGFunc<SGSqlTransferItem,Object,Object,AbstractApiResult<?>> IntegrityValidAction=null;
	/// <summary>
	/// 转移数据之后(执行存储过程之前)的操作(如转Json,根据增量表更新主表等)
	/// </summary>
	// public Action<DbReportService> AfterTransferAction=null;
	public SGAction1<SGSqlTransferItem> AfterTransferAction = null;


	public SGSqlTransferItem() {
		action = (a,b,c) -> {
			ISqlExecute dstExec;
				dstExec = SGSqlExecute.Init(DstJdbc);
				return dstExec.TransferTable(this, d -> a.setFinishedNum(d), null);
		};
	}
	public Boolean IsHugeData() {
		return _sqlCommandTimeout.IsHuge();
	}

	private PFSqlCommandTimeoutSecond _sqlCommandTimeout = PFSqlCommandTimeoutSecond.NormalTransfer();

	public PFSqlCommandTimeoutSecond getSqlCommandTimeout() {
		return _sqlCommandTimeout;
	}

	public void setSqlCommandTimeout(PFSqlCommandTimeoutSecond _sqlCommandTimeout) {
		this._sqlCommandTimeout = _sqlCommandTimeout;
	}
//	public PFSqlCommandTimeoutSecond SqlCommandTimeout { get { return _sqlCommandTimeout; } set { _sqlCommandTimeout = value; } }
//    /// <summary>
//    /// 便于做一些数据转换
//    /// </summary>
//    public Func<DataTable, DataTable> DataRender { get { return _dataRender; } set { _dataRender = value; } }
//    public Func<String, DateTime, String> SqlRender=null;

	// public SqlInsertCollection InsertCollection=null;
//
	/// <summary>
	/// 源数据分表
	/// </summary>
	public String[] SrcTableSeparate = null;

//    /// <summary>
//    /// 默认删除原数据,王总的t_orders_sum例外
//    /// </summary>
//    //public Boolean RemoveOldData { get { return _removeOldData; } set { _removeOldData = value; } }
    public Integer BulkBatch=null;
//    /// <summary>
//    /// 是增量表
//    /// </summary>
//    public Boolean IsTableChange=null;
//    ///// <summary>
//    ///// 上次增量更新的时时间
//    ///// </summary>
//    //public DateTime? LastChangeUpdateDate=null;
//    /// <summary>
//    /// 有对应的增量表
//    /// </summary>
//    public Boolean HasTableChange=null;

	/// <summary>
	/// 为了保存一些常量,便于在AfterTransferAction 等方法里使用
	/// </summary>
	public Map<String, Object> ViewData = null;
	/*
	 * 常用月份字段
	 */
	private PFCmonth cmonth = null;
	private PFCmonth lastCmonth = null;
	private PFCmonth nextCmonth = null;
	/**
	 * 有时需要用到天来判断. 如2022-03-15日之后执行不同的脚本,之类--benjamin 20220215
	 */
	private SGDate now = null;
	
	/**
	 * 数据最小日期,通常是月份,如:里格order_order的最小月份是2022.04 
	 * 本来觉得在TransferTable方法里判断没数据就跳过,这样更好,但后来发现不行,如order_order,最小月份虽然是2021.08,但2021.04也有几张订单,因为里格是按commitTime来移历史订单的
	 */
	public SGDate MinDate = null;

	public PFCmonth getPFCmonth() {
		return cmonth;
	}

	/**
	 * 月份,格式:yyyy.MM
	 * @return
	 */
	public String getCmonth() {
		return cmonth == null ? null : cmonth.getCmonth();
	}

	/**
	 * Task的开发者尽量不要调用此方法, 使用setPFDate
	 * @param cmonth
	 */
	@Deprecated
	public void setCmonth(PFCmonth cmonth) {
		this.cmonth = cmonth;
		this.lastCmonth = cmonth.AddMonths(-1);
		this.nextCmonth = cmonth.AddMonths(1);
	}

	/**
	 * 用setPFDate
	 * @param cmonth
	 */
	@Deprecated
	public void setCmonth(String cmonth) {
		PFCmonth pfCmonth = new PFCmonth();
		pfCmonth.setCmonth(cmonth);
		this.setCmonth(pfCmonth);
	}
	public void setSGDate(SGDate pfDate) {
		now=pfDate;
		setCmonth(pfDate.toPFCmonth());
	}
	public SGDate getNow() {
		return now;
	}
	//不需要此方法,直接用updateByCmonth就行了
//	public void setNow(PFDate now) {
//		this.now=now;
//	}

//	public PFSqlTransferItem updateByCmonth(String cmonth) {
//		if (TransferByMonth != null) {
//			setCmonth(cmonth);
//
//			if (SrcSql != null) {
//				//格式规范:下划线最后一位含有格式的意思,如 cmonth表示 2021.10 ncmonth表示2021-10
//				SrcSql = SrcSql.replace("{cmonth}", cmonth);//2021.10
//				SrcSql = SrcSql.replace("{nmonth}", cmonth.replace(".","-"));//2021-10
//				SrcSql = SrcSql.replace("{last_cmonth}", getLastCmonth());
//				SrcSql = SrcSql.replace("{next_cmonth}", getNextCmonth());
//				SrcSql = SrcSql.replace("{year}", String.valueOf(getYear()));
//				//上年的月
//				SrcSql = SrcSql.replace("{last_year_cmonth}", String.valueOf(getPFCmonth().ToPFDate().AddYears(-1).toCmonth()));//上年同月份(同比)
//				//下月的年
//				SrcSql = SrcSql.replace("{next_month_year}", String.valueOf(getNextPFCmonth().getYear()));
//				SrcSql = SrcSql.replace("{ym}", getPFCmonth().getYm());
//			}
//
//			TransferByMonth.accept(this);
//		}
//		return this;
//	}
	/**
	 * 此方法一般是提供给生成多个Task的通用方法来调用,所以在此方法里,多半要防止用户已经自行设置过cmonth之类(所以用if null来判断)
	 * 按月更新
	 * 原则上,当TransferByMonth和TransferByYear都为空时,按时间段生成Task时不要自动调用此方法,在定义tidb_daily_monthly_hyzl 时自行调用updateSqlByCmonth方法
	 * @param time 这里用PFDate格式是因为:日结数据虽然也是复盖整个月的数据,但可能从某天起就用新的脚本,所以用PFDate比较方便
	 * @return
	 */
	public SGSqlTransferItem updateByCmonth(SGDate time) {
//		if(null==now) {
//			now=time;
//		}
		//String cmonth=time.toCmonth();

		//setCmonth(cmonth);
		if(null==this.cmonth) {
			//setCmonth(cmonth);
			setSGDate(time);
		}
		if (TransferByMonth != null) {
			//setCmonth(cmonth);
			TransferByMonth.accept(this);
			
			if (SrcSql != null) {
//				//格式规范:下划线最后一位含有格式的意思,如 cmonth表示 2021.10 ncmonth表示2021-10
//				SrcSql = SrcSql.replace("{cmonth}", cmonth);//2021.10
//				SrcSql = SrcSql.replace("{nmonth}", cmonth.replace(".","-"));//2021-10
//				SrcSql = SrcSql.replace("{last_cmonth}", getLastCmonth());
//				SrcSql = SrcSql.replace("{next_cmonth}", getNextCmonth());
//				SrcSql = SrcSql.replace("{year}", String.valueOf(getYear()));
//				//上年的月
//				SrcSql = SrcSql.replace("{last_year_cmonth}", String.valueOf(getPFCmonth().ToPFDate().AddYears(-1).toCmonth()));//上年同月份(同比)
//				//下月的年
//				SrcSql = SrcSql.replace("{next_month_year}", String.valueOf(getNextPFCmonth().getYear()));
//				SrcSql = SrcSql.replace("{ym}", getPFCmonth().getYm());
				
				updateSqlByCmonth();
			}

		}
		
		if (this.TransferByYear != null) {
			//setCmonth(cmonth);
			TransferByYear.accept(this);
			
			if(SrcSql!=null) {
				SrcSql = SrcSql.replace("{year}", String.valueOf(getYear()));
			}
		}
		
		
//		//想了几次,当TransferByMonth和TransferByYear都为null时,确实没必要自动replace sql了.
//		//因为如 tidb_daily_monthly_hyzl,多个月只产生1个Task,说明月份不是通过时间范围time来决定的,而是由
//		//后来觉得还是需要,当tidb_daily_monthly_hyzl时,也想让用户自己调用updateByCmonth方法来执行这段
//		if (SrcSql != null) {
//			//格式规范:下划线最后一位含有格式的意思,如 cmonth表示 2021.10 ncmonth表示2021-10
//			SrcSql = SrcSql.replace("{cmonth}", cmonth);//2021.10
//			SrcSql = SrcSql.replace("{nmonth}", cmonth.replace(".","-"));//2021-10
//			SrcSql = SrcSql.replace("{last_cmonth}", getLastCmonth());
//			SrcSql = SrcSql.replace("{next_cmonth}", getNextCmonth());
//			SrcSql = SrcSql.replace("{year}", String.valueOf(getYear()));
//			//上年的月
//			SrcSql = SrcSql.replace("{last_year_cmonth}", String.valueOf(getPFCmonth().ToPFDate().AddYears(-1).toCmonth()));//上年同月份(同比)
//			//下月的年
//			SrcSql = SrcSql.replace("{next_month_year}", String.valueOf(getNextPFCmonth().getYear()));
//			SrcSql = SrcSql.replace("{ym}", getPFCmonth().getYm());
//		}
		return this;
	}
	
	public SGSqlTransferItem updateSqlByCmonth() {
//		now=time;
//		String cmonth=time.toCmonth();
//
//		setCmonth(cmonth);
		String cmonth=this.getCmonth();
		if (SrcSql != null) {
			//格式规范:下划线最后一位含有格式的意思,如 cmonth表示 2021.10 ncmonth表示2021-10
			SrcSql = SrcSql.replace("{cmonth}", cmonth);//2021.10
			SrcSql = SrcSql.replace("{nmonth}", cmonth.replace(".","-"));//2021-10
			SrcSql = SrcSql.replace("{last_cmonth}", getLastCmonth());
			SrcSql = SrcSql.replace("{next_cmonth}", getNextCmonth());
			SrcSql = SrcSql.replace("{year}", String.valueOf(getYear()));
			//上年的月
			SrcSql = SrcSql.replace("{last_year_cmonth}", String.valueOf(getPFCmonth().ToSGDate().AddYears(-1).toCmonth()));//上年同月份(同比)
			//下月的年
			SrcSql = SrcSql.replace("{next_month_year}", String.valueOf(getNextPFCmonth().getYear()));
			SrcSql = SrcSql.replace("{ym}", getPFCmonth().getYm());
		}
		return this;
	}
	
	public SGSqlCreateTableCollection getSqlCreateTableCollection() {
		SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(DstJdbc);
		models.TableName = DstTableName;
		models.PrimaryKey = DstTablePrimaryKeys;
		models.ClusteredIndex = DstTableClusteredIndex;
		models.TableIndex = DstTableIndex;
		models.TableComment = DstTableComment;
		return models;
	}

	/**
	 * 按年更新
	 * @param year
	 * @return
	 */
	public SGSqlTransferItem updateByYear(int year) {
		SGDate time=new SGDate(year,1,1,1,1,1);
//		now=time;
//		String cmonth=String.valueOf(year)+".01";
//		setCmonth(cmonth);
//		if (this.TransferByYear != null) {
//			if(SrcSql!=null) {
//				SrcSql = SrcSql.replace("{year}", String.valueOf(year));
//			}
//			TransferByYear.accept(this);
//		}
		this.updateByCmonth(time);
		return this;
	}

	public PFCmonth getLastPFCmonth() {
		return lastCmonth;
	}

	public String getLastCmonth() {
		return lastCmonth.getCmonth();
	}

//	public void setLastCmonth(PFCmonth lastCmonth) {
//		this.lastCmonth = lastCmonth;
//	}

	public PFCmonth getNextPFCmonth() {
		return nextCmonth;
	}

	public String getNextCmonth() {
		return nextCmonth.getCmonth();
	}

	public int getYear() {
		return cmonth.ToSGDate().GetYear();
	}

	/**
	 * 测试时一条一条插入便于找错
	 */
	public Boolean TransferOneByOne = false;

//	public void setNextCmonth(PFCmonth nextCmonth) {
//		this.nextCmonth = nextCmonth;
//	}

//  public PFSqlTransferItem Apply(PFModelConfig src)
//  {
//      this.PropertyName = src.PropertyName;
//      this.DataSet = src.DataSet;
//      this.FieldId = src.FieldId;
//      this.FieldName = src.FieldName;
//      this.LowerFieldName = src.LowerFieldName;
//      this.FieldText = src.FieldText;
//      this.FieldType = src.FieldType;
//      this.Precision = src.Precision;
//      this.FieldSqlLength = src.FieldSqlLength;
//      this.FieldWidth = src.FieldWidth;
//      this.setVisible(src.getVisible()); 
//      this.Required = src.Required;
//      return this;
//      //return TransExpV2<PFModelConfig, PFModelConfig>.Trans(this);
//  }
//  public PFSqlTransferItem TClone()
//  {
//      return new PFModelConfig().Apply(this);
//
//  }
}
