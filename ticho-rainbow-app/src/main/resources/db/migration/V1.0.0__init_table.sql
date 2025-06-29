-- @formatter:on
DROP TABLE IF EXISTS `sys_client`;
CREATE TABLE `sys_client`
(
    `id`          bigint NOT NULL COMMENT '主键编号',
    `access_key`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端秘钥',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '客户端名称',
    `expire_at`   datetime                                                      DEFAULT NULL COMMENT '过期时间',
    `status`      tinyint                                                       DEFAULT NULL COMMENT '状态;1-启用,0-禁用',
    `sort`        int                                                           DEFAULT NULL COMMENT '排序',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint                                                        DEFAULT '0' COMMENT '版本号',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_delete`   tinyint                                                       DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`),
    KEY           `access_key_index` (`access_key`),
    KEY           `name_index` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户端信息';
INSERT INTO `sys_client`
VALUES (1686382520213438464, '68bfe8f0af124ecfa093350ab8d4b44f', '1019319473@qq.com', '2024-05-31 17:16:25', 1, 10,
        '默认客户端', 1, 'admin', NOW(), NULL, NULL, 0);

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`
(
    `id`          bigint NOT NULL COMMENT '主键编号',
    `code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '字典编码',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '字典名称',
    `is_sys`      tinyint                                                       DEFAULT NULL COMMENT '是否系统字典;1-是,0-否',
    `status`      tinyint                                                       DEFAULT NULL COMMENT '状态;1-启用,0-禁用',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint                                                        DEFAULT '0' COMMENT '版本号',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_delete`   tinyint                                                       DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典';
INSERT INTO `sys_dict`
VALUES (1746819045291720704, 'commonStatus', '通用状态', 1, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1747613546708992000, 'yesOrNo', '是否', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1748240900481351680, 'userStatus', '用户状态', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1748248903905443840, 'menuType', '菜单类型', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1748261738358243328, 'protocolType', '协议类型', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1781894572796805120, 'planTask', '计划任务', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1783760085005107200, 'fileStorageType', '文件存储类型', 0, 1, '', 0, 'admin', NOW(), NULL, NULL,
        0),
       (1783761099317837824, 'fileStatus', '文件状态', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1787471707963916288, 'taskLogStatus', '计划任务日志状态', 0, 1, NULL, 0, 'admin', NOW(), NULL,
        NULL, 0),
       (1787650363709456384, 'httpType', 'HTTP协议类型', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1788838308642553856, 'sex', '性别', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1790214184961572864, 'channelStatus', '通道状态', 0, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0);

DROP TABLE IF EXISTS `sys_dict_label`;
CREATE TABLE `sys_dict_label`
(
    `id`          bigint NOT NULL COMMENT '主键编号',
    `code`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '字典编码',
    `label`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '字典标签',
    `value`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典值',
    `icon`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '图标',
    `color`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '颜色',
    `sort`        smallint                                                      DEFAULT NULL COMMENT '排序',
    `status`      tinyint                                                       DEFAULT NULL COMMENT '状态;1-启用,0-禁用',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint                                                        DEFAULT '0' COMMENT '版本号',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_delete`   tinyint                                                       DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典标签';
