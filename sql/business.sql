use train_business;
drop table if exists `station`;
create table `station`
(
    `id`          bigint      not null comment 'id',
    `name`        varchar(30) not null comment '站名',
    `name_pinyin` varchar(50) not null comment '站名拼音',
    `name_py`     varchar(50) not null comment '站名拼音首字母',
    `create_time` datetime(3) comment '创建时间',
    `update_time` datetime(3) comment '更新时间',
    primary key (`id`),
    unique key (`name`)
);

drop table if exists `train`;
create table `train`
(
    `id`           bigint             not null primary key comment 'id',
    `code`         varchar(20) unique not null comment '车次编号',
    `type`         char(1)            not null comment '车次类型|枚举',
    `start`        varchar(20)        not null comment '始发站',
    `start_pinyin` varchar(50)        not null comment '始发站拼音',
    `start_time`   time               not null comment '出发时间',
    `end`          varchar(20)        not null comment '终点站',
    `end_pinyin`   varchar(50)        not null comment '终点站拼音',
    `end_time`     time               not null comment '到终点站时间',
    `interval_day` int                not null comment '出发和到站之间间隔的天数',
    `create_time`  datetime(3) comment '创建时间',
    `update_time`  datetime(3) comment '修改时间'
) comment = '车次';

DROP TABLE IF EXISTS `train_station`;
CREATE TABLE `train_station`
(
    `id`           bigint        NOT NULL COMMENT 'id',
    `train_id`     bigint        NOT NULL COMMENT '车次id',
    `train_index`  int           NOT NULL COMMENT '车站序号',
    `station_name` varchar(20)   NOT NULL COMMENT '站名',
    `name_pinyin`  varchar(50)   NOT NULL COMMENT '站名拼音',
    `in_time`      time        DEFAULT NULL COMMENT '进站时间',
    `out_time`     time        DEFAULT NULL COMMENT '出站时间',
    `stop_time`    time        DEFAULT NULL COMMENT '停站时间',
    `km`           decimal(8, 2) NOT NULL COMMENT '里程（公里）| 从上一站到本站的距离',
    `create_time`  datetime(3) DEFAULT NULL COMMENT '新增时间',
    `update_time`  datetime(3) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `train_code` (`train_id`, `train_index`),
    UNIQUE KEY `train_code_2` (`train_id`, `station_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='火车车站';

DROP TABLE IF EXISTS `train_carriage`;
create table `train_carriage`
(
    `id`           bigint not null primary key comment 'id',
    `train_id`     bigint NOT NULL COMMENT '车次id',
    `train_index`  int    not null comment '火车箱号',
    `seat_type`    char   not null comment '座位类型|枚举',
    `seat_count`   int    not null comment '排数',
    `row_count`    int    not null comment '排数',
    `column_count` int    not null comment '列数',
    `create_time`  datetime(3) DEFAULT NULL COMMENT '新增时间',
    `update_time`  datetime(3) DEFAULT NULL COMMENT '修改时间',
    unique (`train_id`, `train_index`)
) COMMENT ='车厢';

DROP TABLE IF EXISTS `train_seat`;
create table `train_seat`
(
    `id`                  bigint      not null primary key comment 'id',
    `train_id`            bigint      NOT NULL COMMENT '车次id',
    `carriage_id`         bigint      not null comment '火车箱号',
    `seat_type`           char        not null comment '座位类型|枚举',
    `seat_row`            char(2)     not null comment '排',
    `seat_col`            VARCHAR(10) not null comment '列|枚举',
    `carriage_seat_index` int         not null comment '同车厢座序',
    `create_time`         datetime(3) DEFAULT NULL COMMENT '新增时间',
    `update_time`         datetime(3) DEFAULT NULL COMMENT '修改时间'
) COMMENT ='座位';

drop table if exists `daily_train`;
create table `daily_train`
(
    `id`           bigint      not null primary key comment 'id',
    `start_date`   date        not null comment '发车日期',
    `code`         varchar(20) not null comment '车次编号',
    `type`         char(1)     not null comment '车次类型|枚举',
    `start`        varchar(20) not null comment '始发站',
    `start_pinyin` varchar(50) not null comment '始发站拼音',
    `start_time`   time        not null comment '出发时间',
    `end`          varchar(20) not null comment '终点站',
    `end_pinyin`   varchar(50) not null comment '终点站拼音',
    `end_time`     time        not null comment '到终点站时间',
    `interval_day` int         not null comment '出发和到站之间间隔的天数',
    `create_time`  datetime(3) comment '创建时间',
    `update_time`  datetime(3) comment '修改时间',
    unique (`code`, `start_date`)
) comment = '每日车次';


DROP TABLE IF EXISTS `daily_train_station`;
CREATE TABLE `daily_train_station`
(
    `id`             bigint        NOT NULL COMMENT 'id',
    `daily_train_id` bigint        NOT NULL COMMENT '车次id',
    `train_index`    int           NOT NULL COMMENT '车站序号',
    `station_name`   varchar(20)   NOT NULL COMMENT '站名',
    `name_pinyin`    varchar(50)   NOT NULL COMMENT '站名拼音',
    `in_time`        time        DEFAULT NULL COMMENT '进站时间',
    `out_time`       time        DEFAULT NULL COMMENT '出站时间',
    `stop_time`      time        DEFAULT NULL COMMENT '停站时间',
    `km`             decimal(8, 2) NOT NULL COMMENT '里程（公里）| 从上一站到本站的距离',
    `create_time`    datetime(3) DEFAULT NULL COMMENT '新增时间',
    `update_time`    datetime(3) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `train_code` (`daily_train_id`, `train_index`),
    UNIQUE KEY `train_code_2` (`daily_train_id`, `station_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='每日车站';

DROP TABLE IF EXISTS `daily_train_carriage`;
create table `daily_train_carriage`
(
    `id`             bigint not null primary key comment 'id',
    `daily_train_id` bigint NOT NULL COMMENT '车次id',
    `train_index`    int    not null comment '火车箱号',
    `seat_type`      char   not null comment '座位类型|枚举',
    `seat_count`     int    not null comment '排数',
    `row_count`      int    not null comment '排数',
    `column_count`   int    not null comment '列数',
    `create_time`    datetime(3) DEFAULT NULL COMMENT '新增时间',
    `update_time`    datetime(3) DEFAULT NULL COMMENT '修改时间',
    unique (`daily_train_id`, `train_index`)
) COMMENT ='每日车厢';

DROP TABLE IF EXISTS `daily_train_seat`;
create table `daily_train_seat`
(
    `id`                  bigint       not null primary key comment 'id',
    `daily_train_id`      bigint       NOT NULL COMMENT '每日车次id',
    `carriage_id`         bigint       not null comment '火车箱号',
    `carriage_index`      int          not null comment '所在的车厢',
    `seat_type`           char         not null comment '座位类型|枚举',
    `seat_row`            char(2)      not null comment '排',
    `seat_col`            VARCHAR(10)  not null comment '列|枚举',
    `carriage_seat_index` int          not null comment '同车厢座序',
    `seat_sell`           varchar(100) not null comment '售卖情况|比如100100就是表示第一站与第四站被卖出去了',
    `create_time`         datetime(3) DEFAULT NULL COMMENT '新增时间',
    `update_time`         datetime(3) DEFAULT NULL COMMENT '修改时间'
) COMMENT ='每日座位';

drop table if exists `daily_train_ticket`;
create table `daily_train_ticket`
(
    `id`             bigint        not null comment 'id',
    `daily_train_id` bigint        NOT NULL COMMENT '每日车次id',
    `start_date`     date          NOT NULL COMMENT '出发日期',
    `start`          varchar(20)   not null comment '出发站',
    `start_pinyin`   varchar(50)   not null comment '出发站拼音',
    `start_time`     time          not null comment '出发时间',
    `start_index`    int           not null comment '出发站序|本站是整个车次的第几站',
    `end`            varchar(20)   not null comment '到达站',
    `end_pinyin`     varchar(50)   not null comment '到达站拼音',
    `end_time`       time          not null comment '到站时间',
    `end_index`      int           not null comment '到站站序|本站是整个车次的第几站',
    `ydz`            int           not null comment '一等座余票',
    `ydz_price`      decimal(8, 2) not null comment '一等座票价',
    `edz`            int           not null comment '二等座余票',
    `edz_price`      decimal(8, 2) not null comment '二等座票价',
    `rw`             int           not null comment '软卧余票',
    `rw_price`       decimal(8, 2) not null comment '软卧票价',
    `yw`             int           not null comment '硬卧余票',
    `yw_price`       decimal(8, 2) not null comment '硬卧票价',
    `create_time`    datetime(3) comment '新增时间',
    `update_time`    datetime(3) comment '修改时间',
    primary key (`id`),
    unique key `date_start_end_unique` (`daily_train_id`, `start`, `end`)
) engine = innodb
  default charset = utf8mb4 comment ='余票信息';

drop table if exists `confirm_order`;
create table `confirm_order`
(
    `id`                    bigint      not null comment 'id',
    `member_id`             bigint      not null comment '会员id',
    `date`                  date        not null comment '日期',
    `train_code`            varchar(20) not null comment '车次编号',
    `start`                 varchar(20) not null comment '出发站',
    `end`                   varchar(20) not null comment '到达站',
    `daily_train_ticket_id` bigint      not null comment '余票ID',
    `tickets`               json        not null comment '车票',
    `status`                char(1)     not null comment '订单状态|枚举[ConfirmOrderStatusEnum]',
    `create_time`           datetime(3) comment '新增时间',
    `update_time`           datetime(3) comment '修改时间',
    primary key (`id`),
    index `date_train_code_index` (`date`, `train_code`)
) engine = innodb
  default charset = utf8mb4 comment ='确认订单';

drop table if exists `train_token`;
create table `train_token`
(
    `id`             bigint      not null comment 'ID',
    `daily_train_id` bigint      NOT NULL COMMENT '每日车次id',
    `start_date`     date        not null COMMENT '发车日期',
    `train_code`     varchar(20) not null COMMENT '列车编号',
    `type`           char(1)     not null comment '车次类型|枚举',
    `token_count`    int         not null COMMENT '令牌余量',
    `create_time`    datetime(3) comment '新增时间',
    `update_time`    datetime(3) comment '修改时间',
    primary key (`id`),
    unique (`daily_train_id`),
    unique (`start_date`, `train_code`)
) engine = innodb
  default charset = utf8mb4 comment ='秒杀令牌';


drop table if exists `undo_log`;
-- 注意此处0.7.0+ 增加字段 context
CREATE TABLE `undo_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20)   NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11)      NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
