package com.perfect.demo.model;

import java.io.File;

import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;

public class PcDeskImgNameResult{
	private String _imgName;
	private long _lastModified;
	private String _lastModifiedDateString;
	private String _hashMD5;
	public PcDeskImgNameResult(File file) {
    	setImgName(file.getName());
    	setLastModified(file.lastModified());
    	setHashMD5(SGDataHelper.GetHashMD5(file));
	}
	public String getHashMD5() {
		return _hashMD5;
	}
	public void setHashMD5(String hashMD5) {
		this._hashMD5 = hashMD5;
	}
	public long getLastModified() {
		return _lastModified;
	}
	public void setLastModified(long lastModified) {
		this._lastModified = lastModified;
		this._lastModifiedDateString=SGDataHelper.TimeStampToDateString(lastModified, null);
	}
	public String getImgName() {
		return _imgName;
	}
	public void setImgName(String imgName) {
		this._imgName = imgName;
	}
	public String getLastModifiedDateString() {
		return _lastModifiedDateString;
	}
//	public void setLastModifiedDateString(String lastModifiedDateString) {
//		this._lastModifiedDateString = lastModifiedDateString;
//	}
}
