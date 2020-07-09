package com.lemon.cases;

import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.utiles.Authentication;
import com.lemon.utiles.ExcelUtils;
import com.lemon.utiles.HttpUtil;
import com.lemon.utiles.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author DanpingLi
 * @date 2020/6/12 - 17:01
 * 新增case
 * http://api.lemonban.com/futureloan/loan/add
 */
public class AddCase extends BaseCase{

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo){
        // 1.参数化替换
          paramsReplace(caseInfo);
        // 2.数据库前置查询结果（数据断言必须在接口执行前后都查询）
        //2.1获取带token的请求头
        Map<String, String> headers = Authentication.getTokenHeader();
        //3. 调用接口
        HttpResponse response = HttpUtil.call(caseInfo,headers);
        String body = HttpUtil.printResponse(response);
        //从响应体里获取 loan_id
        Authentication.json2Vars(body,"$.data.id","${loan_id}");

        // 4.断言响应结果
        boolean assertResponseFlag = assertResponse(body, caseInfo.getExpectResult());
        // 5.添加接口响应回写内容
        addWriteBackData(caseInfo.getId(),Constants.Response_WRITE_BACK_CELLNUM, body);
        // 6.数据库后置查询结果
        // 7.数据库断言
        // 8.添加断言回写内容
        String assertResult = assertResponseFlag ? "passed" : "failed";
        addWriteBackData(caseInfo.getId(),Constants.ASSERT_WRITE_BACK_CELLNUM, assertResult);
        // 9.添加日志
        // 10.报表断言
        Assert.assertEquals(assertResult,"passed");
    }


    /**
     *
     * @param caseInfo             caseInfo对象
     * @param beforeSqlResult      sql前置查询结果
     * @param afterSqlResult       sql后置查询结果
     * @return                     数据库断言结果
     */
    public boolean sqlAssert(CaseInfo caseInfo, Object beforeSqlResult, Object afterSqlResult) {
        boolean flag = false;
        if(StringUtils.isNotBlank(caseInfo.getSql())){
            if(beforeSqlResult == null || afterSqlResult == null){
                System.out.println("数据库断言失败");
            }else{
                BigDecimal b1 = (BigDecimal)beforeSqlResult;
                BigDecimal b2 = (BigDecimal)afterSqlResult;
                //充值后 - 充值前得到的结果  b2 - b1
                BigDecimal result1 = b2.subtract(b1);
                //参数amount
                Object obj = JSONPath.read(caseInfo.getParams(), "$amount");
                BigDecimal result2 = new BigDecimal(obj.toString());
                System.out.println(b1);
                System.out.println(b2);
                System.out.println(result1);
                System.out.println(result2);
                //结果 == 参数 amount
                if(result1.compareTo(result2) == 0){
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
