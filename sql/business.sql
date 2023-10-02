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
)comment = '车次';

DROP TABLE IF EXISTS `train_station`;
CREATE TABLE `train_station` (
                                 `id` bigint NOT NULL COMMENT 'id',
                                 `train_id` bigint NOT NULL COMMENT '车次id',
                                 `train_index` int NOT NULL COMMENT '车站序号',
                                 `station_name` varchar(20) NOT NULL COMMENT '站名',
                                 `name_pinyin` varchar(50) NOT NULL COMMENT '站名拼音',
                                 `in_time` time DEFAULT NULL COMMENT '进站时间',
                                 `out_time` time DEFAULT NULL COMMENT '出站时间',
                                 `stop_time` time DEFAULT NULL COMMENT '停站时间',
                                 `km` decimal(8,2) NOT NULL COMMENT '里程（公里）| 从上一站到本站的距离',
                                 `create_time` datetime(3) DEFAULT NULL COMMENT '新增时间',
                                 `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `train_code` (`train_id`,`train_index`),
                                 UNIQUE KEY `train_code_2` (`train_id`,`station_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='火车车站';

DROP TABLE  IF EXISTS `train_carriage`;

create table `train_carriage` (
    `id` bigint not null   primary key comment 'id',
    `train_id` bigint NOT NULL COMMENT '车次id',
    `train_index` int not null comment '火车箱号',
    `seat_type` char not null comment '座位类型|枚举',
    `seat_count` int not null comment '排数',
    `row_count` int not null comment '排数',
    `column_count` int not null comment '列数',
    `create_time` datetime(3) DEFAULT NULL COMMENT '新增时间',
    `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
    unique (`train_id`,`train_index`)
)COMMENT='车厢';

DROP TABLE IF EXISTS `train_seat`;
create table `train_seat`(
    `id` bigint not null comment 'id',
    `train_id` bigint NOT NULL COMMENT '车次id',
    `carriage_id` bigint not null comment '火车箱号',
    `seat_type` char not null comment '座位类型|枚举',
    `seat_row` char(2) not null comment '排',
    `seat_col` VARCHAR(10) not null comment '列|枚举',
    `carriage_seat_index` int not null comment '同车厢座序',
    `create_time` datetime(3) DEFAULT NULL COMMENT '新增时间',
    `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间'
)COMMENT='座位';

drop table if exists `daily_train`;
create table `daily_train` (
                         `id` bigint not null primary key comment 'id' ,
                         `start_date` date not null comment '发车日期',
                         `code` varchar(20)  not null  comment '车次编号',
                         `type` char(1) not null comment '车次类型|枚举',
                         `start` varchar(20) not null comment '始发站',
                         `start_pinyin` varchar(50) not null comment '始发站拼音',
                         `start_time` time not null  comment '出发时间',
                         `end` varchar(20) not null comment '终点站',
                         `end_pinyin` varchar(50) not null comment '终点站拼音',
                         `end_time` time not null  comment '到终点站时间',
                         `interval_day` int not null comment '出发和到站之间间隔的天数',
                         `create_time` datetime(3) comment '创建时间',
                         `update_time` datetime(3) comment '修改时间',
                         unique (`code`,`start_date`)
)comment = '每日车次';


DROP TABLE IF EXISTS `daily_train_station`;
CREATE TABLE `daily_train_station` (
                                 `id` bigint NOT NULL COMMENT 'id',
                                 `daily_train_id` bigint NOT NULL COMMENT '车次id',
                                 `train_index` int NOT NULL COMMENT '车站序号',
                                 `station_name` varchar(20) NOT NULL COMMENT '站名',
                                 `name_pinyin` varchar(50) NOT NULL COMMENT '站名拼音',
                                 `in_time` time DEFAULT NULL COMMENT '进站时间',
                                 `out_time` time DEFAULT NULL COMMENT '出站时间',
                                 `stop_time` time DEFAULT NULL COMMENT '停站时间',
                                 `km` decimal(8,2) NOT NULL COMMENT '里程（公里）| 从上一站到本站的距离',
                                 `create_time` datetime(3) DEFAULT NULL COMMENT '新增时间',
                                 `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `train_code` (`daily_train_id`,`train_index`),
                                 UNIQUE KEY `train_code_2` (`daily_train_id`,`station_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='每日车站';

