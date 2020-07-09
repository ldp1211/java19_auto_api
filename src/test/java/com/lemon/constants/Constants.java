package com.lemon.constants;

import com.lemon.utiles.ExcelUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DanpingLi
 * @date 2020/6/22 - 20:36
 */
public class Constants {
    //数据驱动excel路径
    //\src\test\resources\cases_v3.xlsx
    //public static final String EXCEL_PATH = Constants.class.getClassLoader().getResource("./cases_v3.xlsx").getPath();
    //public static final String EXCEL_PATH = "D:\\code\\idea\\java19_api\\java19_auto_api_v8\\src\\test\\resources\\cases_v3.xlsx";
    public static final String EXCEL_PATH = System.getProperty("user.dir")+ "/src/test/resources/cases_v3.xlsx";
    public static final String PROPERTIES_PATH = System.getProperty("user.dir")+ "/src/test/resources/params.properties";

    //默认请求头
    public static final Map<String,String> HEADERS = new HashMap<>();
    //excel响应回写列
    public static final int Response_WRITE_BACK_CELLNUM = 8;
    //excel断言回写列
    public static final int ASSERT_WRITE_BACK_CELLNUM = 10;

    //数据库连接url
                                        // jdbc:数据库名称://ip:port/数据库名称
    public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    //数据库用户名
    public static final String JDBC_USERNAME = "future";
   //数据库密码
    public static final String JDBC_PASSWORD = "123456";
}
