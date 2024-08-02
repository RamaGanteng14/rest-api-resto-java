/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : wms_part2

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 02/08/2024 12:35:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for branches
-- ----------------------------
DROP TABLE IF EXISTS `branches`;
CREATE TABLE `branches`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `branch_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `branch_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_branch_code`(`branch_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of branches
-- ----------------------------
INSERT INTO `branches` VALUES ('04aebf59-fd5d-4c1b-9d03-44962b50ee98', '0201', 'Cilandak', 'Jl. Cilandak Raya No.XX', '08123441234');
INSERT INTO `branches` VALUES ('10dbe9c4-2c3d-4ab7-861e-f4b589abec1e', '0206', 'cibubur', 'cibubur raya', '123456789');
INSERT INTO `branches` VALUES ('2d72867c-5f32-40ef-9a05-b72aabe1bcff', '0205', 'Cilandak', 'Jl. Cilandak Raya No.XX', '08123441234');
INSERT INTO `branches` VALUES ('32efe1e0-0115-4cbb-81cf-f69399b7a00f', '0202', 'Cilandak', 'Jl. Cilandak Raya No.XX', '08123441234');

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `product_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `price` bigint NULL DEFAULT NULL,
  `branch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES ('3f8cfd3a-460a-4d83-b002-969efb0c687d', 'P001', 'APPLE ', 15000, '04aebf59-fd5d-4c1b-9d03-44962b50ee98');
INSERT INTO `products` VALUES ('484799b3-bcd6-45a8-81f1-cb25b48fcf7f', 'P0021', 'Example Product e', 15000, '32efe1e0-0115-4cbb-81cf-f69399b7a00f');
INSERT INTO `products` VALUES ('5a66086a-6d6e-400c-84cc-a3b37d0867f7', '0201', 'mie mie', 2000, '32efe1e0-0115-4cbb-81cf-f69399b7a00f');

-- ----------------------------
-- Table structure for transactions
-- ----------------------------
DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trans_date` date NULL DEFAULT NULL,
  `receipt_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `transaction_type` enum('ONLINE','EAT_IN','TAKE_AWAY') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `total` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transactions
-- ----------------------------
INSERT INTO `transactions` VALUES ('31925be4-8e21-4d3b-ab2f-95ed96bfbb66', '2024-08-02', '0201-2024-0002', 'ONLINE', 360000);
INSERT INTO `transactions` VALUES ('4855bf8a-ed95-4ccd-8e95-f25477a0a578', '2024-08-02', '0201-2024-0003', 'TAKE_AWAY', 360000);
INSERT INTO `transactions` VALUES ('58124f7d-ce39-4931-8c47-04708ef077c3', '2024-08-02', '0201-2024-0004', 'TAKE_AWAY', 360000);
INSERT INTO `transactions` VALUES ('6d5f1fb6-5212-4e99-b834-b1cda984a3d0', '2024-08-01', '0201-2024-0001', 'EAT_IN', 150000);

-- ----------------------------
-- Table structure for transactions_detail
-- ----------------------------
DROP TABLE IF EXISTS `transactions_detail`;
CREATE TABLE `transactions_detail`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trans_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `quantity` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transactions_detail
-- ----------------------------
INSERT INTO `transactions_detail` VALUES ('3e04fa53-77a7-4d47-801c-d21784ff1441', '4855bf8a-ed95-4ccd-8e95-f25477a0a578', '484799b3-bcd6-45a8-81f1-cb25b48fcf7f', 5);
INSERT INTO `transactions_detail` VALUES ('65fb1651-53b4-48a7-a6f1-ddfb21417e77', '58124f7d-ce39-4931-8c47-04708ef077c3', '3f8cfd3a-460a-4d83-b002-969efb0c687d', 19);
INSERT INTO `transactions_detail` VALUES ('69155351-9f40-41fb-806c-1b069c9135d6', '31925be4-8e21-4d3b-ab2f-95ed96bfbb66', '484799b3-bcd6-45a8-81f1-cb25b48fcf7f', 5);
INSERT INTO `transactions_detail` VALUES ('8bc203a7-038a-472a-9018-24ba2941bf7c', '6d5f1fb6-5212-4e99-b834-b1cda984a3d0', '3f8cfd3a-460a-4d83-b002-969efb0c687d', 3);
INSERT INTO `transactions_detail` VALUES ('8de889c7-a1a8-40ce-b2c7-ab52421d9d6d', '4855bf8a-ed95-4ccd-8e95-f25477a0a578', '3f8cfd3a-460a-4d83-b002-969efb0c687d', 19);
INSERT INTO `transactions_detail` VALUES ('943655b9-6e0a-4166-baf1-7b2279b88c45', '58124f7d-ce39-4931-8c47-04708ef077c3', '484799b3-bcd6-45a8-81f1-cb25b48fcf7f', 5);
INSERT INTO `transactions_detail` VALUES ('d46d92ae-98b8-4e8b-b517-ddbcc06e43aa', '6d5f1fb6-5212-4e99-b834-b1cda984a3d0', '484799b3-bcd6-45a8-81f1-cb25b48fcf7f', 7);
INSERT INTO `transactions_detail` VALUES ('d7228c63-350c-4fa3-996e-5ddf7094edc1', '31925be4-8e21-4d3b-ab2f-95ed96bfbb66', '3f8cfd3a-460a-4d83-b002-969efb0c687d', 19);

SET FOREIGN_KEY_CHECKS = 1;
