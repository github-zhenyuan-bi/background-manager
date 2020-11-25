package pro.bzy.boot.framework.utils;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.parents.MyUtil;

@Slf4j
public class CookieUtil implements MyUtil {

    
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
     * 往cookies里塞值
     * @param response
     */
    public final static void setCookiesToRespone(HttpServletRequest request, HttpServletResponse response, 
            int expired, @NonNull final String name, final String value) {
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
        newCookie.setMaxAge(expired);
        newCookie.setPath("/");
        response.addCookie(newCookie);
        log.warn("往Cookies中填充键值对：{} ={}", name, value);
    }
}
