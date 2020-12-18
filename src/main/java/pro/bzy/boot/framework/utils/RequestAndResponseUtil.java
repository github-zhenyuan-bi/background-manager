package pro.bzy.boot.framework.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.parents.MyUtil;

@Slf4j
public class RequestAndResponseUtil implements MyUtil {

    
    /**
     * 获取请求IP
     * @param request
     * @return
     */
    public static final String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    
    
    /**
     * 从request请求中获取cookies
     * @param request
     */
    public final static Map<String, String> getCookiesFromRequest(ServletRequest servletRequest) {
        log.debug("正在从request中获取cookie内容");
        // 从request中拿去全部的cookies数据
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        Cookie[] cookies = httpRequest.getCookies();
        
        Map<String, String> cookiesNamesAndValues = Maps.newHashMap();
        if (CollectionUtil.isEmpty(cookies)) {
            log.warn("cookie中未存储内容，取值为空");
            return cookiesNamesAndValues;
        }
        
        // 将cookies转成map 便于后面操作
        for (Cookie cookie : cookies) {
            cookiesNamesAndValues.put(
                    cookie.getName(), 
                    cookie.getValue());
        }
        return cookiesNamesAndValues;
    }
    
    
    
    /**
     * 从cookies获取header中拿取token
     * @param key
     * @param httpRequest
     * @param nameAndValuesOfCookies
     * @return
     */
    public static String getJwtTokenFromCookiesOrRequestHeader(final String key,
            HttpServletRequest httpRequest, Map<String, String> nameAndValuesOfCookies) {
        String valueOfkey = null;
        // 优先从header中获取
        valueOfkey =  httpRequest.getHeader(key);
        if (!StringUtils.isEmpty(valueOfkey)) {
            log.debug("从【Header】中获取【 {}】 ={}", key, valueOfkey);
            return valueOfkey;
        }
        valueOfkey = nameAndValuesOfCookies.get(key);
        if (!StringUtils.isEmpty(valueOfkey)) {
            log.debug("从【Cookies】中获取 【{}】 ={}", key, valueOfkey);
            return valueOfkey;
        }
        
        return valueOfkey;
    }
    
    
    
    /**
     * 从cookies获取header中拿取token
     * @param key
     * @param httpRequest
     * @param nameAndValuesOfCookies
     * @return
     */
    public static String getJwtTokenFromCookiesOrRequestHeader(final String key,
            HttpServletRequest httpRequest) {
        return getJwtTokenFromCookiesOrRequestHeader(key, httpRequest, getCookiesFromRequest(httpRequest));
    }
    
    
    
    /**
     * 往cookies里塞值
     * @param response
     */
    public final static void setCookiesToRespone(HttpServletRequest request, HttpServletResponse response, 
            @NonNull final String name, final String value, long expired) {
        Cookie[] cookies = request.getCookies();
        Cookie newCookie = null;
        if (!CollectionUtil.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    newCookie = cookie;
                    break;
                }
            }
        } 
        if (newCookie == null) {
            newCookie = new Cookie(name, value);
        }
        newCookie.setValue(value);
        newCookie.setMaxAge((int) expired);
        newCookie.setPath("/");
        response.addCookie(newCookie);
        log.warn("往Cookies中填充键值对：{} ={}", name, value);
    }
    
    
    
    /**
     * 想响应数据中插入 access_token得内容到 cookie和header
     * @param request
     * @param response
     * @param value
     */
    public final static void setCookiesAndHeaderToResponeForAccessToken(HttpServletRequest request, HttpServletResponse response, 
            final String value) {
        setCookiesToRespone(request, response, 
                SystemConstant.JWT_ACCESS_TOKEN_KEY, value, 
                PropertiesUtil.getJwtTokenExpire(SystemConstant.JWT_REFRESH_TOKEN_EXPIRE_KEY_IN_YML));
        response.setHeader(SystemConstant.JWT_ACCESS_TOKEN_KEY, value);
    }
    
    /**
     * 想响应数据中插入 access_token得内容到 cookie和header
     * @param request
     * @param response
     * @param value
     */
    public final static void setCookiesAndHeaderToResponeForRefreshToken(HttpServletRequest request, HttpServletResponse response, 
            final String value) {
        setCookiesToRespone(request, response, 
                SystemConstant.JWT_REFRESH_TOKEN_KEY, value, 
                PropertiesUtil.getJwtTokenExpire(SystemConstant.JWT_REFRESH_TOKEN_EXPIRE_KEY_IN_YML));
        response.setHeader(SystemConstant.JWT_REFRESH_TOKEN_KEY, value);
    }
    
    
    
    
    /**
     * 删除cookie中的jwttoken认证信息
     * @param request
     * @param response
     */
    public static void clearLoginJwttokenDataFromCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (!CollectionUtil.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                // 删除access_token 和 refresh_token
                if (SystemConstant.JWT_ACCESS_TOKEN_KEY.equals(cookie.getName())
                        || SystemConstant.JWT_REFRESH_TOKEN_KEY.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        } 
    }
    
    
    
    
    /**
     * 判断是否ajax请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")); 
    }
    
    
    
    /**
     * 使用httpResponse发送json
     * @param response
     * @param data
     * @throws IOException
     */
    public static void responseJson(HttpServletResponse response, 
            Integer responseCode, Object data) throws IOException {
        if (responseCode != null)
            response.setStatus(responseCode);
        response.setCharacterEncoding(SystemConstant.CHARSET_UTF8);
        response.setContentType(SystemConstant.HTTP_CONTENT_TYPE_JSON);
        @Cleanup PrintWriter writer = response.getWriter();
        writer.write(JSONObject.toJSONString(data));
    }
    public static void responseJson(HttpServletResponse response,Object data) throws IOException {
        responseJson(response, null, data);
    }
    
    
    
    
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }
    
    
    
    public static void setJwttokenDatasToRequest(HttpServletRequest request, Object datas) {
        request.setAttribute(SystemConstant.JWT_BASESTORAGE_DATAS_KEY, datas);
    }
    
    
    public static Map<String, Object> getJwttokenStorageDatasFromRequest(HttpServletRequest request) {
        Object jwttokenDatas = request.getAttribute(SystemConstant.JWT_BASESTORAGE_DATAS_KEY);
        if (jwttokenDatas == null)
            throw new RuntimeException("从request中获取jwttoken存储的数据为空");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> datas = (Map<String, Object>) jwttokenDatas;
        return datas;
    }
}
