package com.sellgirl.sgJavaHelper.sql;

public class BatchQueryOption {

    private int fetchSize = Integer.MIN_VALUE;//mysql 500000
    private int hugeFetchSize = Integer.MIN_VALUE;//mysql 500000

	public int getHugeFetchSize() {
		return hugeFetchSize;
	}

	public void setHugeFetchSize(int hugeFetchSize) {
		this.hugeFetchSize = hugeFetchSize;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
	
}
