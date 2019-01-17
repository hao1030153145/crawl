package com.transing.crawl.web.controller;

import com.jeeframework.testframework.AbstractSpringBaseControllerTest;
import com.transing.crawl.web.form.UserQueryRequest;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 用户登录注册单元测试类
 *
 * @author lance
 * @version 1.0 2017-02-14 11:29
 */
public class UserControllerTest extends AbstractSpringBaseControllerTest
{
    @Test
    public void userListNoLogin() throws Exception
    {
        String requestURI = "/userList.html";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get(requestURI)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getForwardedUrl()
                .equals("/login.html"));

    }

    @Test
    public void userListLoginSuccess() throws Exception
    {
        String requestURI = "/userList.html";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get(requestURI)
                        .param("testInLogin", "no")).andDo(print())
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getModelAndView().getViewName()
                .equals("user/userList"));

    }

    @Test
    public void getAppVersionJSONLoginSuccess() throws Exception
    {
        String requestURI = "/getAppVersion.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get(requestURI)
                        .param("testInLogin", "no")
                        .param("appVersion", "1.0.0")).andDo(print())
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);
    }

    @Test
    public void getAppVersionJSONLoginSuccessAppVersionNull() throws Exception
    {
        String requestURI = "/getAppVersion.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get(requestURI)
                        .param("testInLogin", "no")).andDo(print())
                .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 1111);
    }

    @Test
    public void userListJSONNoLogin() throws Exception
    {
        String requestURI = "/userList.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 11013);

    }

    @Test
    public void userListJSON() throws Exception
    {
        String requestURI = "/userList.json";

        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.set_search("false");
        userQueryRequest.setNd("1487041694190");
        userQueryRequest.setPage(1);
        userQueryRequest.setRows(10);

        JSONObject jsonObject = JSONObject.fromObject(userQueryRequest);
        String jsonString = jsonObject.toString();

        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);

        userQueryRequest.setNickName("1");
        userQueryRequest.setCreateTime("2016-12-12 12:12:12");
        userQueryRequest.setSidx("uid");
        jsonObject = JSONObject.fromObject(userQueryRequest);
        jsonString = jsonObject.toString();
        mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        response = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);

        userQueryRequest.setSord("desc");
        jsonObject = JSONObject.fromObject(userQueryRequest);
        jsonString = jsonObject.toString();
        mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        response = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);
    }

    @Test
    public void userListJSONInvalidJSON() throws Exception
    {
        String requestURI = "/userList.json";

        String jsonString = "";

        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(jsonString)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 11015);

    }

    @Test
    public void getCrawlInputParamsByDatasourceTypeText() throws Exception
    {
        String requestURI = "/getCrawlInputParamsByDatasourceType.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get(requestURI)
                        .param("testInLogin", "no")
                        .param("datasourceTypeId", "6")).andDo(print())
                .andExpect(status().isOk()).andReturn();
        assertTrue(com.jeeframework.util.json.JSONUtils
                .isJSONValid(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void getDataSourceAndDataSourceTypeListText() throws Exception
    {
        String requestURI = "/getDataSourceAndDataSourceTypeList.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get(requestURI)
                        .param("testInLogin", "no")).andDo(print())
                .andExpect(status().isOk()).andReturn();
        assertTrue(com.jeeframework.util.json.JSONUtils
                .isJSONValid(mvcResult.getResponse().getContentAsString()));
    }


    @Test
    public void testMatch() throws Exception{
        String taskURl="http://s.weibo.com/weibo/宝马&typeall=1&suball=1&timescope=custom:2017-09-04-0:2017-09-04-2@scope@&page=@page@";
        if(taskURl.matches(".*@.*?@.*")){
            taskURl= taskURl.replaceAll("@.*?@","");
        }
        System.out.println(taskURl);
    }

}