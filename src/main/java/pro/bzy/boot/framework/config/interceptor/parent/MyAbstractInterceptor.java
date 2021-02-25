package pro.bzy.boot.framework.config.interceptor.parent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.utils.CacheUtil;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.entity.Log;
import pro.bzy.boot.framework.web.mapper.LogMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 我的拦截器抽象父类
 * @author zhenyuan.bi
 *
 */
@NoArgsConstructor
public abstract class MyAbstractInterceptor {

    /**
     * 保存未登陆认证前的一次访问地址
     * @param request
     */
    protected void saveLastAccessUrlIfUnlogin(HttpServletRequest request) {
        request.getSession().setAttribute(SystemConstant.SAVED_URL, request.getRequestURI());
    }
    
    
    
    
    /**
     * 检测是否认证登陆
     * @param response
     * @return
     */
    protected boolean isAuthenticated(HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        boolean isAuthen = subject.isAuthenticated();
        return isAuthen;
    }
    
    
    @Getter 
    @Setter 
    protected LogMapper logMapper;
    
    protected Map<String, String> apiDescCache = Maps.newConcurrentMap();
    
    
    
    /**
     * 访问信息存储到数据库中
     * @param request
     * @param handler
     */
    @SuppressWarnings("unchecked")
    protected void logToDB(HttpServletRequest request, Object handler) {
        if (handler instanceof HandlerMethod) { 
            /*if ("/".equals(request.getRequestURI())) 
                return;*/
            
            String access_token = RequestAndResponseUtil.getJwtTokenFromCookiesOrRequestHeader(SystemConstant.JWT_ACCESS_TOKEN_KEY, request); 
            String refresh_token = RequestAndResponseUtil.getJwtTokenFromCookiesOrRequestHeader(SystemConstant.JWT_REFRESH_TOKEN_KEY, request);
            String accesstor = "", fromWhere = "";
            if (StringUtils.hasText(access_token) && StringUtils.hasText(refresh_token)) {
                Map<String, Object> baseStorageDatas = (Map<String, Object>) request.getAttribute(SystemConstant.JWT_BASESTORAGE_DATAS_KEY);
                try {
                    if (baseStorageDatas == null) {
                        baseStorageDatas = CacheUtil.get(access_token, Map.class);
                        request.setAttribute(SystemConstant.JWT_BASESTORAGE_DATAS_KEY, baseStorageDatas);
                    }
                    accesstor = baseStorageDatas.getOrDefault(SystemConstant.JWT_LOGIN_USERID_KEY, "").toString();
                    fromWhere = baseStorageDatas.getOrDefault(SystemConstant.JWT_LOGIN_FROMWHERE_KEY, "").toString();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                return;
            }
            String apidesc = apiDescCache.get(request.getRequestURI());
            if (StringUtils.isEmpty(apidesc)) {
                HandlerMethod handerMethod = (HandlerMethod) handler;
                Api controllerApiAnno = handerMethod.getBeanType().getAnnotation(Api.class);
                ApiOperation methodApiOperationAnno = handerMethod.getMethodAnnotation(ApiOperation.class);
                String apidescTemp = controllerApiAnno == null? "" : controllerApiAnno.value()+"-";
                apidescTemp += methodApiOperationAnno == null? "" : methodApiOperationAnno.value();
                apiDescCache.put(request.getRequestURI(), apidescTemp);
                apidesc = apidescTemp;
            }
            logMapper.insert(Log.builder()
                    .accessorIp(RequestAndResponseUtil.getIpAddress(request))
                    .accessor(accesstor)
                    .accessModule(request.getRequestURI())
                    .accessTime(DateUtil.getNow())
                    .field1(apidesc)
                    .field2(fromWhere)
                    .build());
        }
    }



    public MyAbstractInterceptor(LogMapper logMapper) {
        super();
        this.logMapper = logMapper;
    }
}
