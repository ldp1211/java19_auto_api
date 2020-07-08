package com.lemon.cases;

import cn.binarywang.tools.generator.ChineseIDCardNumberGenerator;
import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackDate;
import com.lemon.utiles.Authentication;
import com.lemon.utiles.ExcelUtils;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author DanpingLi
 * @date 2020/6/28 - 16:29
 * 用例父类
 */
public class BaseCase {
    //手机号和证件号随机生成
//    public static void main(String[] args) {
//        String mobileNumber = ChineseMobileNumberGenerator.getInstance().generate();
//        String iDCardNumber = ChineseIDCardNumberGenerator.getInstance().generate();
//        System.out.println("mobileNumber=" +mobileNumber);
//        System.out.println("iDCardNumber=" +iDCardNumber);
//    }
    private static Logger logger = Logger.getLogger(BaseCase.class);
    public int sheetIndex;

    @BeforeSuite
    public void init() throws Exception {
        logger.info("======init=====");
        Constants.HEADERS.put("X-Lemonban-Media-Type", "lemonban.v2");
        Constants.HEADERS.put("Content-Type", "application/json");
        //存入参数变量
//        Authentication.VARS.put("${register_mb}","183177714233");
//        Authentication.VARS.put("${register_pwd}","12345678");
//        Authentication.VARS.put("${login_mb}","183177714234");
//        Authentication.VARS.put("${login_pwd}","12345678");
//        Authentication.VARS.put("${amount}","3000");
        //创建properties对象
        Properties prop = new Properties();
        //获取配置路径
        String path = BaseCase.class.getClassLoader().getResource("./params.properties").getPath();
        FileInputStream fis = new FileInputStream(path);
        //读取配置文件中的内容并添加到prop中
        prop.load(fis);
        fis.close();
        //把prop中所有的内容一次性放入VARS
        Authentication.VARS.putAll((Map)prop);
        logger.info("Authentication.VARS===" +Authentication.VARS);
    }

    @AfterSuite
    public void finish() {
        //批量执行回写
        ExcelUtils.batchWrite();
        System.out.println("=====finish=====");
    }

    @BeforeClass
    @Parameters({"sheetIndex"})
    public void beforeClass(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    /**
     * @param rownum  行号
     * @param cellnum 列号
     * @param content 回写内容
     */

    public void addWriteBackData(int rownum, int cellnum, String content) {
        WriteBackDate wbd =
                new WriteBackDate(sheetIndex, rownum, cellnum, content);
        //添加到回写集合
        ExcelUtils.wbdList.add(wbd);
    }

    /**
     * 接口响应断言
     *
     * @param body         接口响应字符串
     * @param expectResult excel中期望值
     * @return 断言结果
     */
    @Step("assert响应断言")
    public boolean assertResponse(String body, String expectResult) {
        //json转成map
        Map<String, Object> map = JSONObject.parseObject(expectResult, Map.class);
        Set<String> keySet = map.keySet();
        boolean assertResponseFlag = true;
        for (String expression : keySet) {
            //1.获取期望值
            Object expectValue = map.get(expression);
            //2.通过jsonpath找到实际值
            Object actualValue = JSONPath.read(body, expression);
            //3.比较期望值和实际值
            if (expectValue == null && actualValue != null) {
                assertResponseFlag = false;
                break;
            }
            if (expectValue == null && actualValue == null) {
                continue;
            }
            if (!expectValue.equals(actualValue)) {
                assertResponseFlag = false;
                break;
            }
        }
        logger.info("响应断言结果：" + assertResponseFlag);
        return assertResponseFlag;
    }

    /**
     * 参数化替换方法
     * @param caseInfo  caseInfo 对象
     */
    public void paramsReplace(CaseInfo caseInfo) {
        //sql:select leave_amount from member m where m.id =${member_id};  11
        //params:"member_id":"${member_id}","amount":"${amount}"}   11 3000

        //获取VARS中所有的key
        Set<String> keySet = Authentication.VARS.keySet();
        //参数化替换遍历
        for (String key : keySet) {
            //key = ${member_id}  value = 11
            //key是占位符，value是实际值
            String value = Authentication.VARS.get(key).toString();
            //替换sql
            if(StringUtils.isNotBlank(caseInfo.getSql())){
                String sql = caseInfo.getSql().replace(key, value);
                //把替换之后的sql重新设置到caseInfo中
                caseInfo.setSql(sql);
            }
            //替换params
            if(StringUtils.isNotBlank(caseInfo.getParams())){
                String params = caseInfo.getParams().replace(key, value);
                //把替换之后的params重新设置到caseInfo中
                caseInfo.setParams(params);
            }
            //替换 expectResult
            if(StringUtils.isNotBlank(caseInfo.getExpectResult())){
                String expectResult = caseInfo.getExpectResult().replace(key, value);
                //把替换之后的 expectResult重新设置到caseInfo中
                caseInfo.setExpectResult(expectResult);
            }
            //替换 url
            if(StringUtils.isNotBlank(caseInfo.getUrl())){
                String url = caseInfo.getUrl().replace(key, value);
                //把替换之后的 url重新设置到caseInfo中
                caseInfo.setUrl(url);
            }
        }
    }

}
