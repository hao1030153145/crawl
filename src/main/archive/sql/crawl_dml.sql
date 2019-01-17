


CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'userid自增序列',
  `mobile` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '移动手机号',
  `passwd` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述信息',
  `avatar` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `birthday` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '生日',
  `token` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '注册后的apikey',
  `createtime` datetime NOT NULL COMMENT '创建日期',
  `lastmodifytime` datetime NOT NULL,
  `sex` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0 无 ，1为男性，2为女性',
  `province` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `city` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `country` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `source` tinyint(1) NOT NULL DEFAULT '1' COMMENT '来源网站，1代表weixin，2代表微博',
  PRIMARY KEY (`uid`),
  KEY `idx_user_mobile` (`mobile`) USING BTREE,
  KEY `idx_user_token` (`token`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `boss_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'userid自增序列',
  `passwd` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'username',
  `nickname` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述信息',
  `avatar` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `type` tinyint(3) NOT NULL DEFAULT '1' COMMENT '用户类型， 1 是普通用户 ，2 是管理员',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮件地址',
  `createtime` datetime NOT NULL COMMENT '创建日期',
  `lastmodifytime` datetime NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `idx_user_name` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `boss_user` (`passwd`, `username`, `nickname`,description, avatar,type,email,createtime, `lastmodifytime`)
VALUES (password('admin111'), 'admin','管理员','管理员','',2,'admin', now(), now());

INSERT INTO  `user` (`uid`, `mobile`, `passwd`, `nickname`, `description`, `avatar`, `birthday`, `token`, `createtime`, `lastmodifytime`, `sex`, `province`, `city`, `country`, `source`)
VALUES ('1', '123', '123', '123', '123', '123', '123', '123', '2017-02-04 15:11:36', '2017-02-04 15:11:39', '0', '1', '1', '1', '1');


INSERT INTO `storage_type_field_type` VALUES ('1', 'int');
INSERT INTO `storage_type_field_type` VALUES ('2', 'text');
INSERT INTO `storage_type_field_type` VALUES ('3', 'datetime');
INSERT INTO `storage_type_field_type` VALUES ('4', 'float');

INSERT INTO `processor` VALUES ('1', '正则处理器', null, '1', '2017-06-23 15:57:05', '');
INSERT INTO `processor` VALUES ('2', 'javascript', null, '2', '2017-06-23 15:57:34', '');

INSERT INTO `analysis_type` VALUES ('1', 'XPATH');
INSERT INTO `analysis_type` VALUES ('2', 'REGEX');
INSERT INTO `analysis_type` VALUES ('3', 'JSON');

INSERT INTO `style` VALUES ('1', 'input', '文本框', null, '2017-06-28 11:57:11', '2017-06-28 11:57:14');
INSERT INTO `style` VALUES ('2', 'datetime', '时间选择器', null, '2017-06-28 11:57:34', '2017-06-28 11:57:37');
INSERT INTO `style` VALUES ('3', 'checkbox', '选择框', null, '2017-06-28 11:59:31', '2017-06-28 11:59:33');
INSERT INTO `style` VALUES ('4', 'file', '上传文件', null, '2017-06-28 11:59:57', '2017-06-28 12:00:00');
INSERT INTO `style` VALUES ('5', 'input-file', '输入与上传', null, '2017-06-28 12:00:35', '2017-06-28 12:00:37');


--v1.1.0---

INSERT INTO `processor` VALUES ('3', '新浪微博', 'com.transing.crawl.util.processor.impl.WeiboProcessor', '3', '2017-08-07 18:45:59', null);
--v1.1.1---
INSERT INTO `processor` VALUES ('4', '环球搜索', 'com.transing.crawl.util.processor.impl.HuanQiuProcessor', '4', '2017-08-16 15:57:34', '');
INSERT INTO `processor` VALUES ('5', '汽车之家论坛', 'com.transing.crawl.util.processor.impl.AutoHomeProcessor', '5', '2017-08-16 15:57:34', '');
INSERT INTO `analysis_type` VALUES ('4', 'FIXED');

--v1.2.0--
INSERT INTO `processor` VALUES ('6', '组合时间处理器', 'com.transing.crawl.util.processor.impl.TimeIntervalSplitProcessor', '6', '2017-08-16 15:57:34', '');
INSERT INTO `processor` VALUES ('7', '参数模板处理器', 'com.transing.crawl.util.processor.impl.ParameterTemplateProcessor', '7', '2017-08-16 15:57:34', '');
INSERT INTO `style` VALUES ('7', 'page', '分页器', null, '2017-09-06 10:27:11', '2017-09-06 10:27:13');

--v1.3.0.e--
INSERT INTO `processor` VALUES ('8', '重定向处理器', 'com.transing.crawl.util.processor.impl.RedirectProcessor', '8', '2017-08-16 15:57:34', '');


--v1.3.1--
DELETE FROM `processor`;
INSERT INTO `processor` VALUES ('1', '正则处理器', 'com.transing.crawl.util.processor.impl.commonProcessors.BuiltInProcessor', '1', '2017-06-23 15:57:05', 'commonProc');
INSERT INTO `processor` VALUES ('2', 'javascript', 'com.transing.crawl.util.processor.impl.commonProcessors.BuiltInProcessor', '2', '2017-06-23 15:57:34', 'commonProc');
INSERT INTO `processor` VALUES ('3', '新浪微博', 'com.transing.crawl.util.processor.impl.preProcessors.WeiboProcessor', '3', '2017-08-07 18:45:59', 'preposeProc');
INSERT INTO `processor` VALUES ('4', '环球搜索', 'com.transing.crawl.util.processor.impl.preProcessors.HuanQiuProcessor', '4', '2017-08-16 15:57:34', 'preposeProc');
INSERT INTO `processor` VALUES ('5', '汽车之家论坛', 'com.transing.crawl.util.processor.impl.preProcessors.AutoHomeProcessor', '5', '2017-08-16 15:57:34', 'preposeProc');
INSERT INTO `processor` VALUES ('6', '组合时间处理器', 'com.transing.crawl.util.processor.impl.paramProcessors.TimeIntervalSplitProcessor', '6', '2017-09-06 09:47:48', 'paramProc');
INSERT INTO `processor` VALUES ('7', '参数模板处理器', 'com.transing.crawl.util.processor.impl.paramProcessors.ParameterTemplateProcessor', '7', '2017-09-06 09:51:40', 'paramProc');
INSERT INTO `processor` VALUES ('8', '重定向处理器', 'com.transing.crawl.util.processor.impl.paramProcessors.RedirectProcessor', '8', '2017-08-16 15:57:34', 'paramProc');
INSERT INTO `processor` VALUES ('9', '标签过滤处理器', 'com.transing.crawl.util.processor.impl.SuffProcessors.TagFilterProcessor', '9', '2017-11-21 11:13:28', 'suffixProc');