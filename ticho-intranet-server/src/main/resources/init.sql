
DROP TABLE IF EXISTS `sys_client`;
CREATE TABLE `sys_client` (
  `id` bigint NOT NULL COMMENT '主键标识',
  `access_key` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端秘钥',
  `name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端名称',
  `status` tinyint DEFAULT NULL COMMENT '状态;1-正常,0-停用',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` bigint DEFAULT '0' COMMENT '乐观锁;控制版本更改',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
  PRIMARY KEY (`id`),
  KEY `access_key_index` (`access_key`),
  KEY `name_index` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户端信息';

INSERT INTO `sys_client` VALUES (1686382520213438464,'68bfe8f0af124ecfa093350ab8d4b44f','1019319473@qq.com',1,10,'默认客户端',1,'admin','2023-08-01 22:24:59','admin','2024-01-07 12:56:23',0);

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL COMMENT '主键编号',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型编码',
  `label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典标签',
  `value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典值',
  `sort` smallint DEFAULT NULL COMMENT '排序',
  `status` tinyint DEFAULT NULL COMMENT '状态;1-正常,0-停用',
  `remark` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` bigint DEFAULT '0' COMMENT '乐观锁;控制版本更改',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典';

DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL COMMENT '主键编号',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型编码',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型名称',
  `is_sys` tinyint DEFAULT NULL COMMENT '是否系统字典;1-是,0-否',
  `status` tinyint DEFAULT NULL COMMENT '状态;1-正常,0-停用',
  `remark` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` bigint DEFAULT '0' COMMENT '乐观锁;控制版本更改',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典类型';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL COMMENT '主键编号;',
  `parent_id` bigint DEFAULT NULL COMMENT '父级id',
  `structure` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '结构',
  `type` tinyint DEFAULT NULL COMMENT '类型;1-目录,2-菜单,3-按钮',
  `perms` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
  `name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题;目录名称、菜单名称',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由地址',
  `component` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
  `component_name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件名称',
  `redirect` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '转发地址',
  `ext_flag` tinyint DEFAULT NULL COMMENT '是否外链菜单;1-是,0-否',
  `keep_alive` tinyint DEFAULT NULL COMMENT '是否缓存;1-是,0-否',
  `invisible` tinyint DEFAULT NULL COMMENT '菜单和目录是否可见;1-是,0-否',
  `closable` tinyint DEFAULT NULL COMMENT '菜单是否可关闭;1-是,0-否',
  `icon` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` tinyint DEFAULT NULL COMMENT '状态;1-正常,0-禁用',
  `remark` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` bigint DEFAULT '0' COMMENT '乐观锁;控制版本更改',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单信息';

INSERT INTO `sys_menu` VALUES (1580826339636596737,0,'1580826339636596737',1,NULL,'系统管理','/system','LAYOUT','System','',0,0,1,0,'ant-design:account-book-outlined',20,1,'',0,'zhajianjun','2022-10-14 15:42:24','admin','2024-01-11 00:30:57',0),(1580831598392037378,1580826339636596737,'1580826339636596737-1580831598392037378',2,NULL,'角色管理','/system/role','system/role/index','Role','',0,0,1,0,'ion:layers-outline',30,1,'',0,'zhajianjun','2022-10-14 16:03:18','admin','2024-01-09 16:10:57',0),(1580831864789061633,1580826339636596737,'1580826339636596737-1580831864789061633',2,NULL,'菜单管理','/system/menu','system/menu/index','Menu','',0,0,1,0,'ion:layers-outline',60,1,'',0,'zhajianjun','2022-10-14 16:04:22','admin','2024-01-09 16:10:57',0),(1693466547550445570,0,'1693466547550445570',1,NULL,'主页','/dashboard','LAYOUT','LAYOUT','/dashboard/workbench',0,0,1,NULL,'ion:grid-outline',10,1,NULL,0,'admin','2023-08-21 11:34:23','admin','2024-01-14 16:38:00',0),(1693497514038157313,1693466547550445570,'1693466547550445570-1693497514038157313',2,NULL,'工作台','/dashboard/workbench','dashboard/workbench/index','Workbench',NULL,0,0,0,NULL,'ant-design:home-outlined',10,1,NULL,0,'admin','2023-08-21 13:37:26','admin','2024-01-13 18:30:17',0),(1694016595833151489,1580831864789061633,'1580826339636596737-1580831864789061633-1694016595833151489',3,'system:menu:save','菜单新增',NULL,NULL,'MenuAdd',NULL,NULL,NULL,NULL,NULL,NULL,20,1,NULL,0,'admin','2023-08-23 00:00:04','admin','2024-01-14 15:35:42',0),(1695715629383811072,1580831864789061633,'1580826339636596737-1580831864789061633-1695715629383811072',3,'system:menu:list','菜单查询',NULL,NULL,'MenuSelect',NULL,NULL,NULL,NULL,NULL,NULL,10,1,NULL,0,'admin','2023-08-27 16:31:26','admin','2024-01-14 15:35:45',0),(1695716465233100800,1580826339636596737,'1580826339636596737-1695716465233100800',2,NULL,'用户管理','/system/user','system/user/index','User',NULL,0,0,1,NULL,'ant-design:ant-design-outlined',10,1,NULL,0,'admin','2023-08-27 16:34:45','admin','2024-01-09 17:30:44',0),(1697273400566743040,1693497514038157313,'1693466547550445570-1693497514038157313-1697273400566743040',3,'system:user:getUserDtl','基础权限',NULL,NULL,'BasticPerms',NULL,0,0,1,NULL,NULL,1,1,NULL,0,'admin','2023-08-31 23:41:28','admin','2024-01-14 15:33:04',0),(1697278317306052608,1580831864789061633,'1580826339636596737-1580831864789061633-1697278317306052608',3,'system:menu:update','菜单修改',NULL,NULL,'MenuEdit',NULL,NULL,NULL,NULL,NULL,NULL,20,1,NULL,0,'admin','2023-09-01 00:00:59','admin','2024-01-14 16:45:03',0),(1697420336175251456,1580831864789061633,'1580826339636596737-1580831864789061633-1697420336175251456',3,'system:menu:remove','菜单删除',NULL,NULL,'MenuDel',NULL,NULL,NULL,NULL,NULL,NULL,20,1,NULL,0,'admin','2023-09-01 09:25:19','admin','2024-01-14 15:35:16',0),(1697491896345034752,1695716465233100800,'1580826339636596737-1695716465233100800-1697491896345034752',3,'system:user:page','用户查询',NULL,NULL,'UserSelect',NULL,0,0,1,NULL,NULL,10,1,NULL,0,'admin','2023-09-01 14:09:41','admin','2024-01-14 15:33:36',0),(1697530405667733504,1695716465233100800,'1580826339636596737-1695716465233100800-1697530405667733504',3,'system:user:save','用户新增',NULL,NULL,'UserAdd',NULL,NULL,NULL,NULL,NULL,NULL,20,1,NULL,0,'admin','2023-09-01 16:42:42','admin','2024-01-14 15:35:20',0),(1697533041406115840,1695716465233100800,'1580826339636596737-1695716465233100800-1697533041406115840',3,'system:user:update','用户修改',NULL,NULL,'UserEdit',NULL,NULL,NULL,NULL,NULL,NULL,30,1,NULL,0,'admin','2023-09-01 16:53:10','admin','2024-01-14 15:35:22',0),(1697533206095462400,1695716465233100800,'1580826339636596737-1695716465233100800-1697533206095462400',3,'system:user:remove','用户删除',NULL,NULL,'UserDel',NULL,NULL,NULL,NULL,NULL,NULL,40,1,NULL,0,'admin','2023-09-01 16:53:50',NULL,'2024-01-14 15:35:25',0),(1697533570328821760,1580831598392037378,'1580826339636596737-1580831598392037378-1697533570328821760',3,'system:role:page,system:role:list','角色查询',NULL,NULL,'RoleSelect',NULL,0,0,1,NULL,NULL,10,1,NULL,0,'admin','2023-09-01 16:55:16','admin','2024-01-14 15:34:06',0),(1697534055433633792,1580831598392037378,'1580826339636596737-1580831598392037378-1697534055433633792',3,'system:role:save','角色新增',NULL,NULL,'RoleAdd',NULL,NULL,NULL,NULL,NULL,NULL,20,1,NULL,0,'admin','2023-09-01 16:57:12',NULL,'2024-01-14 15:35:29',0),(1697534315883134976,1580831598392037378,'1580826339636596737-1580831598392037378-1697534315883134976',3,'system:role:update','角色修改',NULL,NULL,'RoleEdit',NULL,NULL,NULL,NULL,NULL,NULL,30,1,NULL,0,'admin','2023-09-01 16:58:14','admin','2024-01-14 15:35:31',0),(1697534451833110528,1580831598392037378,'1580826339636596737-1580831598392037378-1697534451833110528',3,'system:user:remove','角色删除',NULL,NULL,'RoleDel',NULL,NULL,NULL,NULL,NULL,NULL,40,1,NULL,0,'admin','2023-09-01 16:58:47',NULL,'2024-01-14 15:35:34',0),(1698536628588380160,1580826339636596737,'1580826339636596737-1698536628588380160',2,NULL,'用户详情','/system/user/userDetail/:id','system/user/UserDetail','UserDetail',NULL,0,0,0,NULL,'ant-design:account-book-outlined',9,1,NULL,0,'admin','2023-09-04 11:21:05','admin','2024-01-09 16:10:57',0),(1698586399516655616,0,'1698586399516655616',1,NULL,'外部页面','/link','LAYOUT','Link',NULL,0,0,1,NULL,'ant-design:link-outlined',40,1,NULL,0,'admin','2023-09-04 14:38:51','admin','2024-01-14 16:51:11',0),(1698587434666688512,1698586399516655616,'1698586399516655616-1698587434666688512',2,NULL,'AntDesignVue(外链)','https://www.antdv.com/components/overview-cn',NULL,NULL,'',1,0,1,NULL,'ant-design:linkedin-outlined',20,1,NULL,0,'admin','2023-09-04 14:42:58','admin','2024-01-14 17:19:31',0),(1744651429282316288,0,'1744651429282316288',1,NULL,'映射管理','/intranet','LAYOUT','Intranet',NULL,0,0,1,NULL,'ant-design:coffee-outlined',30,1,NULL,0,'admin','2024-01-09 17:24:50','admin','2024-01-14 16:45:48',0),(1744651827317571584,1744651429282316288,'1744651429282316288-1744651827317571584',2,NULL,'客户端管理','/intranet/client','intranet/client/index','Client',NULL,0,0,1,NULL,'ant-design:check-square-outlined',10,1,NULL,0,'admin','2024-01-09 17:26:24','admin','2024-01-14 16:46:37',0),(1745443976514633728,1744651429282316288,'1744651429282316288-1745443976514633728',2,NULL,'端口管理','/intranet/port','intranet/port/index','Port',NULL,0,0,1,NULL,'ant-design:ant-design-outlined',20,1,NULL,0,'admin','2024-01-11 21:54:07','admin','2024-01-14 16:46:26',0),(1746454367088672768,1744651827317571584,'1744651429282316288-1744651827317571584-1746454367088672768',3,'intranet:client:list','客户端查询',NULL,NULL,'Client',NULL,0,0,1,NULL,NULL,10,1,NULL,0,'admin','2024-01-14 16:49:03',NULL,NULL,0),(1746456609858519040,1698586399516655616,'1698586399516655616-1746456609858519040',2,NULL,'AntDesignVue(内嵌)','antd',NULL,'Antd','https://www.antdv.com/components/overview-cn',1,0,1,NULL,'ant-design:search-outlined',10,1,NULL,0,'admin','2024-01-14 16:57:58','admin','2024-01-14 17:19:31',0);

DROP TABLE IF EXISTS `sys_op_log`;
CREATE TABLE `sys_op_log` (
  `id` bigint NOT NULL COMMENT '主键编号;',
  `url` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求地址',
  `type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志类型',
  `method` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方法',
  `params` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求参数',
  `message` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '内容',
  `total_time` int DEFAULT NULL COMMENT '总耗时长（毫秒）',
  `ip` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求IP',
  `operate_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人',
  `operate_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `is_err` tinyint DEFAULT NULL COMMENT '是否异常',
  `err_message` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日志信息';

DROP TABLE IF EXISTS `sys_port`;
CREATE TABLE `sys_port` (
  `id` bigint NOT NULL COMMENT '主键标识',
  `access_key` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端秘钥',
  `port` int DEFAULT NULL COMMENT '主机端口',
  `endpoint` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端地址',
  `domain` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '域名',
  `status` tinyint DEFAULT NULL COMMENT '状态;1-正常,0-停用',
  `forever` tinyint DEFAULT NULL COMMENT '是否永久;1-是,0-否',
  `expire_at` datetime DEFAULT NULL COMMENT '过期时间',
  `type` int DEFAULT NULL COMMENT '协议类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` bigint DEFAULT '0' COMMENT '乐观锁;控制版本更改',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
  PRIMARY KEY (`id`),
  KEY `access_key_index` (`access_key`),
  KEY `port_index` (`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='端口信息';

INSERT INTO sys_port (id, access_key, port, endpoint, `domain`, status, forever, expire_at, `type`, sort, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1686385202043027456, '68bfe8f0af124ecfa093350ab8d4b44f', 80, '127.0.0.1:5122', NULL, 1, 1, NULL, 1, 80, NULL, 0, 'admin', '2023-08-01 22:35:38', 'admin', '2024-01-14 17:39:51', 0);

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL COMMENT '主键编号;',
  `code` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色编码',
  `name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名称',
  `status` tinyint DEFAULT NULL COMMENT '状态;1-正常,0-停用',
  `remark` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` bigint DEFAULT '0' COMMENT '乐观锁;控制版本更改',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色信息';

INSERT INTO `sys_role` VALUES (1620586462021824513,'admin','管理员',1,'',0,'zhajianjun','2023-02-01 08:54:56','admin','2024-01-12 21:03:19',0),(1620586621594120193,'test','测试角色',1,'11',0,'zhajianjun','2023-02-01 08:55:34','admin','2024-01-12 21:03:17',0),(1697263639444140034,'test1','test',1,'1',0,'admin','2023-08-31 23:02:40',NULL,'2023-08-31 23:26:13',1),(1697266529789399041,'test1','test1',1,'',0,'admin','2023-08-31 23:14:09','admin','2023-08-31 23:29:33',1),(1697267594005958657,'test3','test3',1,'',0,'admin','2023-08-31 23:18:23','admin','2023-08-31 23:28:57',1);

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  KEY `menu_index` (`menu_id`),
  KEY `role_index` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单关联关系';

INSERT INTO `sys_role_menu` VALUES (1620586462021824513,1693466547550445570),(1620586462021824513,1693497514038157313),(1620586462021824513,1580826339636596737),(1620586462021824513,1580831598392037378),(1620586462021824513,1580831864789061633),(1620586462021824513,1695715629383811072),(1620586462021824513,1694016595833151489),(1620586621594120193,1693466547550445570),(1620586621594120193,1580826339636596737),(1620586621594120193,1693497514038157313),(1620586621594120193,1697273400566743040),(1620586621594120193,1580831864789061633),(1620586621594120193,1695715629383811072),(1620586621594120193,1697420336175251456),(1620586621594120193,1694016595833151489),(1620586621594120193,1697278317306052608),(1620586621594120193,1580831598392037378),(1620586621594120193,1695716465233100800),(1620586621594120193,1697491896345034752),(1620586621594120193,1697533570328821760),(1620586621594120193,1698536628588380160);

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT '主键编号',
  `username` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账户;账户具有唯一性',
  `password` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `nickname` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `realname` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '真实姓名',
  `idcard` varchar(18) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证号',
  `sex` tinyint DEFAULT NULL COMMENT '性别;0-男,1-女',
  `age` tinyint DEFAULT NULL COMMENT '年龄',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `address` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '家庭住址',
  `education` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '学历',
  `email` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `qq` bigint DEFAULT NULL COMMENT 'QQ号码',
  `wechat` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信号码',
  `mobile` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号码',
  `photo` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像地址',
  `last_ip` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后登录ip地址',
  `last_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint DEFAULT NULL COMMENT '用户状态;1-正常,2-未激活,3-已锁定,4-已注销',
  `remark` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` bigint DEFAULT '0' COMMENT '乐观锁;控制版本更改',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
  PRIMARY KEY (`id`),
  KEY `username_index` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息';

INSERT INTO `sys_user` VALUES (1580743544448905218,'admin','$2a$10$E/mhGMUGKHcn8G1VYTDdXut4YgxmbtZ2qj7WmUrEN3/rlhwe9aVzG','管理员','管理员',NULL,NULL,NULL,NULL,NULL,NULL,'1@qq.com',1,NULL,'1',NULL,NULL,NULL,1,'123123',0,NULL,'2022-10-14 10:13:24','admin','2024-01-12 21:06:03',0);

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  KEY `role_index` (`role_id`),
  KEY `user_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联关系';

INSERT INTO `sys_user_role` VALUES (1580743544448905218,1620586462021824513),(1580743544448905218,1620586621594120193),(1580819689349496833,1620586621594120193),(1697448140262621186,1620586621594120193),(1697448140262621186,1620586462021824513),(1697442104894095361,1620586621594120193);
