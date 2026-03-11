package com.sellgirl.sgJavaHelper;

public class SGPathConfig
{
    //#region Field
    private String _imagePath = "Images/Perfect";
    /// <summary>
    /// 样式的存放目录(/Css)
    /// </summary>
    //private String _cssPath = "Css/Perfect";
    private String _cssPath = "css/Perfect";
    /// <summary>
    /// 字段列名等的存放目录
    /// </summary>
    //private String _configPath = "Configs/XmlConfig";
    private String _configPath = "XmlConfig";
    /// <summary>
    /// DataBox脚本路径
    /// </summary>
    private String _dataBoxJsPath = "Content/My97DatePicker/WdatePicker.js";
    
    public String getImagePath() {
		return _imagePath;
	}

	public void setImagePath(String _imagePath) {
		this._imagePath = _imagePath;
	}

	public String getCssPath() {
		return _cssPath;
	}

	public void setCssPath(String _cssPath) {
		this._cssPath = _cssPath;
	}

	public String getConfigPath() {
		return _configPath;
	}

	public void setConfigPath(String _configPath) {
		this._configPath = _configPath;
	}

	public String getDataBoxJsPath() {
		return _dataBoxJsPath;
	}

	public void setDataBoxJsPath(String _dataBoxJsPath) {
		this._dataBoxJsPath = _dataBoxJsPath;
	}
//    
//    //#endregion
//    /// <summary>
//    /// 组件图片等的存放目录(/Images)
//    /// </summary>
//    public String ImagePath { get { return _imagePath; } set { _imagePath = value; } }
//    /// <summary>
//    /// 样式的存放目录(/Css)
//    /// </summary>
//    public String CssPath { get { return _cssPath; } set { _cssPath = value; } }
//    /// <summary>
//    /// 字段列名等的存放目录
//    /// </summary>
//    public String ConfigPath { get { return _configPath; } set { _configPath = value; } }//最前面不加/是为了方便AppDomainAppPath拼接的情况
//
//    /// <summary>
//    /// 字段列名等的存放目录
//    /// </summary>
//    public String DataBoxJsPath { get { return _dataBoxJsPath; } set { _dataBoxJsPath = value; } }

}