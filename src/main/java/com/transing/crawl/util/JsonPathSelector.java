package com.transing.crawl.util;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonPathSelector  {

    private String jsonPathStr;

    private JsonPath jsonPath;

    public JsonPathSelector(String jsonPathStr) {
        this.jsonPathStr = jsonPathStr;
        this.jsonPath = JsonPath.compile(this.jsonPathStr);
    }

    public String select(String text) {
        Object object = jsonPath.read(text);
        if (object == null) {
            return null;
        }
        if (object instanceof List) {
            List list = (List) object;
            if (list != null && list.size() > 0) {
                return toString(list.iterator().next());
            }
        }
        return object.toString();
    }

    private String toString(Object object) {
        if (object instanceof Map) {
            return JSON.toJSONString(object);
        } else {
            return String.valueOf(object);
        }
    }

    public List<String> selectList(String text) {
        List<String> list = new ArrayList<String>();

        try {
            Object object = jsonPath.read(text);
            if (object == null) {
                return list;
            }
            if (object instanceof List) {
                List<Object> items = (List<Object>) object;
                for (Object item : items) {
                    list.add(toString(item));
                }
            } else {
                list.add(toString(object));
            }
            return list;
        } catch (Exception e) {
            return list;
        }
    }

}
