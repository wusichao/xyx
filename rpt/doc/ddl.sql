
-- 活动表
create table if not exists `campaign`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`account_id` bigint(20) NOT NULL, 
	`name` varchar(60) NOT NULL,
	`begin_date` date NOT NULL,
	`end_date` date NOT NULL,
	`cost_type` varchar(50) NOT NULL, -- 结算方式 CPM CPC CPL CPA
	`target_url` varchar(2048) NOT NULL, -- 活动地址
	`creation` datetime NOT NULL,  -- 创建时间
	`removed` bit(1) NOT NULL DEFAULT b'0',
	`last_modified` datetime NOT NULL,
	PRIMARY key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 渠道表
create table if not exists `channel`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL,
	`creation` datetime NOT NULL,
	`removed` bit(1) NOT NULL DEFAULT b'0',
	`last_modified` datetime NOT NULL,
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 媒体表
create table if not exists `media`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL,
	`creation` datetime NOT NULL,
	`removed` bit(1) NOT NULL DEFAULT b'0',
	`last_modified` datetime NOT NULL,
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 注册公司表，公司名称唯一
create table if not exists `company`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`name` varchar(64) NOT NULL,
	PRIMARY key (`id`),
	UNIQUE KEY `name` (`name`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
-- 账户表
create table if not exists `account`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`email` varchar(50) NOT NULL, -- 邮箱账号
	`password` varchar(150) NOT NULL, 
	`company_id` bigint(20) NOT NULL,
	`vertical` varchar(255) NOT NULL,
	`web_site` varchar(2048) NULL,
	`contact` varchar(50) NOT NULL,
	`cellphone` varchar(32) NOT NULL,
	`license_path` varchar(255) NOT NULL,  -- 营业执照,文件存储在服务器，此处是文件路径
	`invitation_code` varchar(40) NOT NULL, -- 邀请码
	`status` varchar(20) NOT NULL,  -- 审核状态 NOT_CHECKED APPROVED DISAPPROVED 
	`approved` bit(1) DEFAULT b'0', -- 是否审核通过
	`active` bit(1) DEFAULT b'0', -- 是否激活
	PRIMARY KEY `id` (`id`),
	CONSTRAINT `FK_AC_CO` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 账户-渠道关联表campaign
create table if not exists `account_channel_rel`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`account_id` bigint(20) NOT NULL,
	`channel_id` bigint(20) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `FK_AC_CH_REL_CHANNEL` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`),
	CONSTRAINT `FK_AC_CH_REL_ACCOUNT` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 账户-媒体关联表
create table if not exists `account_media_rel`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`account_id` bigint(20) NOT NULL,
	`media_id` bigint(20) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `FK_AC_ME_REL_ACCOUNT` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
	CONSTRAINT `FK_AC_ME_REL_MEDIA` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 活动-渠道关联表
create table if not exists `campaign_channel_rel`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`campaign_id` bigint(20) NOT NULL,
	`channel_id` bigint(20) NOT NULL,
	PRIMARY key (`id`),
	CONSTRAINT `FK_CA_CH_REL_CAMPAIGN` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`),
	CONSTRAINT `FK_CA_CH_REL_CHANNEL` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 活动-媒体关联表
create table if not exists `campaign_media_rel`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`campaign_id` bigint(20) NOT NULL,
	`media_id` bigint(20) NOT NULL,
	PRIMARY key (`id`),
	CONSTRAINT `FK_CA_ME_REL_CAMPAIGN` FOREIGN KEY (`campaign_id`) REFERENCES `campaign` (`id`),
	CONSTRAINT `FK_CA_ME_REL_MEDIA` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 基础报表
-- 转化漏斗，维度排名(渠道报表，创意报表) 查询此表
create table if not exists `rpt_base`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`imp` bigint(20) NOT NULL,
	`click` bigint(20) NOT NULL,
	`reach` bigint(20) NOT NULL,
	`conversion` bigint(20) NOT NULL,
	`cost` decimal(19,8) NOT NULL DEFAULT '0.00000000',
	`campaign_id` bigint(20) NOT NULL,
	`channel_id` bigint(20) NOT NULL,
	`creative_id` bigint(20) NOT NULL,
	`day` date NOT NULL,
	`creation` datetime NOT NULL,
	`removed` bit(1) NOT NULL DEFAULT b'0',
	PRIMARY KEY (`day`,`campaign_id`,`channel_id`,`creative_id`),
	KEY `id` (`id`),
	KEY `day` (`day`),
	KEY `campaign_id` (`campaign_id`),
	KEY `channel_id` (`channel_id`),
	KEY  `creative_id` (`creative_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 域名维度报表，可能会拆分到单独数据库，先分离出来
-- 维度排名 ，媒体报表
create table if not exists `rpt_media`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`imp` bigint(20) NOT NULL,
	`click` bigint(20) NOT NULL,
	`reach` bigint(20) NOT NULL,
	`conversion` bigint(20) NOT NULL,
	`cost` decimal(19,8) NOT NULL DEFAULT '0.00000000',
	`campaign_id` bigint(20) NOT NULL,
	`media_id` bigint(20) NOT NULL,
	`day` date NOT NULL,
	`creation` datetime NOT NULL,
	`removed` bit(1) NOT NULL DEFAULT b'0',
	PRIMARY KEY (`day`,`campaign_id`,`domain`),
	KEY `id` (`id`),
	KEY `day` (`day`),
	KEY `domain` (`domain`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 维度排名，地域报表
create table `rpt_geo`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`imp` bigint(20) NOT NULL,
	`click` bigint(20) NOT NULL,
	`reach` bigint(20) NOT NULL,
	`conversion` bigint(20) NOT NULL,
	`cost` decimal(19,8) NOT NULL DEFAULT '0.00000000',
	`campaign_id` bigint(20) NOT NULL,
	`geo_id` bigint(20) NOT NULL,
	`day` date NOT NULL,
	`creation` datetime NOT NULL,
	`removed` bit(1) NOT NULL DEFAULT b'0',
	PRIMARY KEY (`day`,`campaign_id`,`geo_id`),
	KEY `id` (`id`),
	KEY `day` (`day`),
	KEY `campaign_id` (`campaign_id`),
	KEY `geo_id` (`geo_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 维度排名，小时报表
create table `rpt_hour`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`imp` bigint(20) NOT NULL,
	`click` bigint(20) NOT NULL,
	`reach` bigint(20) NOT NULL,
	`conversion` bigint(20) NOT NULL,
	`cost` decimal(19,8) NOT NULL DEFAULT '0.00000000',
	`campaign_id` bigint(20) NOT NULL,
	`day` date NOT NULL,
	`hour` int(11) NOT NULL,
	`creation` datetime NOT NULL,
	`removed` bit(1) NOT NULL DEFAULT b'0',
	PRIMARY KEY (`day`,`campaign_id`,`hour`),
	KEY `id` (`id`),
	KEY `day` (`day`),
	KEY `campaign_id` (`campaign_id`),
	KEY `hour` (`hour`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 点击日志详情表
	
CREATE TABLE `click_log` (
  `action_id` varchar(255) NOT NULL,
  `action_pre_id` varchar(255) DEFAULT NULL,
  `action_type` varchar(255) NOT NULL,
  `req_time` datetime NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `account_id` bigint(20) NOT NULL,
  `campaign_id` bigint(20) NOT NULL,
  `channel_id` bigint(20) NOT NULL,
  `creative_id` bigint(20) NOT NULL,
  `media_id` bigint(20) NOT NULL,
  `cvt_id` bigint(20) NOT NULL COMMENT '点击转化点，日志记录多个时，拆分成多条记录'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;