-- TODO 下面语句需要在 as-os 数据库运行

alter table as_os_sys_user add column name varchar(255);
alter table as_os_sys_user add column dept_id varchar(64);
alter table as_os_sys_user add column dept_code varchar(255);
alter table as_os_sys_user add column remark varchar(255);
alter table as_os_sys_user add column modifier_user_id varchar(255);
alter table as_os_sys_user add column modify_time datetime;

alter table as_os_sys_menu add column component varchar(255) COMMENT '模块（前端使用）';

alter table as_os_sys_user add column position_id varchar(64) COMMENT '职位ID';
alter table as_os_sys_user add column position_name varchar(255) COMMENT '职位名称';

alter table as_os_sys_log add column user_agent varchar(255) COMMENT '操作用户代理信息';
alter table as_os_sys_log add column request_uri varchar(255) COMMENT '操作的URI';
alter table as_os_sys_log add column http_method varchar(255) COMMENT 'http的请求方式';
alter table as_os_sys_log add column del_flag varchar(1) COMMENT '删除标记';

update as_os_sys_log set del_flag=0 where 1=1;

-- 初始化部分数据，比如权限等等
-- 大菜单文件夹
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'账户管理',null,null,'0',null,8);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'设备管理',null,null,'0',null,9);

-- 下面是具体的菜单
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='账户管理' ),'SAAS租户','tenant/sys-tenant',null,'1',null,1);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='账户管理' ),'项目','ibrs/as-project',null,'1',null,2);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'附件上传','/asuser/sys-attachment',null,'1',null,10);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备管理' ),'设备列表','ibrs/as-device',null,'1',null,1);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备管理' ),'设备种类','ibrs/as-device-kind',null,'1',null,2);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备管理' ),'设备类型','ibrs/as-device-type',null,'1',null,3);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备管理' ),'设备型号','ibrs/as-device-model',null,'1',null,4);

-- 下面是权限
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='SAAS租户' ),'查看',null,'os:tenant:list,os:tenant:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='SAAS租户' ),'保存',null,'os:tenant:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='SAAS租户' ),'修改',null,'os:tenant:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='SAAS租户' ),'删除',null,'os:tenant:delete','2',null,0);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='项目' ),'查看',null,'os:project:list,os:project:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='项目' ),'保存',null,'os:project:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='项目' ),'修改',null,'os:project:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='项目' ),'删除',null,'os:project:delete','2',null,0);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备列表' ),'查看',null,'os:device:list,os:device:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备列表' ),'保存',null,'os:device:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备列表' ),'修改',null,'os:device:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备列表' ),'删除',null,'os:device:delete','2',null,0);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备种类' ),'查看',null,'os:deviceKind:list,os:deviceKind:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备种类' ),'保存',null,'os:deviceKind:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备种类' ),'修改',null,'os:deviceKind:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备种类' ),'删除',null,'os:deviceKind:delete','2',null,0);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备类型' ),'查看',null,'os:deviceType:list,os:deviceType:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备类型' ),'保存',null,'os:deviceType:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备类型' ),'修改',null,'os:deviceType:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备类型' ),'删除',null,'os:deviceType:delete','2',null,0);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备型号' ),'查看',null,'os:deviceModel:list,os:deviceModel:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备型号' ),'保存',null,'os:deviceModel:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备型号' ),'修改',null,'os:deviceModel:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='设备型号' ),'删除',null,'os:deviceModel:delete','2',null,0);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='附件上传' ),'查看',null,'os:attachment:list,os:attachment:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='附件上传' ),'保存',null,'os:attachment:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='附件上传' ),'修改',null,'os:attachment:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='附件上传' ),'删除',null,'os:attachment:delete','2',null,0);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'部门管理','/sys/dept',null,'1',null,11);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='部门管理' ),'查看',null,'os:dept:list,os:dept:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='部门管理' ),'保存',null,'os:dept:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='部门管理' ),'修改',null,'os:dept:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='部门管理' ),'删除',null,'os:dept:delete','2',null,0);


