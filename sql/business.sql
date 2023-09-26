use train_business;
drop table if exists `station`;
create table `station` (
    `id` bigint not null comment 'id',
    `name` varchar(30) not null comment '站名',
    `name_pinyin` varchar(50) not null  comment '站名拼音',
    `name_py` varchar(50) not null  comment '站名拼音首字母',
    `create_time` datetime(3) comment '创建时间',
    `update_time` datetime(3) comment '更新时间',
    primary key (`id`),
    unique key (`name`)
);

drop table if exists `train`;
create table `train` (
    `id` bigint not null primary key comment 'id' ,
    `code` varchar(20) unique not null  comment '车次编号',
    `type` char(1) not null comment '车次类型|枚举',
    `start` varchar(20) not null comment '始发站',
    `start_pinyin` varchar(50) not null comment '始发站拼音',
    `start_time` time not null  comment '出发时间',
    `end` varchar(20) not null comment '终点站',
    `end_pinyin` varchar(50) not null comment '终点站拼音',
    `end_time` time not null  comment '到终点站时间',
    `interval_day` int not null comment '出发和到站之间间隔的天数',
    `create_time` datetime(3) comment '创建时间',
    `update_time` datetime(3) comment '修改时间'
)comment = '车次'