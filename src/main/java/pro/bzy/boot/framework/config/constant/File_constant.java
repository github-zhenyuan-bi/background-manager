package pro.bzy.boot.framework.config.constant;

public interface File_constant {

    /*
     * 文件大小
     */
    long KB_64 = 64;
    long KB_128 = 2 * KB_64;
    long KB_256 = 4 * KB_64;
    long KB_512 = 8 * KB_64;
    long MB_1 = 16 * KB_64;
    long MB_2 = 32 * KB_64;
    long MB_4 = 64 * KB_64;
    long MB_8 = 128 * KB_64;
    
    /** yml文件的后缀名 */
    String YML_SUFFIX = "yml";
    
    /** properties文件的后缀名 */
    String PROPERTIES_SUFFIX = "properties";
    
    /** application文件的后缀名 */
    String APPLICATION_YML = "application.yml"; 
    /** application文件的后缀名 */
    String APPLICATION_PROPERTIES = "application.properties";   // application配置文件名称
}
