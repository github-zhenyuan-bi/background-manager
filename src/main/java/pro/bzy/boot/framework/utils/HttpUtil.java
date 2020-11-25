package pro.bzy.boot.framework.utils;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.lang.Nullable;

import com.google.common.base.Joiner;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.parents.MyUtil;

/**
 * Http请求工具
 */
@Slf4j
public class HttpUtil implements MyUtil {
    
    
    /**
     * Http get请求
     * @param url   请求地址
     * @return
     * @throws Exception
     */
    public static String httpGet(@NonNull String url) throws Exception {
        return httpGet(url, null, null, SystemConstant.HTTP_CONTENT_TYPE_JSON);
    }
    
    
    
    /**
     * Http get请求
     * @param url   请求地址
     * @param headers   请求头
     * @return
     * @throws Exception
     */
    public static String httpGet(@NonNull String url, 
            Map<String, String> headers) throws Exception {
        return httpGet(url, headers, null, SystemConstant.HTTP_CONTENT_TYPE_JSON);
    }
    
    
    
    /**
     * Http get请求
     * @param url   请求地址
     * @param params    请求参数
     * @param headers   请求头
     * @return
     * @throws Exception
     */
    public static String httpGet(@NonNull String url, 
            Map<String, String> headers, Map<String, String> params) throws Exception {
        return httpGet(url, headers, params, SystemConstant.HTTP_CONTENT_TYPE_JSON);
    }
    
    
    
    /**
     * Http get请求
     * @param url   请求地址
     * @param params    请求参数
     * @param headers   请求头
     * @param headerContentType 返回值类型
     * @return
     * @throws Exception
     */
    public static String httpGet(@NonNull String url, 
            Map<String, String> headers, Map<String, String> params, String headerContentType) throws Exception {
        try {
            // 创建请求客户端 
            @Cleanup CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            
            // 拼接url
            String paramsForEndofUrl = (!CollectionUtil.isEmpty(params))
                    ? Joiner.on("&").withKeyValueSeparator("=").join(params)
                    : "";
            url += (url.contains("?"))? "&" : "?"; 
            url += paramsForEndofUrl;
            
            //  Http get请求
            HttpGet httpGet = new HttpGet(url);
            
            // 请求头
            httpGet.setHeader("Content-Type", headerContentType);
            if (!CollectionUtil.isEmpty(headers))
                headers.forEach( (k, v) -> httpGet.setHeader(k, v) );
            
            // 进行请求 并返回响应数据
            @Cleanup CloseableHttpResponse response  = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            
            // 返回 utf8解码
            String responseResult = responseEntity != null
                    ? EntityUtils.toString(responseEntity, SystemConstant.CHARSET_UTF8)
                    : "";
                    
            // 打印请求信息
            printRequestAndResponseInfo(httpGet, null, response, responseResult);
            
            return responseResult;
        } catch (Exception e) {
            printRequestErrorInfo("http", "get", url, params, headers, e);
            throw e;
        }
    }

    
    
    /**
     * Http Post请求
     * @param url   请求地址
     * @return
     * @throws Exception
     */
    public static String httpPost(@NonNull final String url) throws Exception {
        return httpPost(url, null, null, SystemConstant.HTTP_CONTENT_TYPE_JSON);
    }
    
    
    
    /**
     * Http Post请求
     * @param url   请求地址
     * @param params    请求参数
     * @return
     * @throws Exception
     */
    public static String httpPost(@NonNull final String url, 
            Object params) throws Exception {
        return httpPost(url, params, null, SystemConstant.HTTP_CONTENT_TYPE_JSON);
    }
    
    
    
    /**
     * Http Post请求
     * @param url   请求地址
     * @param params    请求参数
     * @param headers   请求头
     * @return
     * @throws Exception
     */
    public static String httpPost(@NonNull final String url, 
            Object params, Map<String, String> headers) throws Exception {
        return httpPost(url, params, headers, SystemConstant.HTTP_CONTENT_TYPE_JSON);
    }
    
    
    
    /**
     * Http Post请求
     * @param url   请求地址
     * @param params    请求参数
     * @param headers   请求头
     * @param headerContentType 返回值类型
     * @return
     * @throws Exception
     */
    public static String httpPost(@NonNull final String url, 
            Object params, Map<String, String> headers, String headerContentType) throws Exception {
        try {
            // 创建请求客户端 和 post请求
            @Cleanup CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            
            // 请求头
            httpPost.setHeader("Content-Type", headerContentType);
            if (CollectionUtil.isEmpty(headers))
                headers.forEach( (k, v) -> httpPost.setHeader(k, v) );
            
            // 添加请求参数
            httpPost.setEntity(params != null
                    ? new StringEntity(params.toString(), SystemConstant.CHARSET_UTF8)
                    : null);
            
            // 进行请求 并返回响应数据
            @Cleanup CloseableHttpResponse response  = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            
            // 返回 utf8解码
            String responseResult = responseEntity != null
                    ? EntityUtils.toString(responseEntity, SystemConstant.CHARSET_UTF8)
                    : "";
                    
            // 打印请求信息
            printRequestAndResponseInfo(null, httpPost, response, responseResult);
            
            return responseResult;
        } catch (Exception e) {
            printRequestErrorInfo("http", "post", url, params, headers, e);
            throw e;
        }
    }
    
    
    
    /**
     * 请求异常是 打印错误信息
     * @param httpOrhttps   请求源 http 或 https
     * @param type  get or post
     * @param url   请求地址
     * @param params    请求参数
     * @param headers   请求头部
     */
    private static void printRequestErrorInfo(String httpOrhttps, String type, String url,
            Object params, Map<String, String> headers, Exception e) {
        log.error("异常原因# {}", e.getMessage());
        log.error("请求失败# {}", httpOrhttps);
        log.error("请求类型# {}", type);
        log.error("请求地址# {}", url);
        log.error("请求头部# {}", headers);
        log.error("请求参数# {}", params);
    }
    
    
    private static void printRequestAndResponseInfo(
            @Nullable HttpGet httpGet, 
            @Nullable HttpPost httpPost, 
            @NonNull HttpResponse response,
            @NonNull String responseResult) {
        
        log.info("########## 【HTTP请求信息打印】 ##########");
        try {
            // 请求地址 、请求参数
            String url = "", params = "";
            
            if (httpPost != null) {
                log.info("请求类型# {}", "POST");
                url    = httpPost.getURI().toString();
                params = EntityUtils.toString(httpPost.getEntity()); 
            } else if (httpGet != null) {
                log.info("请求类型# {}", "GET");
                url = httpGet.getURI().toString();
            } 
            log.info("请求地址# {}", url);
            log.info("请求参数# {}", params);
            log.info("响应状态# {}", response.getStatusLine());
            log.info("响应结果# {}", responseResult);
        } catch (Exception e) {
            log.error("HTTP请求信息打印异常", e);
        } 
    }
}
