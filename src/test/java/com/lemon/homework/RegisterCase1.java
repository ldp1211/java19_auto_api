package com.lemon.homework;

import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.utiles.ExcelUtils;
import com.lemon.utiles.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author DanpingLi
 * @date 2020/6/12 - 17:01
 */
public class RegisterCase1 extends BaseCase1{

    @Test(dataProvider = "datas")
    public void test(CaseInfo1 caseInfo1) {
        // 1.参数化替换
        paramsReplace(caseInfo1);
        // 2.数据库前置查询结果（数据断言必须在接口执行前后都查询）
        Object beforeSqlResult = SQLUtils1.getSingleResult(caseInfo1.getSql());
        // 3.调用接口
        HttpResponse response = HttpUtils1.call(caseInfo1, Constants1.HEADERS);
        //打印响应
        String body = HttpUtils1.printResponse(response);
        //4.断言响应结果
        boolean assertResponseFlag = assertResponse(body, caseInfo1.getExpectResult());
        //5.添加接口响应回写内容
        addWriteBackData1(caseInfo1.getId(), Constants1.RESPONSE_WRITE_BACK_CELLNUM, body);
        // 6.数据库后置查询结果
        Object afterSqlResult = SQLUtils1.getSingleResult(caseInfo1.getSql());
        // 7.数据库断言
        boolean assertSqlFlag = sqlAssert(caseInfo1.getSql(), beforeSqlResult, afterSqlResult);
        // 8.添加断言回写内容
        String assertResult = assertResponseFlag && assertSqlFlag ? "passed" : "failed";
        addWriteBackData1(caseInfo1.getId(), Constants1.ASSERT_WRITE_BACK_CELLNUM, assertResult);
        // 9.添加日志
        // 10.报表断言

    }

    /**
     *数据库断言
     * @param sql                    sql语句
     * @param beforeSqlResult        sql前置查询结果
     * @param afterSqlResult         sql后置查询结果
     * @return                       数据库断言结果
     */
    public boolean sqlAssert(String sql, Object beforeSqlResult, Object afterSqlResult) {
        boolean flag = false;
        if(StringUtils.isNotBlank(sql)){
            if(beforeSqlResult == null || afterSqlResult == null){
                System.out.println("数据库断言失败");
            }else{
                Long l1 = (Long)beforeSqlResult;
                Long l2 = (Long)afterSqlResult;
                //接口执行之前查询结果为0，接口执行之后查询结果为1
                if(l1 == 0 && l2 == 1){
                    System.out.println("数据库断言成功");
                    flag = true;
                }else{
                    System.out.println("数据库断言失败");
                }
            }
        }else{
            System.out.println("sql为空，不需要数据库断言");
        }
        return flag;
    }


    @DataProvider
    public Object[] datas(){

            Object[] datas = ExcelUtils1.getDatas(sheetIndex,1, CaseInfo1.class);
            return datas;
    }
}
