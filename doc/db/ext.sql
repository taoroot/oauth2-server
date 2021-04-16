DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '角色标识',
  `remark` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='角色表';


DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `mr_index` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色用户表';


DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL NULL COMMENT '菜单ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `mr_index` (`menu_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';


DROP TABLE IF EXISTS `sys_menu_authority`;
CREATE TABLE `sys_menu_authority` (
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `authority_id` int(11) NOT NULL COMMENT '权限ID',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_id` (`menu_id`,`authority_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限关联表';


DROP TABLE IF EXISTS `sys_authority`;
CREATE TABLE `sys_authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `authority` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `name` varchar(350) DEFAULT NULL COMMENT 'the name is used by <keep-alive> (must set!!!)',
  `path` varchar(250) DEFAULT NULL,
  `method` varchar(250) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `sys_authority_index` (`authority`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限表';



DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` int(11) NOT NULL COMMENT '父菜单ID',
  `path` varchar(128) DEFAULT NULL,
  `title` varchar(32) DEFAULT NULL COMMENT 'the name show in sidebar and breadcrumb (recommend set)',
  `name` varchar(350) DEFAULT NULL COMMENT 'the name is used by <keep-alive> (must set!!!)',
  `component` varchar(64) DEFAULT NULL,
  `always_show` varchar(255) DEFAULT '0' COMMENT '0-开启，1- 关闭',
  `redirect` varchar(32) DEFAULT NULL COMMENT 'if set noRedirect will no redirect in the breadcrumb',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `weight` int(11) NOT NULL DEFAULT '1' COMMENT '排序值',
  `type` char(1) NOT NULL DEFAULT '0' COMMENT '菜单类型 （0菜单 1按钮）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `breadcrumb` int(1) DEFAULT '0',
  `hidden` varchar(255) DEFAULT '0' COMMENT '0-开启，1- 关闭',
  `authority` varchar(100) DEFAULT NULL,
  `no_cache` int(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1127 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单表';
