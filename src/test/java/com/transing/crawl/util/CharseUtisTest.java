package com.transing.crawl.util;

import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.client.HttpResponse;

public class CharseUtisTest {
    public static void main(String[] args) throws Exception{
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        HttpResponse response = httpClientHelper
                .doGetAndRetBytes("http://money.163.com/special/00251LR5/cpznList.html", "utf-8", "utf-8",
                        null, null);
        CharsetUtils charsetUtils = new CharsetUtils();
        try {
            String code = charsetUtils.CharsetCheckEntrance(response.getContentBytes(), "utf8", "utf8");
            System.out.println(code);
        }catch (Exception e){

        }
    }


}
