CREATE TABLE IF NOT EXISTS `sg_user`(
   `user_id` INT UNSIGNED AUTO_INCREMENT,
   `user_name` VARCHAR(100) NOT null unique,
   `pwd` VARCHAR(20) ,
   `invitation_code` VARCHAR(20) ,
   `email` VARCHAR(40) ,
   `sign_day` INT not null default 0,   
   `last_sign` datetime,
    `vip1` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'resource会员',
    `vip1_expire` DATETIME COMMENT '会员有效期截止时间',
    `vip2` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'ebook会员',
    `vip2_expire` DATETIME COMMENT '会员有效期截止时间',
   `point` INT not null default 0 ,
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

CREATE TABLE IF NOT EXISTS `sg_vip_order` (
    `vip_order_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_no` VARCHAR(64) NOT NULL COMMENT '商户订单号，唯一',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `vip_type` TINYINT NOT NULL COMMENT '会员类型：1-月度，2-季度，3-年度',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额（元）',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0-待支付，1-已支付，2-已取消',
    `pay_time` DATETIME COMMENT '支付时间',
    `transaction_id` VARCHAR(128) COMMENT '第三方支付流水号',
    `expire_time` DATETIME COMMENT '会员有效期截止时间',
    `create_time` DATETIME ,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sg_user_buy` (
    `user_buy_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `source_type` TINYINT NOT NULL COMMENT '1:电子书 2:视频',
    `source_id` BIGINT NOT NULL,
    `create_date` DATETIME NOT NULL,
    PRIMARY KEY (`user_buy_id`, `user_id`),   -- 复合主键
   UNIQUE INDEX `idx_user_source` (`user_id`, `source_type`, `source_id`)-- ,
    -- INDEX `idx_user_time` (`user_id`,`create_date`) deepseek建议
) ENGINE=InnoDB DEFAULT CHARSET=utf8
PARTITION BY HASH (user_id)
PARTITIONS 8;   -- 8个分区，根据预估数据量调整



CREATE TABLE IF NOT EXISTS `sg_resource`(
   `resource_id` INT UNSIGNED AUTO_INCREMENT,
   `resource_name` VARCHAR(100) NOT null,
   `resource_author` VARCHAR(20) ,
   `cover` VARCHAR(50) ,
   `description` VARCHAR(100),
   `netdisk` VARCHAR(100) ,
   `extract_code` VARCHAR(20) ,
   `unlock_password` VARCHAR(20) ,
   `duration` INT  COMMENT '时长(秒)/漫画图片数量',
   `size` INT  COMMENT '文件大小KB',
   `create_date` datetime,
   PRIMARY KEY ( `resource_id` ),
   INDEX  idx_create_date (`create_date`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sg_img`(
   `resource_id` INT UNSIGNED AUTO_INCREMENT,
   `resource_name` VARCHAR(100) NOT null,
   `resource_author` VARCHAR(20) ,
   `cover` VARCHAR(50) ,
   `description` VARCHAR(100),
   `netdisk` VARCHAR(100) ,
   `extract_code` VARCHAR(20) ,
   `unlock_password` VARCHAR(20) ,
   `duration` INT  COMMENT '时长(秒)/漫画图片数量',
   `size` INT  COMMENT '文件大小KB',
   `create_date` datetime,
   PRIMARY KEY ( `resource_id` ),
   INDEX  idx_create_date (`create_date`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sg_comic`(
   `resource_id` INT UNSIGNED AUTO_INCREMENT,
   `resource_name` VARCHAR(100) NOT null,
   `resource_author` VARCHAR(20) ,
   `cover` VARCHAR(50) ,
   `description` VARCHAR(100),
   `netdisk` VARCHAR(100) ,
   `extract_code` VARCHAR(20) ,
   `unlock_password` VARCHAR(20) ,
   `duration` INT  COMMENT '时长(秒)/漫画图片数量',
   `size` INT  COMMENT '文件大小KB',
   `create_date` datetime,
   PRIMARY KEY ( `resource_id` ),
   INDEX  idx_create_date (`create_date`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
