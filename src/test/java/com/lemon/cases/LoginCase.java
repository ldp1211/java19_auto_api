package com.lemon.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackDate;
import com.lemon.utiles.Authentication;
import com.lemon.utiles.ExcelUtils;
import com.lemon.utiles.HttpUtil;
import io.qameta.allure.Description;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author DanpingLi
 * @date 2020/6/23 - 10:22
 *
 *
 */
public class LoginCase extends BaseCase{

   //管理员账号 {login_mb}=18112312397  ${login_pwb}=12345678


    @Test(dataProvider = "datas",description = "登录测试description属性")
    @Description("description注解")

    public void test(CaseInfo caseInfo){
        // 1.参数化替换
        paramsReplace(caseInfo);
        System.out.println(caseInfo.getParams());
        // 2.数据库前置查询结果（数据断言必须在接口执行前后都查询）
        // 3.调用接口
        HttpResponse response = HttpUtil.call(caseInfo,Constants.HEADERS);
        String body = HttpUtil.printResponse(response);
        // 3.1.从响应体里获取token
            Authentication.json2Vars(body,"$.data.token_info.token","${token}");
        // 3.2.从响应体里获取 member_id
            Authentication.json2Vars(body,"$.data.id","${member_id}");
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
       // Assert.assertEquals(assertResult,"passed");

    }



    @DataProvider
    public Object[] datas(){

        Object[] datas = ExcelUtils.getDatas(sheetIndex,1, CaseInfo.class);
        return datas;
    }
}
