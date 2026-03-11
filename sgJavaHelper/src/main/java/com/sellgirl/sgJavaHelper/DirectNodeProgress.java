package com.sellgirl.sgJavaHelper;

import java.util.List;
//import java.util.Map;

/**
 * 用于描述DirectNode的进度
 * @author Administrator
 *
 */
public class DirectNodeProgress {
	private int finishedNodeQty;
	private int successNodeQty;
	private int nodeQty;
	//private Map<String, Integer> finishedPercent;
	private List<DirectNodeProgressItem> progressItem;
	public int getFinishedNodeQty() {
		return finishedNodeQty;
	}
	public void setFinishedNodeQty(int finishedNodeQty) {
		this.finishedNodeQty = finishedNodeQty;
	}
	public void setSuccessNodeQty(int successNodeQty) {
		this.successNodeQty = successNodeQty;
	}
	public int getNodeQty() {
		return nodeQty;
	}
	public void setNodeQty(int nodeQty) {
		this.nodeQty = nodeQty;
	}
//	public Map<String, Integer> getFinishedPercent() {
//		return finishedPercent;
//	}
//	public void setFinishedPercent(Map<String, Integer> finishedPercent) {
//		this.finishedPercent = finishedPercent;
//	}
	public int getFinishedNodePercent() {
		return finishedNodeQty*100/nodeQty;
	}
	public boolean isAllSuccess() {
		//return finishedNodeQty==nodeQty;
		return successNodeQty==nodeQty;
	}
	public List<DirectNodeProgressItem> getProgressItem() {
		return progressItem;
	}
	public void setProgressItem(List<DirectNodeProgressItem> progressItem) {
		this.progressItem = progressItem;
	}
}
