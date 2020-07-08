package com.lemon.homework;

import com.lemon.constants.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DanpingLi
 * @date 2020/6/23 - 18:07
 */
public class Constants1 {
    //数据驱动excel路径
    public static final String EXCEL_PATH = "D:\\code\\idea\\java19_api\\java19_auto_api_v8\\src\\test\\resources\\cases_v3.xlsx";

    //默认请求头
    public static final Map<String,String> HEADERS = new HashMap<>();

    //EXCEL响应回写列
    public static final int RESPONSE_WRITE_BACK_CELLNUM = 8;

    //EXCEL断言回写列
    public static final int ASSERT_WRITE_BACK_CELLNUM = 10;

    //数据库连接url
    public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    //数据库用户名
    public static final String JDBC_USERNAME = "future";
    //数据库密码
    public static final String JDBC_PASSWORD = "123456";
}
