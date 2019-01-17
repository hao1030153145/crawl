package com.transing.crawl.web.controller;

import com.jeeframework.testframework.AbstractSpringBaseControllerTest;
import com.wordnik.swagger.annotations.ApiParam;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestParam;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CrawlTaskTest extends AbstractSpringBaseControllerTest {

    @Test
    public void getCrawlTaskListText() throws Exception {
        String requestURI = "/crawlTask/getCrawlTaskList.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .param("project", "")
                        .param("taskName", "")
                        .param("datasourceId","")
                        .param("datasourceTypeId","")
                        .param("status","")
                        .param("createdTime","")
                        .param("page","1")
                        .param("size","15")
        ).andDo(print())
                .andExpect(status().isOk()).andReturn();
        assertTrue(com.jeeframework.util.json.JSONUtils
                .isJSONValid(mvcResult.getResponse().getContentAsString()));
    }


    @Test
    public void getCrawlSubTaskListText() throws Exception {
        String requestURI = "/crawlTask/getCrawlSubTaskList.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .param("taskId", "238912")
                        .param("subTaskId","")
                        .param("taskStatus","")
                        .param("startTime","")
                        .param("endTime","")
                        .param("page","1")
                        .param("size","15")
        ).andDo(print())
                .andExpect(status().isOk()).andReturn();
        assertTrue(com.jeeframework.util.json.JSONUtils
                .isJSONValid(mvcResult.getResponse().getContentAsString()));
    }



}
