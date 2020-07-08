package com.lemon.utiles;

import com.alibaba.fastjson.JSONObject;
import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import io.qameta.allure.Step;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author DanpingLi
 * @date 2020/6/11 - 17:14
 */
public class HttpUtil {
    //mock
    public static void main(String[] args) throws Exception {
        String url ="http://mock-api.com/jz8XGNz4.mock/audit";
        String params ="{ \"loan_id\":\"206595\", \"approved_or_not\": true }";
        HttpResponse httpResponse = HttpUtil.jsonpost(url,params, Constants.HEADERS);
        printResponse(httpResponse);

    }
    private static Logger logger = Logger.getLogger(HttpUtil.class);

    @Step("call方法")
    public static HttpResponse call(CaseInfo caseInfo,Map<String,String> headers)  {
        String method = caseInfo.getMethod();
        String contentType = caseInfo.getContentType();
        //如果请求方式是post
        try {
        if ("post".equalsIgnoreCase(method)) {
            //如果参数类型是json
            if ("json".equalsIgnoreCase(contentType)) {
                return HttpUtil.jsonpost(caseInfo.getUrl(), caseInfo.getParams(),headers);
                //如果参数类型是form
            } else if ("form".equalsIgnoreCase(contentType)) {
                String params = json2KeyValue(caseInfo.getParams());
                return HttpUtil.formpost(caseInfo.getUrl(), params,headers);
            } else {
                System.out.println("没有发送http请求" + caseInfo);
            }
        //如果请求方式是get
        } else if ("get".equalsIgnoreCase(method)) {
            return HttpUtil.get(caseInfo.getUrl(),headers);
        //如果请求方式是patch
        } else if ("patch".equalsIgnoreCase(method)) {
           return HttpUtil.patch(caseInfo.getUrl(), caseInfo.getParams(),headers);
        } else {
            System.out.println("没有发送http请求" + caseInfo);
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送 http get 请求
     *
     * @param url url 必须带参数，如果不带不会自动携带
     *            url?KEY=VALUE&KEY2=VALUE2
     *            url/xxx/yyy/2/zzz(http://api.lemonban.com/futureloan/member/11574/info)
     * @throws Exception
     */
    public static HttpResponse get(String url,Map<String,String> headers) throws Exception {
        //1.创建get请求  并写入接口地址
        HttpGet get = new HttpGet(url);
        //2.在get请求上添加请求头
        addHeaders(headers, get);
//        get.addHeader("X-Lemonban-Media-Type", "lemonban.v2");
        //3.创建一个客户端   XXXS  XXXUtils  工具类
        CloseableHttpClient client = HttpClients.createDefault();
        //4.客户端发送请求，并且返回相应对象（响应头  响应体  响应状态码）
        HttpResponse response = client.execute(get);
        //5.获取响应头 响应体 响应状态码
        //5.1. 获取响应体
//        printResponse(response);
        return response;

    }

    /**
     * 发送 http post请求
     * @param url  接口请求地址
     * @param params  json格式的参数
     * @param headers 请求头
     * @return
     * @throws Exception
     */

    public static HttpResponse jsonpost(String url, String params,Map<String,String> headers) throws Exception {
        //1.创建post请求  并写入接口地址
        HttpPost post = new HttpPost(url);
        //2.在Post请求上添加请求头
        addHeaders(headers, post);
//        post.addHeader("X-Lemonban-Media-Type", "lemonban.v2");
//        post.addHeader("Content-Type", "application/json");
        //3.请求参数  加载在请求体里
        StringEntity stringEntity = new StringEntity(params, "utf-8");
        post.setEntity(stringEntity);
        //4.创建一个客户端
        HttpClient client = HttpClients.createDefault();
        //5.客户端发送请求，并且返回响应对象（响应头 响应体 响应状态码）
        HttpResponse response = client.execute(post);
        //6.获取响应头  响应体 响应状态码
//        printResponse(response);
        return response;
    }



    /**
     *
     * @param url
     * @param params   key=value格式的参数
     * @throws Exception
     */

    public static HttpResponse formpost(String url, String params,Map<String,String> headers) throws Exception {
        //1.创建post请求  并写入接口地址
        HttpPost post = new HttpPost(url);
        //2.在Post请求上添加请求头
        addHeaders(headers, post);
//        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //3.请求参数  加载在请求体里
        StringEntity stringEntity = new StringEntity(params, "utf-8");
        post.setEntity(stringEntity);
        //4.创建一个客户端
        HttpClient client = HttpClients.createDefault();
        //5.客户端发送请求，并且返回响应对象（响应头 响应体 响应状态码）
        HttpResponse response = client.execute(post);
        //6.获取响应头  响应体 响应状态码
//        printResponse(response);
        return response;
    }

    public static HttpResponse patch(String url,String params,Map<String,String> headers) throws Exception {
        //1.创建post请求  并写入接口地址
        HttpPatch patch = new HttpPatch(url);
        //2.在Post请求上添加请求头
        addHeaders(headers, patch);
//        patch.addHeader("X-Lemonban-Media-Type", "lemonban.v2");
//        patch.addHeader("Content-Type", "application/json");
        //3.请求参数  加载在请求体里
        StringEntity stringEntity = new StringEntity(params, "utf-8");
        patch.setEntity(stringEntity);
        //4.创建一个客户端
        HttpClient client = HttpClients.createDefault();
        //5.客户端发送请求，并且返回响应对象（响应头 响应体 响应状态码）
        HttpResponse response = client.execute(patch);
        //6.获取响应头  响应体 响应状态码
//        printResponse(response);
        return response;
    }

    public static String printResponse(HttpResponse response) {
        try {
         //5.1获取响应体
        HttpEntity entity = response.getEntity();
        String body = null;
        body = EntityUtils.toString(entity);
        logger.info(body);
        //5.2. 获取响应头
        Header[] allHeaders = response.getAllHeaders();
        System.out.println(Arrays.toString(allHeaders));
        //5.3 响应状态码
        //链式编程  调用方法之后继续调用方法
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * json参数转成form参数
     * @param jsonStr
     * @return
     */
    public static String json2KeyValue(String jsonStr){
        //把json转成 map
        Map<String,String> map= JSONObject.parseObject(jsonStr, Map.class);
        //获取所有key
        Set<String> keySet = map.keySet();
        String result = "";
        //遍历key
        for(String key : keySet){
            //通过key获取值
            String value = map.get(key);
            //拼接key=value&
            result += key + "=" + value + "&";
        }
        //去掉最后一个多余的&
        result = result.substring(0,result.length()-1);
        return result;
    }

    /**
     *添加请求头
     * @param headers 请求头map
     * @param request  请求对象
     */
    public static void addHeaders(Map<String, String> headers, HttpRequest request) {
        Set<String> keySet = headers.keySet();
        for (String name : keySet) {
            String value = headers.get(name);
            request.addHeader(name,value);
        }
    }
}
