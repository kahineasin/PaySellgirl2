package com.sellgirl.sgJavaSpringHelper;

public class BatchInsertOption {

    private int _processBatch = 500;
//    public int ProcessBatch { get { return _processBatch; } set { _processBatch = value; } }
    public int getProcessBatch() {
		return _processBatch;
	}

	public void setProcessBatch(int _processBatch) {
		this._processBatch = _processBatch;
	}

	private Boolean _autoUpdateModel = true;

    /// <summary>
    /// 自动更新模型(默认true)
    /// </summary>
	public Boolean getAutoUpdateModel() {
		return _autoUpdateModel;
	}

	public void setAutoUpdateModel(Boolean _autoUpdateModel) {
		this._autoUpdateModel = _autoUpdateModel;
	}

	//public Boolean AutoUpdateModel { get { return _autoUpdateModel; } set { _autoUpdateModel = value; } }

    /// <summary>
    /// 用于下面这种更新的行数
    /// update xx from xx where xx;
    /// update xx from xx where xx;//...n行
    /// 可以认为是tidb里的 max_allowed_packet=67108864 变量
    /// </summary>
    public void SetTidbBatchUpdateProcessBatch()
    {
        //// Packets larger than max_allowed_packet are not allowed
        //_processBatch =  500000;

        ////100000非常慢
        //_processBatch = 100000;

        _processBatch = 10000;
    }
}
