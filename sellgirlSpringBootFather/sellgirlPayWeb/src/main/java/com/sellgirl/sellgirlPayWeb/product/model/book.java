package com.sellgirl.sellgirlPayWeb.product.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.sellgirl.sgJavaHelper.SGDate;

/**
* 书
*/
public class book {
        /**
        * book_id
         */
		public  int book_id ;
		
        /**
        * book_name
         */
		public  String book_name ;
		
        /**
        * book_author
         */
		public  String book_author ;

		public  String letter ;
        public String getLetter() {
			return letter;
		}
		public void setLetter(String letter) {
			this.letter = letter;
		}
        /**
        * cover
         */
		public  String cover ;
//		
//        /**
//        * create_date
//         */
//		public  SGDate create_date ;
//		
				public  int getBook_id (){
		  return book_id;
		}
		public  void setBook_id (int book_id){
		  this.book_id=book_id;
		}
		public  String getBook_name (){
		  return book_name;
		}
		public  void setBook_name (String book_name){
		  this.book_name=book_name;
		}
		public  String getBook_author (){
		  return book_author;
		}
		public  void setBook_author (String book_author){
		  this.book_author=book_author;
		}
		public  String getCover (){
		  return cover;
		}
		public  void setCover (String cover){
		  this.cover=cover;
		}
//		public  SGDate getCreate_date (){
//		  return create_date;
//		}
//		public  void setCreate_date (SGDate create_date){
//		  this.create_date=create_date;
//		}
}