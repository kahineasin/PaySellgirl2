package com.sellgirl.sgJavaHelper;

public class DirectNodeProgressItem {
	protected String hashId = null;
	/**
	 * 完成度 0~100 (便于显示任务进度)
	 * 一般可以在action执行之中设置此值
	 * 由于此值是于用户自行设置，所以不能准确表示node是否执行成功（比如transfer100%,但task.afterAction却失败的情况)
	 */
	protected int finishedPercent = 0;
	/**
	 * 对于无法评估总量的,由于无法计算finishedPercent,用此值表示进度
	 * (如迁移表时,不知道总行数,可以用finishNum表示已经迁移的行数)
	 */
	protected int finishedNum=0;
	
	/**
	 * 完成了不相当于成功了,还要要这个属性好一些
	 */
	protected boolean success=false;

	public DirectNodeProgressItem(DirectNode node) {
		this.hashId=node.getHashId();
		this.finishedPercent=node.getFinishedPercent();
		this.finishedNum=node.getFinishedNum();
		this.success=node.success;
	}

	public String getHashId() {
		return hashId;
	}
	public void setHashId(String hashId) {
		this.hashId = hashId;
	}
	public int getFinishedPercent() {
		return finishedPercent;
	}
	public void setFinishedPercent(int finishedPercent) {
		this.finishedPercent = finishedPercent;
	}
	public int getFinishedNum() {
		return finishedNum;
	}
	public void setFinishedNum(int finishedNum) {
		this.finishedNum = finishedNum;
	}
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
