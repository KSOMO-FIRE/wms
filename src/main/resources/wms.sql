/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3305
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3305
 Source Schema         : wms

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 19/04/2025 16:09:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '货名',
  `storage` int NOT NULL COMMENT '仓库',
  `goodsType` int NOT NULL COMMENT '分类',
  `count` int NULL DEFAULT NULL COMMENT '数量',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `specification` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '规格',
  `shelfLife` date NULL DEFAULT NULL COMMENT '保质期',
  `imageUrl` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '商品图片',
  `productionDate` date NULL DEFAULT NULL COMMENT '生产日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, '康师傅桶面', 2, 1, 20, 'nothing', '1*24', '2025-04-18', 'https://img-s.msn.cn/tenant/amp/entityid/AA1D5fS0.img?w=768&h=1024&m=6&x=430&y=124&s=234&d=457', '2025-06-01');

-- ----------------------------
-- Table structure for goodstype
-- ----------------------------
DROP TABLE IF EXISTS `goodstype`;
CREATE TABLE `goodstype`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类名',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goodstype
-- ----------------------------
INSERT INTO `goodstype` VALUES (1, '方便面', NULL);
INSERT INTO `goodstype` VALUES (2, '面包', '');
INSERT INTO `goodstype` VALUES (3, '饼干', '');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int NOT NULL,
  `menuCode` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单编码',
  `menuName` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单名字',
  `menuLevel` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单级别',
  `menuParentCode` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单的父code',
  `menuClick` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '点击触发的函数',
  `menuRight` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '权限 0超级管理员，1表示管理员，2表示普通用户，可以用逗号组合使用',
  `menuComponent` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `menuIcon` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '001', '管理员管理', '1', NULL, 'Admin', '0', 'admin/AdminManage.vue', 'el-icon-s-custom');
INSERT INTO `menu` VALUES (2, '002', '用户管理', '1', NULL, 'User', '0,1', 'user/UserManage.vue', 'el-icon-user-solid');
INSERT INTO `menu` VALUES (3, '003', '仓库管理', '1', NULL, 'Storage', '0,1', 'storage/StorageManage', 'el-icon-office-building');
INSERT INTO `menu` VALUES (4, '004', '分类管理', '1', NULL, 'Goodstype', '0,1', 'goodstype/GoodstypeManage', 'el-icon-menu');
INSERT INTO `menu` VALUES (5, '005', '物品管理', '1', NULL, 'Goods', '0,1,2', 'goods/GoodsManage', 'el-icon-shopping-bag-1');
INSERT INTO `menu` VALUES (6, '006', '记录管理', '1', NULL, 'Record', '0,1,2', 'record/RecordManage', 'el-icon-s-order');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `goods` int NOT NULL COMMENT '货品id',
  `userId` int NULL DEFAULT NULL COMMENT '取货人/补货人',
  `admin_id` int NULL DEFAULT NULL COMMENT '操作人id',
  `count` int NULL DEFAULT NULL COMMENT '数量',
  `createtime` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (1, 1, 4, 2, 10, '2025-03-07 13:24:58', NULL);
INSERT INTO `record` VALUES (2, 1, NULL, 1, 10, NULL, '');

-- ----------------------------
-- Table structure for storage
-- ----------------------------
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '仓库名',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of storage
-- ----------------------------
INSERT INTO `storage` VALUES (1, '2222', NULL);
INSERT INTO `storage` VALUES (2, '1111', '');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '名字',
  `password` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `sex` int NULL DEFAULT NULL COMMENT '性别(男1，女0）',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电话',
  `role_id` int NULL DEFAULT NULL COMMENT '角色 0超级管理员，1管理员，2普通账号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMPRESSION = 'LZ4' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'sa', '超级管理员', '123', 18, 1, '111', 0);
INSERT INTO `user` VALUES (2, 'admin', '王小明', '123456', 20, 0, '11432001012', 1);
INSERT INTO `user` VALUES (3, 'user+', '王小明1', '123456', 19, 1, '123456789', 2);
INSERT INTO `user` VALUES (4, 'user1', '王小明2', '123456', 22, 1, '123456789', 2);
INSERT INTO `user` VALUES (6, 'admin1', '天道', '123456', 22, 1, '15172525855', 2);
INSERT INTO `user` VALUES (7, 'admin2', '总司', '123456', 22, 1, '15909349711', 1);

SET FOREIGN_KEY_CHECKS = 1;
