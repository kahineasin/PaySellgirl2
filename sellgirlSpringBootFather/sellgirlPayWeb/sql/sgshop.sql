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


