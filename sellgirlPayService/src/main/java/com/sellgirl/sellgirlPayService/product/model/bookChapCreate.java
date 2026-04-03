package com.sellgirl.sellgirlPayService.product.model;

import java.math.BigDecimal;
import java.util.Date;

import com.sellgirl.sgJavaHelper.SGDate;

/**
* 章节
*/
public class bookChapCreate {
//        /**
//        * book_chap_id
//         */
//		public  int book_chap_id ;
		
        /**
        * book_chap_name
         */
		public  String book_chap_name ;
		
        /**
        * book_id
         */
		public  int book_id ;
		
        /**
        * content
         */
		public  String content ;
		
        /**
        * create_date
         */
		public  SGDate create_date ;
		
//				public  int getBook_chap_id (){
//		  return book_chap_id;
//		}
//		public  void setBook_chap_id (int book_chap_id){
//		  this.book_chap_id=book_chap_id;
//		}
		public  String getBook_chap_name (){
		  return book_chap_name;
		}
		public  void setBook_chap_name (String book_chap_name){
		  this.book_chap_name=book_chap_name;
		}
		public  int getBook_id (){
		  return book_id;
		}
		public  void setBook_id (int book_id){
		  this.book_id=book_id;
		}
		public  String getContent (){
		  return content;
		}
		public  void setContent (String content){
		  this.content=content;
		}
		public  SGDate getCreate_date (){
		  return create_date;
		}
		public  void setCreate_date (SGDate create_date){
		  this.create_date=create_date;
		}
}


