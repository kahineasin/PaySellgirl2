package com.sellgirl.sellgirlPayWeb.product.model;


import java.math.BigDecimal;
import java.util.Date;
import com.sellgirl.sgJavaHelper.SGDate;

/**
* 资源
*/
public class resource {
        /**
        * resource_id
         */
		public  int resource_id ;
		
        /**
        * resource_name
         */
		public  String resource_name ;
		
        /**
        * resource_author
         */
		public  String resource_author ;
		
        /**
        * cover
         */
		public  String cover ;
		public  String description ;
		
		/**
        * netdisk
         */
		public  String netdisk ;
		
        /**
        * extractCode
         */
		public  String extractCode ;
		
        /**
        * unlockPassword
         */
		public  String unlockPassword ;
		
        /**
        * duration
         */
		public  int duration ;
		
        /**
        * size
         */
		public  int size ;
		
        /**
        * create_date
         */
		public  SGDate create_date ;
		
				public  int getResource_id (){
		  return resource_id;
		}
		public  void setResource_id (int resource_id){
		  this.resource_id=resource_id;
		}
		public  String getResource_name (){
		  return resource_name;
		}
		public  void setResource_name (String resource_name){
		  this.resource_name=resource_name;
		}
		public  String getResource_author (){
		  return resource_author;
		}
		public  void setResource_author (String resource_author){
		  this.resource_author=resource_author;
		}
		public  String getCover (){
		  return cover;
		}
		public  void setCover (String cover){
		  this.cover=cover;
		}

        public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public  String getNetdisk (){
		  return netdisk;
		}
		public  void setNetdisk (String netdisk){
		  this.netdisk=netdisk;
		}
		public  String getExtractCode (){
		  return extractCode;
		}
		public  void setExtractCode (String extractCode){
		  this.extractCode=extractCode;
		}
		public  String getUnlockPassword (){
		  return unlockPassword;
		}
		public  void setUnlockPassword (String unlockPassword){
		  this.unlockPassword=unlockPassword;
		}
		public  int getDuration (){
		  return duration;
		}
		public  void setDuration (int duration){
		  this.duration=duration;
		}
		public  int getSize (){
		  return size;
		}
		public  void setSize (int size){
		  this.size=size;
		}
		public  SGDate getCreate_date (){
		  return create_date;
		}
		public  void setCreate_date (SGDate create_date){
		  this.create_date=create_date;
		}
}


