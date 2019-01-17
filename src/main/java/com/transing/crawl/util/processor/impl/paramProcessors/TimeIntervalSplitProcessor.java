package com.transing.crawl.util.processor.impl.paramProcessors;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.DateUtil;
import com.transing.crawl.util.processor.BaseProcessor;
import com.wordnik.swagger.annotations.Api;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 包: com.transing.crawl.util.processor.impl
 * 源文件:TimeIntervalSplitProcessor.java
 * 时间区间格式划分
 * 比如将微博数据划分成
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年08月29日
 */
@Api(value = "时间格式划分",description = "格式:content=>{\"start\":\"2017-03-05 0:0:00\",\"end\":\"2017-03-05 12:0:00\",\"step\":\"0\",\"splitValue\":\"1\"};\n" +
        "processorValue=>timescope=custom:@start@:@end@\nyyyy-MM-dd-H")
public class TimeIntervalSplitProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        try
        {
            /**
             * {无:0,按小时:1,按天:2,按月:3,按周:4}
             */
            String [] templateValue=processorValue.split("\n");
            //custom:@start@:@end@
            String urlTempate=templateValue[0];
            //yyyy-mm-dd-H
            String dateTemplate=templateValue[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateTemplate);

            JSONObject jsonObject=JSONObject.fromObject(content);
            String start=jsonObject.getString("start");
            String end =jsonObject.containsKey("end")?jsonObject.getString("end"):"";
            if(Validate.isEmpty(end)){
                end=DateUtil.formatDate(new Date());
            }
            int step = jsonObject.getInt("step");
            String splitValue=jsonObject.getString("splitValue");

            Date startDate= DateUtil.parseDate(start);
            Date endDate=DateUtil.parseDate(end);

            Calendar calendar=Calendar.getInstance();
            calendar.setTime(startDate);

            if(splitValue.equalsIgnoreCase("0")|| step==0|| startDate.getTime()==endDate.getTime()){
                String startTims=simpleDateFormat.format(startDate);
                String endTimes=simpleDateFormat.format(endDate);
                String suffix=urlTempate.replaceAll("@start@",startTims).replaceAll("@end@",endTimes);
                return suffix;
            }

            Date date=startDate;
            StringBuffer dateTimeSplits=new StringBuffer();

            while (date.before(endDate))
            {
                if (splitValue.equalsIgnoreCase("1"))
                {
                    //按小时
                    calendar.add(Calendar.HOUR, step);

                }
                else if (splitValue.equalsIgnoreCase("2"))
                {
                        //按天
                        calendar.add(Calendar.DAY_OF_YEAR, step);
                }else if (splitValue.equalsIgnoreCase("3"))
                {
                            //按月
                        calendar.add(Calendar.MONTH, step);
                }else if (splitValue.equalsIgnoreCase("4"))
                {
                                //按周
                       calendar.add(Calendar.WEEK_OF_YEAR, step);
                }
                //计算结果上减1秒
                calendar.add(Calendar.SECOND,-1);
                Date endtem=calendar.getTime();
                if(endtem.after(endDate)){
                    endtem=endDate;
                }
                String startTimes=simpleDateFormat.format(date);
                String endTimes=simpleDateFormat.format(endtem);
                String suffix=urlTempate.replaceAll("@start@",startTimes).replaceAll("@end@",endTimes);
                dateTimeSplits.append(suffix+",");
                //还原成原计算结果
                calendar.add(Calendar.SECOND,1);
                endtem=calendar.getTime();
                date=endtem;
            }
            if (dateTimeSplits.toString().endsWith(",")){
                return dateTimeSplits.toString().substring(0,dateTimeSplits.length()-1);
            }else{
                return dateTimeSplits.toString();
            }
        }
        catch (Exception e)
        {
            LoggerUtil.errorTrace(loggerName,e.getMessage(),e);
        }
        return null;
    }

    public static void main(String[] args)
    {
        TimeIntervalSplitProcessor timeIntervalSplitProcessor=new TimeIntervalSplitProcessor();
        String t=timeIntervalSplitProcessor.runProcessor("{\"start\":\"2017-03-05 0:0:00\",\"end\":\"2017-03-05 12:0:00\",\"step\":\"0\",\"splitValue\":\"1\"}",
                JSONObject.fromObject("{}"),"timescope=custom:@start@:@end@\nyyyy-MM-dd-H");
        System.out.println(t);
    }
}
