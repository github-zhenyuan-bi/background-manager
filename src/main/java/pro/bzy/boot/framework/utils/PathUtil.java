package pro.bzy.boot.framework.utils;

import java.io.File;
import java.io.IOException;

import lombok.NonNull;
import pro.bzy.boot.framework.utils.parents.MyUtil;

/**
 * 路径工具类
 */
public class PathUtil implements MyUtil {
    
    
    /** 项目下的java文件基本包路径 */
    private static final String BASE_JAVA_PATH = "src" + 
                                File.separator + "main" + 
                                File.separator + "java" +
                                File.separator;
    
    /** 项目下的test.java 文件基本包路径 */
    private static final String BASE_TEST_JAVA_PATH = "src" + 
                                    File.separator + "test" + 
                                    File.separator + "java" +
                                    File.separator; 
    
    /**
     * @return 项目路径
     */
    public static final String getProjectPath() {
        return System.getProperty("user.dir") + File.separator;
    }
    
    
    
    /**
     * @return classpath路径
     */
    public static String getClasspath() {
        return getClasspath("");
    }
    
    
    
    /**
     * 获取classpath下的资源路径 
     * @param name classpath下的相对资源路径名 不以/开头
     * @return
     */
    public static String getClasspath(String name) {
        return PathUtil.class.getClassLoader().getResource(name).getPath();
    }
    
    
    
    /**
     * 获取某个类所在的包名路径
     * @param clazz 类
     * @return
     */
    public static String getPackagePath(@NonNull Class<?> clazz) {
        return clazz.getResource("").getPath();
    }
    
    
    
    /**
     * 开发环境下获取.java的文件绝对路径
     * @param clazz java类
     * @return
     */
    public static String getJavaPath(@NonNull Class<?> clazz) {
        String projectPath = getProjectPath(); 
        // 包名默认用a.c.b格式 将其替换成a/c/b
        String javaPackagePath = clazz.getPackage().getName().replace(SystemConstant.DOT, File.separator);
        return projectPath + BASE_JAVA_PATH + javaPackagePath + File.separator;
    }
    
    
    
    /**
     * 开发环境下获取test包绝对路径
     * @return
     */
    public static String getTestPackagePath() {
        return getProjectPath() + BASE_TEST_JAVA_PATH;
    }
    
    
    
    /**
     * @return 返回war包项目 WEB-INF路径
     * @throws IOException
     */
    public static String getWEB_INFPath() throws IOException {
        return getWebappResourcePath(SystemConstant.WEB_INF_PATH);
    }
    
    
    
    public static String getWebappResourcePath(final String resource) throws IOException {
        return SpringContextUtil.getApplicationContext().getResource(resource).getURI().getPath();
    }
}
