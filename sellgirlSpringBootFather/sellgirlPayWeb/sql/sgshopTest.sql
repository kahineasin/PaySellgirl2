use sgshop;
-- ------- 更新表-------
alter table sg_user add column point INT not null default 0; 
alter table sg_user drop column point; 
  select *  from sgshop.sg_user

CREATE TABLE IF NOT EXISTS `sg_resource_img`(
   `resource_img_id` INT UNSIGNED AUTO_INCREMENT,
   `url` VARCHAR(100) NOT null unique,
   `resource_id` INT ,
   `create_date` datetime,
   PRIMARY KEY ( `resource_img_id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
select * from sg_book limit 20
-- use sellgirlbbs2
-- drop table sg_user;
-- drop table sg_book;
-- drop table sg_book_chap;
  -- drop table sg_resource
  -- drop table sg_img
  -- drop table sg_comic
  -- drop table sg_resource_img
delete from sg_book;
delete from sg_book_chap;
  select * from sgshop.sg_user;
  select * from sgshop.sg_book;
  select * from sgshop.sg_book_chap;
select count(1) from sg_book  where  `letter`='X'  
select * from sg_book  where  `letter`='X'  
select  * from sg_book  where  `book_id`=10 limit 1;
  select max(book_id) from sgshop.sg_book; 
select * from sgshop.sg_book limit 1000,10; 
select * from sgshop.sg_book where `book_id`>999 limit 10;
-- drop table sg_user_buy
select * from sg_user_buy where user_id is null
-- delete from sg_user_buy
select user_id from sg_user_buy  where  `user_id`=2  and  `source_id`=6  
user_id from sg_resource  where  `user_id`=2  and  `source_id`=2500  
-- INSERT INTO sg_user
-- ( user_name,pwd, invitation_code,email,  create_date)
-- VALUES( 'cc','cc', 'cc', 'cc', '2026-01-01 01:01:01');
-- 
-- select * from user  where  `user_name`='ddd'
-- 
--  select version()

-- UPDATE accounts SET balance = balance - 100 WHERE id = 1;

  select *  from sgshop.sg_user where user_id=1
  update sgshop.sg_user set sign_day=5,last_sign='2026-01-01 01:01:01' where user_id=2
update sgshop.sg_user set point=point+1000 where user_id=2
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
  select * from sgshop.sg_user order by user_id desc;
  select * from sgshop.sg_user limit 20;
  select * from sgshop.sg_user where  user_id =1000 limit 1
    select count(*) 
    -- delete
    from sgshop.sg_user where create_date >'2026-04-01 1:42:44.0'
  delete from sgshop.sg_user where 
  update sgshop.sg_user set sign_day=19,last_sign='2026-03-30 17:37:26.0' where user_id=2
  
  select status from sg_vip_order  where  `vip_order_id`=2  limit 1
    select * from sg_vip_order order by create_time desc limit 20
  
   update sg_user set  `vip1`= 0 ,`vip1_expire`= '2026-03-19 00:07:14' ,`vip2`= 0 ,`vip2_expire`= '2026-03-19 00:07:14'   where  `user_id`=1  limit 1
   update sg_user set point=point+1 where user_id=1
   update sg_user set invitation_code='' where user_id=1
   select user_id,user_name,point from sg_user where user_id=1;
   select * from sg_resource where resource_name like '%2420%'
   select * from sg_resource limit 20;
   select * from sg_resource where resource_id=2500 limit 20;
   select max(resource_id),count(*) as m from sg_comic limit 1;
   
   select * from sg_comic limit 20;
   select * from sg_comic where create_date>'2026-04-05 01:16:33' limit 20;
   update sg_comic set create_date='2026-04-03 17:16:33' where resource_id=1
   select * from sg_comic where resource_id>127 limit 100;
   select * from sg_comic where resource_id=423 limit 20;
   select * from sg_comic where resource_id=424 limit 20;
   select * from sg_comic where resource_id=846 limit 20;
   select * from sg_comic where resource_name like '%放工后%'
   select * from sg_comic where '2026-04-04 01:01:01'<create_date
   
   update sg_comic set netdisk='https://pan.baidu.com/s/153A9gYMg6DEiZBYOKMA8-Q',extract_code='4d95' where resource_name='C-No.00423'
   
   select * from sg_img limit 20;
   select * from sg_img where resource_id=334 limit 20;
   
   select * from sg_resource t INNER JOIN(select resource_id from sg_resource order by create_date desc limit 0,10) tmp ON t.resource_id=tmp.resource_id
   
   
   insert into sg_resource (`resource_id`,`resource_name`,`resource_author`,`cover`,`netdisk`,`extract_code`,`unlock_password`,`duration`,`size`,`create_date`) values ( '0' , 'V-No.00013' , '飞鸟与鱼' , '1.jpg,2.jpg,3.jpg' , 'https://pan.baidu.com/s/1KUZFi84x2HiF7EbTsFhykg?pwd=5zqk' , '5zqk' , 'bdhome.xyz' , '0' , '0' , '2026-03-28 00:54:32' )
   
   select * from sg_resource t INNER JOIN(select resource_id from where resource_name like '%a%' or resource_author like '%a%' order by create_date desc limit 0,5) tmp ON t.resource_id=tmp.resource_id
   
   insert into sg_user_buy (`user_id`,`source_type`,`source_id`) values ( '2' ,1, '2500' )