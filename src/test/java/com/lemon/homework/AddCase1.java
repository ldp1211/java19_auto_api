package com.lemon.homework;

import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.utiles.Authentication;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author DanpingLi
 * @date 2020/6/23 - 18:27
 */
public class AddCase1 extends BaseCase1{

    @Test(dataProvider = "datas")
    public void test(CaseInfo1 caseInfo1){
        // 1.参数化替换
        paramsReplace(caseInfo1);
        // 2.数据库前置查询结果（数据断言必须在接口执行前后都查询）
        //2.1获取带token的请求头
        Map<String, String> headers = Authentication1.getTokenHeader();
        //3. 调用接口
        HttpResponse response = HttpUtils1.call(caseInfo1,headers);
        String body = HttpUtils1.printResponse(response);
        //从响应体里获取 loan_id
        Authentication1.json2Vars(body,"$.data.id","${loan_id}");
        //4.断言响应结果
        boolean assertResponseFlag = assertResponse(body, caseInfo1.getExpectResult());
        //5.添加接口响应回写内容
        addWriteBackData1(caseInfo1.getId(), Constants1.RESPONSE_WRITE_BACK_CELLNUM, body);
        // 6.数据库后置查询结果
        // 7.数据库断言
        // 8.添加断言回写内容
        String assertResult = assertResponseFlag ? "passed" : "failed";
        addWriteBackData1(caseInfo1.getId(), Constants1.ASSERT_WRITE_BACK_CELLNUM, assertResult);
        // 9.添加日志
        // 10.报表断言
    }

    public boolean sqlAssert(CaseInfo1 caseInfo1, Object beforeSqlResult, Object afterSqlResult) {
        boolean flag = false;
        if(StringUtils.isNotBlank(caseInfo1.getSql())){
            if(beforeSqlResult == null || afterSqlResult == null){
                System.out.println("数据库断言失败");
            }else{
                BigDecimal b1 = (BigDecimal)beforeSqlResult;
                BigDecimal b2 = (BigDecimal)afterSqlResult;
                //充值后 - 充值前得到的结果  b2 - b1
                BigDecimal result1 = b2.subtract(b1);
                //参数amount
                Object obj = JSONPath.read(caseInfo1.getParams(), "$amount");
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
        Object[] datas = ExcelUtils1.getDatas(sheetIndex,1, CaseInfo1.class);
        return datas;
    }
}
