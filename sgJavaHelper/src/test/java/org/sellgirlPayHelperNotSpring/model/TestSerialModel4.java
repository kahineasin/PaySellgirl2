package org.sellgirlPayHelperNotSpring.model;

import com.fasterxml.jackson.annotation.JsonInclude;


public class TestSerialModel4<T> extends  TestSerialAbsBase{
	public String aa="aa";
	public T ll=null;
	/**
	 * 不会被json序列化
	 */
	@SuppressWarnings("unused")
	private String cc="cc";
	/**
	 * 会被json序列化(因为有get方法)
	 */
	private String dd="dd";
	public String getDd() {
		return dd;
	}
	/**
	 * 不会序列化(有transient)
	 */
	public transient String gg="gg";
	/**
	 * 序列化
	 */
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	public String hh="hh";
	/**
	 * 不序列化(但没办法像C#那样指定默认值是什么)
	 */
	@JsonInclude(value=JsonInclude.Include.NON_DEFAULT)
	public String ii;	
	
//	@JsonInclude(value=JsonInclude.Include.CUSTOM,valueFilter=)
//	public String jj="jj";	

//	/**
//	 * 不序列化
//	 */
//	@JsonInclude(value=JsonInclude.Include.NON_DEFAULT)
//	public String jj="jj";
}
