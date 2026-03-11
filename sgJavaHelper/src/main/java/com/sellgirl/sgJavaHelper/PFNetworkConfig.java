package com.sellgirl.sgJavaHelper;


public class PFNetworkConfig
{
    private int _downloadSpeed = 1024 * 1024 * 10;
    public int getDownloadSpeed() {
		return _downloadSpeed;
	}
	public void setDownloadSpeed(int _downloadSpeed) {
		this._downloadSpeed = _downloadSpeed;
	}
	//public int DownloadSpeed { get { return _downloadSpeed; } set { _downloadSpeed = value; } }
}
