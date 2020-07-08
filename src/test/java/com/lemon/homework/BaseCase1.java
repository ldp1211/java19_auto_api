package com.lemon.homework;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.cases.BaseCase;
import com.lemon.pojo.CaseInfo;
import com.lemon.utiles.Authentication;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2020/6/29 - 11:43
 */
public class BaseCase1 {
    public int sheetIndex;

    @BeforeSuite
    public void init() throws Exception {
        Constants1.HEADERS.put("X-Lemonban-Media-Type", "lemonban.v2");
        Constants1.HEADERS.put("Content-Type", "application/json");
        System.out.println("=====init1=====");
        //创建properties对象
        Properties prop = new Properties();
        //获取配置路径
        String path = BaseCase1.class.getClassLoader().getResource("./params.properties").getPath();
        FileInputStream fis = new FileInputStream(path);
        //读取配置文件中的内容并添加到prop中
        prop.load(fis);
        fis.close();
        //把prop中所有的内容一次性放入VARS
        Authentication1.VARS.putAll((Map)prop);
        System.out.println("Authentication1.VARS===" +Authentication1.VARS);
    }


    @AfterSuite
    public void finish() {
        //批量执行回写
        ExcelUtils1.batchWrite();
        System.out.println("=====finish1=====");
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
    public void addWriteBackData1(int rownum, int cellnum, String content) {
        WriteBackDate1 wbd1 =
                new WriteBackDate1(sheetIndex, rownum, cellnum, content);
        ExcelUtils1.wbd1List.add(wbd1);
    }

    /**
     * 接口响应断言
     * @param body   接口响应字符串
     * @param expectResult  excel中期望值
     * @return  断言结果
     */
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
        System.out.println("响应断言结果：" + assertResponseFlag);
        return assertResponseFlag;
    }

    /**
     * 参数化替换方法
     * @param caseInfo1  caseInfo1 对象
     */
    public void paramsReplace(CaseInfo1 caseInfo1) {
        //获取VARS中所有的key
        Set<String> keySet = Authentication1.VARS.keySet();
        //参数化替换遍历
        for (String key : keySet) {
            String value = Authentication1.VARS.get(key).toString();
            //替换sql
            if(StringUtils.isNotBlank(caseInfo1.getSql())){
                String sql = caseInfo1.getSql().replace(key, value);
                //把替换之后的sql重新设置到caseInfo中
                caseInfo1.setSql(sql);
            }
            //替换params
            if(StringUtils.isNotBlank(caseInfo1.getParams())){
                String params = caseInfo1.getParams().replace(key, value);
                //把替换之后的params重新设置到caseInfo中
                caseInfo1.setParams(params);
            }
            //替换 expectResult
            if(StringUtils.isNotBlank(caseInfo1.getExpectResult())){
                String expectResult = caseInfo1.getExpectResult().replace(key, value);
                //把替换之后的 expectResult重新设置到caseInfo中
                caseInfo1.setExpectResult(expectResult);
            }
            //替换 url
            if(StringUtils.isNotBlank(caseInfo1.getUrl())){
                String url = caseInfo1.getUrl().replace(key, value);
                //把替换之后的 url重新设置到caseInfo中
                caseInfo1.setUrl(url);
            }
        }
    }
}
