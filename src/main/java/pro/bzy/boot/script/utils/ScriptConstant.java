package pro.bzy.boot.script.utils;


/**
 * 
 * 剧本常量池
 * @author user
 *
 */
public interface ScriptConstant {
    
    String JUBEN_UPLOAD_COVER_IMG_PATH = "/assets/images/juben/cover/";     // 剧本封面图上传相对路径
    String JUBEN_UPLOAD_CHARACTER_IMG_PATH = "/assets/images/juben/character/";     // 剧本人物图上传相对路径
    
    
    String BULLETIN_THEME_CONSTANT_KEY = "script_bulletin_theme";
    
    
    String BULLETIN_SENDWAY_SHOUDONG = "1";
    String BULLETIN_SENDWAY_XITONG = "2";
    
    
    String BULLETIN_SEND_MODE_DELAY_CODE = "1";
    String BULLETIN_SEND_MODE_PERIOD_CODE = "2";
    String BULLETIN_SEND_MODE_DELAY = "延时推送";
    String BULLETIN_SEND_MODE_PERIOD = "周期推送";
    
    String BULLETIN_TIMING_TASK_NAME1 = "延时通知任务-";
    String BULLETIN_TIMING_TASK_NAME2 = "周期通知任务-";
    
    
}
