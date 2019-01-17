/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50543
Source Host           : 127.0.0.1:3306
Source Database       : crawl

Target Server Type    : MYSQL
Target Server Version : 50543
File Encoding         : 65001

Date: 2017-06-28 14:02:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for analysis_type
-- ----------------------------
DROP TABLE IF EXISTS `analysis_type`;
CREATE TABLE `analysis_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `analysis_name` varchar(20) DEFAULT NULL COMMENT '解析名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_account
-- ----------------------------
DROP TABLE IF EXISTS `crawl_account`;
CREATE TABLE `crawl_account` (
  `id` int(11) NOT NULL,
  `datasource_id` int(11) NOT NULL DEFAULT '0' COMMENT '数据源id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(30) NOT NULL COMMENT '密码',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(30) DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(20) DEFAULT NULL COMMENT '昵称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抓取账号表';

-- ----------------------------
-- Table structure for crawl_account_strategy
-- ----------------------------
DROP TABLE IF EXISTS `crawl_account_strategy`;
CREATE TABLE `crawl_account_strategy` (
  `id` int(11) NOT NULL,
  `datasource_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `request_interval` int(11) DEFAULT NULL COMMENT '抓取间隔',
  `max_req_num_minute` int(11) DEFAULT NULL COMMENT '每分钟最大次数',
  `max_req_num_hour` int(11) DEFAULT NULL COMMENT '每小时最大次数',
  `max_req_num_day` int(11) DEFAULT NULL COMMENT '每天最大次数',
  `max_login_num_ip_hour` int(11) DEFAULT NULL COMMENT '每小时每个ip登陆次数',
  `login_interval` int(11) DEFAULT NULL COMMENT '登陆间隔',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_input_param
