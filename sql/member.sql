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
    `create_time` datetime not null comment '创建时间',
    `update_time` datetime not null comment '修改时间',
    index(`member_id`)#创建索引便于查找
)comment '乘客'