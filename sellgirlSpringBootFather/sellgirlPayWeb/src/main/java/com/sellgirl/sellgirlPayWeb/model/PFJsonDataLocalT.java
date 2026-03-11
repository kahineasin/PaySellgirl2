package com.sellgirl.sellgirlPayWeb.model;


public  class PFJsonDataLocalT<T>{
	public T Data ;
	public Boolean Result=true;
	public PFJsonDataLocalT(T data) {
		Data=data;
	}
}