INSERT INTO `sys_dict_label`
VALUES (1746819115722473472, 'commonStatus', '启用', '1', 'ant-design:account-book-filled', '#008000', 10, 1, NULL, 0,
        'admin', NOW(), NULL, NULL, 0),
       (1746819180134400000, 'commonStatus', '禁用', '0', 'ant-design:aliwangwang-outlined', '#FF0000', 15, 1, NULL, 0,
        'admin', NOW(), NULL, NULL, 0),
       (1747613674828201984, 'yesOrNo', '是', '1', NULL, '#ba8003', 10, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1747613713164140544, 'yesOrNo', '否', '0', NULL, '', 20, 1, NULL, 0, 'admin', NOW(), NULL, NULL,
        0),
       (1748242704657022976, 'userStatus', '正常', '1', NULL, '#49CC90', 10, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1748242754888007680, 'userStatus', '未激活', '2', NULL, '#FCA130', 20, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1748243717862457344, 'userStatus', '已锁定', '3', NULL, '#FFC0CB', 30, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1748244029088202752, 'userStatus', '已注销', '4', NULL, '#EF3D3D', 40, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1748249124630691840, 'menuType', '目录', '1', NULL, NULL, 10, 1, NULL, 0, 'admin', NOW(), NULL,
        NULL, 0),
       (1748249178116456448, 'menuType', '菜单', '2', NULL, NULL, 20, 1, NULL, 0, 'admin', NOW(), NULL,
        NULL, 0),
       (1748249227869290496, 'menuType', '按钮', '3', NULL, NULL, 30, 1, NULL, 0, 'admin', NOW(), NULL,
        NULL, 0),
       (1748261877877571584, 'protocolType', 'HTTP', '1', NULL, '#49CC90', 10, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1748261927231946752, 'protocolType', 'HTTPS', '2', NULL, '#61AFFE', 20, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1748262053644075008, 'protocolType', 'SSH', '3', NULL, '', 30, 1, NULL, 0, 'admin', NOW(), NULL,
        NULL, 0),
       (1748262127493185536, 'protocolType', 'TELNET', '4', NULL, NULL, 40, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1748262168333123584, 'protocolType', 'DATABASE', '5', NULL, NULL, 50, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1748262274067333120, 'protocolType', 'RDESKTOP', '6', NULL, NULL, 60, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1748262311816069120, 'protocolType', 'TCP', '7', NULL, NULL, 70, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1781908812278530048, 'planTask', '内网映射数据刷新', 'top.ticho.rainbow.application.task.IntranetTask', NULL,
        NULL, 10, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1783736290294890496, 'planTask', '临时文件清除定时任务', 'top.ticho.rainbow.application.task.FileTmpClearTask',
        NULL, NULL, 20, 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1783760154588610560, 'fileStorageType', '公共', '1', NULL, NULL, 10, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1783760215653482496, 'fileStorageType', '私有', '2', NULL, NULL, 20, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1783761169928945664, 'fileStatus', '正常', '1', NULL, '#008000', 10, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1783761253580144640, 'fileStatus', '停用', '2', NULL, '#0b2bcb', 20, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1783761324472270848, 'fileStatus', '分片上传', '3', NULL, '#ac6706', 30, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1783761418344988672, 'fileStatus', '作废', '99', NULL, '#fc0303', 40, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1784442289230184448, 'planTask', '日志清除', 'top.ticho.rainbow.application.task.LogClearTask', '', NULL, 30, 1,
        NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1787471846036209664, 'taskLogStatus', '执行成功', '1', NULL, '#008000', 10, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1787471895998758912, 'taskLogStatus', '执行失败', '0', NULL, '#fc0303', 20, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1787650442407182336, 'httpType', 'GET', 'GET', '', '#49CC90', 10, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1787650566067847168, 'httpType', 'POST', 'POST', NULL, '#61AFFE', 20, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1787650660896866304, 'httpType', 'PUT', 'PUT', NULL, '#FCA130', 30, 1, NULL, 0, 'admin', NOW(),
        NULL, NULL, 0),
       (1787650747752513536, 'httpType', 'DELETE', 'DELETE', NULL, '#EF3D3D', 40, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1787650804346257408, 'httpType', 'HEAD', 'HEAD', NULL, '#800080', 50, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1787650909816225792, 'httpType', 'OPTIONS', 'OPTIONS', '', '#FFA500', 60, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1787650985535995904, 'httpType', 'PATCH', 'PATCH', NULL, '#00FFFF', 70, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1787651060878278656, 'httpType', 'TRACE', 'TRACE', NULL, '#FFC0CB', 80, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1787651138888138752, 'httpType', 'CONNECT', 'CONNECT', NULL, '#A52A2A', 90, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1788838419762249728, 'sex', '男', '0', NULL, NULL, 10, 1, NULL, 0, 'admin', NOW(), NULL, NULL,
        0),
       (1788838598833864704, 'sex', '女', '1', NULL, NULL, 10, 1, NULL, 0, 'admin', NOW(), NULL, NULL,
        0),
       (1790214309058445312, 'channelStatus', '已激活', '1', NULL, '#108ee9', 10, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1790214564529307648, 'channelStatus', '未激活', '0', NULL, '#FF5500', 10, 1, NULL, 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1791388986879508480, 'planTask', '邮件发送', 'top.ticho.rainbow.application.task.EmailTask', NULL, NULL, 40, 1,
        NULL, 0, 'admin', NOW(), NULL, NULL, 0);

DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info`
(
    `id`                 bigint NOT NULL COMMENT '主键编号',
    `type`               tinyint(1) NOT NULL COMMENT '存储类型;1-公共,2-私有',
    `file_name`          varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名称',
    `ext`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '文件扩展名',
    `path`               varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '存储路径',
    `size`               bigint                                                        DEFAULT NULL COMMENT '文件大小;单位字节',
    `content_type`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'MIME类型',
    `original_file_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原始文件名',
    `chunk_id`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分片id',
    `chunk_metadata`     json                                                          DEFAULT NULL COMMENT '分片元数据',
    `md5`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'md5',
    `metadata`           json                                                          DEFAULT NULL COMMENT '文件元数据',
    `status`             tinyint                                                       DEFAULT NULL COMMENT '状态;1-启用,2-停用,3-分片上传,99-作废',
    `remark`             varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`            bigint                                                        DEFAULT '0' COMMENT '版本号',
    `create_by`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time`        datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time`        datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_delete`          tinyint                                                       DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文件信息';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`                  bigint NOT NULL COMMENT '主键编号;',
    `parent_id`           bigint                                                        DEFAULT NULL COMMENT '父级id',
    `structure`           varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '结构',
    `type`                tinyint                                                       DEFAULT NULL COMMENT '类型;1-目录,2-菜单,3-按钮',
    `perms`               varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
    `name`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '标题;目录名称、菜单名称',
    `path`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '路由地址',
    `component`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '组件路径',
    `component_name`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '组件名称',
    `redirect`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '转发地址',
    `ext_flag`            tinyint                                                       DEFAULT NULL COMMENT '是否外链菜单;1-是,0-否',
    `keep_alive`          tinyint                                                       DEFAULT NULL COMMENT '是否缓存;1-是,0-否',
    `invisible`           tinyint                                                       DEFAULT NULL COMMENT '菜单和目录是否可见;1-是,0-否',
    `current_active_menu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '当前激活的菜单',
    `closable`            tinyint                                                       DEFAULT NULL COMMENT '菜单是否可关闭;1-是,0-否',
    `icon`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '图标',
    `sort`                int                                                           DEFAULT NULL COMMENT '排序',
    `status`              tinyint                                                       DEFAULT NULL COMMENT '状态;1-启用,0-禁用',
    `remark`              varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`             bigint                                                        DEFAULT '0' COMMENT '版本号',
    `create_by`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time`         datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time`         datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_delete`           tinyint                                                       DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单信息';
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1580826339636596737, 0, '1580826339636596737', 1, NULL, '系统管理', '/system', 'LAYOUT', 'System', '', 0, NULL, 1, NULL, 0, 'ant-design:account-book-outlined', 20, 1, '', 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1580831598392037378, 1580826339636596737, '1580826339636596737-1580831598392037378', 2, NULL, '角色管理', '/system/role', 'system/role/index', 'Role', '', 0, 1, 1, '', 0, 'ant-design:mac-command-outlined', 20, 1, '', 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1580831864789061633, 1580826339636596737, '1580826339636596737-1580831864789061633', 2, NULL, '菜单管理', '/system/menu', 'system/menu/index', 'Menu', '', 0, 1, 1, '', 0, 'ion:layers-outline', 30, 1, '', 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1693466547550445570, 0, '1693466547550445570', 1, NULL, '主页', '/home', 'LAYOUT', 'LAYOUT', '/home/workbench', 0, NULL, 1, NULL, NULL, 'ion:grid-outline', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1693497514038157313, 1693466547550445570, '1693466547550445570-1693497514038157313', 2, NULL, '工作台', '/home/workbench', 'dashboard/workbench/index', 'Workbench', NULL, 0, 1, 0, '/dashboard', NULL, 'ant-design:home-outlined', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1694016595833151489, 1580831864789061633, '1580826339636596737-1580831864789061633-1694016595833151489', 3, 'system:menu:save', '菜单新增', NULL, NULL, 'MenuAdd', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1695715629383811072, 1580831864789061633, '1580826339636596737-1580831864789061633-1695715629383811072', 3, 'system:menu:list', '菜单查询', NULL, NULL, 'MenuSelect', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1695716465233100800, 1580826339636596737, '1580826339636596737-1695716465233100800', 2, NULL, '用户管理', '/system/user', 'system/user/index', 'User', NULL, 0, 0, 1, '', NULL, 'ant-design:ant-design-outlined', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697278317306052608, 1580831864789061633, '1580826339636596737-1580831864789061633-1697278317306052608', 3, 'system:menu:update', '菜单修改', NULL, NULL, 'MenuEdit', NULL, NULL, NULL, 1, NULL, NULL, NULL, 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697420336175251456, 1580831864789061633, '1580826339636596737-1580831864789061633-1697420336175251456', 3, 'system:menu:remove', '菜单删除', NULL, NULL, 'MenuDel', NULL, NULL, NULL, 1, NULL, NULL, '', 60, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697491896345034752, 1695716465233100800, '1580826339636596737-1695716465233100800-1697491896345034752', 3, 'system:user:page', '用户查询', NULL, NULL, 'UserSelect', NULL, NULL, NULL, 1, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697530405667733504, 1695716465233100800, '1580826339636596737-1695716465233100800-1697530405667733504', 3, 'system:user:save', '用户新增', NULL, NULL, 'UserAdd', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697533041406115840, 1695716465233100800, '1580826339636596737-1695716465233100800-1697533041406115840', 3, 'system:user:update', '用户修改', NULL, NULL, 'UserEdit', NULL, NULL, NULL, NULL, NULL, NULL, '', 70, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697533206095462400, 1695716465233100800, '1580826339636596737-1695716465233100800-1697533206095462400', 3, 'system:user:remove', '用户删除', NULL, NULL, 'UserDel', NULL, NULL, NULL, NULL, NULL, NULL, '', 100, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697533570328821760, 1580831598392037378, '1580826339636596737-1580831598392037378-1697533570328821760', 3, 'system:role:page,system:role:list', '角色查询', NULL, NULL, 'RoleSelect', NULL, NULL, NULL, 1, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697534055433633792, 1580831598392037378, '1580826339636596737-1580831598392037378-1697534055433633792', 3, 'system:role:save', '角色新增', NULL, NULL, 'RoleAdd', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697534315883134976, 1580831598392037378, '1580826339636596737-1580831598392037378-1697534315883134976', 3, 'system:role:update', '角色修改', NULL, NULL, 'RoleEdit', NULL, NULL, NULL, NULL, NULL, NULL, '', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1697534451833110528, 1580831598392037378, '1580826339636596737-1580831598392037378-1697534451833110528', 3, 'system:user:remove', '角色删除', NULL, NULL, 'RoleDel', NULL, NULL, NULL, NULL, NULL, NULL, '', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1698536628588380160, 1580826339636596737, '1580826339636596737-1698536628588380160', 2, NULL, '用户详情', '/system/user/userDetail/:id', 'system/user/UserDetail', 'UserDetail', NULL, 0, 1, 0, '/system/user', NULL, 'ant-design:account-book-outlined', 9, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1698586399516655616, 0, '1698586399516655616', 1, NULL, '外部页面', '/link', 'LAYOUT', 'Link', NULL, 0, NULL, 1, NULL, NULL, 'ant-design:link-outlined', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1698587434666688512, 1698586399516655616, '1698586399516655616-1698587434666688512', 2, NULL, 'AntDesignVue(外链)', 'https://www.antdv.com/components/overview-cn', NULL, NULL, '', 1, 1, 1, NULL, NULL, 'ant-design:linkedin-outlined', 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1744651429282316288, 0, '1744651429282316288', 1, NULL, '映射管理', '/intranet', 'LAYOUT', 'Intranet', NULL, 0, NULL, 1, NULL, NULL, 'ant-design:coffee-outlined', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1744651827317571584, 1744651429282316288, '1744651429282316288-1744651827317571584', 2, NULL, '客户端管理', '/intranet/client', 'intranet/client/index', 'Client', NULL, 0, 1, 1, NULL, NULL, 'ant-design:check-square-outlined', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1745443976514633728, 1744651429282316288, '1744651429282316288-1745443976514633728', 2, NULL, '端口管理', '/intranet/port', 'intranet/port/index', 'Port', NULL, 0, 1, 1, NULL, NULL, 'ant-design:ant-design-outlined', 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1746454367088672768, 1744651827317571584, '1744651429282316288-1744651827317571584-1746454367088672768', 3, 'intranet:client:list,intranet:client:page', '客户端查询', NULL, NULL, 'ClientSelect', NULL, NULL, NULL, 1, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1746456609858519040, 1698586399516655616, '1698586399516655616-1746456609858519040', 2, NULL, 'AntDesignVue(内嵌)', 'antd', NULL, 'Antd', 'https://www.antdv.com/components/overview-cn', 1, 1, 1, NULL, NULL, 'ant-design:search-outlined', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1746496929841283072, 1580826339636596737, '1580826339636596737-1746496929841283072', 2, NULL, '字典管理', '/dict', 'system/dict/index', 'Dict', NULL, 0, 1, 1, NULL, NULL, 'ant-design:hdd-filled', 50, 1, NULL, 1, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1746497347082256384, 1746496929841283072, '1580826339636596737-1746496929841283072-1746497347082256384', 3, 'system:dict:page,system:dict-label:find', '字典查询', NULL, NULL, 'DictSelect', NULL, NULL, NULL, 1, NULL, NULL, '', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748327614482743296, 1746496929841283072, '1580826339636596737-1746496929841283072-1748327614482743296', 3, 'system:dict:save', '字典新增', NULL, NULL, 'DictAdd', NULL, NULL, NULL, 1, NULL, NULL, NULL, 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748328477867638784, 1746496929841283072, '1580826339636596737-1746496929841283072-1748328477867638784', 3, 'system:dict:update', '字典新增', NULL, NULL, 'DictEdit', NULL, NULL, NULL, 1, NULL, NULL, '', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748328739021783040, 1746496929841283072, '1580826339636596737-1746496929841283072-1748328739021783040', 3, 'system:dict:remove', '字典删除', NULL, NULL, 'DictDel', NULL, NULL, NULL, 1, NULL, NULL, '', 60, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748330810697580544, 1580831864789061633, '1580826339636596737-1580831864789061633-1748330810697580544', 3, 'system:menu:save', '菜单复制新增', NULL, NULL, 'MenuCopy', NULL, NULL, NULL, 1, NULL, NULL, NULL, 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748331110812614656, 1744651827317571584, '1744651429282316288-1744651827317571584-1748331110812614656', 3, 'intranet:client:save', '客户端新增', NULL, NULL, 'ClientAdd', NULL, NULL, NULL, 1, NULL, NULL, NULL, 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748331197211082752, 1744651827317571584, '1744651429282316288-1744651827317571584-1748331197211082752', 3, 'intranet:client:update', '客户端修改', NULL, NULL, 'ClientEdit', NULL, NULL, NULL, 1, NULL, NULL, '', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748331292065267712, 1744651827317571584, '1744651429282316288-1744651827317571584-1748331292065267712', 3, 'intranet:client:remove', '客户端删除', NULL, NULL, 'ClientDel', NULL, NULL, NULL, 1, NULL, NULL, '', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748331503634350080, 1745443976514633728, '1744651429282316288-1745443976514633728-1748331503634350080', 3, 'intranet:port:page', '端口查询', NULL, NULL, 'PortSelect', NULL, NULL, NULL, 1, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748331594143236096, 1745443976514633728, '1744651429282316288-1745443976514633728-1748331594143236096', 3, 'intranet:port:save', '端口新增', NULL, NULL, 'PortAdd', NULL, NULL, NULL, 1, NULL, NULL, NULL, 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748331711571165184, 1745443976514633728, '1744651429282316288-1745443976514633728-1748331711571165184', 3, 'intranet:port:save', '端口复制', NULL, NULL, 'PortCopy', NULL, NULL, NULL, 1, NULL, NULL, '', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748331806605705216, 1745443976514633728, '1744651429282316288-1745443976514633728-1748331806605705216', 3, 'intranet:port:remove', '端口删除', NULL, NULL, 'PortDel', NULL, NULL, NULL, 1, NULL, NULL, '', 60, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1748340313358860288, 1745443976514633728, '1744651429282316288-1745443976514633728-1748340313358860288', 3, 'intranet:port:update', '端口修改', NULL, NULL, 'PortEdit', NULL, NULL, NULL, 1, NULL, NULL, '', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1767096858825457664, 1693466547550445570, '1693466547550445570-1767096858825457664', 2, NULL, '个人中心', '/user/profile', 'system/user/profile/index', 'Profile', NULL, 0, 1, 0, '/dashboard', NULL, 'ant-design:user-outlined', 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1771802307621552128, 1767096858825457664, '1693466547550445570-1767096858825457664-1771802307621552128', 3, 'system:user:find-self-detail,system:user:find-self-info,system:user:modify-self-passwordf,system:menu:route,system:dict:list,system:user:updateForSelf,system:dict:flush', '个人中心权限', NULL, NULL, 'UserPerm', NULL, NULL, NULL, 1, NULL, NULL, '', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1773940982715252736, 1580826339636596737, '1580826339636596737-1773940982715252736', 2, NULL, '计划任务', '/system/task', 'system/task/index', 'Task', NULL, 0, 1, 1, NULL, NULL, 'ant-design:loading-3-quarters-outlined', 60, 1, NULL, 1, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1782674731338563584, 0, '1782674731338563584', 1, NULL, '资源管理', '/storage', 'LAYOUT', 'Storage', NULL, 0, 0, 1, NULL, NULL, 'ant-design:file-zip-twotone', 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1782675070078943232, 1782674731338563584, '1782674731338563584-1782675070078943232', 2, NULL, '文件管理', '/storage/file', 'storage/file/index', 'File', NULL, 0, 0, 1, '', NULL, 'ant-design:file-text-twotone', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1787445934443986944, 1580826339636596737, '1580826339636596737-1787445934443986944', 1, NULL, '日志', '/system/log', 'LAYOUT', 'Log', NULL, 0, 0, 1, NULL, NULL, 'ant-design:logout-outlined', 70, 1, NULL, 1, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1787446556790620160, 1787445934443986944, '1580826339636596737-1787445934443986944-1787446556790620160', 2, NULL, '操作日志', '/system/log/oplog', 'system/oplog/index', 'OpLog', NULL, 0, 1, 1, '', NULL, 'ant-design:history-outlined', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1787447065161236480, 1787445934443986944, '1580826339636596737-1787445934443986944-1787447065161236480', 2, NULL, '任务日志', '/system/log/tasklog', 'system/tasklog/index', 'TaskLog', NULL, 0, 1, 1, '', NULL, 'ant-design:login-outlined', 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1789206090173382656, 1695716465233100800, '1580826339636596737-1695716465233100800-1789206090173382656', 3, 'system:user:lock', '用户锁定', NULL, NULL, 'UserLock', NULL, 0, 0, 1, NULL, NULL, '', 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1789206190429831168, 1695716465233100800, '1580826339636596737-1695716465233100800-1789206190429831168', 3, 'system:user:unLock', '用户解锁', NULL, NULL, 'UserUnLock', NULL, 0, 0, 1, NULL, NULL, '', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1789206306523971584, 1695716465233100800, '1580826339636596737-1695716465233100800-1789206306523971584', 3, 'system:user:import,system:user:download-import-template', '用户导入', NULL, NULL, 'UserImport', NULL, 0, 0, 1, NULL, NULL, '', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1789206604688654336, 1695716465233100800, '1580826339636596737-1695716465233100800-1789206604688654336', 3, 'system:user:export', '用户导出', NULL, NULL, 'UserExport', NULL, 0, 0, 1, NULL, NULL, '', 60, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790267397697961984, 1580831598392037378, '1580826339636596737-1580831598392037378-1790267397697961984', 3, 'system:role:export', '角色导出', NULL, NULL, 'RoleExport', NULL, 0, 0, 1, NULL, NULL, '', 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790268328569208832, 1746496929841283072, '1580826339636596737-1746496929841283072-1790268328569208832', 3, 'system:dict:flush', '字典刷新', NULL, NULL, 'DictFlush', NULL, 0, 0, 1, NULL, NULL, NULL, 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790268772053942272, 1746496929841283072, '1580826339636596737-1746496929841283072-1790268772053942272', 3, 'system:dict:export', '字典导出', NULL, NULL, 'DictExport', NULL, 0, 0, 1, NULL, NULL, '', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790269142499065856, 1746496929841283072, '1580826339636596737-1746496929841283072-1790269142499065856', 3, 'system:dict-label:save', '字典标签新增', NULL, NULL, 'DictLabelAdd', NULL, 0, 0, 1, NULL, NULL, '', 70, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790269430693888000, 1746496929841283072, '1580826339636596737-1746496929841283072-1790269430693888000', 3, 'system:dict-label:update', '字典标签修改', NULL, NULL, 'DictLabelEdit', NULL, 0, 0, 1, NULL, NULL, NULL, 80, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790269739432411136, 1746496929841283072, '1580826339636596737-1746496929841283072-1790269739432411136', 3, 'system:dict-label:remove', '字典标签删除', NULL, NULL, 'DictLabelDel', NULL, 0, 0, 1, NULL, NULL, '', 90, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790270033570562048, 1773940982715252736, '1580826339636596737-1773940982715252736-1790270033570562048', 3, 'system:task:page', '计划任务查询', NULL, NULL, 'TaskSelect', NULL, 0, 0, 1, NULL, NULL, '', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790270637776830464, 1773940982715252736, '1580826339636596737-1773940982715252736-1790270637776830464', 3, 'system:task:save', '计划任务新增', NULL, NULL, 'TaskAdd', NULL, 0, 0, 1, NULL, NULL, '', 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790270795960811520, 1773940982715252736, '1580826339636596737-1773940982715252736-1790270795960811520', 3, 'system:task:export', '计划任务导出', NULL, NULL, 'TaskExport', NULL, 0, 0, 1, NULL, NULL, '', 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790271254167552000, 1773940982715252736, '1580826339636596737-1773940982715252736-1790271254167552000', 3, 'system:task:update', '计划任务修改', NULL, NULL, 'TaskEdit', NULL, 0, 0, 1, NULL, NULL, NULL, 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790271360166002688, 1773940982715252736, '1580826339636596737-1773940982715252736-1790271360166002688', 3, 'system:task:run-once', '任务执行一次', NULL, NULL, 'TaskRunOnce', NULL, 0, 0, 1, NULL, NULL, '', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790271627208949760, 1773940982715252736, '1580826339636596737-1773940982715252736-1790271627208949760', 3, '', '查看日志', NULL, NULL, 'GoTaskLog', NULL, 0, 0, 1, NULL, NULL, NULL, 60, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790271832700485632, 1773940982715252736, '1580826339636596737-1773940982715252736-1790271832700485632', 3, 'system:task:resume', '计划任务启动', NULL, NULL, 'TaskResume', NULL, 0, 0, 1, NULL, NULL, NULL, 70, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790271929832177664, 1773940982715252736, '1580826339636596737-1773940982715252736-1790271929832177664', 3, 'system:task:pause', '计划任务暂停', NULL, NULL, 'TaskPause', NULL, 0, 0, 1, NULL, NULL, '', 80, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790272097415593984, 1773940982715252736, '1580826339636596737-1773940982715252736-1790272097415593984', 3, 'system:task:remove', '计划任务删除', NULL, NULL, 'TaskDel', NULL, 0, 0, 1, NULL, NULL, NULL, 90, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790272321785692160, 1782675070078943232, '1782674731338563584-1782675070078943232-1790272321785692160', 3, 'storage:file:page', '文件查询', NULL, NULL, 'FileSelect', NULL, 0, 0, 1, NULL, NULL, '', 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790272484616962048, 1782675070078943232, '1782674731338563584-1782675070078943232-1790272484616962048', 3, 'storage:file:upload,storage:file:upload-chunk,storage:file:compose-chunk', '文件上传', NULL, NULL, 'FileUpload', NULL, 0, 0, 1, NULL, NULL, NULL, 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790273217261207552, 1782675070078943232, '1782674731338563584-1782675070078943232-1790273217261207552', 3, 'storage:file:export', '文件信息导出', NULL, NULL, 'FileExport', NULL, 0, 0, 1, NULL, NULL, '', 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790274085394055168, 1782675070078943232, '1782674731338563584-1782675070078943232-1790274085394055168', 3, 'storage:file:downloadById,storage:file:presigned', '文件下载', NULL, NULL, 'FileDownload', NULL, 0, 0, 1, NULL, NULL, '', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790274176934739968, 1782675070078943232, '1782674731338563584-1782675070078943232-1790274176934739968', 3, 'storage:file:upload,storage:file:upload-chunk,storage:file:compose-chunk', '文件断点续传', NULL, NULL, 'FileContinueUpload', NULL, 0, 0, 1, NULL, NULL, '', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790274507466866688, 1782675070078943232, '1782674731338563584-1782675070078943232-1790274507466866688', 3, 'storage:file:enable', '文件启用', NULL, NULL, 'FileEnable', NULL, 0, 0, 1, NULL, NULL, NULL, 60, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790274573187416064, 1782675070078943232, '1782674731338563584-1782675070078943232-1790274573187416064', 3, 'storage:file:disable', '文件停用', NULL, NULL, 'FileDisable', NULL, 0, 0, 1, NULL, NULL, '', 70, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790274714891976704, 1782675070078943232, '1782674731338563584-1782675070078943232-1790274714891976704', 3, 'storage:file:cancel', '文件作废', NULL, NULL, 'FileCancel', NULL, 0, 0, 1, NULL, NULL, NULL, 80, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790274881124827136, 1782675070078943232, '1782674731338563584-1782675070078943232-1790274881124827136', 3, 'storage:file:remove', '文件删除', NULL, NULL, 'FileDel', NULL, 0, 0, 1, NULL, NULL, NULL, 80, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790275265968996352, 1787446556790620160, '1580826339636596737-1787445934443986944-1787446556790620160-1790275265968996352', 3, 'system:op-log:page', '操作日志查询', NULL, NULL, 'OpLogSelect', NULL, 0, 0, 1, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790275396168581120, 1787446556790620160, '1580826339636596737-1787445934443986944-1787446556790620160-1790275396168581120', 3, 'system:op-log:export', '操作日志导出', NULL, NULL, 'OpLogExport', NULL, 0, 0, 1, NULL, NULL, '', 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790275535696297984, 1787446556790620160, '1580826339636596737-1787445934443986944-1787446556790620160-1790275535696297984', 3, '', '操作日志查看详情', NULL, NULL, 'OpLogDetail', NULL, 0, 0, 1, NULL, NULL, NULL, 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790275976295350272, 1787447065161236480, '1580826339636596737-1787445934443986944-1787447065161236480-1790275976295350272', 3, 'system:taskLog:page', '任务日志查询', NULL, NULL, 'TaskLogSelect', NULL, 0, 0, 1, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790276090334281728, 1787447065161236480, '1580826339636596737-1787445934443986944-1787447065161236480-1790276090334281728', 3, 'system:taskLog:export', '任务日志导出', NULL, NULL, 'TaskLogExport', NULL, 0, 0, 1, NULL, NULL, NULL, 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790276206780743680, 1787447065161236480, '1580826339636596737-1787445934443986944-1787447065161236480-1790276206780743680', 3, '', '任务日志查看详情', NULL, NULL, 'TaskLogDetail', NULL, 0, 0, 1, NULL, NULL, NULL, 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790276580069605376, 1744651827317571584, '1744651429282316288-1744651827317571584-1790276580069605376', 3, 'intranet:client:export', '客户端导出', NULL, NULL, 'ClientExport', NULL, 0, 0, 1, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790277526426222592, 1745443976514633728, '1744651429282316288-1745443976514633728-1790277526426222592', 3, 'intranet:port:export', '端口导出', NULL, NULL, 'PortExport', NULL, 0, 0, 1, NULL, NULL, NULL, 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1790917847682121728, 1744651429282316288, '1744651429282316288-1790917847682121728', 2, NULL, '流量统计', '/intranet/flow-monitor', 'intranet/flow-monitor/index', 'FlowMonitor', NULL, 0, 0, 1, '', NULL, 'ant-design:file-excel-outlined', 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1929521914242400256, 1695716465233100800, '1580826339636596737-1695716465233100800-1929521914242400256', 3, '', '用户修改密码', NULL, NULL, 'UserEditPassword', NULL, 0, 0, 1, NULL, NULL, '', 100, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1929522281172697088, 1695716465233100800, '1580826339636596737-1695716465233100800-1929522281172697088', 3, '', '用户重置密码', NULL, NULL, 'UserResetPassword', NULL, 0, 0, 1, NULL, NULL, NULL, 100, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1929522360138858496, 1695716465233100800, '1580826339636596737-1695716465233100800-1929522360138858496', 3, '', '用户注销', NULL, NULL, 'UserLogOut', NULL, 0, 0, 1, NULL, NULL, NULL, 100, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1934175912341798912, 1580826339636596737, '1580826339636596737-1934175912341798912', 2, NULL, '配置管理', '/system/setting', 'system/setting/index', 'Setting', NULL, 0, 0, 1, '', NULL, 'ant-design:setting-outlined', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1934176425720414208, 1934175912341798912, '1580826339636596737-1934175912341798912-1934176425720414208', 3, 'system:setting:page', '配置查询', NULL, NULL, 'SettingSelect', NULL, 0, 0, 1, NULL, NULL, NULL, 10, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1934176670181228544, 1934175912341798912, '1580826339636596737-1934175912341798912-1934176670181228544', 3, 'system:setting:save', '配置新增', NULL, NULL, 'SettingAdd', NULL, 0, 0, 1, NULL, NULL, '', 20, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1934177068728188928, 1934175912341798912, '1580826339636596737-1934175912341798912-1934177068728188928', 3, 'system:setting:modify', '配置修改', NULL, NULL, 'SettingEdit', NULL, 0, 0, 1, NULL, NULL, '', 40, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1934177217609203712, 1934175912341798912, '1580826339636596737-1934175912341798912-1934177217609203712', 3, 'system:setting:remove', '配置删除', NULL, NULL, 'SettingDel', NULL, 0, 0, 1, NULL, NULL, '', 30, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);
INSERT INTO sys_menu (id, parent_id, `structure`, `type`, perms, name, `path`, component, component_name, redirect, ext_flag, keep_alive, invisible, current_active_menu, closable, icon, sort, status, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1934177440544849920, 1934175912341798912, '1580826339636596737-1934175912341798912-1934177440544849920', 3, 'system:setting:export', '配置导出', NULL, NULL, 'SettingExport', NULL, 0, 0, 1, NULL, NULL, '', 50, 1, NULL, 0, 'admin', '2025-06-15 16:38:12', NULL, NULL, 0);

DROP TABLE IF EXISTS `sys_op_log`;
CREATE TABLE `sys_op_log`
(
    `id`          bigint NOT NULL COMMENT '主键编号',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
    `url`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求地址',
    `type`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '请求类型',
    `position`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方法',
    `req_body`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求体',
    `req_params`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
    `res_body`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应体',
    `start_time`  datetime                                                      DEFAULT NULL COMMENT '请求开始时间',
    `end_time`    datetime                                                      DEFAULT NULL COMMENT '请求结束时间',
    `consume`     int                                                           DEFAULT NULL COMMENT '请求间隔(毫秒)',
    `mdc`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'mdc信息',
    `trace_id`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '链路id',
    `ip`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '请求IP',
    `res_status`  smallint                                                      DEFAULT NULL COMMENT '响应状态',
    `operate_by`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '操作人',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `is_err`      tinyint                                                       DEFAULT NULL COMMENT '是否异常',
    `err_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '异常信息',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志';

