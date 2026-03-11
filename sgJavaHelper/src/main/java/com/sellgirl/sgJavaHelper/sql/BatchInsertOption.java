package com.sellgirl.sgJavaHelper.sql;

public class BatchInsertOption {

    //private int _processBatch = 500;

	/**
	 * 测试结果:
	 * 1.sqlServer中insert脚本插入时,10000行以上就卡住(29数据库)
	 */
    private int _processBatch = 50000;
//    public int ProcessBatch { get { return _processBatch; } set { _processBatch = value; } }
	/**
	 * 把null转为默认值.
	 */
	public boolean convertNullTo0=true;
	public int getProcessBatch() {
		return _processBatch;
	}

	public void setProcessBatch(int processBatch) {
		this._processBatch = processBatch;
	}

	private Boolean _autoUpdateModel = true;

    /// <summary>
    /// 自动更新模型(默认true)
    /// </summary>
	public Boolean getAutoUpdateModel() {
		return _autoUpdateModel;
	}

	public void setAutoUpdateModel(Boolean autoUpdateModel) {
		this._autoUpdateModel = autoUpdateModel;
	}


    public boolean isConvertNullTo0() {
		return convertNullTo0;
	}

	public void setConvertNullTo0(boolean convertNullTo0) {
		this.convertNullTo0 = convertNullTo0;
	}

}
