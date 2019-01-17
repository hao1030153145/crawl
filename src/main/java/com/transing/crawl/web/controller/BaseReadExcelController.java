package com.transing.crawl.web.controller;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:BaseReadExcelController.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月30日
 */
public interface BaseReadExcelController
{

    List read2003Data(List<HSSFRow> list);

    List read2007Data(List<XSSFRow> list);
}
