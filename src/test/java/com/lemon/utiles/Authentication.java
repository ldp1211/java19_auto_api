package com.lemon.utiles;

import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DanpingLi
 * @date 2020/6/23 - 15:55
 *
 * 接口鉴权类
 */
public class Authentication {
    //类似于jmeter中的用户变量  vars
    public static Map<String, Object> VARS = new HashMap<>();


    /**
     * 使用jsonpath获取内容存储到VARS变量，给其他接口使用
     *
     * @param json       json字符串
     * @param expression jsonpath表达式
     * @param key        存储到VARS种的key
     */
    public static void json2Vars(String json, String expression, String key) {
        //如果json不是空，则继续操作
        if (StringUtils.isNotBlank(json)) {
            //使用jsonpath获取内容
            Object obj = JSONPath.read(json, expression);
            System.out.println(key + ":" + obj);
            //如果获取的内容不为空，存入VARS变量，给其他接口使用
            if (obj != null) {
                Authentication.VARS.put(key, obj);
            }
        }
    }

    /**
     *获取带token的请求头map集合
     * @return
     */
    public static Map<String, String> getTokenHeader() {
        Object token = Authentication.VARS.get("${token}");
        System.out.println("Recharge token:" +token);
        //3.2添加到请求头
        //3.3 改造call支持传递请求头
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer " + token);
        headers.putAll(Constants.HEADERS);
        return headers;
    }
}