-- 插入 区域管理
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'区域管理','/ibrs/as-region',null,'1',null,11);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='区域管理' ),'查看',null,'os:region:list,os:region:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='区域管理' ),'保存',null,'os:region:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='区域管理' ),'修改',null,'os:region:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='区域管理' ),'删除',null,'os:region:delete','2',null,0);

-- 插入 职位表
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'职位管理','/sys/position',null,'1',null,12);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='职位管理' ),'查看',null,'os:position:list,os:position:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='职位管理' ),'保存',null,'os:position:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='职位管理' ),'修改',null,'os:position:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='职位管理' ),'删除',null,'os:position:delete','2',null,0);

-- 插入 工作岗位
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'岗位管理','/sys/post',null,'1',null,13);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='岗位管理' ),'查看',null,'os:post:list,os:post:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='岗位管理' ),'保存',null,'os:post:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='岗位管理' ),'修改',null,'os:post:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='岗位管理' ),'删除',null,'os:post:delete','2',null,0);

-- 插入 黑名单
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'黑名单管理','/sys/black-list',null,'1',null,14);

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='黑名单管理' ),'查看',null,'os:blackList:list,os:blackList:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='黑名单管理' ),'保存',null,'os:blackList:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='黑名单管理' ),'修改',null,'os:blackList:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='黑名单管理' ),'删除',null,'os:blackList:delete','2',null,0);


-- 插入  地址库
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num,component)
values((select menu_id from as_os_sys_menu temp where `name`='设备管理' ),'地址库','/ibrs/as-address-base',null,'1',null,14,'components/pages/development/addressLibrary');

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='地址库' ),'查看',null,'os:deviceBase:list,os:deviceBase:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='地址库' ),'保存',null,'os:deviceBase:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='地址库' ),'修改',null,'os:deviceBase:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='地址库' ),'删除',null,'os:deviceBase:delete','2',null,0);


-- 反正先删除，如果没有就没有删
delete from as_os_sys_menu where parent_id =(select menu_id from(select menu_id from as_os_sys_menu temp where `name`='banner管理')c ) ;
delete from as_os_sys_menu where name='banner管理';

delete from as_os_sys_menu where parent_id =(select menu_id from(select menu_id from as_os_sys_menu temp where `name`='行业咨询')c ) ;
delete from as_os_sys_menu where name='行业咨询';

delete from as_os_sys_menu where parent_id =(select menu_id from(select menu_id from as_os_sys_menu temp where `name`='行业资讯')c ) ;
delete from as_os_sys_menu where name='行业资讯';


delete from as_os_sys_menu where parent_id =(select menu_id from(select menu_id from as_os_sys_menu temp where `name`='常见故障')c ) ;
delete from as_os_sys_menu where name='常见故障';

delete from as_os_sys_menu where parent_id =(select menu_id from(select menu_id from as_os_sys_menu temp where `name`='产品中心')c ) ;
delete from as_os_sys_menu where name='产品中心';

delete from as_os_sys_menu where parent_id =(select menu_id from(select menu_id from as_os_sys_menu temp where `name`='系统消息')c ) ;
delete from as_os_sys_menu where name='系统消息';

delete from as_os_sys_menu where name='内容管理';

-- 插入内容管理
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num,component)
values(0,'内容管理','/content','os:content','0',null,10,'components/pages/main');

-- 插入产品中心
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num,component)
values((select menu_id from as_os_sys_menu temp where `name`='内容管理' ),'产品中心','/productCenter','os:product','1',null,10,'components/pages/content/productCenter');

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='产品中心' ),'查看',null,'os:product:list,os:product:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='产品中心' ),'保存',null,'os:product:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='产品中心' ),'修改',null,'os:product:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='产品中心' ),'删除',null,'os:product:delete','2',null,0);

-- 插入常见故障
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num,component)
values((select menu_id from as_os_sys_menu temp where `name`='内容管理' ),'常见故障','/malfunction','os:malfunction','1',null,20,'components/pages/content/malfunction');

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='常见故障' ),'查看',null,'os:malfunction:list,os:malfunction:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='常见故障' ),'保存',null,'os:malfunction:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='常见故障' ),'修改',null,'os:malfunction:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='常见故障' ),'删除',null,'os:malfunction:delete','2',null,0);

