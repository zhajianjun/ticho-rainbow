CREATE DATABASE if not exists `ticho_intranet` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
use `ticho_intranet`;

CREATE table if not exists `sys_client`
(
    `id`          bigint(20) NOT NULL COMMENT '主键标识',
    `access_key`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端秘钥',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '客户端名称',
    `enabled`     tinyint(1)                                                    DEFAULT NULL COMMENT '是否开启;1-开启,0-关闭',
    `sort`        int(11)                                                       DEFAULT NULL COMMENT '排序',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint(20)                                                    DEFAULT '0' COMMENT '乐观锁;控制版本更改',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime                                                      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`   tinyint(1)                                                    DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`),
    KEY `access_key_index` (`access_key`),
    KEY `name_index` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='客户端信息';


CREATE TABLE if not exists `sys_port`
(
    `id`          bigint(20) NOT NULL COMMENT '主键标识',
    `access_key`  varchar(256) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '客户端秘钥',
    `port`        int(11)                                                       DEFAULT NULL COMMENT '主机端口',
    `endpoint`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端地址',
    `domain`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '域名',
    `enabled`     tinyint(1)                                                    DEFAULT NULL COMMENT '是否开启;1-开启,0-关闭',
    `forever`     tinyint(1)                                                    DEFAULT NULL COMMENT '是否永久;1-是,0-否',
    `expire_at`   datetime                                                      DEFAULT NULL COMMENT '过期时间',
    `type`        int(11)                                                       DEFAULT NULL COMMENT '协议类型',
    `sort`        int(11)                                                       DEFAULT NULL COMMENT '排序',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint(20)                                                    DEFAULT '0' COMMENT '乐观锁;控制版本更改',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime                                                      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`   tinyint(1)                                                    DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`),
    KEY `port_index` (`port`),
    KEY `access_key_index` (`access_key`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='端口信息';

CREATE TABLE if not exists `sys_user`
(
    `id`          bigint(20) NOT NULL COMMENT '主键标识',
    `username`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
    `password`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
    `remark`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
    `version`     bigint(20)                                                    DEFAULT '0' COMMENT '乐观锁;控制版本更改',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime                                                      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`   tinyint(1)                                                    DEFAULT '0' COMMENT '删除标识;0-未删除,1-已删除',
    PRIMARY KEY (`id`),
    KEY `username_index` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户信息';

# # 用户密码 admin/123456
INSERT INTO sys_user (id, username, password, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES (1595236845495468033, 'admin', '$2a$10$E/mhGMUGKHcn8G1VYTDdXut4YgxmbtZ2qj7WmUrEN3/rlhwe9aVzG', '管理员', 0, 'admin', '2023-01-01 00:00:00', 'admin', '2023-01-01 00:00:00', 0);

INSERT INTO sys_client (id, access_key, name, enabled, sort, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES (1590574652453814272, '68bfe8f0af124ecfa093350ab8d4b44f', '1019319473@qq.com', 1, 10, '', 0, NULL, '2023-01-01 00:00:00', 'admin', '2023-01-01 00:00:00', 0);

INSERT INTO sys_port (id, access_key, port, endpoint, `domain`, enabled, forever, expire_at, `type`, sort, remark, version, create_by, create_time, update_by, update_time, is_delete) VALUES (1608692604372066305, '68bfe8f0af124ecfa093350ab8d4b44f', 8888, '127.0.0.1:5122', '', 1, 1, '2023-01-01 00:00:00', 6, 10070, null, 0, 'admin', '2023-01-01 00:00:00', 'admin', '2023-01-01 00:00:00', 0);