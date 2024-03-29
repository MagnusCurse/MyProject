-- 创建数据库

drop database if exists mycnblog_plus;
create database mycnblog_plus DEFAULT CHARACTER SET utf8mb4;

-- 使用数据数据

use mycnblog_plus

-- 创建用户表

drop table if exists user_info;
create table user_info(
     id int primary key auto_increment,
     username varchar(100) unique,
     password varchar(64) not null,
     photo varchar(500) default '',
     create_time datetime default now(),
     update_time datetime default now(),
     mail varchar(100) unique,
     nickname varchar(100) unique,
     introduction varchar(500),
     avatar_url varchar(200) default ''
) default charset 'utf8mb4';

-- 创建博客表

drop table if exists blog_info;
create table blog_info(
    id int primary key auto_increment,
    title varchar(100) not null,
    content text not null,
    create_time datetime default now(),
    update_time datetime default now(),
    user_id int not null,
    like_count int default 0,
    view_count int default 0
)default charset 'utf8mb4';

-- 创建评论表

drop table if exists comment_info;
create table comment_info(
    id int primary key auto_increment,
    parent_id int,
    user_id int,
    blog_id int,
    username varchar(100),
    comment varchar(360),
    create_time datetime default now(),
    like_count int,
    replied_username varchar(100)
)default charset 'utf8mb4';

--创建用户点赞表

drop table if exists blog_like;
create table blog_like(
    id int primary key auto_increment,
    liked_blog_id int not null, --     被点赞博客的 id
    liked_post_id int not null, --     点赞该博客用户的 id
    status tinyint(1) default '1', --     点赞状态,0 取消/ 1 点赞
    create_time datetime default now(),
    update_time datetime default now()
)default charset 'utf8mb4';