-- 插入 行业资讯
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num,component)
values((select menu_id from as_os_sys_menu temp where `name`='内容管理' ),'行业资讯','/advisory','os:industry','1',null,30,'components/pages/content/advisory');

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='行业资讯' ),'查看',null,'os:industry:list,os:industry:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='行业资讯' ),'保存',null,'os:industry:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='行业资讯' ),'修改',null,'os:industry:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='行业资讯' ),'删除',null,'os:industry:delete','2',null,0);

-- 插入 banner管理
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num,component)
values((select menu_id from as_os_sys_menu temp where `name`='内容管理' ),'banner管理','/banner','os:banner','1',null,30,'components/pages/content/banner');

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='banner管理' ),'查看',null,'os:banner:list,os:banner:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='banner管理' ),'保存',null,'os:banner:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='banner管理' ),'修改',null,'os:banner:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='banner管理' ),'删除',null,'os:banner:delete','2',null,0);

-- 插入系统消息
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num,component)
values((select menu_id from as_os_sys_menu temp where `name`='系统管理' ),'系统消息','/notice','os:notice','1',null,30,'components/pages/system/notice');

insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统消息' ),'查看',null,'os:notice:list,os:notice:info','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统消息' ),'保存',null,'os:notice:save','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统消息' ),'修改',null,'os:notice:update','2',null,0);
insert into as_os_sys_menu(parent_id,`name`,url,perms,`type`,icon,order_num)
values((select menu_id from as_os_sys_menu temp where `name`='系统消息' ),'删除',null,'os:notice:delete','2',null,0);

-- TODO 下面数据库需要在 as-user 数据库运行

