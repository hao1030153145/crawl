package com.transing.crawl.web.controller;

import com.jeeframework.testframework.AbstractSpringBaseControllerTest;
import com.transing.crawl.util.WebUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:crawlDataToCorpus.java
 * 语料库并发测试
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年01月23日
 */
public class crawlDataToCorpus extends AbstractSpringBaseControllerTest
{
    @Test
    public void corpusInto(){
        for (int i=0;i<178;i++){
            new Thread(new writeThead()).start();
            new Thread(new readThead()).start();
        }
    }

    class writeThead extends Thread{

        @Override
        public void run()
        {
            try
            {
                System.out.println("==================write:"+this.getName());
                String contentes = "{\"image\":\"http://mmbiz.qpic.cn/mmbiz_gif/GYvhlHtsSSBehH2Kia4slOEb46owuAgEFLoIOnRjlqsvYBIYtN40hicIMp0D01lUOwRIwedZOGraUr2Ib30lGOZg/0?wx_fmt=gif\",\"ltimes\":26,\"author\":\"\",\"crawltime\":{\"date\":22,\"hours\":10,\"seconds\":53,\"month\":0,\"timezoneOffset\":-480,\"year\":118,\"minutes\":56,\"time\":1516589813002,\"day\":1},\"detailId\":\"1211\",\"source\":\"顺义教委\",\"title\":\"【校园风采】东风教育集团裕龙校区迎接西藏教师交流团\",\"content\":\"近日，东风教育集团裕龙校区迎来该校申秀丽老师支教所在地西藏教师团队，开展“异地交流促发展相互交流共成长”主题交流会。 校长史淑惠就学校整体情况、办学宗旨、育人目标及课程建设向来宾一行作介绍，结合“小学语文阅读课程的建构与实施”，着重交流课程建设的特色与创新。她希望通过双方相互学习，一起助力学生终身发展，让每个生命绽放精彩。与会人员观摩数学复习、语文阅读两节现场课，参观校园文化建设，听取由8位教师呈现的班主任工作经验及教育故事分享。围绕学生核心素养全面均衡发展，西藏教育同仁就教学、班级管理中的典型问题与裕龙校区干部教师深度交流，各抒己见，在思维碰撞中，双方收获良多。西藏教师团一行直观感受到东风教育集团裕龙校区“多元发展，追求卓越”的办学理念和育人成果，对学校办学特色给予高度评价，真诚希望双方在“携手共进，和谐共赢”活动后，开启深度合作模式，实现合作共赢，共同发展的目标。裕龙校区将以本次活动为契机，以合作为动力，以援藏教师为纽带，不断创新、求实进取，为首都教育援藏工作做出新贡献。资料来源：东风教育集团裕龙校区制 作：刘峣总 编：郝景强审 核：秦学如（版权所有，转发请注明出处。侵权必究）\",\"url\":\"http://mp.weixin.qq.com/s?__biz=MzIyMjIxNTQ2Ng==&mid=2653016676&idx=2&sn=f7ef6f147dbf53b3c0cfad083ec1efd4&scene=0&key=c6a79670a446543ac88dae3d19f6e5659869c30cba895ba5e3e435de80805f7772027a7fc12f54b31acfc3d47e7d93048c4219c3487089f6dcc1a8adcca9195fce4acc015940ce660bf80f15c87f9717&ascene=1&uin=NDc5MjM3MjU0&devicetype=android-23&version=2605033c&nettype=WIFI&abtest_cookie=AgABAAoADAAHAGmKHgCWih4An4oeAL%2BKHgDFih4A2YoeAP%2BKHgAAAA%3D%3D&lang=zh_CN&pass_ticket=y1XKhqMJWNmYaMo3bD0n6oEW4pGHsUgxBh63wxNiTl1VNYtZHWNEp6yStIjSCz6m\",\"commentlist\":\"W10=\",\"uid\":\"shunyijiaowei\",\"datetime\":{\"date\":11,\"hours\":0,\"seconds\":0,\"month\":0,\"timezoneOffset\":-480,\"year\":118,\"minutes\":0,\"time\":1515600000000,\"day\":4},\"htimes\":\"13736\",\"tempurl\":\"http://mp.weixin.qq.com/s?src=11×tamp=1516087345&ver=639&signature=sWCeTn*2yk6oBnEDkILggD3espTjeXqbHTqE7ScPvzC0-7GBZPJgFe25oYJUjUb9bha82YH7FX30j-THiOPzH9IezyUspaLM6UncjitkLyITmbG39**zAbHHMX*KHRCM&new=1\",\"projectID\":122,\"vtimes\":736,\"indexId\":45081}";
                String coprusInterfaceURI = "/addDataInSearcher.json";
                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put("dataType", "concurrenttest");
                postParam.put("dataJSON", contentes);
                Object object = WebUtil.callRemoteService(
                        "http://corpus2test.dookoo.net" + coprusInterfaceURI,
                        "post", postParam);
                System.out.println(object.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class readThead extends Thread{
        @Override
        public void run()
        {
            try{
                System.out.println("==================read:"+this.getName());
                String url="/getDataInSearcher.json";
                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put("dataType", "news");
                Object object = WebUtil.callRemoteService(
                        "http://corpus2test.dookoo.net" + url,
                        "post", postParam);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
