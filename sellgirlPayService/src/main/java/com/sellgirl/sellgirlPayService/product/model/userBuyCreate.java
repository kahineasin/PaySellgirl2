package com.sellgirl.sellgirlPayService.product.model;

import com.sellgirl.sgJavaHelper.SGDate;

public class userBuyCreate {
	public long user_id;
	public ProductType source_type;
	public long source_id; 
	public  SGDate create_date ;
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public ProductType getSource_type() {
		return source_type;
	}
	public void setSource_type(ProductType source_type) {
		this.source_type = source_type;
	}
	public long getSource_id() {
		return source_id;
	}
	public void setSource_id(long source_id) {
		this.source_id = source_id;
	}
	public SGDate getCreate_date() {
		return create_date;
	}
	public void setCreate_date(SGDate create_date) {
		this.create_date = create_date;
	}
}