DROP TABLE IF EXISTS `sys_port`;
CREATE TABLE `sys_port`
(
    `id`          bigint NOT NULL COMMENT '主键编号',
    `access_key`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端秘钥',
    `port`        int                                                           DEFAULT NULL COMMENT '主机端口',
    `endpoint`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端地址',
    `domain`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '域名',
    `expire_at`   datetime                                                      DEFAULT NULL COMMENT '过期时间',
    `status`      tinyint                                                       DEFAULT NULL COMMENT '状态;1-启用,0-禁用',
    `type`        int                                                           DEFAULT NULL COMMENT '协议类型',
    `sort`        int                                                           DEFAULT NULL COMMENT '排序',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint                                                        DEFAULT '0' COMMENT '版本号',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_delete`   tinyint                                                       DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`),
    KEY           `access_key_index` (`access_key`),
    KEY           `port_index` (`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='端口信息';

INSERT INTO `sys_port`
VALUES (1686385202043027456, '68bfe8f0af124ecfa093350ab8d4b44f', 80, '127.0.0.1:5173', NULL, '2025-04-27 13:59:09', 1,
        1, 10, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1791022592950272000, '68bfe8f0af124ecfa093350ab8d4b44f', 81, '127.0.0.1:5173', NULL, '2025-04-27 13:59:09', 0,
        1, 10, NULL, 0, 'admin', NOW(), NULL, NULL, 0);

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint NOT NULL COMMENT '主键编号',
    `code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '角色编码',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '角色名称',
    `status`      tinyint                                                       DEFAULT NULL COMMENT '状态;1-启用,0-禁用',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint                                                        DEFAULT '0' COMMENT '版本号',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_delete`   tinyint                                                       DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色信息';
INSERT INTO `sys_role`
VALUES (1620586462021824513, 'admin', '管理员', 1, '', 0, 'admin', NOW(), NULL, NULL, 0),
       (1748337956856266752, 'guest', '游客', 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0);

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `role_id` bigint NOT NULL COMMENT '角色id',
    `menu_id` bigint NOT NULL COMMENT '菜单id',
    KEY       `menu_index` (`menu_id`),
    KEY       `role_index` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单关联关系';
INSERT INTO `sys_role_menu`
VALUES (1748337956856266752, 1693466547550445570),
       (1748337956856266752, 1693497514038157313),
       (1748337956856266752, 1767096858825457664),
       (1748337956856266752, 1771802307621552128),
       (1748337956856266752, 1695716465233100800),
       (1748337956856266752, 1697491896345034752),
       (1748337956856266752, 1789206604688654336),
       (1748337956856266752, 1580826339636596737),
       (1620586462021824513, 1695715629383811072),
       (1620586462021824513, 1694016595833151489);

DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task`
(
    `id`              bigint                                                       NOT NULL COMMENT '任务ID',
    `name`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
    `content`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '任务内容',
    `param`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '任务参数',
    `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT 'cron执行表达式',
    `status`          tinyint                                                               DEFAULT NULL COMMENT '任务状态;1-启用,0-禁用',
    `remark`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '备注信息',
    `version`         bigint                                                                DEFAULT '0' COMMENT '版本号',
    `create_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '创建人',
    `create_time`     datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '修改人',
    `update_time`     datetime                                                              DEFAULT NULL COMMENT '修改时间',
    `is_delete`       tinyint                                                               DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='计划任务信息';
INSERT INTO `sys_task`
VALUES (1773971312662806528, '内网映射数据刷新', 'top.ticho.rainbow.application.task.IntranetTask', '',
        '0 0/5 * * * ? *', 0, NULL,
        0, 'admin', NOW(), NULL, NULL, 0),
       (1783736600820187136, '缓存文件清除', 'top.ticho.rainbow.application.task.FileTmpClearTask', NULL,
        '0 0 0 * * ? *', 1, NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1784442553299369984, '日志清除', 'top.ticho.rainbow.application.task.LogClearTask', '1', '0 0 0 * * ? *', 1,
        NULL, 0, 'admin', NOW(), NULL, NULL, 0),
       (1791389321148760064, '邮件发送', 'top.ticho.rainbow.application.task.EmailTask',
        '{\n  \"to\": \"1019319473@qq.com\",\n  \"subject\": \"问候\",\n  \"content\": \"早上好\"\n}', '0 0 0 * * ? *',
        1, NULL, 0, 'admin', NOW(), NULL, NULL, 0);


DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint NOT NULL COMMENT '主键编号',
    `username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '账号;具有唯一性',
    `password`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
    `nickname`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '昵称',
    `realname`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '真实姓名',
    `idcard`      varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '身份证号',
    `sex`         tinyint                                                       DEFAULT NULL COMMENT '性别;0-男,1-女',
    `age`         tinyint                                                       DEFAULT NULL COMMENT '年龄',
    `birthday`    date                                                          DEFAULT NULL COMMENT '出生日期',
    `address`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '家庭住址',
    `education`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '学历',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '邮箱',
    `qq`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT 'QQ号码',
    `wechat`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '微信号码',
    `mobile`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '手机号码',
    `photo`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像地址',
    `last_ip`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '最后登录ip地址',
    `last_time`   datetime                                                      DEFAULT NULL COMMENT '最后登录时间',
    `status`      tinyint                                                       DEFAULT NULL COMMENT '用户状态;1-正常,2-未激活,3-已锁定,4-已注销',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint                                                        DEFAULT '0' COMMENT '版本号',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                                                      DEFAULT NULL COMMENT '修改时间',
    `is_delete`   tinyint                                                       DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`),
    KEY           `username_index` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息';
INSERT INTO `sys_user`
VALUES (1580743544448905218, 'admin', '$2a$10$zfkCMIaIthoTTSqSMHWByOdu7qiXUXbRWyEASK2vxUcHzZy3Drplu', '管理员',
        '管理员', NULL, NULL, NULL, NULL, NULL, NULL, '1@qq.com', '1', NULL, '1', '1785234662445023232', NULL, NULL, 1,
        '管理员', 0, 'admin', NOW(), NULL, NULL, 0),
       (1748338125299515392, 'guest', '$2a$10$xvyzDFRdCu2B9x7w8kHlbu.sYhPDcrYi0BmGrF1WTmErVEmhe/twu', '游客', '游客',
        NULL, NULL, NULL, NULL, NULL, NULL, '2@qq.com', NULL, NULL, '2', NULL, NULL, NULL, 1, '游客', 0, 'admin',
        NOW(), NULL, NULL, 0),
       (1789842367612715008, 'zhangsan', '$2a$10$LI76j94IKi3dTVKbZXoPu.KKCe7Le.NL83MLLhXh3fzvFTMTcdU5C', '张三', '张三',
        NULL, NULL, NULL, NULL, NULL, NULL, '1019319473@qq.com', NULL, NULL, '13966981511', NULL, NULL, NULL, 1, NULL,
        NULL, 'admin', NOW(), NULL, NULL, 0);


DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id` bigint NOT NULL COMMENT '用户id',
    `role_id` bigint NOT NULL COMMENT '角色id',
    KEY       `role_index` (`role_id`),
    KEY       `user_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联关系';
INSERT INTO `sys_user_role`
VALUES (1580743544448905218, 1620586462021824513),
       (1748338125299515392, 1748337956856266752),
       (1789842367612715008, 1748337956856266752);

DROP TABLE IF EXISTS `sys_task_log`;
CREATE TABLE `sys_task_log`
(
    `id`           bigint NOT NULL COMMENT '主键编号',
    `task_id`      bigint NOT NULL COMMENT '任务ID',
    `content`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '任务内容',
    `param`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务参数',
    `execute_time` datetime                                                      DEFAULT NULL COMMENT '执行时间',
    `start_time`   datetime                                                      DEFAULT NULL COMMENT '执行开始时间',
    `end_time`     datetime                                                      DEFAULT NULL COMMENT '执行结束时间',
    `consume`      int                                                           DEFAULT NULL COMMENT '执行间隔(毫秒)',
    `mdc`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'mdc信息',
    `trace_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '链路id',
    `status`       tinyint                                                       DEFAULT NULL COMMENT '执行状态;1-执行成功,0-执行异常',
    `operate_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '操作人',
    `create_time`  datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `is_err`       tinyint                                                       DEFAULT NULL COMMENT '是否异常',
    `err_message`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '异常信息',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='计划任务日志';

DROP TABLE IF EXISTS `sys_setting`;
CREATE TABLE `sys_setting` (
  `id` bigint NOT NULL COMMENT '主键编号',
  `key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'key',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'value',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` bigint DEFAULT '0' COMMENT '版本号',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_delete` tinyint DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配置信息';

INSERT INTO sys_setting (id, `key`, value, sort, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1936273744880205824, 'LOGIN_MODE', '2', 10, '登录模式;1-无,2-验证码', 0, 'admin', NOW(), NULL, NULL, 0);
INSERT INTO sys_setting (id, `key`, value, sort, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES(1936612686892630016, 'INIT_PASSWORD', '123456', 20, '默认密码', 0, 'admin', NOW(), NULL, NULL, 0);
