package com.foreign.exchange.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Http请求公共类
 *
 * @author
 * @create 2020-07-17-16:09
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 不带参数的GET请求
     * @param url
     * @param charset
     * @return
     */
    public static String sendGet(String url, String charset) {
        String result = "";
        BufferedReader in = null;

        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            //chrome浏览器
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();

            String headerString;
            //响应头
            for (Iterator var = map.keySet().iterator(); var.hasNext(); headerString = (String) var.next()) {
            }

            String line;
            for (in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset)); (line = in.readLine()) != null; result = result + line)
            {
            }

        } catch (Exception ex) {
            System.out.println("发送GET请求出现异常:" + ex);
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 带参数的GET请求
     * @param url
     * @param param
     * @return
     */
    public static String sendGet(String url,Map<String,String> param){
        StringBuffer sb = (new StringBuffer(url)).append("?");
        try {
            if (param !=null && !param.isEmpty()){
                Iterator var = param.entrySet().iterator();

                //拼接URL请求
                while (var.hasNext()){
                    Map.Entry<String,String> entry = (Map.Entry<String, String>) var.next();
                    sb.append((String)entry.getKey()).append("=").append((String) entry.getValue()).append("&");
                }

                String targetUrl = sb.substring(0,sb.length()-1);
                //配置请求参数
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).setConnectionRequestTimeout(5000).build();
                HttpGet get = new HttpGet(targetUrl);
                get.setConfig(requestConfig);
                CloseableHttpResponse response = null;
                CloseableHttpClient client = null;
                try {
                    client = HttpClientBuilder.create().build();
                    response = client.execute(get);
                    if (response.getStatusLine().getStatusCode() == 200){
                        HttpEntity resEntity = response.getEntity();
                        String message = EntityUtils.toString(resEntity,"utf-8");
                        return  message;
                    }
                    logger.error("请求失败，statusCode="+ response.getStatusLine().getStatusCode());
                }catch (Exception ex)
                {
                     throw  new  RuntimeException(ex);
                }finally {
                    closeResponse(response);
                }
                return  null;
            }else {
                throw  new RuntimeException("param不能为空！");
            }
        }catch (Exception ex)
        {
            throw  new RuntimeException(ex);
        }
    }

    //弃用
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            //chrome浏览器
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            out = new PrintWriter(connection.getOutputStream());
            out.print(param);
            out.flush();

            String line;
            for (in = new BufferedReader(new InputStreamReader(connection.getInputStream())); (line = in.readLine()) != null; result += line) {
            }
        } catch (Exception ex) {
            System.out.println("发送post请求异常：" + ex);
            ex.printStackTrace();

        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }

        }
        return result;
    }

    /**
     * doPost带参数
     * @param url
     * @param param
     * @return
     */
    private  static  String sendPost(String url,Map<String,String> param){
        StringBuffer sb = (new StringBuffer(url)).append("?");
        try {
            if (param != null && !param.isEmpty()){
                List<NameValuePair> formparams = new ArrayList();
                Iterator var = param.entrySet().iterator();

                while (var.hasNext()){
                    Map.Entry<String,String> entry = (Map.Entry<String, String>) var.next();
                    formparams.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
                    sb.append((String) entry.getKey()).append("=").append((String)entry.getValue()).append("&");
                }

                new UrlEncodedFormEntity(formparams,"utf-8");
                String targetUrl = sb.substring(0,sb.length()-1);
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).setConnectionRequestTimeout(5000).build();
                HttpPost post = new HttpPost(targetUrl);
                post.setConfig(requestConfig);
                CloseableHttpResponse response = null;
                CloseableHttpClient client = null;

                try {
                    client = HttpClientBuilder.create().build();
                    response = client.execute(post);
                    Header[] headers = response.getAllHeaders();

                    //去掉header
                    if (headers != null){
                        int headerlen = headers.length;
                        for (int i=0;i<headerlen;i++){
                            Header header = headers[i];
                        }
                    }

                    if (response.getStatusLine().getStatusCode() ==200){
                        HttpEntity resEntity = response.getEntity();
                        String message = EntityUtils.toString(resEntity,"utf-8");
                        return  message;

                    }
                    logger.error("请求失败，statusCode："+response.getStatusLine().getStatusCode());
                }catch (Exception ex){
                    throw  new RuntimeException(ex);
                }finally {
                    closeResponse(response);
                }
                return  null;
            }else{
                throw new RuntimeException("param不能为空");
            }

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
    private  static  void  closeResponse(CloseableHttpResponse response){
        try {
            if (response !=null){
                response.close();
            }
        }catch (IOException ex){
            logger.error("关闭response出错",ex);
        }
    }

    //单元测试
//    public static void main(String[] args) {
//        //1.DoGet_no_param
////        String testUrl = "http://localhost:8088/items/info/bingan-1001";
//        //2.doGet_param
////        String testUrl = "http://localhost:8088/items/comments";
////        Map<String,String> map = new HashMap<>();
////        map.put("itemId","cake-1001");
////        map.put("level","1");
////        map.put("page","2");
////        map.put("pageSize","10");
////        String result = sendGet(testUrl,"utf-8");
////        String result = sendGet(testUrl,map);
//        //3.DoPost_no_param
//
//        //4.DoPost_param
//        String testUrl = "http://localhost:8088/address/delete";
//        Map<String,String> map = new HashMap<>();
//        map.put("userId","200628A9CA316D68");
//        map.put("addressId","200723BFXZF91TXP");
//
//        String result = sendPost(testUrl,map);
//        System.out.println(result);
//    }
}
