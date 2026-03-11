create database sgshop;
use sgshop;
CREATE TABLE IF NOT EXISTS `sg_user`(
   `user_id` INT UNSIGNED AUTO_INCREMENT,
   `user_name` VARCHAR(100) NOT null unique,
   `pwd` VARCHAR(20) ,
   `invitation_code` VARCHAR(20) ,
   `email` VARCHAR(40) ,
   `sign_day` INT not null default 0,   
   `last_sign` datetime,
   `create_date` datetime,
   PRIMARY KEY ( `user_id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `sg_book`(
   `book_id` INT UNSIGNED AUTO_INCREMENT,
   `book_name` VARCHAR(100) NOT null unique,
   `book_author` VARCHAR(20) ,
   `letter` VARCHAR(10) ,
   `cover` VARCHAR(20) ,
   `create_date` datetime,
   PRIMARY KEY ( `book_id` ),
   INDEX  idx_letter (`letter`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sg_book_chap`(
   `book_chap_id` INT UNSIGNED AUTO_INCREMENT,
   `book_chap_name` VARCHAR(100) NOT null ,
   `book_id` INT ,
   `content` TEXT ,
   `create_date` datetime,
   PRIMARY KEY ( `book_chap_id` ),
   INDEX  idx_book_id (`book_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- use sellgirlbbs2
-- drop table sg_user;
-- drop table sg_book;
-- drop table sg_book_chap;
delete from sg_book;
delete from sg_book_chap;
  select * from sgshop.sg_user;
  select * from sgshop.sg_book;
  select * from sgshop.sg_book_chap;
select * from sg_book  where  `letter`='X'  
select * from sg_book  where  `letter`='X'  
select  * from sg_book  where  `book_id`=10 limit 1;
  select max(book_id) from sgshop.sg_book; 
-- INSERT INTO sg_user
-- ( user_name,pwd, invitation_code,email,  create_date)
-- VALUES( 'cc','cc', 'cc', 'cc', '2026-01-01 01:01:01');
-- 
-- select * from user  where  `user_name`='ddd'
-- 
--  select version()

-- UPDATE accounts SET balance = balance - 100 WHERE id = 1;


  select max(book_id) from sgshop.sg_book
  select * from sg_book where book_name='人间烟火' -- 'äººé—´çƒŸç�«' -- '二马'
  select * from sg_book where book_id=239
  delete from sgshop.sg_book where book_name='人间烟火'
  select * from sgshop.sg_book_chap where book_id=999 -- 224
  delete from sgshop.sg_book_chap where book_id=999
  
  select a.book_name,b.* from sg_book a
  left join sg_book_chap b on b.book_id=a.book_id
  
  
  where a.book_name='今日简史'
  
  show variables like 'character_set%'