-- ----------------------------
DROP TABLE IF EXISTS `crawl_input_param`;
CREATE TABLE `crawl_input_param` (
  `id` int(11) NOT NULL,
  `datasource_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `datasource_type_id` int(11) DEFAULT NULL COMMENT '数据源类型id',
  `param_cn_name` varchar(50) DEFAULT NULL COMMENT '参数中文名',
  `param_en_name` varchar(50) DEFAULT NULL COMMENT '参数英文名',
  `prompt` varchar(500) DEFAULT NULL COMMENT '提示',
  `style_id` int(11) DEFAULT NULL COMMENT '样式id',
  `restrictions` text COMMENT '约束条件',
  `is_required` int(11) DEFAULT NULL COMMENT '是否必填（1为必填，0为不必填）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='输入参数';

-- ----------------------------
-- Table structure for crawl_rule
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule`;
CREATE TABLE `crawl_rule` (
  `id` int(11) NOT NULL,
  `datasource_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `datasource_type_id` int(11) DEFAULT NULL COMMENT '数据源类型id',
  `rule_name` varchar(200) DEFAULT NULL COMMENT '规则名称',
  `request_method` varchar(5) DEFAULT NULL COMMENT '请求方法（get为get请求，post为post请求，put为put请求，delete为delete请求）',
  `request_encoding` varchar(10) DEFAULT NULL COMMENT '请求编码（utf-8为utf-8，gbk为gkb）',
  `response_encoding` varchar(10) DEFAULT NULL COMMENT '响应编码（utf-8为utf-8，gbk为gkb）',
  `crawl_url` varchar(500) DEFAULT NULL COMMENT '抓取网址模板',
  `test_url` varchar(2000) DEFAULT NULL COMMENT '测试网址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_rule_detail
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_detail`;
CREATE TABLE `crawl_rule_detail` (
  `id` int(11) NOT NULL,
  `rule_id` int(11) DEFAULT NULL COMMENT '抓取规则id',
  `page_type` int(11) DEFAULT NULL COMMENT '页面类型（1为列表，2为内容）',
  `status` tinyint(4) DEFAULT NULL COMMENT '通过状态（0不通过，1通过）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `detail_name` varchar(20) DEFAULT NULL COMMENT '抓取规则名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_rule_detail_field
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_detail_field`;
CREATE TABLE `crawl_rule_detail_field` (
  `id` int(11) NOT NULL,
  `crawl_rule_detail_id` int(11) NOT NULL COMMENT '页面抓取规则id',
  `rule_id` int(11) DEFAULT NULL COMMENT '抓取规则id',
  `storage_type_field_id` int(11) DEFAULT NULL COMMENT '存储字段id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_rule_detail_field_parse
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_detail_field_parse`;
CREATE TABLE `crawl_rule_detail_field_parse` (
  `id` int(11) NOT NULL,
  `detail_field_id` int(11) DEFAULT NULL,
  `parse_type` int(11) DEFAULT NULL COMMENT '解析方式（1xpath，2正则表达式，3json）',
  `parse_expression` varchar(500) DEFAULT NULL COMMENT '解析表达式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_rule_detail_field_suff_proc
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_detail_field_suff_proc`;
CREATE TABLE `crawl_rule_detail_field_suff_proc` (
  `id` int(11) NOT NULL,
  `detail_field_id` int(11) DEFAULT NULL,
  `processor_id` int(11) DEFAULT NULL COMMENT '处理器id',
  `processor_value` varchar(5000) DEFAULT NULL COMMENT '处理器输入的值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_rule_detail_parse
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_detail_parse`;
CREATE TABLE `crawl_rule_detail_parse` (
  `id` int(11) NOT NULL,
  `crawl_rule_detail_id` int(11) DEFAULT NULL,
  `parse_type` int(11) DEFAULT NULL COMMENT '解析方式（1xpath，2正则表达式，3json）',
  `parse_expression` varchar(500) DEFAULT NULL COMMENT '解析表达式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面规则的解析 解析方式表';

-- ----------------------------
-- Table structure for crawl_rule_detail_pre_proc
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_detail_pre_proc`;
CREATE TABLE `crawl_rule_detail_pre_proc` (
  `id` int(11) NOT NULL,
  `crawl_rule_detail_id` int(11) DEFAULT NULL,
  `processor_id` int(11) DEFAULT NULL COMMENT '处理器id',
  `processor_value` varchar(5000) DEFAULT NULL COMMENT '处理器输入的值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面抓取规则前置处理器';

-- ----------------------------
-- Table structure for crawl_rule_detail_suff_proc
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_detail_suff_proc`;
CREATE TABLE `crawl_rule_detail_suff_proc` (
  `id` int(11) NOT NULL,
  `crawl_rule_detail_id` int(11) DEFAULT NULL,
  `processor_id` int(11) DEFAULT NULL COMMENT '处理器id',
  `processor_value` varchar(5000) DEFAULT NULL COMMENT '处理器输入的值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面规则的后置处理器';

-- ----------------------------
-- Table structure for crawl_rule_page
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_page`;
CREATE TABLE `crawl_rule_page` (
  `id` int(11) NOT NULL,
  `crawl_rule_id` int(11) DEFAULT NULL,
  `first_url` varchar(300) DEFAULT NULL COMMENT '分页首页',
  `calculation` int(11) DEFAULT NULL COMMENT '计算方式（1，批量替换，2总页码，3总条数，4下一页）',
  `format_type` int(11) DEFAULT NULL COMMENT '格式(1为数字，2为日期，3为字母)',
  `start_page` int(11) DEFAULT NULL COMMENT '开始页',
  `end_page` int(11) DEFAULT NULL COMMENT '结束页',
  `step_length` int(11) DEFAULT NULL COMMENT '步长',
  `fomat` varchar(10) DEFAULT NULL COMMENT '格式样式',
  `end_page_identifier` varchar(300) DEFAULT NULL COMMENT '结束标识符',
  `end_page_similarity` int(11) DEFAULT NULL COMMENT '相识度',
  `page_size` int(11) DEFAULT NULL COMMENT '每页条数',
  `max_page` int(11) DEFAULT NULL COMMENT '最大页码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_rule_page_parse
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_page_parse`;
CREATE TABLE `crawl_rule_page_parse` (
  `id` int(11) NOT NULL,
  `crawl_rule_page_id` int(11) DEFAULT NULL,
  `parse_type` int(11) DEFAULT NULL COMMENT '解析方式（1xpath，2正则表达式，3json）',
  `parse_expression` varchar(500) DEFAULT NULL COMMENT '解析表达式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抓取规则分页 解析方式表';

-- ----------------------------
-- Table structure for crawl_rule_page_suff_proc
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_page_suff_proc`;
CREATE TABLE `crawl_rule_page_suff_proc` (
  `id` int(11) NOT NULL,
  `crawl_rule_page_id` int(11) DEFAULT NULL COMMENT '抓取规则分页id',
  `processor_id` int(11) DEFAULT NULL COMMENT '处理器id',
  `processor_value` varchar(5000) DEFAULT NULL COMMENT '处理器输入的值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抓取规则请求头部后置处理器';

-- ----------------------------
-- Table structure for crawl_rule_request_header
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_request_header`;
CREATE TABLE `crawl_rule_request_header` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `crawl_rule_id` int(11) DEFAULT NULL,
  `header_name` varchar(30) DEFAULT NULL COMMENT '参数名',
  `header_type` int(11) DEFAULT NULL COMMENT '参数类型（1为url，2为form）',
  `input_type` int(11) DEFAULT NULL COMMENT '输入类型（1为输入值，2为选择输入参数，3为内部变量）',
  `header_value` varchar(2000) DEFAULT NULL COMMENT '参数值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_rule_request_param
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_request_param`;
CREATE TABLE `crawl_rule_request_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `crawl_rule_id` int(11) NOT NULL,
  `param_name` varchar(30) DEFAULT NULL COMMENT '参数名',
  `param_type` int(11) DEFAULT NULL COMMENT '参数类型（1为url，2为form）',
  `input_type` int(11) DEFAULT NULL COMMENT '输入类型（1为输入值，2为选择输入参数，3为内部变量）',
  `param_value` varchar(50) DEFAULT NULL COMMENT '参数值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_rule_req_header_suff_proc
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_req_header_suff_proc`;
CREATE TABLE `crawl_rule_req_header_suff_proc` (
  `id` int(11) NOT NULL,
  `request_header_id` int(11) DEFAULT NULL COMMENT '请求参数id',
  `processor_id` int(11) DEFAULT NULL COMMENT '处理器id',
  `processor_value` varchar(5000) DEFAULT NULL COMMENT '处理器输入的值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抓取规则请求头部后置处理器';

-- ----------------------------
-- Table structure for crawl_rule_req_param_suff_proc
-- ----------------------------
DROP TABLE IF EXISTS `crawl_rule_req_param_suff_proc`;
CREATE TABLE `crawl_rule_req_param_suff_proc` (
  `id` int(11) NOT NULL,
  `request_param_id` int(11) DEFAULT NULL COMMENT '请求参数id',
  `processor_id` int(11) DEFAULT NULL COMMENT '处理器id',
  `processor_value` varchar(5000) DEFAULT NULL COMMENT '处理器输入的值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抓取规则请求参数后置处理器';

-- ----------------------------
-- Table structure for crawl_sub_task
-- ----------------------------
DROP TABLE IF EXISTS `crawl_sub_task`;
CREATE TABLE `crawl_sub_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL COMMENT '任务id',
  `crawl_url` varchar(200) DEFAULT NULL COMMENT '抓取url',
  `task_progress` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行的进度。（0-100）',
  `task_status` int(1) NOT NULL DEFAULT '0' COMMENT '0,待启动，1启动中，2已完成,4停止，9异常',
  `error_msg` text COMMENT '错误提示信息',
  `host` varchar(10) DEFAULT NULL COMMENT '主机',
  `param_value` varchar(2000) DEFAULT NULL COMMENT '关键字参数集合',
  `crawl_data_num` int(11) DEFAULT NULL COMMENT '抓取数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawl_task_info
-- ----------------------------
DROP TABLE IF EXISTS `crawl_task_info`;
CREATE TABLE `crawl_task_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(50) DEFAULT NULL COMMENT '抓取任务名称',
  `datasource_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `datasource_type_id` int(11) DEFAULT NULL COMMENT '数据源类型id',
  `error_msg` text COMMENT '错误提示信息',
  `task_status` int(1) NOT NULL DEFAULT '0' COMMENT '0,待启动，1启动中，2已完成,4停止，9异常',
  `json_param` text COMMENT '参数信息',
  `complete_sub_task_num` int(11) DEFAULT NULL COMMENT '完成子任务数量',
  `sub_task_num` int(11) DEFAULT NULL COMMENT '子任务数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for datasource
-- ----------------------------
DROP TABLE IF EXISTS `datasource`;
CREATE TABLE `datasource` (
  `id` int(11) NOT NULL,
  `datasource_name` varchar(50) DEFAULT NULL COMMENT '数据源名称',
  `is_need_login` tinyint(4) DEFAULT NULL COMMENT '是否需要登陆（1为需要，0为不需要）',
  `login_clazz` varchar(200) DEFAULT NULL COMMENT '登陆类',
  `status` int(11) DEFAULT NULL COMMENT '状态(1为未发布，2为已修改，3为已发布,0为已下架)',
  `is_need_proxy_ip` tinyint(4) DEFAULT NULL COMMENT '是否需要代理ip',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源表';

-- ----------------------------
-- Table structure for datasource_type
-- ----------------------------
DROP TABLE IF EXISTS `datasource_type`;
CREATE TABLE `datasource_type` (
  `id` int(11) NOT NULL,
  `datasource_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `datasource_type_name` varchar(50) DEFAULT NULL COMMENT '数据源类型名称',
  `storage_type_id` int(11) DEFAULT NULL COMMENT '存储类型id',
  `is_need_login` tinyint(4) DEFAULT NULL COMMENT '是否需要登陆（1为需要，0为不需要）',
  `is_need_proxy_ip` tinyint(4) DEFAULT NULL COMMENT '是否需要代理ip',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源类型表';

-- ----------------------------
-- Table structure for processor
-- ----------------------------
DROP TABLE IF EXISTS `processor`;
CREATE TABLE `processor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processor_name` varchar(50) DEFAULT NULL COMMENT '处理器名称',
  `processor_path` varchar(500) DEFAULT NULL COMMENT '处理器路径',
  `sort_no` int(11) DEFAULT NULL COMMENT '排序号',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `detail_name` varchar(20) DEFAULT NULL COMMENT '抓取规则名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for proxy_ip
-- ----------------------------
DROP TABLE IF EXISTS `proxy_ip`;
CREATE TABLE `proxy_ip` (
  `id` int(11) NOT NULL,
  `host` varchar(15) DEFAULT NULL COMMENT '主机',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `is_anonymous` tinyint(4) DEFAULT NULL COMMENT '是否匿名（1为匿名，0为 非匿名）',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态（1为有效，0为无效）',
  `speed` int(11) DEFAULT NULL COMMENT '速度(单位 是 KB/s)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代理ip';

-- ----------------------------
-- Table structure for proxy_ip_state
-- ----------------------------
DROP TABLE IF EXISTS `proxy_ip_state`;
CREATE TABLE `proxy_ip_state` (
  `id` int(11) NOT NULL,
  `proxy_ip` int(11) DEFAULT NULL COMMENT '代理ip的id',
  `datasource_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `error_num_hour` int(11) DEFAULT NULL COMMENT '每小时错误次数',
  `error_message` varchar(0) DEFAULT NULL COMMENT '失败信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for proxy_ip_strategy
-- ----------------------------
DROP TABLE IF EXISTS `proxy_ip_strategy`;
CREATE TABLE `proxy_ip_strategy` (
  `id` int(11) NOT NULL,
  `datasource_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `request_interval` int(11) DEFAULT NULL COMMENT '访问间隔',
  `max_req_num_minute` int(11) DEFAULT NULL COMMENT '每分钟最大次数',
  `max_req_num_hour` int(11) DEFAULT NULL COMMENT '每小时最大次数',
  `max_req_num_day` int(11) DEFAULT NULL COMMENT '每天最大次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for storage_type
-- ----------------------------
DROP TABLE IF EXISTS `storage_type`;
CREATE TABLE `storage_type` (
  `id` int(11) NOT NULL,
  `storage_type_name` varchar(50) DEFAULT NULL COMMENT '存储类型名称',
  `storage_type_table` varchar(20) NOT NULL COMMENT '存储类型表',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储类型表';

-- ----------------------------
-- Table structure for storage_type_field
-- ----------------------------
DROP TABLE IF EXISTS `storage_type_field`;
CREATE TABLE `storage_type_field` (
  `id` int(11) NOT NULL,
  `storage_type_id` int(11) DEFAULT NULL COMMENT '存储类型id',
  `field_en_name` varchar(50) DEFAULT NULL COMMENT '字段英文名',
  `field_desc` varchar(50) DEFAULT NULL COMMENT '字段描述',
  `field_type` varchar(20) DEFAULT NULL COMMENT '字段类型',
  `field_length` int(11) DEFAULT NULL COMMENT '字段长度',
  `decimal_length` int(11) DEFAULT '0' COMMENT '小数点长度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `field_cn_name` varchar(50) DEFAULT NULL COMMENT '字段中文名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储字段类型字段表';

-- ----------------------------
-- Table structure for storage_type_field_type
-- ----------------------------
DROP TABLE IF EXISTS `storage_type_field_type`;
CREATE TABLE `storage_type_field_type` (
  `id` int(11) NOT NULL,
  `field_type` varchar(20) DEFAULT NULL COMMENT '存储字段类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for style
-- ----------------------------
DROP TABLE IF EXISTS `style`;
CREATE TABLE `style` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `style_code` varchar(20) DEFAULT NULL COMMENT '样式编码',
  `style_name` varchar(20) DEFAULT NULL COMMENT '样式名称',
  `style_path` varchar(500) DEFAULT NULL COMMENT '指向路径',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lastmodify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for verify_code
-- ----------------------------
DROP TABLE IF EXISTS `verify_code`;
CREATE TABLE `verify_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datasource_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `proxy_ip` varchar(15) DEFAULT NULL COMMENT '代理ip',
  `host` varchar(15) DEFAULT NULL COMMENT '主机',
  `account` varchar(50) DEFAULT NULL COMMENT '账号',
  `img` text COMMENT '图片',
  `content` varchar(10) DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `crawl_task_info`
ADD COLUMN `detail_id`  varchar(50) NULL COMMENT '工作流节点信息id' AFTER `complete_sub_task_num`;

ALTER TABLE `crawl_rule_detail_field`
ADD COLUMN `is_null`  int(2) NULL COMMENT '字段是否允许为空(0-不允许为空,1-允许为空)' AFTER `storage_type_field_id`;


--v1.2.0--
ALTER TABLE `crawl_input_param`
ADD COLUMN `control_prop`  varchar(500) NULL AFTER `is_required`;


---v1.3.0--
ALTER TABLE `crawl_task_info`
ADD COLUMN `project_id`  varchar(11) NULL AFTER `sub_task_num`;
ALTER TABLE `crawl_sub_task`
MODIFY COLUMN `crawl_url`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抓取url' AFTER `task_id`;

--v1.4.0--
ALTER TABLE `crawl_sub_task`
ADD COLUMN `complate_page`  varchar(255) NULL AFTER `param_value`;
insert into processor(id,processor_name,processor_path,sort_no,lastmodify_time,type)
VALUES(11,"关键词转码","com.transing.crawl.util.processor.impl.paramProcessors.UnicodeProcessor",11,NOW(),"paramProc");

-- v1.4.1--
ALTER TABLE `crawl_task_info`
ADD COLUMN `discard_num`  int(11) NULL COMMENT '丢弃任务数' AFTER `sub_task_num`;

-- v1.4.1 --
ALTER TABLE `crawl_rule_detail_field`
ADD COLUMN `is_unique` int(2) NULL COMMENT '字段是否为去重标准(0-是去重标准, 1-不是去重标准)' AFTER `is_null`;