-- 创建生成租户的默认菜单模板
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768064', '个人管理', 'personal', '/personal', '-1', 'md-document', '30', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/main', '/personal', NULL, '个人管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768065', '个人资料', 'personal:message', '/message', '656855295271768064', '', '29', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/personal/message', 'message', NULL, '个人资料', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768066', '修改密码', 'personal:password', '/password', '656855295271768064', '', '30', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/personal/password', 'password', NULL, '修改密码', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768067', '桌面', 'home', '/', '-1', 'dashboard', '0', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '1', 'Layout', '/dashboard', NULL, '首页', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768068', '系统管理', 'sys', '/system', '-1', 'ios-people', '20', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/main', 'system', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768069', '用户管理', 'sys:user', '/dept', '656855295271768068', '', '27', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '1', 'components/pages/member/depts', 'user', NULL, '用户管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768070', '部门与用户', 'sys:dept', '/dept', '656855295271768068', '', '21', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '1', 'components/pages/system/depts', 'dept', NULL, '部门管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768071', '角色与权限', 'sys:role', '/role', '656855295271768068', '', '22', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/system/role', 'role', NULL, '角色管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768073', '新增用户', 'sys:user:add', NULL, '656855295271768069', '', '1', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768074', '删除用户', 'sys:user:del', NULL, '656855295271768069', '', '2', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768075', '修改用户', 'sys:user:edit', NULL, '656855295271768069', '', '3', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768076', '导出用户', 'sys:user:export', NULL, '656855295271768069', '', '4', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768077', '导入用户', 'sys:user:import', NULL, '656855295271768069', '', '5', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768078', '新增部门', 'sys:dept:add', NULL, '656855295271768070', '', '1', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768079', '修改部门', 'sys:dept:edit', NULL, '656855295271768070', '', '2', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768080', '删除部门', 'sys:dept:del', NULL, '656855295271768070', '', '3', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768081', '新增角色', 'sys:role:add', NULL, '656855295271768071', '', '1', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768082', '修改角色', 'sys:role:edit', NULL, '656855295271768071', '', '2', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768083', '删除角色', 'sys:role:del', NULL, '656855295271768071', '', '3', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768084', '分配权限', 'sys:role:auth', NULL, '656855295271768071', '', '4', '1', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', NULL, NULL, NULL, NULL, 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768090', '职位管理', 'sys:position', '/postion', '656855295271768068', NULL, '23', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/system/position', 'position', NULL, '首页', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768091', '岗位管理', 'sys:post', '/post', '656855295271768068', NULL, '24', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/system/post', 'post', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768092', '设备管理', 'device', '/development', '-1', 'md-watch', '10', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/main', 'development', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768096', '区域设备', 'device:list', '/devicelist', '656855295271768092', NULL, '0', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/development/devicelist', 'devicelist', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768097', '区域用户管理', 'sys:regions', '/regions', '656855295271768068', NULL, '26', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '1', 'components/pages/system/regions', 'regions', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768098', '部门与人员', 'sys:deptsRegions', '/deptsRegions', '656855295271768068', NULL, '40', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/system/deptsRegions', 'deptsRegions', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768099', '日志管理', 'sys:log', '/log', '656855295271768068', NULL, '41', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/system/log', 'log', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768102', '报警管理', 'alarm', '/alarmmain', '-1', 'md-thermometer', '4', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/main', 'alarmmain', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768108', '区域负责人', 'alarm:responsible', '/responsible', '656855295271768102', NULL, '5', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/alarmmain/responsible', 'responsible', NULL, '系统管理', 'AS', 'osDemo');
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `remark`, `application_code`, `tenant_code`) VALUES ('656855295271768114', '项目管理', 'sys:project', '/project', '656855295271768068', NULL, '22', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/system/project', '/project', NULL, NULL, 'AS', 'osDemo');

-- 给原来的aswl用户添加一个项目管理菜单
INSERT INTO `sys_menu`(`id`, `name`, `permission`, `url`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_date`, `modifier`, `modify_date`, `del_flag`, `component`, `path`, `redirect`, `is_hidden`, `remark`, `application_code`, `tenant_code`) VALUES ('6568552952717681234', '项目管理', 'sys:project', '/project', (select id from sys_menu m where name='系统管理' and tenant_code='aswl'), NULL, '22', '0', 'admin', '2019-12-18 13:48:26', NULL, NULL, '0', 'components/pages/system/project', '/project', NULL, 0, NULL, 'AS', 'ttt');


-- 给租户表添加数据sql
alter table sys_tenant add column effective_start_time timestamp NULL DEFAULT NULL COMMENT '生效开始时间';
alter table sys_tenant add column effective_end_time timestamp NULL DEFAULT NULL COMMENT '生效结束时间';
alter table sys_tenant add column max_device_count int NULL COMMENT '设备上限数量';
alter table sys_tenant add column valid_time varchar(255) NULL COMMENT '最后的有效使用时长，如果一直没断，就所有时间加起来，如果中途已过期，就按过期后的第一次续费开始算时间，以年月分隔开，每次启用和续费都要算一次';
alter table sys_tenant add column valid_count int NULL COMMENT '续费有效数量';
alter table sys_tenant add column valid_unit varchar(20) NULL COMMENT '续费有效期单位';

-- 添加租户续费日志表
DROP TABLE IF EXISTS `sys_tenant_log`;
CREATE TABLE `sys_tenant_log` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `tenant_code` varchar(255) DEFAULT NULL COMMENT '租户标识',
  `status` varchar(20) DEFAULT NULL COMMENT '状态',
  `creator` varchar(255) DEFAULT NULL,
  `create_date` varchar(64) DEFAULT NULL,
  `valid_count` int(11) DEFAULT NULL COMMENT '续费有效数量',
  `valid_unit` varchar(20) DEFAULT NULL COMMENT '续费有效期单位',
  `effective_start_time` timestamp NULL DEFAULT NULL COMMENT '生效开始时间',
  `effective_end_time` timestamp NULL DEFAULT NULL COMMENT '生效结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='SAAS租户信息记录表';



-- TODO 下面数据库需要在 as-ibrs 数据库运行

-- 局域网 设置默认项目ID （可能已经有了该项目就不用添加了）

-- 这条数据如果存在就会报错
INSERT INTO `as-ibrs`.`as_project`(`project_id`, `project_code`, `project_name`, `project_owner`, `project_des`, `start_at`, `end_at`, `creator_id`, `create_at`, `updater_id`, `update_at`, `remark`, `application_code`, `tenant_code`) VALUES ('1', 'GZ-201911-001', '测试项目', '测试业主单位', '测试项目', '2019-11-14', '2020-11-14', NULL, '2019-11-14 15:50:50', NULL, '2019-11-14 15:50:53', NULL, 'AS', 'aswl');

update as_device set project_id ='1' where project_id is null;
update as_region set project_id ='1' where project_id is null;

-- 创建通用分类树表
DROP TABLE IF EXISTS `as_category_tree`;
CREATE TABLE `as_category_tree` (
  `id` varchar(64) NOT NULL DEFAULT '' COMMENT '主键',
  `category` tinyint(3) DEFAULT NULL COMMENT '类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类',
  `node_name` varchar(255) DEFAULT NULL COMMENT '名称',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父id',
  `node_code` varchar(255) DEFAULT NULL COMMENT '节点编码',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改者',
  `modify_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用分类树表，普通的树可以直接用一个type辨别来获取';

INSERT INTO `as_category_tree` VALUES ('1',1,'产品中心','-1','A01',1,'admin','2020-03-04 09:23:14',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('2',2,'常见故障','-1','A02',2,'admin','2020-03-04 09:23:14',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('3',3,'行业咨询','-1','A03',3,'admin','2020-03-04 09:23:14',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('5',1,'产品中心分类2','1','A01A02',2,'admin','2020-03-04 09:23:14',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('6',2,'常见故障分类1','2','A02A01',1,'admin','2020-03-13 10:04:30',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('687964295736201216',1,'产品中心分类4','1','A01A04',1,'test','2020-03-13 10:04:30',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('689470929532751872',2,'常见故障分类3','2','A02A02',3,'test','2020-03-17 13:51:19',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('689495851621552128',1,'行业咨询分类3','3','A03A03',3,'test','2020-03-17 15:30:21',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('689496757616381952',1,'行业资讯分类3','3','A03A04',3,'test','2020-03-17 15:33:57',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('689498459677528064',3,'行业资讯分类3','3','A03A05',3,'test','2020-03-17 15:40:43',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('689498628653453312',2,'常见故障分类4','2','A02A03',4,'test','2020-03-17 15:41:23',0,NULL,NULL);
INSERT INTO `as_category_tree` VALUES ('8',3,'行业资讯分类1','3','A03A01',1,'admin','2020-03-13 10:04:30',0,'test','2020-03-17 15:31:52');
INSERT INTO `as_category_tree` VALUES ('9',3,'行业资讯分类2','3','A03A02',2,'admin','2020-03-13 10:04:30',0,'test','2020-03-17 15:32:03');



-- 创建 banner管理
DROP TABLE IF EXISTS `as_content_banner` ;
CREATE TABLE `as_content_banner` (
  `id` varchar(64) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `banner_image` varchar(255) DEFAULT NULL COMMENT 'banner图',
  `display_position` varchar(64) DEFAULT NULL COMMENT '显示位置',
  `content_link` varchar(255) DEFAULT NULL COMMENT '链接',
  `is_release` tinyint(1) DEFAULT NULL COMMENT '是否已发布 1-已发布，0为未发布',
  `clicks` int(11) DEFAULT '0' COMMENT '点击量',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改者',
  `modify_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '添加时间',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `category` tinyint(3) DEFAULT NULL COMMENT '类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类',
  `content_id` varchar(64) DEFAULT NULL COMMENT '内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='banner管理';

INSERT INTO `as_content_banner` VALUES ('1','标题','http://120.24.102.159/group1/M00/00/00/rBJI315xxsGAIC3hAAIXvVxY-_s690.jpg','1','/ibrs/v1/contentProduct/html/1',1,0,'admin','2020-03-12 16:59:18','admin','2020-03-12',5,0,1,'1');
INSERT INTO `as_content_banner` VALUES ('2','标题2','http://120.24.102.159/group1/M00/00/00/rBJI315xxtSAalaEAAIuXUTyF9o089.jpg','1','/ibrs/v1/contentMalfunction/html/1000',1,0,'admin','2020-03-12 13:13:00','admin','2020-03-12',3,0,2,'1000');
INSERT INTO `as_content_banner` VALUES ('3','标题3','http://120.24.102.159/group1/M00/00/00/rBJI315xxt6AMDy5AAFHsxY4DMc016.jpg','1','/ibrs/v1/contentIndustry/html/101',1,0,NULL,NULL,'admin','2020-03-12',4,0,3,'101');


-- 创建行业资讯表
DROP TABLE IF EXISTS `as_content_industry` ;
CREATE TABLE `as_content_industry` (
  `id` varchar(64) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `category_tree_id` varchar(64) DEFAULT NULL COMMENT '内容分类(category_tree表)',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面',
  `cover_text` varchar(255) DEFAULT NULL COMMENT '封面摘要',
  `showcase_text` varchar(255) DEFAULT NULL COMMENT '橱窗内容',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改者',
  `modify_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '添加时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标记 0:正常;1:删除',
  `is_showcase` tinyint(1) DEFAULT '0' COMMENT '是否显示到橱窗，1-是，0-不是',
  `clicks` int(11) DEFAULT '0' COMMENT '点击量',
  `is_release` tinyint(3) DEFAULT '0' COMMENT '是否已发布 1-已发布，0为未发布',
  `content` longtext COMMENT '内容',
  `likes` int(11) DEFAULT '0' COMMENT '点赞量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行业资讯表';

INSERT INTO `as_content_industry` VALUES ('101','8','测试行业资讯2','尼古拉斯',3,'http://120.24.102.159/group1/M00/00/00/rBJI315wdkeAVFKKAAAr9ovg8TE387.png','摘要','推荐','test','2020-03-17 15:35:53','admin','2019-12-30 01:00:00',0,1,11,0,'测试文字中文<p><br></p>',4);
INSERT INTO `as_content_industry` VALUES ('689497444005842944','8','测试','番薯',4,'http://120.24.102.159/group1/M00/00/00/rBJI315wfhOAKe9TAADebJM-9Vw998.png','无','无','test','2020-03-18 10:04:22','test','2020-03-17 15:36:41',0,0,1,0,'<p>无</p>',1);



-- 创建内容文章点赞表
DROP TABLE IF EXISTS `as_content_like` ;
CREATE TABLE `as_content_like` (
  `id` varchar(64) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `category` tinyint(3) DEFAULT NULL COMMENT '类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类',
  `content_id` varchar(64) DEFAULT NULL COMMENT '内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建者（如果取消点赞，直接删除就是，不留del_flag）',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容文章点赞表(几个功能的点赞都在里面)';


-- 创建常见故障表
DROP TABLE IF EXISTS `as_content_malfunction` ;
CREATE TABLE `as_content_malfunction` (
  `id` varchar(64) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `category_tree_id` varchar(64) DEFAULT NULL COMMENT '内容分类(category_tree表)',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `showcase_text` varchar(255) DEFAULT NULL COMMENT '橱窗内容',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改者',
  `modify_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '添加时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标记 0:正常;1:删除',
  `is_showcase` tinyint(1) DEFAULT '0' COMMENT '是否展示到橱窗',
  `clicks` int(11) DEFAULT '0' COMMENT '点击量',
  `is_release` tinyint(1) DEFAULT '0' COMMENT '是否已发布 1-已发布，0为未发布',
  `content` longtext COMMENT '内容',
  `likes` int(11) DEFAULT '0' COMMENT '点赞量',
  `heat_count` int(11) DEFAULT '0' COMMENT '现在的问题热度，产品认为，问题热度需要自动变，每小时添加2-12个',
  `heat_max_count` int(11) DEFAULT '0' COMMENT '最终的问题热度，一开始就随机生成出来',
  `last_heat_time` datetime DEFAULT NULL COMMENT '最后的问题热度增加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='常见故障表';


INSERT INTO `as_content_malfunction` VALUES ('1000','6','常见故障1','常见故障1',2,'常见故障1','test','2020-03-18 09:32:12','admin','2020-01-01',0,0,9,0,'测试常见故障1',2,0,0,NULL);
INSERT INTO `as_content_malfunction` VALUES ('1001','6','常见故障2','张三',3,'常见故障2','test','2020-03-18 09:32:15','admin','2020-01-01',0,0,12,0,'测试常见故障2<p><br></p>',4,0,0,NULL);



-- 创建 系统消息表

DROP TABLE IF EXISTS `as_content_notice` ;
CREATE TABLE `as_content_notice` (
  `id` varchar(64) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `content` text COMMENT '内容',
  `is_release` tinyint(1) DEFAULT '0' COMMENT '是否已发布 1-已发布，0-未发布',
  `release_time` datetime DEFAULT NULL COMMENT '发布时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改者',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统消息表';


-- 创建 产品中心表
DROP TABLE IF EXISTS `as_content_product` ;
CREATE TABLE `as_content_product` (
  `id` varchar(64) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `category_tree_id` varchar(64) DEFAULT NULL COMMENT '内容分类(category_tree表)',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面',
  `showcase_text` varchar(255) DEFAULT NULL COMMENT '橱窗内容',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改者',
  `modify_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '添加时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标记 0:正常;1:删除',
  `is_showcase` tinyint(1) DEFAULT '0' COMMENT '是否展示到橱窗',
  `clicks` int(11) DEFAULT '0' COMMENT '点击量',
  `is_release` tinyint(1) DEFAULT '0' COMMENT '是否已发布 1-已发布，0为未发布',
  `content` longtext COMMENT '内容',
  `likes` int(11) DEFAULT '0' COMMENT '点赞量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品中心表';

INSERT INTO `as_content_product` VALUES ('1','5','测试标题',10,'','测试橱窗','test','2020-03-16 10:42:22','admin','2020-03-05',0,1,10,0,'\n    <em>test中文中文</em>\n',3);
INSERT INTO `as_content_product` VALUES ('688022188837703680','5','标题10标题1',11,NULL,'0','test','2020-03-16 10:42:20','admin','2020-03-13 13:54:33',0,1,0,0,'内容内容内容10',0);
INSERT INTO `as_content_product` VALUES ('689057491392270336','687964295736201216','测试标题4',12,'http://120.24.102.159/group1/M00/00/00/rBJI315u5D2AcR8rAADVIRcVXGo701.jpg','非常好','test','2020-03-16 10:42:14','test','2020-03-16 10:28:28',0,1,1,0,'新增产品测试&amp;nbsp;',0);
INSERT INTO `as_content_product` VALUES ('689059023072399360','687964295736201216','测试标题2',13,'http://120.24.102.159/group1/M00/00/00/rBJI315u5Z-Aar-jAAAJBkSBW-A285.png','come buy the soft','test','2020-03-16 10:41:52','test','2020-03-16 10:34:33',0,1,0,0,'windows10',0);
INSERT INTO `as_content_product` VALUES ('689059721767948288','687964295736201216','测试标题5',14,'http://120.24.102.159/group1/M00/00/00/rBJI315u5m2AJztHAAAMVZl_glM987.png','来买吧','test','2020-03-16 10:40:36','test','2020-03-16 10:37:20',0,1,0,0,'没有',0);
INSERT INTO `as_content_product` VALUES ('689061371064750080','687964295736201216','测试标题6',15,'http://120.24.102.159/group1/M00/00/00/rBJI315u5_aAKQLtAAAaJd9cgv8338.png',NULL,NULL,NULL,'test','2020-03-16 10:43:53',0,0,0,0,'nothing',0);
INSERT INTO `as_content_product` VALUES ('689068179464523776','687964295736201216','测试产品88',16,'http://120.24.102.159/group1/M00/00/00/rBJI315u9a-ATC9RAADebJM-9Vw419.png',NULL,'test','2020-03-16 14:10:03','test','2020-03-16 11:10:56',0,0,1,0,'奥力给&amp;nbsp;对话框是否显示，可使用 v-model 双向绑定数据。',2);
INSERT INTO `as_content_product` VALUES ('689113526651981824','687964295736201216','12312',17,'http://120.24.102.159/group1/M00/00/00/rBJI315vGHiARt1JAAAZzlF_62U299.png',NULL,'test','2020-03-16 14:03:27','test','2020-03-16 14:11:08',0,0,1,0,'1231232131123',2);
INSERT INTO `as_content_product` VALUES ('689115043631075328','5','测试标题',18,'','测试橱窗','test','2020-03-16 10:42:22','test','2020-03-16 14:17:10',0,0,0,0,'<em>11222test中文中文</em>\n',0);
INSERT INTO `as_content_product` VALUES ('689115281263562752','5','测试标题',19,'','测试橱窗','test','2020-03-16 10:42:22','test','2020-03-16 14:18:06',0,0,0,0,'1231232131123\n',0);
INSERT INTO `as_content_product` VALUES ('689117934022430720','687964295736201216','1111',20,'http://120.24.102.159/group1/M00/00/00/rBJI315vHJSAQGxXAAAE4pHwZKA924.png',NULL,NULL,NULL,'test','2020-03-16 14:28:39',0,0,15,0,'<p>123123213<span style=\"color: rgb(194, 79, 74);\">21311111111</span></p>',2);
INSERT INTO `as_content_product` VALUES ('689119425730842624','5','测试标题',21,'','测试橱窗','test','2020-03-16 10:42:22','test','2020-03-16 14:34:34',0,0,0,0,'1231232131123\n',0);
INSERT INTO `as_content_product` VALUES ('689119501400281088','5','测试标题',22,'','测试橱窗','test','2020-03-16 10:42:22','test','2020-03-16 14:34:52',0,0,0,0,'1231232131123\n',0);
INSERT INTO `as_content_product` VALUES ('689119698104750080','5','测试标题',23,'','测试橱窗','test','2020-03-16 10:42:22','test','2020-03-16 14:35:39',0,0,1,0,'1231232131123\n',0);
INSERT INTO `as_content_product` VALUES ('689119985750118400','5','测试标题',25,'','测试橱窗','test','2020-03-16 10:42:22','test','2020-03-16 14:36:48',0,0,1,0,'123\n',0);
INSERT INTO `as_content_product` VALUES ('689120416865849344','5','测试标题',27,'','测试橱窗','test','2020-03-16 10:42:22','test','2020-03-16 14:38:31',0,0,0,0,'123\n',0);
INSERT INTO `as_content_product` VALUES ('689120497581035520','5','测试标题',28,'http://120.24.102.159/group1/M00/00/00/rBJI315vIG2AbEo5AAAr9ovg8TE195.png','测试橱窗','test','2020-03-16 16:45:08','test','2020-03-16 14:38:50',0,0,0,0,'123\n<p><img src=\"http://120.24.102.159/group1/M00/00/00/rBJI315vIFKAKKIXAAAY047T5Vo496.png\" style=\"max-width:100%;\"><br></p>',1);

alter table as_content_product add column other_url varchar(2000) COMMENT '其它链接，非本系统的链接';
alter table as_content_malfunction add column other_url varchar(2000) COMMENT '其它链接，非本系统的链接';
alter table as_content_industry add column other_url varchar(2000) COMMENT '其它链接，非本系统的链接';
alter table as_content_banner add column likes int(11) DEFAULT '0' COMMENT '点赞量';

ALTER TABLE as_content_like MODIFY COLUMN category tinyint(3) COMMENT '类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类，4-banner的contentLink放外网链接的种类';
ALTER TABLE as_content_like MODIFY COLUMN content_id varchar(64) DEFAULT NULL COMMENT '内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分,如果category为4，就是banner表的id';
ALTER TABLE as_content_banner MODIFY COLUMN category tinyint(3) COMMENT '类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类，4-banner的contentLink放外网链接的种类';
ALTER TABLE as_content_banner MODIFY COLUMN content_id varchar(64) DEFAULT NULL COMMENT '内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分,如果category为4，就是banner表的id';

update as_address_base set project_id ='1' where project_id is null;
update as_content_malfunction set heat_max_count=5000 where heat_max_count is null;


