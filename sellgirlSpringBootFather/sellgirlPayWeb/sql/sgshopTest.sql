use sgshop;


CREATE TABLE IF NOT EXISTS `sg_vip_user`(
   `vip_user_id` INT UNSIGNED AUTO_INCREMENT,
   `user_id` INT NOT null ,
    `vip` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0-电子书，1-资源',
    `expire_time` DATETIME COMMENT '会员有效期截止时间',
   PRIMARY KEY ( `vip_user_id` ),
    INDEX `idx_user_id` (`user_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 根据 书名 章名 查询
  select a.book_id,a.book_name,b.* from sg_book a
  left join sg_book_chap b on b.book_id=a.book_id
  where a.book_name ='复活' and b.book_chap_name ='二'

-- use sellgirlbbs2
-- drop table sg_user;
-- drop table sg_book;
-- drop table sg_book_chap;
delete from sg_book;
delete from sg_book_chap;
  select * from sgshop.sg_user;
  select * from sgshop.sg_book;
  select * from sgshop.sg_book_chap;
select count(1) from sg_book  where  `letter`='X'  
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

  select *  from sgshop.sg_user

  select max(book_id) from sgshop.sg_book
  select * from sg_book where book_name='人间烟火' -- 'äººé—´çƒŸç�«' -- '二马'
  select * from sg_book where book_id=239
    select * from sg_book limit 20
  delete from sgshop.sg_book where book_name='人间烟火'
  select * from sgshop.sg_book_chap where book_id=999 -- 224
  select count(*) from sgshop.sg_book_chap
  delete from sgshop.sg_book_chap where book_id=999
  
  select a.book_name,b.* from sg_book a
  left join sg_book_chap b on b.book_id=a.book_id
  
  
  where a.book_name='今日简史'
  
  show variables like 'character_set%'
  
  select * from sgshop.sg_vip_order
  select * from sgshop.sg_user;
  
  select status from sg_vip_order  where  `vip_order_id`=2  limit 1
  
   update sg_user set  `vip1`= 0 ,`vip1_expire`= '2026-03-19 00:07:14' ,`vip2`= 0 ,`vip2_expire`= '2026-03-19 00:07:14'   where  `user_id`=1  limit 1