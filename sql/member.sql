#会员表的结构  会员id 与电话号码
use train_member;
drop table if exists `member`;

create table `member`(
     `id` bigint not null primary key  comment 'ID' ,
     `mobile` varchar(11) unique not null comment '手机号'
);

#乘客表 ,乘客与会员关联，会员为乘客购买车票
drop table if exists `passenger`;

create table `passenger` (
    `id` bigint not null  primary key comment 'id',
    `member_id` bigint not null comment '会员id',
    `name` varchar(20) not null comment '乘车人姓名',
    `id_card` varchar(18) not null comment '身份证',
    `type` char(1) not null comment '旅客类型|枚举',
    `create_time` datetime(3) not null comment '创建时间',
    `update_time` datetime(3) not null comment '修改时间',#精确到毫秒
    index(`member_id`),#创建索引便于查找
    unique (`member_id`,`id_card`)
)comment '乘客';

drop table if exists `ticket`;
create table `ticket` (
                          `id` bigint not null comment 'id',
                          `member_id` bigint not null comment '会员id',
                          `passenger_id` bigint not null comment '乘客id',
                          `daily_train_ticket_id` bigint not null  comment '车次票的ID',
                          `passenger_name` varchar(20) comment '乘客姓名',
                          `train_date` date not null comment '日期',
                          `train_code` varchar(20) not null comment '车次编号',
                          `carriage_index` int not null comment '箱序',
                          `seat_row` char(2) not null comment '排号|01, 02',
                          `seat_col` varchar(20) not null comment '列号|枚举[SeatColEnum]',
                          `start_station` varchar(20) not null comment '出发站',
                          `start_time` time not null comment '出发时间',
                          `end_station` varchar(20) not null comment '到达站',
                          `end_time` time not null comment '到站时间',
                          `seat_type` char(1) not null comment '座位类型|枚举[SeatTypeEnum]',
                          `create_time` datetime(3) comment '新增时间',
                          `update_time` datetime(3) comment '修改时间',
                          primary key (`id`),
                          index `member_id_index` (`member_id`)
) engine=innodb default charset=utf8mb4 comment='车票';

drop table if exists `undo_log`;
-- 注意此处0.7.0+ 增加字段 context
CREATE TABLE `undo_log` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `branch_id` bigint(20) NOT NULL,
                            `xid` varchar(100) NOT NULL,
                            `context` varchar(128) NOT NULL,
                            `rollback_info` longblob NOT NULL,
                            `log_status` int(11) NOT NULL,
                            `log_created` datetime NOT NULL,
                            `log_modified` datetime NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;