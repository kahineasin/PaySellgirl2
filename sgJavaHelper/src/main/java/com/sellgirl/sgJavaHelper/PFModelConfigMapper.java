package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;
import java.util.List;

public class PFModelConfigMapper {
	public PFModelConfigMapper() {}
	public PFModelConfigMapper(String modelName,String xmlDataSetName) {
		this.ModelName=modelName;
		this.XmlDataSetName=xmlDataSetName;
	}
	public PFModelConfigMapper(String modelName,String xmlDataSetName,List<String> otherXmlDataSetName) {
		this(modelName, xmlDataSetName);
		this._otherXmlDataSetName=otherXmlDataSetName;
	}

    public String ModelName ;
    /// <summary>
    /// xml的DataSet节点名
    /// </summary>
    public String XmlDataSetName ;

    private List<String> _otherXmlDataSetName;
    
    
    /// <summary>
    /// 其它xml节点(如果主节点找不到,在这里找)
    /// </summary>
    public List<String> getOtherXmlDataSetName() {
    	if(_otherXmlDataSetName==null) {
    		_otherXmlDataSetName = new ArrayList<String>();
    	}
		return _otherXmlDataSetName;
	}

	public void setOtherXmlDataSetName(List<String> _otherXmlDataSetName) {
		this._otherXmlDataSetName = _otherXmlDataSetName;
	}

	//public List<String> OtherXmlDataSetName { get { return _otherXmlDataSetName ?? (_otherXmlDataSetName = new List<String>()); } set { _otherXmlDataSetName = value; } }

    //private List<PFModelPropertyConfigMapper> _exProperty;
    ///// <summary>
    ///// 例外属性,指定了别的DataSet节点和对应字段
    ///// </summary>
    //public List<PFModelPropertyConfigMapper> ExProperty { get { return _exProperty ?? (_exProperty = new List<PFModelPropertyConfigMapper>()); } set { _exProperty = value; } }

}
