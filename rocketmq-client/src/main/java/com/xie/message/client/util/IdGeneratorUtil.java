package com.xie.message.client.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * HTTP请求及响应，通用字段组装工具类.
 * 
 */
public class IdGeneratorUtil {

    public static final String REQUEST_USER  = "_user"; //请求方ID
    public static final String REQUEST_SIG   = "_sig";  //调用方请求签名
    public static final String REQUEST_TRACE = "_trace"; //调用过程traceid

    public static String newTraceId(String operateType) {
        return operateType + DateUtils.formatDate(new Date(), DateUtils.FUALL_TIMESTAMP)
               + getRandomString(12);
    }

    public static Map<String, String> newRequestHeader(String checkPointCode, String _user,
                                                       String _sig) {
        Map<String, String> header = new HashMap<String, String>();
        header.put(REQUEST_USER, _user);
        header.put(REQUEST_SIG, _sig);
        header.put(REQUEST_TRACE, newTraceId(checkPointCode));

        return header;
    }

    static String randomBaseStr = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 生成随机字符串.
     * 
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(randomBaseStr.length());
            sb.append(randomBaseStr.charAt(number));
        }
        return sb.toString();
    }
    
}
