create database amuse;
use amuse;
create table sys_user(
id BIGINT primary key not null auto_increment,
uuid char(36),
username char(36) not null,
password char(36) not null,
forget_digest char(128),
access_level int not null,
role_id BIGINT,
user_profile_id BIGINT,
created_at timestamp not null,
updated_at timestamp not null
 );

create table sys_role(
id BIGINT primary key not null auto_increment,
name char(36),
created_at timestamp not null
);

create table sys_user_profile(
id BIGINT primary key not null auto_increment,
avatar char(128),
email char(128),
first_name char(32),
last_name char(32),
nick_name char(32),
hobby char(128),
created_at timestamp not null,
updated_at timestamp not null
 );

create table sys_user_friend (
id BIGINT primary key not null auto_increment,
owner_uuid  char(36) not null,
friend_uuid  char(36) not null,
friend_remark char(36) null
 );

create table sys_user_block (
id BIGINT primary key not null auto_increment,
owner_uuid  char(36) not null,
block_uuid  char(36) not null
 );

create table sys_message (
id BIGINT primary key not null auto_increment,
from_id BIGINT,
to_id BIGINT,
to_type_id BIGINT,
msg_type_id INT,
msg_id BIGINT,
created_at timestamp not null
);

create table sys_text_message (
id BIGINT primary key not null auto_increment,
msg char(255),
created_at timestamp not null
);

create table sys_image_message (
id BIGINT primary key not null auto_increment,
url char(128),
filename char(36),
secrete char(64)
);

create table sys_audio_message (
id BIGINT primary key not null auto_increment,
url char(128),
filename char(36),
length int,
secrete char(64),
created_at timestamp not null
);

ALTER TABLE sys_user_profile ADD CONSTRAINT fk_user_id 
FOREIGN KEY (user_id) 
REFERENCES sys_user(id);

INSERT INTO sys_user(`uuid`,`username`,`password`,`access_level`) VALUES(uuid(),'test','123456',1);# 1 row affected.
INSERT INTO sys_user(`uuid`,`username`) VALUES(uuid(),'test1');# 1 row affected.

INSERT INTO sys_user_profile(`avatar`,`email`,`first_name`,`last_name`) VALUES('test1','test1@test.com','a1','b1');

UPDATE sys_user SET user_profile_id = '1' WHERE id = '1';