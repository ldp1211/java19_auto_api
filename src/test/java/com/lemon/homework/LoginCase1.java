package com.lemon.homework;

import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.utiles.Authentication;
import com.lemon.utiles.ExcelUtils;
import com.lemon.utiles.HttpUtil;
import org.apache.http.HttpResponse;
import org.testng.annotations.*;

/**
 * @author DanpingLi
 * @date 2020/6/23 - 18:24
 */
public class LoginCase1 extends BaseCase1{

    @Test(dataProvider = "datas")
    public void test(CaseInfo1 caseInfo1){
        // 1.参数化替换
        paramsReplace(caseInfo1);
        // 2.数据库前置查询结果（数据断言必须在接口执行前后都查询）
        // 3.调用接口
        HttpResponse response = HttpUtils1.call(caseInfo1, Constants1.HEADERS);
        //打印响应
        String body = HttpUtils1.printResponse(response);
        //3.1.从响应体里获取token
        Authentication1.json2Vars(body,"$.data.token_info.token","${token}");
        //3.2从响应体里获取 member_id
        Authentication1.json2Vars(body,"$.data.id","${member_id}");
        //4.断言响应结果
        boolean assertResponseFlag = assertResponse(body,caseInfo1.getExpectResult());
        //5.添加接口响应回写内容
        addWriteBackData1(caseInfo1.getId(),Constants1.RESPONSE_WRITE_BACK_CELLNUM, body);
        // 6.数据库后置查询结果
        // 7.数据库断言
        // 8.添加断言回写内容
        String assertResult = assertResponseFlag ? "passed" : "failed";
        addWriteBackData1(caseInfo1.getId(), Constants1.ASSERT_WRITE_BACK_CELLNUM, assertResult);
        // 9.添加日志
        // 10.报表断言

    }




    @DataProvider
    public Object[] datas(){

        Object[] datas = ExcelUtils1.getDatas(sheetIndex,1, CaseInfo1.class);
        return datas;
    }
}
