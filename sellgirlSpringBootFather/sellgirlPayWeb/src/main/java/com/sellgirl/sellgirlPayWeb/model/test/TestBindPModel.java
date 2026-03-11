package com.sellgirl.sellgirlPayWeb.model.test;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.sellgirl.sgJavaHelper.SGDate;

public class TestBindPModel {

	@JsonSerialize(using = com.sellgirl.sgJavaHelper.SGDateSerialiaer.class)
	private com.sellgirl.sgJavaHelper.SGDate date=null;
	public TestBindPModel(SGDate date) {
		super();
		this.date = date;
	}

	public com.sellgirl.sgJavaHelper.SGDate getDate() {
		return date;
	}

	public void setDate(com.sellgirl.sgJavaHelper.SGDate date) {
		this.date = date;
	}
}
