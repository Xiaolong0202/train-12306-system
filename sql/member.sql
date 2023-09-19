#会员表的结构  会员id 与电话号码
drop table if exists `member`;

create table `member`(
     `id` bigint not null primary key  comment 'ID' ,
     `mobile` varchar(11) unique not null comment '手机号'
)