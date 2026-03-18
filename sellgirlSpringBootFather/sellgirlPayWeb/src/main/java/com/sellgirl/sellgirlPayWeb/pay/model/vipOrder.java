package com.sellgirl.sellgirlPayWeb.pay.model;

import java.math.BigDecimal;

public class vipOrder {

    /**
    * vip_order_id
     */
	public  long vip_order_id ;
	
    /**
    * order_no
     */
	public  String order_no ;
	
//    /**
//    * user_id
//     */
//	public  long user_id ;
//	
//    /**
//    * vip_type
//     */
//	public  Integer vip_type ;
//	
    /**
    * amount
     */
	public  BigDecimal amount ;
//	
//    /**
//    * status
//     */
//	public  Integer status ;
//	
//    /**
//    * pay_time
//     */
//	public  SGDate pay_time ;
//	
//    /**
//    * transaction_id
//     */
//	public  String transaction_id ;
//	
//    /**
//    * expire_time
//     */
//	public  SGDate expire_time ;
//	
//    /**
//    * create_time
//     */
//	public  SGDate create_time ;
//	
	public  long getVip_order_id (){
	  return vip_order_id;
	}
	public  void setVip_order_id (long vip_order_id){
	  this.vip_order_id=vip_order_id;
	}
	public  String getOrder_no (){
	  return order_no;
	}
	public  void setOrder_no (String order_no){
	  this.order_no=order_no;
	}
//	public  long getUser_id (){
//	  return user_id;
//	}
//	public  void setUser_id (long user_id){
//	  this.user_id=user_id;
//	}
//	public  Integer getVip_type (){
//	  return vip_type;
//	}
//	public  void setVip_type (Integer vip_type){
//	  this.vip_type=vip_type;
//	}
	public  BigDecimal getAmount (){
	  return amount;
	}
	public  void setAmount (BigDecimal amount){
	  this.amount=amount;
	}
//	public  Integer getStatus (){
//	  return status;
//	}
//	public  void setStatus (Integer status){
//	  this.status=status;
//	}
//	public  SGDate getPay_time (){
//	  return pay_time;
//	}
//	public  void setPay_time (SGDate pay_time){
//	  this.pay_time=pay_time;
//	}
//	public  String getTransaction_id (){
//	  return transaction_id;
//	}
//	public  void setTransaction_id (String transaction_id){
//	  this.transaction_id=transaction_id;
//	}
//	public  SGDate getExpire_time (){
//	  return expire_time;
//	}
//	public  void setExpire_time (SGDate expire_time){
//	  this.expire_time=expire_time;
//	}
//	public  SGDate getCreate_time (){
//	  return create_time;
//	}
//	public  void setCreate_time (SGDate create_time){
//	  this.create_time=create_time;
//	}
}
