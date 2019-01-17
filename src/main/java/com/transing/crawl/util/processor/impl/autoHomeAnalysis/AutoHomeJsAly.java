package com.transing.crawl.util.processor.impl.autoHomeAnalysis;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoHomeJsAly {

    public String jsAnalysis(String content)  {


        // 取出加密字
        Pattern encryptionPa = Pattern.compile("\\[''\\+(.*)\\),[\\w]+_\\(");
        Matcher encryptionMa = encryptionPa.matcher(content);
        String encryptionCharacter = "";
        while (encryptionMa.find()) {
            encryptionCharacter = encryptionMa.group(1);
        }
        // 取出加密字顺序
        Pattern odrderPa = Pattern.compile("\\(''\\)\\+''\\+(.*[\\w]+_)\\)\\,[\\w]");
        Matcher odrderMa = odrderPa.matcher(content);
        String encryptionOdrder = "";
        while (odrderMa.find()) {
            encryptionOdrder = odrderMa.group(1);
            // System.out.println(data2);
        }
        // 字符的编码
        String value = util1(content, encryptionCharacter);
        // 只保留%后面数据
        String var_v = "";
        Pattern var_ = Pattern.compile("(%[\\w][\\w]+)");
        Matcher vars_ = var_.matcher(value);
        while (vars_.find()) {
            var_v += vars_.group(1);
        }
        // 如果获取到的数据最后一位为"_"那么删除最后三位
        String sub = var_v.substring(var_v.length() - 1, var_v.length());
        if (sub.equals("_")) {
            var_v = var_v.substring(0, var_v.length() - 3);
            // System.out.println(var_v);
        }
        // 解读编码得到文字和标点
        String decode = "";
        try {
            decode = URLDecoder.decode(var_v, "utf-8");
            //System.out.println(decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 获取\u0061数据
        Pattern UncodePa = Pattern.compile("\\\\u(\\w{4})");
        Matcher UncodeMa = UncodePa.matcher(content);
        String data3 = "";
        while (UncodeMa.find()) {
            data3 += UncodeMa.group(0);
        }
        // 得到编码中的数据
        String unicode = decodeUnicode(data3);
        // System.out.println(unicode);

        // 隐藏的字体顺序
        String hiddenOrder = util1(content, encryptionOdrder);
        // System.out.println(hiddenOrder);
        //把顺序和字对应
        Map<String, String> map = new HashMap<>();
        String[] split = hiddenOrder.split(";");
        for (int i = 0; i < split.length; i++) {
            int parseInt = Integer.parseInt(split[i]);
            String c = decode.charAt(parseInt) + "";
            String p = unicode.substring(1, unicode.length());
            p = unicode.charAt(0) + "" + i + p;
            map.put(p, c);
        }
        // 遍历查看数据
        String keys = "";
        String values = "";
        for (Entry<String, String> entry : map.entrySet()) {
            keys = entry.getKey();
            values = entry.getValue();
            content = content.replaceAll("<span class='"+keys+"'></span>", values);
        }
        return content;
    }

    // Unicode转中文
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    // 使用变量值替换文中加密的方法
    private static String util1(String content, String data1) {
        data1 = data1.replace("](''", "");
        // 获取每个字符排列的顺序
        Pattern patt1 = Pattern.compile("\\(\\)\\{return '';}(.*);\\}\\)\\(document\\)");
        Matcher ma1 = patt1.matcher(content);
        String group = "";
        while (ma1.find()) {
            group += ma1.group(1).trim();
        }
        // 取出变量
        Pattern var = Pattern.compile("([\\w]+[_][=]['].['])");
        Matcher vars = var.matcher(group);
        List<String> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        while (vars.find()) {
            list.add(vars.group(1));
        }
        // 存入key value
        for (String string : list) {
            String[] split = string.split("=");
            String key = "";
            String value = "";
            for (int i = 0; i < split.length; i++) {
                if (i % 2 == 0) {
                    key = split[i];
                } else {
                    value = split[i].replace("'", " ").trim();
                }
                map.put(key, value);
            }

        }
        // 遍历查看数据
        String key = "";
        String value = "";
        for (Entry<String, String> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            data1 = data1.replaceAll(key, value);
        }

        String[] split = data1.split("\\+");
        String re = "";
        for (String s : split) {
            re += s;
        }
        return re;

    }
}
