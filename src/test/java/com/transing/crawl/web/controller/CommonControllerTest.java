package com.transing.crawl.web.controller;

import com.jeeframework.testframework.AbstractSpringBaseControllerTest;
import com.transing.crawl.job.SubTaskManager;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:CommonControllerTest.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月10日
 */
public class CommonControllerTest extends AbstractSpringBaseControllerTest
{

    @Test
    public void downTemplateFile() throws Exception{
        String URI="/getTemplateFile.json";
        MvcResult mvcResult=this.mockMvc.perform(MockMvcRequestBuilders.post(URI)).andDo(print()).andExpect(status().isOk()).andReturn();
        assertTrue(true);
    }


}
