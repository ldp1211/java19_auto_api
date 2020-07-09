package com.lemon.cases;

import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackDate;
import com.lemon.utiles.Authentication;
import com.lemon.utiles.ExcelUtils;
import com.lemon.utiles.HttpUtil;
import com.lemon.utiles.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

/**
 * @author DanpingLi
 * @date 2020/6/12 - 17:01
 */
public class RegisterCase extends BaseCase{

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo){
        // 1.参数化替换
        paramsReplace(caseInfo);
        // 2.数据库前置查询结果（数据断言必须在接口执行前后都查询）
        Object beforeSqlResult = SQLUtils.getSingleResult(caseInfo.getSql());
        // 3.调用接口
        HttpResponse response = HttpUtil.call(caseInfo, Constants.HEADERS);
        String body = HttpUtil.printResponse(response);

        // 4.断言响应结果
        boolean assertResponseFlag = assertResponse(body, caseInfo.getExpectResult());
        // 5.添加接口响应回写内容
        addWriteBackData(caseInfo.getId(),Constants.Response_WRITE_BACK_CELLNUM, body);
        // 6.数据库后置查询结果
        Object afterSqlResult = SQLUtils.getSingleResult(caseInfo.getSql());
        // 7.数据库断言
        boolean assertSqlFlag = sqlAssert(caseInfo.getSql(), beforeSqlResult, afterSqlResult);
        // 8.添加断言回写内容
        String assertResult = assertResponseFlag && assertSqlFlag ? "passed" : "failed";
        addWriteBackData(caseInfo.getId(),Constants.ASSERT_WRITE_BACK_CELLNUM, assertResult);
        // 9.添加日志
        // 10.
     Assert.assertEquals(assertResult,"passed");

    }


    /**
     *数据库断言
     * @param sql                     sql语句
     * @param beforeSqlResult         sql前置查询结果
     * @param afterSqlResult          sql后置查询结果
     * @return                        数据库断言结果
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
                if(l1 == 0 && l2 ==1){
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

            Object[] datas = ExcelUtils.getDatas(sheetIndex,1, CaseInfo.class);
            return datas;
    }
}
