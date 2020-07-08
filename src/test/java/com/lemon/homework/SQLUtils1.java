package com.lemon.homework;

import com.lemon.utiles.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author DanpingLi
 * @date 2020/6/30 - 10:51
 */
public class SQLUtils1 {
//    public static void main(String[] args){
//        QueryRunner runner = new QueryRunner();
//        Connection conn = JDBCUtils.getConnection();
//        try {
//            String sql = "select * from member m where m.mobile_phone ='18317714243';";
//            ScalarHandler handler = new ScalarHandler();
//            Object result = runner.query(conn, sql, handler);
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            JDBCUtils.close(conn);
//        }
//    }
    public static void main(String[] args){
//        mapHandler("select * from member m where m.mobile_phone ='18317714243';");
       // getSingleResult("select count(*) from member m where m.mobile_phone ='18317714243';");
    }

    /**
     * 查询数据库单行单列结果集
     * @param sql  sql语句
     * @return   查询结果
     */
    public static Object getSingleResult(String sql) {
        if(StringUtils.isBlank(sql)){
            System.out.println("sql为空");
            return null;
        }
        Object result = null;
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        try {
            //创建处理结果集对象
            ScalarHandler handler = new ScalarHandler();
            //执行查询语句
             result = runner.query(conn, sql, handler);
            //System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn);
        }
        return result;
    }


//    public static void mapHandler(String sql) {
//        QueryRunner runner = new QueryRunner();
//        Connection conn = JDBCUtils.getConnection();
//        try {
//        //String sql = "select * from member m where m.mobile_phone ='18317714243';";
//        MapHandler handler = new MapHandler();
//        Map<String, Object> map = null;
//        map = runner.query(conn, sql, handler);
//        System.out.println(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            JDBCUtils.close(conn);
//        }
//    }
}
